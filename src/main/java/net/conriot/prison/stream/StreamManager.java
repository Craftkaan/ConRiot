package net.conriot.prison.stream;

import java.io.File;
import java.util.HashMap;

import net.conriot.prison.ConRiot;
import net.conriot.prison.util.ConfigAccessor;

public class StreamManager
{
	private ConfigAccessor streamData;
	private HashMap<String, Long> cooldown;
	
	public StreamManager(ConRiot plugin)
	{
		// Get data file, create new one if needed
		streamData = new ConfigAccessor(plugin, "streams.yml");
		if(!(new File(plugin.getDataFolder().getPath(), "streams.yml")).exists())
			streamData.saveDefaultConfig();
		
		// Instantiate cooldowns
		cooldown = new HashMap<String, Long>();
	}
	
	public String getURL(String name)
	{
		cooldown.remove(name);
		cooldown.put(name, System.currentTimeMillis() + 600000); // 10 Minute cooldown
		return streamData.getConfig().getString(name);
	}
	
	public void setURL(String name, String url)
	{
		streamData.getConfig().set(name, url);
		streamData.saveConfig();
	}
	
	public boolean hasCooldown(String name)
	{
		if(!cooldown.containsKey(name))
			return false;
		if(System.currentTimeMillis() > cooldown.get(name))
			return false;
		return true;
	}
}
