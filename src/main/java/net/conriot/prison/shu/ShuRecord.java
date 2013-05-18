package net.conriot.prison.shu;

import lombok.Getter;
import net.conriot.prison.util.ConfigAccessor;

public class ShuRecord
{
	private ConfigAccessor shuData;
	@Getter private String name;
	@Getter private int spots;
	@Getter private int behavior;
	@Getter private int hacking;
	@Getter private int bugs;
	@Getter private int advertising;
	@Getter private int bans;
	@Getter private boolean muted;
	@Getter private long nextRelease;
	
	public ShuRecord(String name, ConfigAccessor shuData)
	{
		this.shuData = shuData;
		this.name = name;
		
		// Check if the player exists in the current data
		if(shuData.getConfig().contains(name))
		{
			spots = shuData.getConfig().getInt(name + ".spots");
			behavior = shuData.getConfig().getInt(name + ".behavior");
			hacking = shuData.getConfig().getInt(name + ".hacking");
			bugs = shuData.getConfig().getInt(name + ".bugs");
			advertising = shuData.getConfig().getInt(name + ".advertising");
			bans = shuData.getConfig().getInt(name + ".bans");
			muted = shuData.getConfig().getBoolean(name + ".muted");
			nextRelease = shuData.getConfig().getLong(name + ".nextRelease");
		} else
		{
			spots = 0;
			behavior = 0;
			hacking = 0;
			bugs = 0;
			advertising = 0;
			bans = 0;
			muted = false;
			nextRelease = 0;
			save();
		}
	}
	
	private void save()
	{
		shuData.getConfig().set(name + ".spots", spots);
		shuData.getConfig().set(name + ".behavior", behavior);
		shuData.getConfig().set(name + ".hacking", hacking);
		shuData.getConfig().set(name + ".bugs", bugs);
		shuData.getConfig().set(name + ".advertising", advertising);
		shuData.getConfig().set(name + ".bans", bans);
		shuData.getConfig().set(name + ".muted", muted);
		shuData.getConfig().set(name + ".nextRelease", nextRelease);
		shuData.saveConfig();
	}
	
	public int addSpot()
	{
		spots++;
		save();
		return spots;
	}
	
	public int addBehavior()
	{
		behavior++;
		muted = true;
		save();
		return behavior;
	}
	
	public int addHacking()
	{
		hacking++;
		save();
		return hacking;
	}
	
	public int addBug()
	{
		bugs++;
		save();
		return bugs;
	}
	
	public int addAdvertising()
	{
		advertising++;
		muted = true;
		save();
		return advertising;
	}
	
	public int addBan()
	{
		bans++;
		save();
		return bans;
	}
	
	public void addTime(int minutes)
	{
		if(System.currentTimeMillis() > nextRelease)
			nextRelease = System.currentTimeMillis() + (minutes * 60 * 1000);
		else
			nextRelease += (minutes * 60 * 1000);
		save();
	}
	
	public void released()
	{
		nextRelease = 0;
		muted = false;
		save();
	}
}
