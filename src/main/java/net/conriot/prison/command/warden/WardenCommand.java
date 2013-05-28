package net.conriot.prison.command.warden;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.conriot.prison.ConRiot;
import net.conriot.prison.Message;
import net.conriot.prison.command.AbstractCommand;

public class WardenCommand extends AbstractCommand
{
	public WardenCommand(ConRiot plugin)
	{
		super(plugin);
		setPlayerAllowed(true);
		setConsoleAllowed(true);
		
		addSubCommand(new WardenAddCommand(plugin), "add");
		addSubCommand(new WardenDelCommand(plugin), "del");
		addSubCommand(new WardenAddPointsCommand(plugin), "addpoints");
		addSubCommand(new WardenDelPointsCommand(plugin), "delpoints");
		addSubCommand(new WardenAddVipCommand(plugin), "addvip");
		addSubCommand(new WardenUpdateCommand(plugin), "update");
	}

	@Override
	public void execute(CommandSender sender, Command command, String label, String[] args, int argpos)
	{
		if(sender.hasPermission("prison.guard.warden"))
		{
			getPlugin().getMessages().send(sender, Message.WARDEN_HELP_HEADER);
			getPlugin().getMessages().send(sender, Message.WARDEN_HELP_ITEM, "/warden update ", "<update, ... ,text!>", "Sends an announcment to all wardens, guards and trainees. This update must be confirmed as read.");
			getPlugin().getMessages().send(sender, Message.WARDEN_HELP_ITEM, "/warden addvip ", "<player> <rank>", "Upgrade the specified player to the given VIP rank. The update will be applied when they next login if they are not online.");
			// TODO - Other sub commands
		} else
		{
			getPlugin().getMessages().send(sender, Message.WARDEN_PERMISSION);
		}
		/*
		sender.sendMessage("warden command help");
		sender.sendMessage("/warden add");
		sender.sendMessage("/warden del");
		sender.sendMessage("/warden addpoints");
		sender.sendMessage("/warden delpoints");
		sender.sendMessage("/warden addvip");
		*/
	}
}
