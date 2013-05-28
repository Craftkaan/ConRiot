package net.conriot.prison.warden;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.conriot.prison.ConRiot;
import net.conriot.prison.Message;
import net.conriot.prison.util.ConfigAccessor;
import net.conriot.prison.util.PlayerUtils;

public class VipManager implements Listener {
	ConRiot plugin;
	ConfigAccessor vipData;
	ConfigAccessor pendingVipData;
	HashMap<String, Vip> vips;
	HashMap<String, Integer> pendingVips;
	
	public VipManager(ConRiot plugin)
	{
		this.plugin = plugin;
		
		// Load up all current VIP's
		vipData = new ConfigAccessor(plugin, "vip.yml");
		if(!(new File(plugin.getDataFolder(), "vip.yml")).exists())
			vipData.saveDefaultConfig();
		vips = new HashMap<String, Vip>();
		for(String s : vipData.getConfig().getKeys(false))
		{
			if(s.equalsIgnoreCase("vip-settings"))
				continue;
			Vip v = new Vip(plugin, vipData);
			if(v.load(s))
				vips.put(s, v);
		}
		
		// Load up all pending VIP's
		pendingVipData = new ConfigAccessor(plugin, "pendingVip.yml");
		if(!(new File(plugin.getDataFolder(), "pendingVip.yml")).exists())
			pendingVipData.saveDefaultConfig();
		pendingVips = new HashMap<String, Integer>();
		for(String s : pendingVipData.getConfig().getKeys(false))
		{
			pendingVips.put(s, pendingVipData.getConfig().getInt(s));
		}
		
		// Register event listeners
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	public boolean addVip(String name, int rank)
	{
		// Announce purchase
		switch(rank)
		{
		case 1:
			plugin.getMessages().broadcast(Message.WARDEN_VIP_PURCHASED, name, "Rat");
			break;
		case 2:
			plugin.getMessages().broadcast(Message.WARDEN_VIP_PURCHASED, name, "Dealer");
			break;
		case 3:
			plugin.getMessages().broadcast(Message.WARDEN_VIP_PURCHASED, name, "Gang");
			break;
		case 4:
			plugin.getMessages().broadcast(Message.WARDEN_VIP_PURCHASED, name, "Leader");
			break;
		case 5:
			plugin.getMessages().broadcast(Message.WARDEN_VIP_PURCHASED, name, "Mafia");
			break;
		}
		// Figure out if we can add now or must queue
		Player p = PlayerUtils.resolvePlayerOnline(name);
		if(p != null)
		{
			Vip v = new Vip(plugin, vipData);
			v.create(name, rank);
			v.apply(p);
			return true;
		} else
		{
			pendingVips.put(name, rank);
			pendingVipData.getConfig().set(name, rank);
			pendingVipData.saveConfig();
			return false;
		}
	}
	
	public boolean addDonation(String name, int amount)
	{
		// Announce donation
		switch(rank)
		{
		case 1:
			plugin.getMessages().broadcast(Message.WARDEN_VIP_PURCHASED, name, "Rat");
			break;
		case 2:
			plugin.getMessages().broadcast(Message.WARDEN_VIP_PURCHASED, name, "Dealer");
			break;
		case 3:
			plugin.getMessages().broadcast(Message.WARDEN_VIP_PURCHASED, name, "Gang");
			break;
		case 4:
			plugin.getMessages().broadcast(Message.WARDEN_VIP_PURCHASED, name, "Leader");
			break;
		case 5:
			plugin.getMessages().broadcast(Message.WARDEN_VIP_PURCHASED, name, "Mafia");
			break;
		}
		// Figure out if we can add now or must queue
		Player p = PlayerUtils.resolvePlayerOnline(name);
		if(p != null)
		{
			Vip v = new Vip(plugin, vipData);
			v.create(name, rank);
			v.apply(p);
			return true;
		} else
		{
			pendingVips.put(name, rank);
			pendingVipData.getConfig().set(name, rank);
			pendingVipData.saveConfig();
			return false;
		}
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		// Check for expiration
		Vip v = vips.get(event.getPlayer().getName());
		if(v != null)
		{
			if(v.getExpiration() != 0 && System.currentTimeMillis() > v.getExpiration())
			{
				v.remove(event.getPlayer());
			}
		}
		// Check for pending VIP status
		if(pendingVips.containsKey(event.getPlayer().getName()))
		{
			Vip vnew = new Vip(plugin, vipData);
			vnew.create(event.getPlayer().getName(), pendingVips.get(event.getPlayer().getName()));
			vnew.apply(event.getPlayer());
			vips.put(event.getPlayer().getName(), vnew);
			pendingVips.remove(event.getPlayer().getName());
			// Send personal message
			switch(vnew.getRank())
			{
			case 1:
				plugin.getMessages().send(event.getPlayer(), Message.WARDEN_VIP_UPGRADED_WITH_EXPIRE, "Rat", vipData.getConfig().getInt("vip-settings.rat"));
				break;
			case 2:
				plugin.getMessages().send(event.getPlayer(), Message.WARDEN_VIP_UPGRADED_WITH_EXPIRE, "Dealer", vipData.getConfig().getInt("vip-settings.dealer"));
				break;
			case 3:
				plugin.getMessages().send(event.getPlayer(), Message.WARDEN_VIP_UPGRADED, "Gang");
				break;
			case 4:
				plugin.getMessages().send(event.getPlayer(), Message.WARDEN_VIP_UPGRADED, "Leader");
				break;
			case 5:
				plugin.getMessages().send(event.getPlayer(), Message.WARDEN_VIP_UPGRADED, "Mafia");
				break;
			}
			pendingVipData.getConfig().set(event.getPlayer().getName(), null);
			pendingVipData.saveConfig();
		}
	}
}
