package net.conriot.prison;

import lombok.Getter;
import lombok.Setter;

public class PlayerData
{
	@Getter private final String name;
	@Getter @Setter private int bounty;

	public PlayerData(String name)
	{
		this.name = name;
	}
}
