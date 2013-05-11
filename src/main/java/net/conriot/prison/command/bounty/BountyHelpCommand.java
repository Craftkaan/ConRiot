package net.conriot.prison.command.bounty;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.conriot.prison.ConRiot;
import net.conriot.prison.command.AbstractCommand;

public class BountyHelpCommand extends AbstractCommand
{
	public BountyHelpCommand(ConRiot plugin)
	{
		super(plugin);
		allowAll();
	}

	@Override
	public void execute(CommandSender sender, Command command, String label, String[] args, int argpos)
	{
		sender.sendMessage("bounty command help");
		sender.sendMessage("/bounty add");
		sender.sendMessage("/bounty mine");
		sender.sendMessage("/bounty list");
		sender.sendMessage("/bounty top5");
	}
}
