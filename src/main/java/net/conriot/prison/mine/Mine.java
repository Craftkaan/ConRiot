package net.conriot.prison.mine;

import java.util.LinkedList;
import java.util.List;

import lombok.Getter;

import net.conriot.prison.ConRiot;
import net.conriot.prison.util.ConfigAccessor;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import com.sk89q.worldedit.bukkit.selections.Selection;

public class Mine
{
	private ConRiot plugin;
	private ConfigAccessor dataFile;
	private List<MineMaterial> materials;
	@Getter private String id;
	@Getter private int tag;
	@Getter private World world;
	@Getter private Location min;
	@Getter private Location max;
	@Getter private int[] type;
	@Getter private byte[] data;
	@Getter private boolean valid;
	
	public Mine(ConRiot plugin, ConfigAccessor dataFile, String id, boolean load)
	{
		this.plugin = plugin;
		this.dataFile = dataFile;
		this.id = id;
		
		// Instantiate needed internals
		materials = new LinkedList<MineMaterial>();
		
		// Check if we should skip loading
		if(!load)
		{
			valid = false;
			return;
		}
		
		// Verify we have enough info
		if(!(valid = hasNeededKeys()))
		{
			plugin.getLogger().warning("Mine '" + id + "' didn't have sufficient keys!");
			return; // Log a nice warning so we know what's up
		}
		
		// Verify we have enough info
		if(!(valid = load()))
		{
			plugin.getLogger().warning("Mine '" + id + "' contained invalid data!");
			return; // Log a nice warning so we know what's up
		}
		
		// Rebuild the material weights arrays
		buildWeights();
	}
	
	private boolean hasNeededKeys()
	{
		if(!dataFile.getConfig().contains("mine." + id + ".world"))
			return false;
		if(!dataFile.getConfig().contains("mine." + id + ".min"))
			return false;
		if(!dataFile.getConfig().contains("mine." + id + ".max"))
			return false;
		if(!dataFile.getConfig().contains("mine." + id + ".materials"))
			return false;
		if(!dataFile.getConfig().contains("mine." + id + ".tag"))
			return false;
		// Nothing critical missing, return true
		return true;
	}
	
	private boolean load()
	{
		world = plugin.getServer().getWorld(dataFile.getConfig().getString("mine." + id + ".world"));
		if(world == null)
			return false;
		String minString[] = dataFile.getConfig().getString("mine." + id + ".min").split(":");
		String maxString[] = dataFile.getConfig().getString("mine." + id + ".max").split(":");
		min = world.getBlockAt(Integer.parseInt(minString[0]), Integer.parseInt(minString[1]), Integer.parseInt(minString[2])).getLocation();
		max = world.getBlockAt(Integer.parseInt(maxString[0]), Integer.parseInt(maxString[1]), Integer.parseInt(maxString[2])).getLocation();
		List<String> list = dataFile.getConfig().getStringList("mine." + id + ".materials");
		if(list.size() == 0)
			return false;
		for(String entry : list) {
			String mat[] = entry.split(":");
			materials.add(new MineMaterial(Material.getMaterial(Integer.parseInt(mat[0])), (byte) Integer.parseInt(mat[1]), Integer.parseInt(mat[2])));
		}
		tag = dataFile.getConfig().getInt("mine." + id + ".tag");
		// Nothing was invalid, return true
		return true;
	}
	
	private void save()
	{
		dataFile.getConfig().set("mine." + id + ".world", world.getName());
		dataFile.getConfig().set("mine." + id + ".min", min.getBlockX() + ":" + min.getBlockY() + ":" + min.getBlockZ());
		dataFile.getConfig().set("mine." + id + ".max", max.getBlockX() + ":" + max.getBlockY() + ":" + max.getBlockZ());
		List<String> mats = new LinkedList<String>();
		for(MineMaterial m : materials)
			mats.add(m.type.getId() + ":" + m.data + ":" + m.weight);
		dataFile.getConfig().set("mine." + id + ".materials", mats);
		dataFile.getConfig().set("mine." + id + ".tag", tag);
		dataFile.saveConfig();
	}
	
	private void buildWeights()
	{
		int sum = 0;
		for(MineMaterial m : materials)
			sum += m.weight;
		// Allocate new data and type arrays
		type = new int[sum];
		data = new byte[sum];
		// Fill out the new arrays
		int index = 0;
		for(MineMaterial m : materials)
		{
			for(int i = 0; i < m.weight; i++)
			{
				type[index] = m.type.getId();
				data[index] = m.data;
				index++;
			}
		}
	}
	
	public void create(Selection s, int tag)
	{
		// Set up a new mine
		world = s.getWorld();
		min = s.getMinimumPoint();
		max = s.getMaximumPoint();
		max.add(1, 1, 1);
		this.tag = tag;
		// Update list of mines
		List<String> mines = dataFile.getConfig().getStringList("mines");
		mines.add(id);
		dataFile.getConfig().set("mines", mines);
		// Save this to disk
		save();
		// Set the mine as valid
		valid = true;
	}
	
	public void delete()
	{
		dataFile.getConfig().set("mine." + id + ".world", null);
		dataFile.getConfig().set("mine." + id + ".min", null);
		dataFile.getConfig().set("mine." + id + ".max", null);
		dataFile.getConfig().set("mine." + id + ".materials", null);
		dataFile.getConfig().set("mine." + id + ".tag", null);
		dataFile.getConfig().set("mine." + id, null);
		List<String> mines = dataFile.getConfig().getStringList("mines");
		mines.remove(id);
		dataFile.getConfig().set("mines", mines);
		dataFile.saveConfig();
	}
	
	public void addMaterial(int type, byte data, int weight)
	{
		MineMaterial m = new MineMaterial(Material.getMaterial(type), data, weight);
		if(materials.contains(m))
			materials.remove(m);
		materials.add(m);
		// Rebuild the weights array
		buildWeights();
		// Save update to disk
		save();
	}
	
	public boolean removeMaterial(int type, byte data)
	{
		MineMaterial m = new MineMaterial(Material.getMaterial(type), data, 0);
		if(materials.contains(m)) {
			materials.remove(m);
			// Rebuild the weights array
			buildWeights();
			// Save update to disk
			save();
			// Return removed
			return true;
		}
		// Return not removed
		return false;
	}
}
