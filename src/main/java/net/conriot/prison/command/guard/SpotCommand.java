package net.conriot.prison.command.guard;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.conriot.prison.ConRiot;
import net.conriot.prison.Message;
import net.conriot.prison.PlayerData;
import net.conriot.prison.command.AbstractCommand;
import net.conriot.prison.util.PlayerUtils;

public class SpotCommand extends AbstractCommand
{
	public SpotCommand(ConRiot plugin)
	{
		super(plugin);
		setPlayerAllowed(true);
	}

	@Override
	public void execute(CommandSender sender, Command command, String label, String[] args, int argpos)
	{
		// spot 		<player> 	<reason>
		// command		args[0]		args[1]
		if(sender.hasPermission("conriot.guard.trainee"))
		{
			if(args.length != 0) {
				PlayerData playerData = getPlugin().getPlayerData().get((Player) sender);
				if(playerData != null)
				{
					if(playerData.isOnGuardDuty())
					{
						if(args.length == 2)
						{
							Player p = PlayerUtils.resolvePlayerOnline(args[0]);
							if(p != null)
							{
								getPlugin().getMessages().broadcast(Message.SPOT_ANNOUNCE, sender.getName(), p.getName(), args[1]);
								getPlugin().getMessages().send(p, Message.SPOT_WARNING, args[1]);
								Bukkit.getScheduler().runTaskLater(getPlugin(), new Countdown(5, p, (Player) sender), 20);
							} else
							{
								getPlugin().getMessages().send(sender, Message.SPOT_INVALID, args[0]);
							}
						} else
						{
							getPlugin().getMessages().send(sender, Message.SPOT_ARGS);
						}
					} else
					{
						getPlugin().getMessages().send(sender, Message.GUARD_NOT_ON);
					}
				} else
				{
					getPlugin().getMessages().send(sender, Message.GUARD_NOT_GUARD);
				}
			} else {
				getPlugin().getMessages().send(sender, Message.SPOT_HELP_HEADER);
				getPlugin().getMessages().send(sender, Message.SPOT_HELP_ITEM, "/spot ", "<player> <reason>", "Sends a warning/countdown to the given player.");
			}
		} else
		{
			getPlugin().getMessages().send(sender, Message.SPOT_PERMISSION);
		}
	}
	
	private class Countdown implements Runnable
	{
		int count;
		Player player, guard;
		
		public Countdown(int count, Player player, Player guard)
		{
			this.count = count;
			this.player = player;
			this.guard = guard;
		}
		
		@Override
		public void run()
		{
			if(count > 0)
			{
				getPlugin().getMessages().send(player, Message.SPOT_COUNTDOWN, count);
				getPlugin().getMessages().send(guard, Message.SPOT_COUNTDOWN, count);
				Bukkit.getScheduler().runTaskLater(getPlugin(), new Countdown(count - 1, player, guard), 40); // 2 Seconds
			}
		}
	}
}
