package net.conriot.prison.warden;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.conriot.prison.ConRiot;
import net.conriot.prison.Message;
import net.conriot.prison.PlayerData;
import net.conriot.prison.util.ConfigAccessor;

public class UpdateManager implements Listener
{
	ConRiot plugin;
	Collection<Update> updates;
	ConfigAccessor updateData;
	
	public UpdateManager(ConRiot plugin)
	{
		this.plugin = plugin;
		
		// Load up all updates the need to be served
		updateData = new ConfigAccessor(plugin, "updates.yml");
		if(!(new File(plugin.getDataFolder(), "updates.yml")).exists())
			updateData.saveDefaultConfig();
		updates = new LinkedList<Update>();
		for(String s : updateData.getConfig().getKeys(false))
		{
			Update u = new Update(plugin, updateData);
			if(u.load(s))
				updates.add(u);
		}
		
		// Register event listeners
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	public void addUpdate(String update)
	{
		Update u = new Update(plugin, updateData);
		u.setMessage(update);
		updates.add(u);
		Date d = new Date(Long.parseLong(u.getKey()));
		SimpleDateFormat df = new SimpleDateFormat("MMM d, H:mm");
		String date = df.format(d);
		for(Player p : Bukkit.getOnlinePlayers())
		{
			PlayerData pd = plugin.getPlayerData().get(p);
			if(pd == null || pd.getGuardRank() == null)
				continue;
			plugin.getMessages().send(p, Message.WARDEN_UPDATE_HEADER, date);
			plugin.getMessages().send(p, Message.WARDEN_UPDATE_MESSAGE, update);
			plugin.getMessages().send(p, Message.WARDEN_UPDATE_CONFIRM);
		}
	}
	
	public void confirm(Player p)
	{
		plugin.getMessages().send(p, Message.WARDEN_CONFIRMED);
		for(Update u : updates)
			u.confirm(p);
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		PlayerData pd = plugin.getPlayerData().get(event.getPlayer().getName());
		if(pd == null || pd.getGuardRank() == null)
			return;
		// Delay updates for 2 seconds after join
		Bukkit.getScheduler().runTaskLater(plugin, new DelayedUpdates(event.getPlayer()), 40);
	}
	
	private class DelayedUpdates implements Runnable
	{
		private Player player;
		
		public DelayedUpdates(Player player)
		{
			this.player = player;
		}
		
		public void run()
		{
			boolean hasMessages = false;
			for(Update u : updates)
			{
				if(u.getPlayers().contains(player.getName()))
				{
					hasMessages = true;
					Date d = new Date(Long.parseLong(u.getKey()));
					SimpleDateFormat df = new SimpleDateFormat("MMM d, H:mm");
					plugin.getMessages().send(player, Message.WARDEN_UPDATE_HEADER, df.format(d));
					plugin.getMessages().send(player, Message.WARDEN_UPDATE_MESSAGE, u.getMessage());
				}
			}
			if(hasMessages)
				plugin.getMessages().send(player, Message.WARDEN_UPDATE_CONFIRM);
		}
		
	}
}
