package net.conriot.prison;

import net.conriot.prison.command.bounty.BountyCommand;

import org.bukkit.plugin.java.JavaPlugin;

public class ConRiot extends JavaPlugin 
{
	@Override
	public void onEnable()
	{
		getCommand("bounty").setExecutor(new BountyCommand(this));
	}
}
