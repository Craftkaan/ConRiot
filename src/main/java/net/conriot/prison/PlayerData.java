package net.conriot.prison;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class PlayerData
{
	@Getter private final String name;
	@Getter @Setter private int bounty;
	@Getter @Setter private boolean onGuardDuty;
	@Getter @Setter private String guardRank;
	@Getter @Setter private List<String> normalRanks;

	public PlayerData(String name)
	{
		this.name = name;
	}
	
	// TOOD: saver/loader @prplz
}
