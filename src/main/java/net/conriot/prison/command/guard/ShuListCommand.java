package net.conriot.prison.command.guard;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.conriot.prison.ConRiot;
import net.conriot.prison.Message;
import net.conriot.prison.command.AbstractCommand;
import net.conriot.prison.util.PlayerUtils;

public class ShuListCommand extends AbstractCommand
{
	public ShuListCommand(ConRiot plugin)
	{
		super(plugin);
		setPlayerAllowed(true);
	}

	@Override
	public void execute(CommandSender sender, Command command, String label, String[] args, int argpos)
	{
		// shu 			list 		<player>
		// command		args[0]		args[1]
		if(sender.hasPermission("conriot.guard.trainee") || sender.isOp())
		{
			if(args.length == 2)
			{
				String name = PlayerUtils.resolveName(args[1]);
				if(name != null)
				{
					getPlugin().getShuManager().showRecord(sender, name);
				} else
				{
					getPlugin().getMessages().send(sender, Message.SHU_NO_RECORD, args[1]);
				}
			} else
			{
				getPlugin().getMessages().send(sender, Message.SHU_ARGS);
			}
		} else
		{
			getPlugin().getMessages().send(sender, Message.SHU_PERMISSION);
		}
	}
}
