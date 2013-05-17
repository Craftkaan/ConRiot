package net.conriot.prison.command.stream;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.conriot.prison.ConRiot;
import net.conriot.prison.Message;
import net.conriot.prison.command.AbstractCommand;

public class StreamAddCommand extends AbstractCommand
{
	public StreamAddCommand(ConRiot plugin)
	{
		super(plugin);
		setPlayerAllowed(true);
	}

	@Override
	public void execute(CommandSender sender, Command command, String label, String[] args, int argpos)
	{
		// stream 		add 		<url>
		// command		args[0]		args[1]
		if(sender.hasPermission("conriot.prison.stream"))
		{
			if(args.length == 2)
			{
				getPlugin().getStreamManager().setURL(sender.getName(), args[1]);
				getPlugin().getMessages().send(sender, Message.STREAM_ADD_SUCCESS, args[1]);
			} else
			{
				getPlugin().getMessages().send(sender, Message.STREAM_ARG_COUNT);
			}
		} else
		{
			getPlugin().getMessages().send(sender, Message.STREAM_PERMISSION);
		}
	}
}
