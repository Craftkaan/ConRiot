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
		sender.sendMessage("/cell add [cellId] [regionId]");
		// At some point we could add forced removal and force rental commands
		// We could also add some sort of lookup commands to get cell owner by
		// cell ID or cell ID's owned by a player of given name.
		//  - Endain
	}
}
