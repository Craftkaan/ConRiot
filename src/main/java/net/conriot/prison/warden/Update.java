package net.conriot.prison.warden;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.entity.Player;

import lombok.Getter;

import net.conriot.prison.ConRiot;
import net.conriot.prison.PlayerData;
import net.conriot.prison.util.ConfigAccessor;

public class Update
{
	ConRiot plugin;
	ConfigAccessor updateData;
	@Getter String key;
	@Getter String message;
	@Getter HashSet<String> players;
	
	public Update(ConRiot plugin, ConfigAccessor updateData)
	{
		this.plugin = plugin;
		this.updateData = updateData;
		players = new HashSet<String>();
	}
	
	public boolean load(String key)
	{
		// Load the data for the given key into this object
		if(!updateData.getConfig().contains(key))
			return false;
		this.key = key;
		message = updateData.getConfig().getString(key + ".message");
		for(String name : updateData.getConfig().getStringList(key + ".players"))
			players.add(name);
		return true;
	}
	
	public void setMessage(String message)
	{
		// Set the message and add all guards, save the update to disk
		this.message = message;
		this.key = Long.toString(System.currentTimeMillis());
		for(PlayerData pd : plugin.getPlayerData().getAll())
		{
			if(pd.getGuardRank() != null)
				players.add(pd.getName());
		}
		save();
	}
	
	public void confirm(Player p)
	{
		// Confirm player has read, remove from list
		if(players.contains(p.getName()))
		{
			players.remove(p.getName());
			save();
		}
	}
	
	private void save()
	{
		// Update the data file
		if(players.size() == 0)
		{
			updateData.getConfig().set(key, null);
		} else
		{
			updateData.getConfig().set(key + ".message", message);
			List<String> playerList = new LinkedList<String>(players);
			updateData.getConfig().set(key + ".players", playerList);
		}
		updateData.saveConfig();
	}
}
