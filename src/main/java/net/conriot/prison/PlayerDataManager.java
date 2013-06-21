package net.conriot.prison;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.conriot.prison.util.ConfigAccessor;
import net.conriot.prison.util.PlayerUtils;

import org.bukkit.entity.Player;

public class PlayerDataManager
{
	private Map<String, PlayerData> dataMap;
	private ConfigAccessor playerData;
	
	public PlayerDataManager(ConRiot plugin)
	{
		this.dataMap = new HashMap<String, PlayerData>();
		
		// Load or create the player data file
		playerData = new ConfigAccessor(plugin, "playerData.yml");
		if(!(new File(plugin.getDataFolder(), "playerData.yml")).exists())
			playerData.saveDefaultConfig();
		
		// Load the player data
		load();
	}
	
	public PlayerData get(Player player)
	{
		return dataMap.get(player.getName());
	}
	
	public PlayerData get(String name)
	{
		name = PlayerUtils.resolveName(name);
		return dataMap.get(name);
	}
	
	public PlayerData getOrCreate(Player player)
	{
		PlayerData data = dataMap.get(player.getName());
		if (data == null)
		{
			data = new PlayerData(playerData, player.getName());
			dataMap.put(player.getName(), data);
		}
		return data;
	}
	
	public PlayerData getOrCreate(String name)
	{	
		name = PlayerUtils.resolveName(name);
		PlayerData data = dataMap.get(name);
		if (data == null)
		{
			data = new PlayerData(playerData, name);
			dataMap.put(name, data);
		}
		return data;
	}
	
	public Collection<PlayerData> getAll()
	{
		return dataMap.values();
	}
	
	public void load()
	{
		// Load all player data
		for(String s : playerData.getConfig().getKeys(false))
		{
			PlayerData pd = new PlayerData(playerData, s);
			pd.load();
			dataMap.put(s, pd);
		}
	}
	
	public void save()
	{
		// Save all layer data
		for(PlayerData pd : dataMap.values())
			pd.save();
	}
}
