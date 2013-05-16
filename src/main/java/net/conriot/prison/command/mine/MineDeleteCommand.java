package net.conriot.prison.command.mine;

import net.conriot.prison.ConRiot;
import net.conriot.prison.Message;
import net.conriot.prison.command.AbstractCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class MineDeleteCommand extends AbstractCommand
{
	public MineDeleteCommand(ConRiot plugin)
	{
		super(plugin);
		setPlayerAllowed(true);
	}

	@Override
	public void execute(CommandSender sender, Command command, String label, String[] args, int argpos)
	{
		// mine 		delete 		<id>
		// command		args[0]		args[1]
		if(sender.isOp()) // OP Check until I know if we need to deal with perms
		{
			if(getPlugin().getMines() != null)
			{
				if(getPlugin().getMines().isValid()) 
				{
					if(args.length == 2)
					{
						if(getPlugin().getMines().delete(args[1]))
							getPlugin().getMessages().send(sender, Message.MINE_DELETE_SUCCESS, args[1]);
						else
							getPlugin().getMessages().send(sender, Message.MINE_DELETE_FAILURE, args[1]);
					} else
					{
						getPlugin().getMessages().send(sender, Message.MINE_ARG_COUNT);
					}
				} else
				{
					getPlugin().getMessages().send(sender, Message.MINE_MANAGER_OFFLINE);
				}
			} else
			{
				getPlugin().getMessages().send(sender, Message.MINE_MANAGER_OFFLINE);
			}
		} else
		{
			getPlugin().getMessages().send(sender, Message.MINE_PERMISSION);
		}
	}
}
