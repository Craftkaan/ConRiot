package net.conriot.prison.command.bounty;

import net.conriot.prison.ConRiot;
import net.conriot.prison.command.AbstractCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class BountyCommand extends AbstractCommand
{
	public BountyCommand(ConRiot plugin)
	{
		super(plugin);
		allowAll();
		
		/*
		bounty add
		bounty mine
		bounty list
		Bounty top5
		Bounty <playername>
		*/
		
		addSubCommand(new BountyAddCommand(plugin), "add");
		addSubCommand(new BountyMineCommand(plugin), "mine");
		addSubCommand(new BountyListCommand(plugin), "list", "ls");
		addSubCommand(new BountyTopCommand(plugin), "top", "top5");
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
