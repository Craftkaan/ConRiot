package net.conriot.prison.cell;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import lombok.Getter;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import net.conriot.prison.ConRiot;
import net.conriot.prison.economy.EconomyException;
import net.conriot.prison.util.ConfigAccessor;

public class CellBlock implements Listener
{
	private ConRiot plugin;
	private RegionManager rm;
	private ConfigAccessor blockFile;
	private World world;
	private ProtectedRegion region;
	private HashMap<Location, Cell> lockToCell;
	private HashMap<Location, Cell> signToCell;
	private HashMap<String, Cell> idToCell;
	@Getter private int price;
	@Getter private boolean valid;
	
	public CellBlock(ConRiot plugin, WorldGuardPlugin wg, String file)
	{
		this.plugin = plugin;
		
		// Instantiate lookup hashmap
		lockToCell = new HashMap<Location, Cell>();
		signToCell = new HashMap<Location, Cell>();
		idToCell = new HashMap<String, Cell>();
		
		// Get the file for the block data
		blockFile = new ConfigAccessor(plugin, "cells/" + file + ".yml");
		
		// Verify we have enough info
		if(!(valid = hasNeededKeys()))
		{
			plugin.getLogger().warning("Cell Block '" + file + "' didn't have sufficient keys!");
			return; // Log a nice warning so we know what's up
		}
		
		// Load the block data
		world = plugin.getServer().getWorld(blockFile.getConfig().getString("world"));
		rm = wg.getRegionManager(world);
		region = rm.getRegion(blockFile.getConfig().getString("region"));
		price = blockFile.getConfig().getInt("price");
		
		// Load all the cells in the block
		List<String> cellList = blockFile.getConfig().getStringList("list");
		for(String entry : cellList)
		{
			Cell c = new Cell(plugin, this, rm, blockFile, world, entry);
			if(c.isValid())
			{
				lockToCell.put(c.getLock(), c);
				signToCell.put(c.getSign(), c);
				idToCell.put(c.getId(), c);
			}
		}
	}
	
	private boolean hasNeededKeys()
	{
		if(!blockFile.getConfig().contains("world"))
			return false;
		if(!blockFile.getConfig().contains("region"))
			return false;
		if(!blockFile.getConfig().contains("price"))
			return false;
		if(!blockFile.getConfig().contains("list"))
			return false;
		// Nothing critical missing, return true
		return true;
	}
	
	public boolean addCell(String regionId, String cellId, Location lock, Location sign, Player p)
	{
		// Get the region to use
		ProtectedRegion r = rm.getRegion(regionId);
		if(r == null)
		{
			p.sendMessage(ChatColor.RED + "No region with Id of: '" + regionId + "'!");
			return false;
		}
		
		// Verify the given region is part of this cell block
		//Vector mid = Vector.getMidpoint(r.getMinimumPoint(), r.getMaximumPoint());
		//if(!region.contains(mid.getBlockX(), mid.getBlockY(), mid.getBlockZ()))
		if(!(region.contains(r.getMinimumPoint()) && region.contains(r.getMaximumPoint())))
		{
			p.sendMessage(ChatColor.RED + "Region: '" + regionId + "' is not a part of this cell block!");
			return false;
		}
		
		// Verify we don't already have a cell with this id
		if(idToCell.containsKey(cellId))
		{
			p.sendMessage(ChatColor.RED + "Cell with an Id of: '" + cellId + "' already exists!");
			return false;
		}
		
		// Create and add the new cell
		Cell c = new Cell(plugin, this, rm, blockFile, world, cellId);
		c.add(r, cellId, lock, sign);
		lockToCell.put(c.getLock(), c);
		signToCell.put(c.getSign(), c);
		idToCell.put(c.getId(), c);
		return true;
	}
	
	public boolean isInside(Location l)
	{
		if(region.contains(l.getBlockX(), l.getBlockY(), l.getBlockZ()))
			return true;
		return false;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockBreak(BlockBreakEvent event)
	{
		// Don't bother is event was cancelled
		if(event.isCancelled())
			return;
		
		// Only care about the event if in this cell block
		if(event.getBlock() == null)
			return;
		if(!isInside(event.getBlock().getLocation()))
			return;
		
		// Remove the cell if there is one for this sign
		if(event.getBlock().getType() == Material.WALL_SIGN)
		{
			Cell c = signToCell.get(event.getBlock().getLocation());
			if(c != null)
			{
				event.getPlayer().sendMessage(ChatColor.RED + " Cell '" + c.getId() + "' has been deleted!");
				signToCell.remove(c.getSign());
				lockToCell.remove(c.getLock());
				idToCell.remove(c.getId());
				c.delete();
			}
		}
	}
	
	@EventHandler
	public void onChunkLoad(ChunkLoadEvent event)
	{
		// Update all signs in the chunk
		for(Entry<Location, Cell> cell : signToCell.entrySet())
			cell.getValue().update();
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event)
	{
		// Only care about the event if in this cell block
		if(event.getClickedBlock() == null)
			return;
		if(!isInside(event.getClickedBlock().getLocation()))
			return;
		
		// Check if right clicked a sign or stepped on pressure plate
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.WALL_SIGN) {
			// Player clicked on a sign
			Cell c = signToCell.get(event.getClickedBlock().getLocation());
			
			// Verify the sign is the correct sign and interact as needed
			if(c != null)
			{
				Player p = event.getPlayer();
				if(p.getName().equals(c.getOwner()) && c.getOwner() != null)
				{
					try // Owner clicked sign, try to extend lease
					{
						plugin.getEconomy().takeMoney(event.getPlayer(), price);
						c.extend(event.getPlayer());
					} catch(EconomyException e)
					{
						event.getPlayer().sendMessage(ChatColor.RED + e.getMessage());
						return;
					}
				} else if(c.getOwner() == null)
				{
					try // No owner, attempt to rent cell
					{
						plugin.getEconomy().takeMoney(event.getPlayer(), price);
						c.start(event.getPlayer());
					} catch(EconomyException e)
					{
						event.getPlayer().sendMessage(ChatColor.RED + e.getMessage());
						return;
					}
				} else
				{
					// Someone tried to rent an owned cell, notify of ownership
					event.getPlayer().sendMessage(ChatColor.RED + "This cell is already owned by " + c.getOwner() + "!");
				}
			}
		} else if (event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType() == Material.STONE_PLATE) {
			// Player stepped on a pressure plate
			Cell c = lockToCell.get(event.getClickedBlock().getLocation());
			if(c != null)
				if(!event.getPlayer().getName().equals(c.getOwner()))
					event.setCancelled(true);
		}
	}
}
