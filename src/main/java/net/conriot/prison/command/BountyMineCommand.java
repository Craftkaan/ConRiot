package net.conriot.prison.command;

import net.conriot.prison.ConRiot;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BountyMineCommand extends AbstractCommand
{
	public BountyMineCommand(ConRiot plugin)
	{
		super(plugin);
		setPlayerAllowed(true);
	}

	@Override
	public void execute(CommandSender sender, Command command, String label, String[] args, int argpos)
	{
		Player player = (Player) sender;
		player.sendMessage("BountyMineCommand.execute");
	}
}
