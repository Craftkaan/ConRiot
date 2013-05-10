package net.conriot.prison.command.warden;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.conriot.prison.ConRiot;
import net.conriot.prison.command.AbstractCommand;

public class WardenCommand extends AbstractCommand
{
	public WardenCommand(ConRiot plugin)
	{
		super(plugin);
		allowAll();
		
		addSubCommand(new WardenAddCommand(plugin), "add");
		addSubCommand(new WardenDelCommand(plugin), "del");
		addSubCommand(new WardenAddPointsCommand(plugin), "addpoints");
		addSubCommand(new WardenDelPointsCommand(plugin), "delpoints");
		addSubCommand(new WardenAddVipCommand(plugin), "addvip");
	}

	@Override
	public void execute(CommandSender sender, Command command, String label, String[] args, int argpos)
	{
		sender.sendMessage("warden command help");
		sender.sendMessage("/warden add");
		sender.sendMessage("/warden del");
		sender.sendMessage("/warden addpoints");
		sender.sendMessage("/warden delpoints");
		sender.sendMessage("/warden addvip");
	}
}
