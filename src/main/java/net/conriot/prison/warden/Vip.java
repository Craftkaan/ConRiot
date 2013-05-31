package net.conriot.prison.warden;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import net.conriot.prison.ConRiot;
import net.conriot.prison.economy.EconomyException;
import net.conriot.prison.util.ConfigAccessor;
import net.milkbowl.vault.permission.Permission;
import lombok.Getter;

public class Vip
{
	private ConRiot plugin;
	private ConfigAccessor vipData;
	@Getter private String name;
	@Getter private int rank;
	@Getter private long expiration;
	@Getter private int donated;
	
	public Vip(ConRiot plugin, ConfigAccessor vipData)
	{
		this.plugin = plugin;
		this.vipData = vipData;
		this.expiration = 0;
		this.donated = 0;
		this.rank = 0;
	}
	
	public boolean load(String name)
	{
		if(!vipData.getConfig().contains(name))
			return false;
		this.name = name;
		rank = vipData.getConfig().getInt(name + ".rank");
		expiration = vipData.getConfig().getLong(name + ".expiration");
		return true;
	}
	
	public void create(String name, int rank)
	{
		this.name = name;
		this.rank = rank;
		switch(rank)
		{
		case 1:
			donated += vipData.getConfig().getInt("vip-costs.rat");
			expiration = System.currentTimeMillis() + (vipData.getConfig().getInt("vip-settings.rat") * 86400000);
			break;
		case 2:
			donated += vipData.getConfig().getInt("vip-costs.dealer");
			expiration = System.currentTimeMillis() + (vipData.getConfig().getInt("vip-settings.dealer") * 86400000);
			break;
		case 3:
			donated += vipData.getConfig().getInt("vip-costs.gang");
			expiration = 0;
			break;
		case 4:
			donated += vipData.getConfig().getInt("vip-costs.leader");
			expiration = 0;
			break;
		case 5:
			donated += vipData.getConfig().getInt("vip-costs.mafia");
			expiration = 0;
			break;
		}
		save();
	}
	
	public void add(String name, int amount, int rank)
	{
		this.name = name;
		this.rank = rank;
		this.donated += amount;
		switch(rank)
		{
		case 1:
			if(expiration == 0)
				expiration = System.currentTimeMillis() + (vipData.getConfig().getInt("vip-days.rat") * 86400000);
			else
				expiration += (vipData.getConfig().getInt("vip-days.rat") * 86400000);
			break;
		case 2:
			if(expiration == 0)
				expiration = System.currentTimeMillis() + (vipData.getConfig().getInt("vip-days.dealer") * 86400000);
			else
				expiration += (vipData.getConfig().getInt("vip-days.dealer") * 86400000);
			break;
		case 3:
			expiration = 0;
			break;
		case 4:
			expiration = 0;
			break;
		case 5:
			expiration = 0;
			break;
		}
		save();
	}
	
	private void save()
	{
		vipData.getConfig().set(name + ".rank", rank);
		vipData.getConfig().set(name + ".expiration", expiration);
		vipData.getConfig().set(name + ".donated", donated);
		vipData.saveConfig();
	}
	
	public void apply(Player p)
	{
		// Get the permissions
		Permission perms = plugin.getPermission();
		// Set up the new rank
		switch(rank)
		{
		case 1: // RAT
			perms.playerAdd(p, "essentials.warps.donor");
			perms.playerAdd(p, "essentials.workbench");
			perms.playerAdd(p, "essentials.joinfullserver");
			perms.playerAdd(p, "essentials.afk.kickexempt");
			perms.playerAdd(p, "essentials.enderchest");
			perms.playerAdd(p, "essentials.balancetop");
			perms.playerAdd(p, "essentials.kits.tools");
			perms.playerAdd(p, "essentials.kits.Rat");
			getWorldGuard().getRegionManager(Bukkit.getWorld("world")).getRegion("ffvip").getMembers().addPlayer(name);
			p.giveExp(100);
			try
			{
				plugin.getEconomy().giveMoney(p, 2500);
			} catch (EconomyException e) { p.sendMessage(ChatColor.RED + e.getMessage()); }
			// Sadly, no other way to set a prefix
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " prefix \"" + ChatColor.GOLD + "[" + ChatColor.AQUA + "+" + ChatColor.GOLD + "] \"");
			break;
		case 2: // DEALER
			perms.playerAdd(p, "essentials.warps.donor");
			perms.playerAdd(p, "essentials.workbench");
			perms.playerAdd(p, "essentials.joinfullserver");
			perms.playerAdd(p, "essentials.feed");
			perms.playerAdd(p, "essentials.afk.kickexempt");
			perms.playerAdd(p, "essentials.enderchest");
			perms.playerAdd(p, "essentials.balancetop");
			perms.playerAdd(p, "alphachest.chest");
			perms.playerAdd(p, "essentials.kits.tools");
			perms.playerAdd(p, "essentials.kits.Gang");
			getWorldGuard().getRegionManager(Bukkit.getWorld("world")).getRegion("ffvip").getMembers().addPlayer(name);
			getWorldGuard().getRegionManager(Bukkit.getWorld("world")).getRegion("ffdg").getMembers().addPlayer(name);
			p.giveExp(265);
			try
			{
				plugin.getEconomy().giveMoney(p, 7500);
			} catch (EconomyException e) { p.sendMessage(ChatColor.RED + e.getMessage()); }
			// Sadly, no other way to set a prefix
	        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " prefix \"" + ChatColor.GOLD + "[" + ChatColor.AQUA + "++" + ChatColor.GOLD + "] \"");
			break;
		case 3: // GANG
			perms.playerAdd(p, "essentials.warps.donor");
			perms.playerAdd(p, "essentials.workbench");
			perms.playerAdd(p, "essentials.joinfullserver");
			perms.playerAdd(p, "essentials.feed");
			perms.playerAdd(p, "essentials.afk.kickexempt");
			perms.playerAdd(p, "essentials.enderchest");
			perms.playerAdd(p, "essentials.balancetop");
			perms.playerAdd(p, "alphachest.chest");
			perms.playerAdd(p, "essentials.keepxp");
			perms.playerAdd(p, "essentials.feed");
			perms.playerAdd(p, "essentials.nick");
			perms.playerAdd(p, "essentials.kits.tools");
			perms.playerAdd(p, "essentials.kits.Dealer");
			getWorldGuard().getRegionManager(Bukkit.getWorld("world")).getRegion("ffvip").getMembers().addPlayer(name);
			getWorldGuard().getRegionManager(Bukkit.getWorld("world")).getRegion("ffdg").getMembers().addPlayer(name);
			p.giveExp(1000);
			try
			{
				plugin.getEconomy().giveMoney(p, 50000);
			} catch (EconomyException e) { p.sendMessage(ChatColor.RED + e.getMessage()); }
			// Sadly, no other way to set a prefix
	        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " prefix \"" + ChatColor.GOLD + "[" + ChatColor.AQUA + "Gang" + ChatColor.GOLD + "] \"");
			break;
		case 4: // LEADER
			perms.playerAdd(p, "essentials.warps.donor");
			perms.playerAdd(p, "essentials.warps.tony");
			perms.playerAdd(p, "essentials.workbench");
			perms.playerAdd(p, "essentials.joinfullserver");
			perms.playerAdd(p, "essentials.feed");
			perms.playerAdd(p, "essentials.afk.kickexempt");
			perms.playerAdd(p, "essentials.enderchest");
			perms.playerAdd(p, "essentials.balancetop");
			perms.playerAdd(p, "alphachest.chest");
			perms.playerAdd(p, "essentials.keepxp");
			perms.playerAdd(p, "essentials.feed");
			perms.playerAdd(p, "essentials.nick");
			perms.playerAdd(p, "essentials.kits.tools");
			perms.playerAdd(p, "essentials.kits.Leader");
			getWorldGuard().getRegionManager(Bukkit.getWorld("world")).getRegion("ffvip").getMembers().addPlayer(name);
			getWorldGuard().getRegionManager(Bukkit.getWorld("world")).getRegion("ffdg").getMembers().addPlayer(name);
			getWorldGuard().getRegionManager(Bukkit.getWorld("world")).getRegion("ffperm").getMembers().addPlayer(name);
			p.giveExp(1800);
			try
			{
				plugin.getEconomy().giveMoney(p, 100000);
			} catch (EconomyException e) { p.sendMessage(ChatColor.RED + e.getMessage()); }
			// Sadly, no other way to set a prefix
	        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " prefix \"" + ChatColor.GOLD + "[" + ChatColor.AQUA + "Leader" + ChatColor.GOLD + "] \"");
			break;
		case 5:
			// TODO @Endain, @Craftkaan
			break;
		}
	}
	
	public void remove(Player p)
	{
		// Get the permissions
		Permission perms = plugin.getPermission();
		// Set up the new rank
		switch(rank)
		{
		case 1:
			perms.playerRemove(p, "essentials.warps.donor");
			perms.playerRemove(p, "essentials.workbench");
			perms.playerRemove(p, "essentials.joinfullserver");
			perms.playerRemove(p, "essentials.afk.kickexempt");
			perms.playerRemove(p, "essentials.enderchest");
			perms.playerRemove(p, "essentials.balancetop");
			perms.playerRemove(p, "essentials.kits.tools");
			perms.playerRemove(p, "essentials.kits.Rat");
			getWorldGuard().getRegionManager(Bukkit.getWorld("world")).getRegion("ffvip").getMembers().removePlayer(name);
			// Sadly, no other way to set a prefix
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " prefix \"\"");
			break;
		case 2:
			perms.playerRemove(p, "essentials.warps.donor");
			perms.playerRemove(p, "essentials.workbench");
			perms.playerRemove(p, "essentials.joinfullserver");
			perms.playerRemove(p, "essentials.feed");
			perms.playerRemove(p, "essentials.afk.kickexempt");
			perms.playerRemove(p, "essentials.enderchest");
			perms.playerRemove(p, "essentials.balancetop");
			perms.playerRemove(p, "alphachest.chest");
			perms.playerRemove(p, "essentials.kits.tools");
			perms.playerRemove(p, "essentials.kits.Gang");
			getWorldGuard().getRegionManager(Bukkit.getWorld("world")).getRegion("ffvip").getMembers().removePlayer(name);
			getWorldGuard().getRegionManager(Bukkit.getWorld("world")).getRegion("ffdg").getMembers().removePlayer(name);
	        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " prefix \"\"");
			break;
		case 3:
			perms.playerRemove(p, "essentials.warps.donor");
			perms.playerRemove(p, "essentials.workbench");
			perms.playerRemove(p, "essentials.joinfullserver");
			perms.playerRemove(p, "essentials.feed");
			perms.playerRemove(p, "essentials.afk.kickexempt");
			perms.playerRemove(p, "essentials.enderchest");
			perms.playerRemove(p, "essentials.balancetop");
			perms.playerRemove(p, "alphachest.chest");
			perms.playerRemove(p, "essentials.keepxp");
			perms.playerRemove(p, "essentials.feed");
			perms.playerRemove(p, "essentials.nick");
			perms.playerRemove(p, "essentials.kits.tools");
			perms.playerRemove(p, "essentials.kits.Dealer");
			getWorldGuard().getRegionManager(Bukkit.getWorld("world")).getRegion("ffvip").getMembers().removePlayer(name);
			getWorldGuard().getRegionManager(Bukkit.getWorld("world")).getRegion("ffdg").getMembers().removePlayer(name);
	        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " prefix \"\"");
			break;
		case 4:
			perms.playerRemove(p, "essentials.warps.donor");
			perms.playerRemove(p, "essentials.warps.tony");
			perms.playerRemove(p, "essentials.workbench");
			perms.playerRemove(p, "essentials.joinfullserver");
			perms.playerRemove(p, "essentials.feed");
			perms.playerRemove(p, "essentials.afk.kickexempt");
			perms.playerRemove(p, "essentials.enderchest");
			perms.playerRemove(p, "essentials.balancetop");
			perms.playerRemove(p, "alphachest.chest");
			perms.playerRemove(p, "essentials.keepxp");
			perms.playerRemove(p, "essentials.feed");
			perms.playerRemove(p, "essentials.nick");
			perms.playerRemove(p, "essentials.kits.tools");
			perms.playerRemove(p, "essentials.kits.Leader");
			getWorldGuard().getRegionManager(Bukkit.getWorld("world")).getRegion("ffvip").getMembers().removePlayer(name);
			getWorldGuard().getRegionManager(Bukkit.getWorld("world")).getRegion("ffdg").getMembers().removePlayer(name);
			getWorldGuard().getRegionManager(Bukkit.getWorld("world")).getRegion("ffperm").getMembers().removePlayer(name);
	        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " prefix \"\"");
			break;
		case 5:
			// TODO @Endain, @Craftkaan
			break;
		}
		save();
	}
	
	private WorldGuardPlugin getWorldGuard()
	{
	    Plugin wgp = plugin.getServer().getPluginManager().getPlugin("WorldGuard");
	    if (wgp == null || !(wgp instanceof WorldGuardPlugin)) {
	        return null;
	    }
	    return (WorldGuardPlugin) wgp;
	}
}
