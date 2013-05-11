package net.conriot.prison.cells;

import org.bukkit.Location;
import org.bukkit.World;

import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import lombok.Getter;
import net.conriot.prison.ConRiot;
import net.conriot.prison.util.ConfigAccessor;

public class Cell
{
	private ConRiot plugin;
	private ConfigAccessor blockFile;
	private ProtectedRegion region;
	@Getter private String id;
	@Getter private String owner;
	@Getter private Location sign;
	@Getter private Location lock;
	@Getter private boolean valid;
	
	public Cell(ConRiot plugin, RegionManager rm, ConfigAccessor blockFile, World world, String id)
	{
		this.plugin = plugin;
		this.blockFile = blockFile;
		this.id = id;
		
		// Verify we have enough info
		if(!(valid = hasNeededKeys()))
		{
			plugin.getLogger().warning("Cell '" + id + "' did not have sufficient keys!");
			return; // Log a nice warning so we know what's up
		}
		
		// Load the cell data
		region = rm.getRegion(blockFile.getConfig().getString("cell." + id + ".region"));
		String lockData[] = blockFile.getConfig().getString("cell." + id + ".lock").split(":");
		lock = new Location(world, Integer.parseInt(lockData[0]), Integer.parseInt(lockData[1]), Integer.parseInt(lockData[2]));
		String signData[] = blockFile.getConfig().getString("cell." + id + ".sign").split(":");
		sign = new Location(world, Integer.parseInt(signData[0]), Integer.parseInt(signData[1]), Integer.parseInt(signData[2]));
		
		// Load the owner data if any
		// TODO @Endain
	}
	
	private boolean hasNeededKeys()
	{
		// TODO @Endain
		return false;
	}
}
