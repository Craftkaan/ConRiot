package net.conriot.prison.command.cells;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.conriot.prison.ConRiot;
import net.conriot.prison.command.AbstractCommand;

public class CellAddCommand extends AbstractCommand
{
	public CellAddCommand(ConRiot plugin)
	{
		super(plugin);
		setPlayerAllowed(true);
	}

	@Override
	public void execute(CommandSender sender, Command command, String label, String[] args, int argpos)
	{
		// cell 		add 		<id>		[region]  < - - Optional
		// command		args[0]		args[1]		args[2]
		
		if(sender.isOp()) // OP Check until I know if we need to deal with perms
		{
			// TODO @Endain
		}
	}
}
