package net.conriot.prison;

import java.util.LinkedList;
import java.util.List;

import net.conriot.prison.util.ConfigAccessor;

import lombok.Getter;
import lombok.Setter;

public class PlayerData
{
	private ConfigAccessor playerData;
	@Getter private final String name;
	@Getter @Setter private int bounty;
	@Getter @Setter private double guardPoints;
	@Getter @Setter private boolean onGuardDuty;
	@Getter @Setter private String guardRank;
	@Getter @Setter private List<String> normalRanks;
	@Getter @Setter private InventoryBackup inv;

	public PlayerData(ConfigAccessor playerData, String name)
	{
		this.playerData = playerData;
		this.name = name;
		// Set some defaults and instantiate stuff as needed
		bounty = 0;
		guardPoints = 0;
		normalRanks = new LinkedList<String>();
	}
	
	public void load()
	{
		bounty = playerData.getConfig().getInt(name + ".bounty");
		guardPoints = playerData.getConfig().getDouble(name + ".guardPoints");
		guardRank = playerData.getConfig().getString(name + ".guardRank");
	}
	
	public void save()
	{
		if(bounty == 0 && guardRank == null)
			playerData.getConfig().set(name, null);
		else
		{
			playerData.getConfig().set(name + ".bounty", bounty);
			playerData.getConfig().set(name + ".guardPoints", guardPoints);
			playerData.getConfig().set(name + ".guardRank", guardRank);
		}
		// Save the file here - has more disk access required but will
		// prevent data loss upon crashes. This can be reversed if the
		// high disk access kills performance.
		playerData.saveConfig();
	}
}
