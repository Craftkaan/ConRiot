package net.conriot.prison;

import net.conriot.prison.command.bounty.BountyCommand;
import net.conriot.prison.command.guard.OffDutyCommand;
import net.conriot.prison.command.guard.OnDutyCommand;
import net.conriot.prison.command.guard.PointsCommand;
import net.conriot.prison.command.guard.ShuCommand;
import net.conriot.prison.command.guard.SpotCommand;

import org.bukkit.plugin.java.JavaPlugin;

public class ConRiot extends JavaPlugin 
{
	@Override
	public void onEnable()
	{
		getCommand("bounty").setExecutor(new BountyCommand(this));
		getCommand("onduty").setExecutor(new OnDutyCommand(this));
		getCommand("offduty").setExecutor(new OffDutyCommand(this));
		getCommand("shu").setExecutor(new ShuCommand(this));
		getCommand("spot").setExecutor(new SpotCommand(this));
		getCommand("points").setExecutor(new PointsCommand(this));
	}
}
