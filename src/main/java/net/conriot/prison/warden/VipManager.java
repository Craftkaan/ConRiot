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
		
		// Load up all current VIP data
		vipData = new ConfigAccessor(plugin, "vip.yml");
		if(!(new File(plugin.getDataFolder(), "vip.yml")).exists())
			vipData.saveDefaultConfig();
		vips = new HashMap<String, Vip>();
		for(String s : vipData.getConfig().getKeys(false))
		{
			if(s.equalsIgnoreCase("vip-days"))
				continue;
			if(s.equalsIgnoreCase("vip-costs"))
				continue;
			Vip v = new Vip(plugin, vipData);
			if(v.load(s))
				vips.put(s, v);
		}
		
		// Load up all pending VIP data
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
	
	public boolean addDonation(String name, int amount)
	{
		// Announce the donation
		plugin.getMessages().broadcast(Message.WARDEN_VIP_DONATED, name, amount);
		// Figure out if we can add now or must queue
		Player p = PlayerUtils.resolvePlayerOnline(name);
		if(p != null)
		{
			Vip v = null;
			if(vips.containsKey(name))
			{
				v = vips.get(name);
				if(v.add(name, amount))
					v.apply(p);
			} else
			{
				v = new Vip(plugin, vipData);
				vips.put(name, v);
				if(v.add(name, amount))
					v.apply(p);
			}
			// Return true since we did not have to queue
			return true;
		} else
		{
			pendingVips.add(name);
			pendingVipData.getConfig().set(name, amount);
			pendingVipData.saveConfig();
			// Return false since we had to queue
			return false;
		}
	}
	
	public Vip getVipRecord(String name)
	{
		return vips.get(name);
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		// Check for expiration
		Vip v = vips.get(event.getPlayer().getName());
		if(v != null)
			if(v.getExpiration() != 0 && System.currentTimeMillis() > v.getExpiration())
				v.remove(event.getPlayer());
		
		// Check for pending VIP status
		if(pendingVips.contains(event.getPlayer().getName()))
		{
			// Apply the update in the manned required
			if(v != null)
			{
				if(v.add(event.getPlayer().getName(), pendingVipData.getConfig().getInt(event.getPlayer().getName())))
					v.apply(event.getPlayer());
			} else
			{
				v = new Vip(plugin, vipData);
				vips.put(event.getPlayer().getName(), v);
				if(v.add(event.getPlayer().getName(), pendingVipData.getConfig().getInt(event.getPlayer().getName())))
					v.apply(event.getPlayer());
			}
			// Clear from the list of pending updates
			pendingVips.remove(event.getPlayer().getName());
			pendingVipData.getConfig().set(event.getPlayer().getName(), null);
			pendingVipData.saveConfig();
		}
	}
}
