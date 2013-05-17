package net.conriot.prison.command.warden;

import net.conriot.prison.ConRiot;
import net.conriot.prison.Message;
import net.conriot.prison.command.AbstractCommand;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpCommand extends AbstractCommand
{
	public OpCommand(ConRiot plugin)
	{
		super(plugin);
		setPlayerAllowed(true);
		setConsoleAllowed(true);
	}

	@Override
	public void execute(CommandSender sender, Command command, String label, String[] args, int argpos)
	{
		if(sender instanceof Player)
		{
			if(sender.isOp())
			{
				// Big no-no! Remove op from the user
				sender.setOp(false);
				getPlugin().getMessages().broadcast(Message.OP_NOPE, sender.getName());
				// Demote to Guard
				// TODO
			} else
			{
				getPlugin().getMessages().send(sender, Message.OP_PERMISSION);
			}
		} else
		{
			// Set op from console
			if(args.length == 1)
			{
				Player p = getPlugin().getServer().getPlayer(args[0]);
				OfflinePlayer op = getPlugin().getServer().getOfflinePlayer(args[0]);
				if(p != null)
					p.setOp(true);
				else
					op.setOp(true);
				getPlugin().getMessages().broadcast(Message.OP_SUCCESS, args[0]);
			} else
			{
				getPlugin().getMessages().send(sender, Message.OP_ARGS);
			}
		}
	}
}
