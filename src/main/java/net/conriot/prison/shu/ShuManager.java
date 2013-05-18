package net.conriot.prison.shu;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitTask;

import net.conriot.prison.ConRiot;
import net.conriot.prison.Message;
import net.conriot.prison.util.ConfigAccessor;

public class ShuManager implements Listener
{
	private ConRiot plugin;
	private ConfigAccessor shuConfig;
	private ConfigAccessor shuData;
	private HashMap<String, ShuRecord> records;
	private HashMap<String, BukkitTask> releases;
	private ShuConfig config;
	
	public ShuManager(ConRiot plugin)
	{
		this.plugin = plugin;
		
		// Grab config files
		shuConfig = new ConfigAccessor(plugin, "shuConfig.yml");
		shuData = new ConfigAccessor(plugin, "shuData.yml");
		
		// Make sure config files exist
		if(!(new File(plugin.getDataFolder(), "shuConfig.yml")).exists())
			shuConfig.saveDefaultConfig();
		if(!(new File(plugin.getDataFolder(), "shuData.yml")).exists())
			shuData.saveDefaultConfig();
		
		// Instantiate records HashMap, etc
		records = new HashMap<String, ShuRecord>();
		releases = new HashMap<String, BukkitTask>();
		config = new ShuConfig(shuConfig);
		
		// Register event listener
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		ShuRecord r = new ShuRecord(event.getPlayer().getName(), shuData);
		records.put(event.getPlayer().getName(), r);
		// Handle player already in the SHU or that were in the SHU
		if(r.getNextRelease() > System.currentTimeMillis())
		{
			// Schedule a future release and move to SHU
			event.getPlayer().teleport(config.getShuLoc());
			releases.put(event.getPlayer().getName(), Bukkit.getScheduler().runTaskLater(plugin, new Release(event.getPlayer().getName()), ticksToRelease(r)));
		} else if(r.getNextRelease() > 0)
		{
			// Release them now
			r.released();
			event.getPlayer().teleport(config.getWorld().getSpawnLocation());
			plugin.getMessages().send(event.getPlayer(), Message.SHU_RELEASED);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		records.remove(event.getPlayer().getName());
		if(releases.containsKey(event.getPlayer().getName()))
		{
			Bukkit.getScheduler().cancelTask(releases.get(event.getPlayer().getName()).getTaskId());
			releases.remove(event.getPlayer().getName());
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerChat(AsyncPlayerChatEvent event)
	{
		ShuRecord r = records.get(event.getPlayer().getName());
		if(r.isMuted())
		{
			event.setCancelled(true);
			plugin.getMessages().send(event.getPlayer(), Message.SHU_MUTED);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		if(releases.containsKey(event.getPlayer().getName()))
			event.setRespawnLocation(config.getShuLoc());
	}
	
	public String resolveName(String name)
	{
		// Check if the player names has ever existed
		// and if so return correctly capitalized name.
		Player p = Bukkit.getServer().getPlayer(name);
		if(p != null)
			return p.getName();
		OfflinePlayer op = Bukkit.getServer().getOfflinePlayer(name);
		if(op.hasPlayedBefore())
			return op.getName();
		return null;
	}
	
	public void onSpot(String name)
	{
		// Get the SHU record
		ShuRecord r;
		if(records.containsKey(name))
			r = records.get(name);
		else
			r = new ShuRecord(name, shuData);
		// Add time to record
		int spots = r.addSpot();
		int minutes = config.getSpotTime() + ((config.getSpotTime() / 2) * (spots - 1));
		if(minutes > config.getSpotMax())
			minutes = config.getSpotMax();
		r.addTime(minutes);
		// SHU them if online
		if(records.containsKey(name))
		{
			// Schedule future release
			releases.put(name, Bukkit.getScheduler().runTaskLater(plugin, new Release(name), ticksToRelease(r)));
			// Move to SHU and notify
			Player p = Bukkit.getPlayer(name);
			p.teleport(config.getShuLoc());
			plugin.getMessages().send(p, Message.SHU_SENT, minutes);
		}
	}
	
	public void onBehavior(String name)
	{
		// Get the SHU record
		ShuRecord r;
		if(records.containsKey(name))
			r = records.get(name);
		else
			r = new ShuRecord(name, shuData);
		// Add time to record
		int behavior = r.addBehavior();
		int minutes = config.getBehaviorTime() + ((config.getBehaviorTime() / 2) * (behavior - 1));
		if(minutes > config.getBehaviorMax())
			minutes = config.getBehaviorMax();
		r.addTime(minutes);
		// SHU them if online
		if(records.containsKey(name))
		{
			// Schedule future release
			releases.put(name, Bukkit.getScheduler().runTaskLater(plugin, new Release(name), ticksToRelease(r)));
			// Move to SHU and notify
			Player p = Bukkit.getPlayer(name);
			p.teleport(config.getShuLoc());
			plugin.getMessages().send(p, Message.SHU_SENT, minutes);
		}
	}
	
	public void onHacking(String name)
	{
		// Get the SHU record
		ShuRecord r;
		if(records.containsKey(name))
			r = records.get(name);
		else
			r = new ShuRecord(name, shuData);
		// Add time to record
		int hacking = r.addHacking();
		int minutes = config.getHackingTime() + ((config.getHackingTime() / 2) * (hacking - 1));
		if(minutes > config.getHackingMax())
			minutes = config.getHackingMax();
		r.addTime(minutes);
		// SHU them if online
		if(records.containsKey(name))
		{
			// Schedule future release
			releases.put(name, Bukkit.getScheduler().runTaskLater(plugin, new Release(name), ticksToRelease(r)));
			// Move to SHU and notify
			Player p = Bukkit.getPlayer(name);
			p.teleport(config.getShuLoc());
			plugin.getMessages().send(p, Message.SHU_SENT, minutes);
		}
	}
	
	public void onBug(String name)
	{
		// Get the SHU record
		ShuRecord r;
		if(records.containsKey(name))
			r = records.get(name);
		else
			r = new ShuRecord(name, shuData);
		// Add time to record
		int bug = r.addBug();
		int minutes = config.getBugTime() + ((config.getBugTime() / 2) * (bug - 1));
		if(minutes > config.getBugMax())
			minutes = config.getBugMax();
		r.addTime(minutes);
		// SHU them if online
		if(records.containsKey(name))
		{
			// Schedule future release
			releases.put(name, Bukkit.getScheduler().runTaskLater(plugin, new Release(name), ticksToRelease(r)));
			// Move to SHU and notify
			Player p = Bukkit.getPlayer(name);
			p.teleport(config.getShuLoc());
			plugin.getMessages().send(p, Message.SHU_SENT, minutes);
		}
	}
	
	public void onAdvertising(String name)
	{
		// Get the SHU record
		ShuRecord r;
		if(records.containsKey(name))
			r = records.get(name);
		else
			r = new ShuRecord(name, shuData);
		// Add time to record
		int advertising = r.addAdvertising();
		int minutes = config.getAdvertisingTime() + ((config.getAdvertisingTime() / 2) * (advertising - 1));
		if(minutes > config.getAdvertisingMax())
			minutes = config.getAdvertisingMax();
		r.addTime(minutes);
		// SHU them if online
		if(records.containsKey(name))
		{
			// Schedule future release
			releases.put(name, Bukkit.getScheduler().runTaskLater(plugin, new Release(name), ticksToRelease(r)));
			// Move to SHU and notify
			Player p = Bukkit.getPlayer(name);
			p.teleport(config.getShuLoc());
			plugin.getMessages().send(p, Message.SHU_SENT, minutes);
		}
	}
	
	public void onBan(String name)
	{
		// Get the SHU record
		ShuRecord r;
		if(records.containsKey(name))
			r = records.get(name);
		else
			r = new ShuRecord(name, shuData);
		// Add time to record
		int bans = r.addBan();
		int minutes = config.getBanTime() + ((config.getBanTime() / 2) * (bans - 1));
		if(minutes > config.getBanMax())
			minutes = config.getBanMax();
		r.addTime(minutes);
		// SHU them if online
		if(records.containsKey(name))
		{
			// Schedule future release
			releases.put(name, Bukkit.getScheduler().runTaskLater(plugin, new Release(name), ticksToRelease(r)));
			// Move to SHU and notify
			Player p = Bukkit.getPlayer(name);
			p.teleport(config.getShuLoc());
			plugin.getMessages().send(p, Message.SHU_SENT, minutes);
		}
	}
	
	public void showRecord(CommandSender s, String name)
	{
		ShuRecord r;
		if(records.containsKey(name))
			r = records.get(name);
		else
			r = new ShuRecord(name, shuData);
		// Display record data
		plugin.getMessages().send(s, Message.SHU_RECORD_HEADER, name);
		plugin.getMessages().send(s, Message.SHU_RECORD_ITEM, "Weapon/Drug violations", r.getSpots());
		plugin.getMessages().send(s, Message.SHU_RECORD_ITEM, "Behavior violations", r.getBehavior());
		plugin.getMessages().send(s, Message.SHU_RECORD_ITEM, "Hacking violations", r.getHacking());
		plugin.getMessages().send(s, Message.SHU_RECORD_ITEM, "Bug abuse violations", r.getBugs());
		plugin.getMessages().send(s, Message.SHU_RECORD_ITEM, "Advertising violations", r.getAdvertising());
		plugin.getMessages().send(s, Message.SHU_RECORD_ITEM, "Times banned", r.getBans());
		if(r.getNextRelease() != 0)
			plugin.getMessages().send(s, Message.SHU_RECORD_ITEM, "Next SHU release", ((r.getNextRelease() - System.currentTimeMillis()) / 60000L) + " minutes");
		else
			plugin.getMessages().send(s, Message.SHU_RECORD_ITEM, "Next SHU release", "Not in SHU");
	}
	
	private long ticksToRelease(ShuRecord r)
	{
		return ((r.getNextRelease() - System.currentTimeMillis()) / 50) + 1;
	}
	
	private class Release implements Runnable
	{
		private String name;
		
		public Release(String name)
		{
			this.name = name;
		}
		
		@Override
		public void run()
		{
			// Release the player if online
			if(records.containsKey(name))
			{
				records.get(name).released();
				Player p = Bukkit.getPlayer(name);
				p.teleport(config.getWorld().getSpawnLocation());
				plugin.getMessages().send(p, Message.SHU_RELEASED);
			}
		}
	}
}
