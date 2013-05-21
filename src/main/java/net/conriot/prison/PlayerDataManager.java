package net.conriot.prison;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.conriot.prison.util.PlayerUtils;

import org.bukkit.entity.Player;

public class PlayerDataManager
{
	private Map<String, PlayerData> dataMap = new HashMap<String, PlayerData>();
	
	public PlayerDataManager()
	{
		
	}
	
	public PlayerData get(Player player)
	{
		return dataMap.get(player.getName());
	}
	
	public PlayerData get(String name)
	{
		return dataMap.get(name);
	}
	
	public PlayerData getOrCreate(Player player)
	{
		String name = PlayerUtils.resolveName(player.getName());
		PlayerData data = dataMap.get(name);
		if (data == null)
		{
			data = new PlayerData(name);
			dataMap.put(name, data);
		}
		return data;
	}
	
	public PlayerData getOrCreate(String name)
	{	
		name = PlayerUtils.resolveName(name);
		PlayerData data = dataMap.get(name);
		if (data == null)
		{
			data = new PlayerData(name);
			dataMap.put(name, data);
		}
		return data;
	}
	
	public Collection<PlayerData> getAll()
	{
		return dataMap.values();
	}
	
	// TODO: saver/loader @Endain
}
