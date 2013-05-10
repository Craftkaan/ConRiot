package net.conriot.prison.command.warden;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.conriot.prison.ConRiot;
import net.conriot.prison.command.AbstractCommand;

public class WardenAddCommand extends AbstractCommand
{

	public WardenAddCommand(ConRiot plugin)
	{
		super(plugin);
		allowAll();
	}

	@Override
	public void execute(CommandSender sender, Command command, String label, String[] args, int argpos)
	{
		sender.sendMessage("WardenAddCommand.execute");
	}
}
