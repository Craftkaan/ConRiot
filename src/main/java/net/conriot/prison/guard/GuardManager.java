package net.conriot.prison.guard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import net.conriot.prison.ConRiot;
import net.conriot.prison.InventoryBackup;
import net.conriot.prison.Message;
import net.conriot.prison.PlayerData;
import net.milkbowl.vault.permission.Permission;

public class GuardManager implements Listener {
	private ConRiot plugin;
	private HashSet<String> pendingOnDuty;
	private HashSet<String> pendingOffDuty;
	private HashMap<String, GuardGear> gear;
	private HashMap<String, Player> onDuty;
	private HashMap<String, Location> lastLocation;
	
	public GuardManager(ConRiot plugin)
	{
		this.plugin = plugin;
		pendingOnDuty = new HashSet<String>();
		pendingOffDuty = new HashSet<String>();
		gear = new HashMap<String, GuardGear>();
		onDuty = new HashMap<String, Player>();
		lastLocation = new HashMap<String, Location>();
		
		// Register event listeners
		Bukkit.getPluginManager().registerEvents(this, plugin);
		
		// Schedule first points check
		plugin.getServer().getScheduler().runTaskLater(plugin, new ActivityChecker(), 1200);
	}
	
	public boolean scheduleOnDuty(Player player)
	{
		if(pendingOnDuty.contains(player.getName()) || pendingOffDuty.contains(player.getName()))
		{
			plugin.getMessages().send(player, Message.GUARD_CANNOT);
			return false;
		}
		if(pendingOffDuty.contains(player.getName()))
		{
			plugin.getMessages().send(player, Message.GUARD_ALREADY_ON);
			return false;
		}
		
		// Schedule the player to go on duty in 10 seconds
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new DelayedOnDuty(player), 200);
		// Add to the list of players about to go ONLINE
		pendingOnDuty.add(player.getName());
		// Return successful scheduling
		return true;
	}
	
	public boolean scheduleOffDuty(Player player)
	{
		if(pendingOnDuty.contains(player.getName()) || pendingOffDuty.contains(player.getName()))
		{
			plugin.getMessages().send(player, Message.GUARD_CANNOT);
			return false;
		}
		if(!plugin.getPlayerData().get(player).isOnGuardDuty())
		{
			plugin.getMessages().send(player, Message.GUARD_NOT_ON);
			return false;
		}
		
		// Schedule the player to go on duty in 10 seconds
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new DelayedOffDuty(player), 200);
		// Add to the list of players about to go offline
		pendingOffDuty.add(player.getName());
		// Return successful scheduling
		return true;
	}
	
	public boolean goOnDuty(Player player)
	{
		PlayerData playerData = plugin.getPlayerData().get(player);
		
		// Check if player is already on duty
		if (playerData.isOnGuardDuty())
			return false;
		
		// Get and save player's normal groups
		Permission perms = plugin.getPermission();
		List<String> groups = Arrays.asList(perms.getPlayerGroups(player));
		playerData.setNormalRanks(groups);
		
		// Log just in case - start
		plugin.getLogger().info("[OnDutyCommand] " + player.getName() + " started at " + groups);
		
		// Set player as on duty, remove from pending list
		playerData.setOnGuardDuty(true);
		pendingOnDuty.remove(player.getName());
		onDuty.put(player.getName(), player);
		lastLocation.put(player.getName(), player.getLocation().clone());
		
		// Add to guard group
		perms.playerAddGroup(player, playerData.getGuardRank());
		// Remove all other groups
		for (String group : groups)
			perms.playerRemoveGroup(player, group);
		
		// Backup player inventory and clear inventory
		playerData.setInv(new InventoryBackup(player));
		// Distribute guard items
		GuardGear gg = new GuardGear(player);
		gg.give();
		gg.buff();
		gear.put(player.getName(), gg);
		
		// Log just in case - end
		plugin.getLogger().info("[OnDutyCommand] " + player.getName() + " now has " + Arrays.asList(perms.getPlayerGroups(player)));
		
		// Return player successfully went on duty
		return true;
	}
	
	public boolean goOffDuty(Player player)
	{
		PlayerData data = plugin.getPlayerData().get(player);
		
		// Make sure player data exists and player is on duty
		if (data == null || !data.isOnGuardDuty())
			return false;
		
		// Restore the player's normal groups
		Permission perms = plugin.getPermission();
		// Groups must be added in reverse, pex add groups to the front
		List<String> groups = new ArrayList<String>(data.getNormalRanks());
		
		// Log just in case - start
		plugin.getLogger().info("[OnDutyCommand] " + player.getName() + " started at " + groups);
		
		// Add old groups back
		Collections.reverse(groups);
		for (String group : groups)
		{
			perms.playerAddGroup(player, group);
		}
		// Remove guard group
		perms.playerRemoveGroup(player, data.getGuardRank());
		
		// Set player as off duty, remove from pending list
		data.setOnGuardDuty(false);
		pendingOffDuty.remove(player.getName());
		onDuty.remove(player.getName());
		lastLocation.remove(player.getName());
		
		// Debuff and remove from gear list
		gear.get(player.getName()).debuff();
		gear.remove(player.getName());
		
		// Move to world spawn
		player.teleport(plugin.getServer().getWorlds().get(0).getSpawnLocation());
		
		// Restore player's old inventory if needed
		if(data.getInv() != null)
		{
			data.getInv().restore();
			data.setInv(null);
		}
		
		// Log just in case - end
		plugin.getLogger().info("[OffDutyCommand] " + player.getName() + " is now " + Arrays.asList(perms.getPlayerGroups(player)));
		
		// Return player successfully went off duty
		return true;
	}
	
	public void onDisable()
	{
		for(Player p : onDuty.values())
			goOffDuty(p);
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		PlayerData data = plugin.getPlayerData().get(event.getPlayer());
		// Force player to go off duty if needed
		if(data != null && data.isOnGuardDuty())
			goOffDuty(event.getPlayer());
		// Remove any drops that would be restored
		gear.remove(event.getPlayer().getName());
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event)
	{
		Player player = event.getEntity();
		PlayerData data = plugin.getPlayerData().get(player);
		// If guard dies, prevent drops of guard gear
		if(data != null && data.isOnGuardDuty())
		{
			event.getDrops().removeAll(gear.get(player.getName()).getLocked());
		}
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		// Restore inventory if needed
		GuardGear gg = gear.get(event.getPlayer().getName());
		if(gg != null)
		{
			gg.give();
			gg.buff();
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInvClick(InventoryClickEvent event)
	{
		// Prevent moving locked items
		GuardGear gg = gear.get(event.getWhoClicked().getName());
		if(gg != null)
		{
			if(gg.isLocked(event.getCurrentItem()))
			{
				//plugin.getLogger().info("Clicked item is locked!");
				//event.setCursor(null);
				event.setCancelled(true);
				Player p = (Player) event.getWhoClicked();
				p.updateInventory(); // Poor form but needed for now
			}
		}
	}
	
	@EventHandler
	public void onInvDrop(PlayerDropItemEvent event)
	{
		// Prevent moving locked items
		GuardGear gg = gear.get(event.getPlayer().getName());
		if(gg != null)
		{
			if(gg.isLocked(event.getItemDrop().getItemStack()))
				event.setCancelled(true);
		}
	}
	
	private class DelayedOnDuty implements Runnable
	{
		private Player player;
		
		public DelayedOnDuty(Player player)
		{
			this.player = player;
		}
		
		public void run()
		{
			if(goOnDuty(player))
				plugin.getMessages().broadcast(Message.GUARD_ON, player.getName());
			else
				plugin.getMessages().send(player, Message.GUARD_ALREADY_ON);
		}
	}
	
	private class DelayedOffDuty implements Runnable
	{
		private Player player;
		
		public DelayedOffDuty(Player player)
		{
			this.player = player;
		}
		
		public void run()
		{
			if(goOffDuty(player))
				plugin.getMessages().broadcast(Message.GUARD_OFF, player.getName());
			else
				plugin.getMessages().send(player, Message.GUARD_NOT_ON);
		}
	}
	
	private class ActivityChecker implements Runnable
	{
		public void run() 
		{
			for(Entry<String, Location> player : lastLocation.entrySet())
			{
				Player p = onDuty.get(player.getKey());
				if(p != null)
				{
					PlayerData pd = plugin.getPlayerData().get(player.getKey());
					// Add points
					if(p.getLocation().distanceSquared(player.getValue()) > 5f)
					{
						pd.setGuardPoints(pd.getGuardPoints() + 15);//0.0166666666667); // 1/60th point per minute
						// Update player position
						player.setValue(p.getLocation().clone());
					}
					// Check if point threshold is reached
					if(pd.getGuardPoints() >= 100)
					{
						if(pd.getGuardRank().equalsIgnoreCase("Trainee"))
						{
							pd.setGuardRank("Guard");
							plugin.getMessages().broadcast(Message.GUARD_NOW_GUARD, player.getKey());
						}
					}
				}
			}
			// Schedule the next points check
			plugin.getServer().getScheduler().runTaskLater(plugin, new ActivityChecker(), 1200);
		}
	}
}
