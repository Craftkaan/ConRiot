package net.conriot.prison.command.guard;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.conriot.prison.ConRiot;
import net.conriot.prison.command.AbstractCommand;

public class PointsCommand extends AbstractCommand
{
	public PointsCommand(ConRiot plugin)
	{
		super(plugin);
		// needs allowances, not sure what this command is though
	}

	@Override
	public void execute(CommandSender sender, Command command, String label, String[] args, int argpos)
	{
		sender.sendMessage("PointsCommand.execute");
	}
}
