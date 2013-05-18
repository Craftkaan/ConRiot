package net.conriot.prison.command.guard;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.conriot.prison.ConRiot;
import net.conriot.prison.Message;
import net.conriot.prison.command.AbstractCommand;

public class ShuCommand extends AbstractCommand
{
	public ShuCommand(ConRiot plugin)
	{
		super(plugin);
		setPlayerAllowed(true);
		
		addSubCommand(new ShuSendCommand(plugin), "send");
		addSubCommand(new ShuListCommand(plugin), "list");
	}

	@Override
	public void execute(CommandSender sender, Command command, String label, String[] args, int argpos)
	{
		if(sender.hasPermission("conriot.guard.trainee"))
		{
			getPlugin().getMessages().send(sender, Message.SHU_HELP_HEADER);
			getPlugin().getMessages().send(sender, Message.SHU_HELP_ITEM, "/shu send ", "<player> <reason>", "Send a player to the SHU!");
			getPlugin().getMessages().send(sender, Message.SHU_HELP_ITEM, "/shu list ", "<player>", "Show a convict's record and status!");
			getPlugin().getMessages().send(sender, Message.SHU_REASONS);
		} else
		{
			getPlugin().getMessages().send(sender, Message.SHU_PERMISSION);
		}
	}
}
