package net.conriot.prison.command.warden;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.conriot.prison.ConRiot;
import net.conriot.prison.command.AbstractCommand;

public class ConfirmCommand extends AbstractCommand
{
	public ConfirmCommand(ConRiot plugin)
	{
		super(plugin);
		setPlayerAllowed(true);
	}

	@Override
	public void execute(CommandSender sender, Command command, String label, String[] args, int argpos)
	{
		getPlugin().getUpdateManager().confirm((Player) sender);
	}
}
