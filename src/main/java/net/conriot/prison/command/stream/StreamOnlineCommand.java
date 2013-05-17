package net.conriot.prison.command.stream;

import net.conriot.prison.ConRiot;
import net.conriot.prison.Message;
import net.conriot.prison.command.AbstractCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class StreamOnlineCommand extends AbstractCommand
{
	public StreamOnlineCommand(ConRiot plugin)
	{
		super(plugin);
		setPlayerAllowed(true);
	}

	@Override
	public void execute(CommandSender sender, Command command, String label, String[] args, int argpos)
	{
		// stream 		online
		// command		args[0]
		if(sender.hasPermission("conriot.prison.stream"))
		{
			if(getPlugin().getStreamManager().hasCooldown(sender.getName()))
			{
				getPlugin().getMessages().send(sender, Message.STREAM_COOLDOWN);
			} else
			{
				String url = getPlugin().getStreamManager().getURL(sender.getName());
				getPlugin().getMessages().broadcast(Message.STREAM_ONLINE_HEADER, sender.getName());
				if(url != null)
					getPlugin().getMessages().broadcast(Message.STREAM_ONLINE, url);
			}
		} else
		{
			getPlugin().getMessages().send(sender, Message.STREAM_PERMISSION);
		}
	}
}
