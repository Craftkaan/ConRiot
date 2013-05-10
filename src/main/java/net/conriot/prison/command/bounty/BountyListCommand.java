package net.conriot.prison.command.bounty;

import net.conriot.prison.ConRiot;
import net.conriot.prison.command.AbstractCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class BountyListCommand extends AbstractCommand
{

	public BountyListCommand(ConRiot plugin)
	{
		super(plugin);
		setPlayerAllowed(true);
		setConsoleAllowed(true);
	}

	@Override
	public void execute(CommandSender sender, Command command, String label, String[] args, int argpos)
	{
		sender.sendMessage("BountyListCommand.execute");
	}
}
