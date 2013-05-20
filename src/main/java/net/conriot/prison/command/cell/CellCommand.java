package net.conriot.prison.command.cell;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.conriot.prison.ConRiot;
import net.conriot.prison.Message;
import net.conriot.prison.command.AbstractCommand;

public class CellCommand extends AbstractCommand
{
	public CellCommand(ConRiot plugin)
	{
		super(plugin);
		setPlayerAllowed(true);
		
		addSubCommand(new CellAddCommand(plugin), "add");
	}
	
	@Override
	public void execute(CommandSender sender, Command command, String label, String[] args, int argpos)
	{
		if(sender.isOp())
		{
			getPlugin().getMessages().send(sender, Message.CELL_HELP_HEADER);
			getPlugin().getMessages().send(sender, Message.CELL_HELP_ITEM, "/cell add ", "<cellId> <regionId>", "Creates a cell with the given ID using the given WorldGuard region.");
		} else
		{
			getPlugin().getMessages().send(sender, Message.CELL_PERMISSION);
		}
	}
}
