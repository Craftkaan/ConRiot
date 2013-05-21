package net.conriot.prison.command.stream;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.conriot.prison.ConRiot;
import net.conriot.prison.Message;
import net.conriot.prison.command.AbstractCommand;

public class StreamCommand extends AbstractCommand
{
	public StreamCommand(ConRiot plugin)
	{
		super(plugin);
		setPlayerAllowed(true);
		
		addSubCommand(new StreamAddCommand(plugin), "add");
		addSubCommand(new StreamOnlineCommand(plugin), "online");
	}
	
	@Override
	public void execute(CommandSender sender, Command command, String label, String[] args, int argpos)
	{
		if(sender.hasPermission("prison.conriot.stream"))
		{
			getPlugin().getMessages().send(sender, Message.STREAM_HELP_HEADER);
			getPlugin().getMessages().send(sender, Message.STREAM_HELP_ITEM, "/stream add ", "<url>", "Sets a custom URL for announcement.");
			getPlugin().getMessages().send(sender, Message.STREAM_HELP_ITEM, "/stream online ", "", "Announces the stream and provides a link.");
		} else
		{
			getPlugin().getMessages().send(sender, Message.STREAM_PERMISSION);
		}
	}
}
