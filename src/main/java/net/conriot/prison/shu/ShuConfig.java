package net.conriot.prison.shu;

import net.conriot.prison.util.ConfigAccessor;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import lombok.Getter;

public class ShuConfig
{
	@Getter private World world;
	@Getter private Location shuLoc;
	@Getter private int spotTime;
	@Getter private int behaviorTime;
	@Getter private int hackingTime;
	@Getter private int bugTime;
	@Getter private int advertisingTime;
	@Getter private int banTime;
	@Getter private int spotMax;
	@Getter private int behaviorMax;
	@Getter private int hackingMax;
	@Getter private int bugMax;
	@Getter private int advertisingMax;
	@Getter private int banMax;
	
	public ShuConfig(ConfigAccessor shuConfig)
	{
		spotTime = shuConfig.getConfig().getInt("spotTime");
		behaviorTime = shuConfig.getConfig().getInt("behaviorTime");
		hackingTime = shuConfig.getConfig().getInt("hackingTime");
		bugTime = shuConfig.getConfig().getInt("bugTime");
		advertisingTime = shuConfig.getConfig().getInt("advertisingTime");
		banTime = shuConfig.getConfig().getInt("banTime");
		spotMax = shuConfig.getConfig().getInt("spotMax");
		behaviorMax = shuConfig.getConfig().getInt("behaviorMax");
		hackingMax = shuConfig.getConfig().getInt("hackingMax");
		bugMax = shuConfig.getConfig().getInt("bugMax");
		advertisingMax = shuConfig.getConfig().getInt("advertisingMax");
		banMax = shuConfig.getConfig().getInt("banMax");
		world = Bukkit.getWorld(shuConfig.getConfig().getString("world"));
		String[] loc = shuConfig.getConfig().getString("shuLoc").split(":");
		shuLoc = new Location(world, Integer.parseInt(loc[0]), Integer.parseInt(loc[1]), Integer.parseInt(loc[2]));
	}
}
