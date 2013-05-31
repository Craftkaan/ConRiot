package net.conriot.prison.warden;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

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
	HashSet<String> pendingVips;
	
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
		pendingVips = new HashSet<String>();
		for(String s : pendingVipData.getConfig().getKeys(false))
		{
			pendingVips.add(s);
		}
		
		// Register event listeners
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	public boolean addVip(String name, int rank)
	{
		// Check if a purchase has been made before, if so add donation instead
		if(vips.containsKey(name))
			return addDonation(name, getAmountFromRank(rank));
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
			pendingVips.add(name);
			pendingVipData.getConfig().set(name + ".rank", rank);
			pendingVipData.saveConfig();
			return false;
		}
	}
	
	public boolean addDonation(String name, int amount)
	{
		// Find the total time
		int total = amount;
		if(vips.containsKey(name))
			total += vips.get(name).getDonated();
		// Announce donation
		int rank = getRankFromAmount(total);
		switch(rank)
		{
		case 1:
			plugin.getMessages().broadcast(Message.WARDEN_VIP_ADDED, name, amount, "Rat");
			break;
		case 2:
			plugin.getMessages().broadcast(Message.WARDEN_VIP_ADDED, name, amount, "Dealer");
			break;
		case 3:
			plugin.getMessages().broadcast(Message.WARDEN_VIP_ADDED, name, amount, "Gang");
			break;
		case 4:
			plugin.getMessages().broadcast(Message.WARDEN_VIP_ADDED, name, amount, "Leader");
			break;
		case 5:
			plugin.getMessages().broadcast(Message.WARDEN_VIP_ADDED, name, amount, "Mafia");
			break;
		}
		// Figure out if we can add now or must queue
		Player p = PlayerUtils.resolvePlayerOnline(name);
		if(p != null)
		{
			if(vips.containsKey(name))
			{
				Vip v = vips.get(name);
				v.add(name, amount, rank);
				v.apply(p);
			} else
			{
				Vip v = new Vip(plugin, vipData);
				vips.put(name, v);
				v.create(name, rank);
				v.apply(p);
			}
			return true;
		} else
		{
			pendingVips.add(name);
			pendingVipData.getConfig().set(name + ".donation", amount);
			pendingVipData.saveConfig();
			return false;
		}
	}
	
	public Vip getVipRecord(String name)
	{
		name = PlayerUtils.resolveName(name);
		return vips.get(name);
	}
	
	private int getRankFromAmount(int amount)
	{
		if(amount >= vipData.getConfig().getInt("vip-costs.mafia"))
			return 5;
		else if(amount >= vipData.getConfig().getInt("vip-costs.leader"))
			return 4;
		else if(amount >= vipData.getConfig().getInt("vip-costs.gang"))
			return 3;
		else if(amount >= vipData.getConfig().getInt("vip-costs.dealer"))
			return 2;
		else if(amount >= vipData.getConfig().getInt("vip-costs.rat"))
			return 1;
		return 0;
	}
	
	private int getAmountFromRank(int rank)
	{
		switch(rank)
		{
		case 1:
			return vipData.getConfig().getInt("vip-costs.rat");
		case 2:
			return vipData.getConfig().getInt("vip-costs.dealer");
		case 3:
			return vipData.getConfig().getInt("vip-costs.gang");
		case 4:
			return vipData.getConfig().getInt("vip-costs.leader");
		case 5:
			return vipData.getConfig().getInt("vip-costs.mafia");
		}
		return 0;
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
		if(pendingVips.contains(event.getPlayer().getName()))
		{
			if(pendingVipData.getConfig().contains(event.getPlayer().getName() + ".donation"))
			{
				if(v != null)
				{
					v.add(event.getPlayer().getName(), pendingVipData.getConfig().getInt(event.getPlayer().getName() + ".donation"), getRankFromAmount(v.getDonated() + pendingVipData.getConfig().getInt(event.getPlayer().getName() + ".donation")));
					v.apply(event.getPlayer());
				} else
				{
					v = new Vip(plugin, vipData);
					v.add(event.getPlayer().getName(), pendingVipData.getConfig().getInt(event.getPlayer().getName() + ".donation"), getRankFromAmount(v.getDonated() + pendingVipData.getConfig().getInt(event.getPlayer().getName() + ".donation")));
					v.apply(event.getPlayer());
					vips.put(event.getPlayer().getName(), v);
				}
			} else
			{
				if(v != null)
				{
					v.add(event.getPlayer().getName(), getAmountFromRank(pendingVipData.getConfig().getInt(event.getPlayer().getName() + ".rank")), pendingVipData.getConfig().getInt(event.getPlayer().getName() + ".rank"));
					v.apply(event.getPlayer());
				} else
				{
					v = new Vip(plugin, vipData);
					v.create(event.getPlayer().getName(), pendingVipData.getConfig().getInt(event.getPlayer().getName() + ".rank"));
					v.apply(event.getPlayer());
					vips.put(event.getPlayer().getName(), v);
				}
			}
			pendingVips.remove(event.getPlayer().getName());
			pendingVipData.getConfig().set(event.getPlayer().getName(), null);
			pendingVipData.saveConfig();
			// Send personal message
			switch(v.getRank())
			{
			case 1:
				plugin.getMessages().send(event.getPlayer(), Message.WARDEN_VIP_UPGRADED_WITH_EXPIRE, "Rat", vipData.getConfig().getInt("vip-days.rat"));
				break;
			case 2:
				plugin.getMessages().send(event.getPlayer(), Message.WARDEN_VIP_UPGRADED_WITH_EXPIRE, "Dealer", vipData.getConfig().getInt("vip-days.dealer"));
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
		}
	}
}
