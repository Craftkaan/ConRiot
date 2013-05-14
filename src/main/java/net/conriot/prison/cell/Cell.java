package net.conriot.prison.cell;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.conriot.prison.ConRiot;
import net.conriot.prison.util.ConfigAccessor;

public class Cell
{
	private ConRiot plugin;
	private ConfigAccessor blockFile;
	private ProtectedRegion region;
	private CellBlock cb;
	@Getter private String id;
	@Getter private String owner;
	@Getter private long expiration;
	@Getter private Location sign;
	@Getter private Location lock;
	@Getter private boolean valid;
	private boolean needsUpdate;
	private BukkitTask expireTask;
	
	public Cell(ConRiot plugin, CellBlock cb, RegionManager rm, ConfigAccessor blockFile, World world, String id)
	{
		this.plugin = plugin;
		this.blockFile = blockFile;
		this.cb = cb;
		this.id = id;
		this.needsUpdate = true;
		
		// Verify we have enough info
		if(!(valid = hasNeededKeys()))
		{
			plugin.getLogger().warning("Cell '" + id + "' didn't have sufficient keys!");
			return; // Log a nice warning so we know what's up
		}
		
		// Load the cell data
		region = rm.getRegion(blockFile.getConfig().getString("cell." + id + ".region"));
		String lockData[] = blockFile.getConfig().getString("cell." + id + ".lock").split(":");
		lock = new Location(world, Integer.parseInt(lockData[0]), Integer.parseInt(lockData[1]), Integer.parseInt(lockData[2]));
		String signData[] = blockFile.getConfig().getString("cell." + id + ".sign").split(":");
		sign = new Location(world, Integer.parseInt(signData[0]), Integer.parseInt(signData[1]), Integer.parseInt(signData[2]));
		
		// Load the owner data if any
		if(blockFile.getConfig().contains("cell." + id + ".owner"))
			owner = blockFile.getConfig().getString("cell." + id + ".owner");
		if(blockFile.getConfig().contains("cell." + id + ".expiration"))
			expiration = blockFile.getConfig().getLong("cell." + id + ".expiration");
		else
			expiration = 0;
		
		// Remove cell owner or set up expiration task if needed
		if(owner != null)
		{
			if(expiration < System.currentTimeMillis())
				end(); // End the current lease
			else
				schedule(); // Schedule the end of current lease
		}
	}
	
	private boolean hasNeededKeys()
	{
		if(!blockFile.getConfig().contains("cell." + id + ".region"))
			return false;
		if(!blockFile.getConfig().contains("cell." + id + ".lock"))
			return false;
		if(!blockFile.getConfig().contains("cell." + id + ".sign"))
			return false;
		// Nothing critical missing, return true
		return true;
	}
	
	private void schedule()
	{
		if(!valid) // Don't bother is invalid cell
			return;
		
		// Cancel the old task if there was one
		if(expireTask != null)
			plugin.getServer().getScheduler().cancelTask(expireTask.getTaskId());
		
		// Register a new expire task if needed
		if(expiration > System.currentTimeMillis())
			expireTask = plugin.getServer().getScheduler().runTaskLater(plugin, new Expire(this), ((expiration - System.currentTimeMillis()) / 50) + 1);
	}
	
	public void save()
	{
		blockFile.getConfig().set("cell." + id + ".owner", owner);
		blockFile.getConfig().set("cell." + id + ".expiration", expiration);
		blockFile.saveConfig();
	}
	
	public void delete()
	{
		// Clear owners of region
		region.getOwners().getPlayers().clear();
		// Delete contents from file
		blockFile.getConfig().set("cell." + id + ".owner", null);
		blockFile.getConfig().set("cell." + id + ".expiration", null);
		blockFile.getConfig().set("cell." + id + ".lock", null);
		blockFile.getConfig().set("cell." + id + ".sign", null);
		blockFile.getConfig().set("cell." + id + ".region", null);
		List<String> list = blockFile.getConfig().getStringList("list");
		list.remove(id);
		blockFile.getConfig().set("list", list);
		blockFile.saveConfig();
	}
	
	public void add(ProtectedRegion region, String id, Location lock, Location sign)
	{
		this.region = region;
		this.id = id;
		this.lock = lock;
		this.sign = sign;
		
		// Save the data to file
		String lockString = lock.getBlockX() + ":" + lock.getBlockY() + ":" + lock.getBlockZ();
		String signString = sign.getBlockX() + ":" + sign.getBlockY() + ":" + sign.getBlockZ();
		blockFile.getConfig().set("cell." + id + ".lock", lockString);
		blockFile.getConfig().set("cell." + id + ".sign", signString);
		blockFile.getConfig().set("cell." + id + ".region", region.getId());
		List<String> list = blockFile.getConfig().getStringList("list");
		list.add(id);
		blockFile.getConfig().set("list", list);
		blockFile.saveConfig();
		
		// Update the sign
		needsUpdate = true;
		update();
		
		// Mark as valid
		valid = true;
	}
	
	public void start(Player p)
	{
		// Send a notification
		p.sendMessage(ChatColor.GREEN + "You have rented cell " + id + "! " + ChatColor.RED + " (-$" + cb.getPrice() + ")");
		
		// Assign the cell to the player
		owner = p.getName();
		region.getOwners().addPlayer(owner);
		expiration = System.currentTimeMillis() + 43200000; // 12 Hours, always
		needsUpdate = true;
		schedule();
		update();
		save();
	}
	
	public void end()
	{
		// Send a notification message
		if(owner != null)
		{
			Player p = plugin.getServer().getPlayer(owner);
			if(p != null)
				p.sendMessage(ChatColor.RED + "Your rental of cell " + id + " has expired!");
		}
		
		// Terminate the lease
		region.getOwners().removePlayer(owner);
		owner = null;
		expiration = 0;
		needsUpdate = true;
		update();
		save();
	}
	
	public void extend(Player p)
	{
		// Send a notification
		p.sendMessage(ChatColor.GOLD + "You have extended your lease duration!" + ChatColor.RED + " (-$" + cb.getPrice() + ")");
		// Increase the lease duration
		expiration = expiration + 43200000; // Add 12 hours
		needsUpdate = true;
		schedule();
		update();
		save();
	}
	
	public void update()
	{
		// Don't bother changing if it hasn't changed
		if(!needsUpdate)
			return;
		
		// Don't bother changing the sign if chunk isn't loaded
		if(!sign.getWorld().getChunkAt(sign).isLoaded())
			return;
		
		// Cast to a sign object to edit
		Sign s = (Sign) (sign.getBlock().getState());
		
		// Set sign contents based on ownership status
		if(owner == null)
		{
			s.setLine(0, ChatColor.GREEN + "FOR RENT");
			s.setLine(1, ChatColor.GREEN + id);
			s.setLine(2, ChatColor.GREEN + "$" + cb.getPrice());
			int h = 12; // 12 hours, always
			s.setLine(3, ChatColor.GREEN + "" + h + " Hours");
		} else
		{
			s.setLine(0, ChatColor.DARK_RED + owner);
			s.setLine(1, ChatColor.DARK_RED + id);
			s.setLine(2, ChatColor.WHITE + "Lease Expires on:");
			Date d = new Date(expiration);
			SimpleDateFormat df = new SimpleDateFormat("MMM d, H:mm");
			s.setLine(3, ChatColor.DARK_RED + df.format(d));
		}
		
		// Force the sign update
		s.update(true);
		needsUpdate = false;
	}
	
	@RequiredArgsConstructor
	private class Expire implements Runnable
	{
		@NonNull private Cell cell;
		
		@Override
		public void run()
		{
			// Simply make a call to end the current lease
			cell.end();
		}
		
	}
}
