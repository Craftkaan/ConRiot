package net.conriot.prison.command.cells;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.conriot.prison.ConRiot;
import net.conriot.prison.command.AbstractCommand;

public class CellCommand extends AbstractCommand
{
	public CellCommand(ConRiot plugin) {
		super(plugin);
		setPlayerAllowed(true);
		
		addSubCommand(new CellAddCommand(plugin), "add");
	}
	
	@Override
	public void execute(CommandSender sender, Command command, String label, String[] args, int argpos)
	{
		sender.sendMessage("cell command help");
		sender.sendMessage("/cell add");
	}
}
