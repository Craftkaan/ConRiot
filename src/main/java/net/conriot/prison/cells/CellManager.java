package net.conriot.prison.cells;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.plugin.Plugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import net.conriot.prison.ConRiot;
import net.conriot.prison.util.ConfigAccessor;

public class CellManager
{
	// Static stuffs
	private static final String config = "cells/config.yml";
	// Non static stuffs
	private ConRiot plugin;
	private WorldGuardPlugin wg;
	private WorldEditPlugin we;
	private ConfigAccessor configFile;
	private List<CellBlock> cellBlocks;
	
	public CellManager(ConRiot plugin)
	{
		this.plugin = plugin;
		
		// Make sure we have WorldGuard, abandon ship otherwise
		if((wg = getWorldGuard()) == null)
		{
			plugin.getLogger().warning("WorldGuard not found! Cell renting diasbled!");
			return; // Log a nice warning, so we know what's up
		}
		
		// Make sure we have WorldGuard, abandon ship otherwise
		if((we = getWorldEdit()) == null)
		{
			plugin.getLogger().warning("WorldEdit not found! Cell renting diasbled!");
			return; // Log a nice warning, so we know what's up
		}
				
		// Load up a list of cell blocks
		configFile = new ConfigAccessor(plugin, config);
		cellBlocks = new LinkedList<CellBlock>();
		List<String> files = configFile.getConfig().getStringList("blocks");
		for(String file : files)
		{
			CellBlock cb = new CellBlock(plugin, wg, file);
			if(cb.isValid())
				cellBlocks.add(cb);
		}
	}
	
	private WorldGuardPlugin getWorldGuard()
	{
	    Plugin wgp = plugin.getServer().getPluginManager().getPlugin("WorldGuard");
	    if (wgp == null || !(wgp instanceof WorldGuardPlugin)) {
	        return null;
	    }
	    return (WorldGuardPlugin) wgp;
	}
	
	private WorldEditPlugin getWorldEdit()
	{
	    Plugin wep = plugin.getServer().getPluginManager().getPlugin("WorldEdit");
	    if (wep == null || !(wep instanceof WorldGuardPlugin)) {
	        return null;
	    }
	    return (WorldEditPlugin) wep;
	}
	
}
