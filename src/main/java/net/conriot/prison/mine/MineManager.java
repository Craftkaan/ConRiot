package net.conriot.prison.mine;

import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.bukkit.selections.Selection;

import lombok.Getter;

import net.conriot.prison.ConRiot;
import net.conriot.prison.mine.refiller.BukkitMineRefiller;
import net.conriot.prison.mine.refiller.MineRefiller;
import net.conriot.prison.mine.refiller.V1_5_R3MineRefiller;
import net.conriot.prison.util.ConfigAccessor;

public class MineManager implements Listener
{
	private ConRiot plugin;
	private static final String file = "mines.yml";
	private ConfigAccessor dataFile;
	private MineRefiller refiller;
	@Getter private HashMap<String, Mine> mines;
	@Getter private boolean valid;
	
	public MineManager(ConRiot plugin)
	{
		this.plugin = plugin;
		
		// Get the file for the block data
		dataFile = new ConfigAccessor(plugin, file);
		
		// Instantiate mines hashmap
		mines = new HashMap<String, Mine>();
		
		// Verify we have enough info
		if(!(valid = hasNeededKeys()))
		{
			plugin.getLogger().warning("Mines data file '" + file + "' didn't have sufficient keys!");
			return; // Log a nice warning so we know what's up
		}
		
		// Load all the cells in the block
		List<String> list = dataFile.getConfig().getStringList("mines");
		for(String entry : list)
		{
			Mine m = new Mine(plugin, dataFile, entry, true);
			if(m.isValid())
				mines.put(entry, m);
		}
		
		// Load up the correct MineRefiller implementation, log manager as active
		String p = plugin.getServer().getClass().getPackage().getName();
   		String version = p.substring(p.lastIndexOf('.') + 1);
  		if (version.equals("v1_5_R3"))
  		{
  			refiller = new V1_5_R3MineRefiller(this);
  			plugin.getLogger().info("Mine Manager is online! [V1_5_R3 NMS]");
  		} else
  		{
  			refiller = new BukkitMineRefiller(this);
  			plugin.getLogger().info("Mine Manager is online! [Bukkit API]");
  		}
  		
  		// Set up piston event listener
  		plugin.getServer().getPluginManager().registerEvents(this, plugin);
  		
  		// Done loading, call setup to start the refills
  		setup();
	}
	
	private boolean hasNeededKeys()
	{
		if(!dataFile.getConfig().contains("mines"))
			return false;
		// Nothing critical missing, return true
		return true;
	}
	
	private void setup() {
		// Schedule set of mines with tag 1/2 to run immediately
		plugin.getServer().getScheduler().runTaskLater(plugin, new Refill(plugin, 1, 0), 100);
		plugin.getServer().getScheduler().runTaskLater(plugin, new Refill(plugin, 2, 0), 100);
		// Schedule mins with tag 1 to run in 10 mintes, tag 2 in 20
		plugin.getServer().getScheduler().runTaskLater(plugin, new Refill(plugin, 1, 1), 100);
		plugin.getServer().getScheduler().runTaskLater(plugin, new Refill(plugin, 2, 2), 10800 + 100); // Offset by 9 minutes
	}
	
	public boolean create(String id, Selection s, int tag)
	{
		// Make sure we don't add a duplicate
		if(mines.containsKey(id))
			return false;
		// Add the new mine
		Mine m = new Mine(plugin, dataFile, id, false);
		m.create(s, tag);
		if(m.isValid())
			mines.put(id, m);
		else
			return false;
		// Return true
		return true;
	}
	
	public boolean delete(String id)
	{
		// See if mine with id exists
		if(!mines.containsKey(id))
			return false;
		// Delete the mine
		Mine m = mines.get(id);
		if(m.isValid()) {
			m.delete();
			mines.remove(id);
		} else
			return false;
		// Return true
		return true;
	}
	
	public boolean addMaterial(String id, int type, byte data, int weight)
	{
		// Try to get the mine by id
		Mine m = mines.get(id);
		if(m == null)
			return false;
		// Add the material to the mine
		m.addMaterial(type, data, weight);
		return true;
	}
	
	public boolean removeMaterial(String id, int type, byte data)
	{
		// Try to get the mine by id
		Mine m = mines.get(id);
		if(m == null)
			return false;
		// Try to remove the material from the mine
		return m.removeMaterial(type, data);
	}
	
	public boolean listMaterials(CommandSender s, String id)
	{
		Mine m = mines.get(id);
		if(m == null)
			return false;
		m.listMaterials(s);
		return true;
	}
	
	@EventHandler
	public void onPistonExtend(BlockPistonExtendEvent event)
	{
		// Check if the event happened in a mine
		for(Mine m : mines.values())
		{
			if(m.isInside(event.getBlock().getX(), event.getBlock().getY(), event.getBlock().getZ()))
				event.setCancelled(true);
		}
	}
	
	private class Refill extends BukkitRunnable
	{
		private ConRiot plugin;
		private int tag;
		private int state;
		
		public Refill(ConRiot plugin, int tag, int state)
		{
			this.plugin = plugin;
			this.tag = tag;
			this.state = state;
		}
		
		public void run()
		{
			switch(state)
			{
			case 0: // Special case, immediately refill, do not schedule another
				plugin.getServer().broadcastMessage(ChatColor.GREEN + "The mines have been reset! " + ChatColor.AQUA + "(" + refiller.refill(tag) + "ms)");
				break;
			case 1: // Mark for refill in 20 minutes
				plugin.getServer().getScheduler().runTaskLater(plugin, new Refill(plugin, tag, state + 1), 22800); // 19 minutes = 22800
				break;
			case 2: // Mark for refill in 1 minutes
				plugin.getServer().broadcastMessage(ChatColor.GOLD + "1 Minute until the mines reset!");
				plugin.getServer().getScheduler().runTaskLater(plugin, new Refill(plugin, tag, state + 1), 1100); // 55 seconds = 1100
				break;
			case 3: // Mark for refill in 5 seconds
				plugin.getServer().broadcastMessage(ChatColor.GOLD + "5!");
				plugin.getServer().getScheduler().runTaskLater(plugin, new Refill(plugin, tag, state + 1), 20);
				break;
			case 4: // Mark for refill in 4 seconds
				plugin.getServer().broadcastMessage(ChatColor.GOLD + "4!");
				plugin.getServer().getScheduler().runTaskLater(plugin, new Refill(plugin, tag, state + 1), 20);
				break;
			case 5: // Mark for refill in 3 seconds
				plugin.getServer().broadcastMessage(ChatColor.GOLD + "3!");
				plugin.getServer().getScheduler().runTaskLater(plugin, new Refill(plugin, tag, state + 1), 20);
				break;
			case 6: // Mark for refill in 2 seconds
				plugin.getServer().broadcastMessage(ChatColor.GOLD + "2!");
				plugin.getServer().getScheduler().runTaskLater(plugin, new Refill(plugin, tag, state + 1), 20);
				break;
			case 7: // Mark for refill in 1 seconds
				plugin.getServer().broadcastMessage(ChatColor.GOLD + "1!");
				plugin.getServer().getScheduler().runTaskLater(plugin, new Refill(plugin, tag, state + 1), 20);
				break;
			case 8: // Refill now, start cycle over
				plugin.getServer().broadcastMessage(ChatColor.GREEN + "The mines have been reset! " + ChatColor.AQUA + "(" + refiller.refill(tag) + "ms)");
				plugin.getServer().getScheduler().runTask(plugin, new Refill(plugin, tag, 1));
				break;
			}
		}
	}
}

