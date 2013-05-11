package net.conriot.prison.cells;

import java.util.HashMap;
import java.util.List;

import lombok.Getter;

import org.bukkit.Location;
import org.bukkit.World;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import net.conriot.prison.ConRiot;
import net.conriot.prison.util.ConfigAccessor;

public class CellBlock
{
	private ConRiot plugin;
	private RegionManager rm;
	private ConfigAccessor blockFile;
	private World world;
	@Getter private ProtectedRegion region;
	private HashMap<Location, Cell> lockToCell;
	private HashMap<Location, Cell> signToCell;
	private HashMap<String, Cell> idToCell;
	private HashMap<String, Cell> ownerToCell;
	@Getter private boolean valid;
	
	public CellBlock(ConRiot plugin, WorldGuardPlugin wg, String file)
	{
		this.plugin = plugin;
		
		// Get the file for the block data
		blockFile = new ConfigAccessor(plugin, file);
		
		// Verify we have enough info
		if(!(valid = hasNeededKeys()))
		{
			plugin.getLogger().warning("Cell Block '" + file + "' did not have sufficient keys!");
			return; // Log a nice warning so we know what's up
		}
		
		// Load the region data
		world = plugin.getServer().getWorld(blockFile.getConfig().getString("world"));
		rm = wg.getRegionManager(world);
		region = rm.getRegion(blockFile.getConfig().getString("region"));
		
		// Load all the cells in the block
		List<String> cellList = blockFile.getConfig().getStringList("list");
		for(String entry : cellList)
		{
			Cell c = new Cell(plugin, rm, blockFile, world, entry);
			if(c.isValid())
			{
				lockToCell.put(c.getLock(), c);
				signToCell.put(c.getSign(), c);
				idToCell.put(c.getId(), c);
				if(c.getOwner() != null)
					ownerToCell.put(c.getOwner(), c);
			}
		}
	}
	
	private boolean hasNeededKeys()
	{
		// TODO @Endain
		return false;
	}
}
