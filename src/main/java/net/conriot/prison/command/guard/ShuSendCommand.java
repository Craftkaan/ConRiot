package net.conriot.prison.command.guard;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.conriot.prison.ConRiot;
import net.conriot.prison.Message;
import net.conriot.prison.PlayerData;
import net.conriot.prison.command.AbstractCommand;

public class ShuSendCommand extends AbstractCommand
{
	public ShuSendCommand(ConRiot plugin)
	{
		super(plugin);
		setPlayerAllowed(true);
	}

	@Override
	public void execute(CommandSender sender, Command command, String label, String[] args, int argpos)
	{
		// shu 			send 		<player>	<reason>
		// command		args[0]		args[1]		args[2]
		if(sender.hasPermission("conriot.guard.trainee"))
		{
			PlayerData playerData = getPlugin().getPlayerData().get((Player) sender);
			if(playerData != null)
			{
				if(playerData.isOnGuardDuty())
				{
					if(args.length <= 3)
					{
						String name = getPlugin().getShuManager().resolveName(args[1]);
						if(name != null)
						{
							if(args[2].equalsIgnoreCase("spotted") || args[2].equalsIgnoreCase("spot") ||
							   args[2].equalsIgnoreCase("drugs") || args[2].equalsIgnoreCase("weapons") ||
							   args[2].equalsIgnoreCase("weapon"))
							{
								getPlugin().getShuManager().onSpot(name);
								getPlugin().getMessages().broadcast(Message.SHU_ANNOUNCE, name, "SPOTTED");
							} else if(args[2].equalsIgnoreCase("behavior"))
							{
								getPlugin().getShuManager().onBehavior(name);
								getPlugin().getMessages().broadcast(Message.SHU_ANNOUNCE, name, "BEHAVIOR");
							} else if(args[2].equalsIgnoreCase("hacking") || args[2].equalsIgnoreCase("cheating"))
							{
								getPlugin().getShuManager().onHacking(name);
								getPlugin().getMessages().broadcast(Message.SHU_ANNOUNCE, name, "HACKING");
							} else if(args[2].equalsIgnoreCase("bug") || args[2].equalsIgnoreCase("bugs"))
							{
								getPlugin().getShuManager().onBug(name);
								getPlugin().getMessages().broadcast(Message.SHU_ANNOUNCE, name, "BUG ABUSE");
							} else if(args[2].equalsIgnoreCase("advertising") || args[2].equalsIgnoreCase("ads"))
							{
								getPlugin().getShuManager().onAdvertising(name);
								getPlugin().getMessages().broadcast(Message.SHU_ANNOUNCE, name, "ADVERTISING");
							} else if(args[2].equalsIgnoreCase("banned") || args[2].equalsIgnoreCase("ban"))
							{
								getPlugin().getShuManager().onBan(name);
								getPlugin().getMessages().broadcast(Message.SHU_ANNOUNCE, name, "BANNED");
							} else
							{
								getPlugin().getMessages().send(sender, Message.SHU_REASONS);
							}
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
					getPlugin().getMessages().send(sender, Message.GUARD_NOT_ON);
				}
			} else
			{
				getPlugin().getMessages().send(sender, Message.GUARD_NOT_GUARD);
			}
		} else
		{
			getPlugin().getMessages().send(sender, Message.SHU_PERMISSION);
		}
	}
}
