package net.conriot.prison.command.warden;

import net.conriot.prison.ConRiot;
import net.conriot.prison.Message;
import net.conriot.prison.command.AbstractCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

public class WardenUpdateCommand extends AbstractCommand implements Listener
{
	public WardenUpdateCommand(ConRiot plugin)
	{
		super(plugin);
		setPlayerAllowed(true);
	}

	@Override
	public void execute(CommandSender sender, Command command, String label, String[] args, int argpos)
	{
		// warden 		update 		<word_1 ... word_n>
		// command		args[0]		args[1]	... args[n]
		if(sender.hasPermission("prison.guard.warden"))
		{
			if(args.length > 1)
			{
				String update = "";
				for(int i = 1; i < args.length; i++)
					update = update + args[i] + " ";
				getPlugin().getMessages().send(sender, Message.WARDEN_UPDATE_ADD, update);
				getPlugin().getUpdateManager().addUpdate(update);
			} else
			{
				getPlugin().getMessages().send(sender, Message.WARDEN_ARG_COUNT);
			}
		} else
		{
			getPlugin().getMessages().send(sender, Message.WARDEN_PERMISSION);
		}
	}
}