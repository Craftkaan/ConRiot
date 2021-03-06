package net.conriot.prison.command.bounty;

import net.conriot.prison.ConRiot;
import net.conriot.prison.Message;
import net.conriot.prison.PlayerData;
import net.conriot.prison.command.AbstractCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class BountyCommand extends AbstractCommand
{
	public BountyCommand(ConRiot plugin)
	{
		super(plugin);
		allowAll();
		
		addSubCommand(new BountyAddCommand(plugin), "add");
		addSubCommand(new BountyMineCommand(plugin), "mine", "me");
		addSubCommand(new BountyListCommand(plugin), "list", "ls");
		addSubCommand(new BountyTopCommand(plugin), "top", "top5");
		addSubCommand(new BountyHelpCommand(plugin), "help", "?");
	}

	@Override
	public void execute(CommandSender sender, Command command, String label, String[] args, int argpos)
	{
		if (args.length == 1)
		{
			PlayerData data = getPlugin().getPlayerData().get(args[0]);
			if (data == null)
			{
				getPlugin().getMessages().send(sender, Message.BOUNTY_HIS_NONE, args[0]);
			}
			else if (data.getBounty() == 0)
			{
				getPlugin().getMessages().send(sender, Message.BOUNTY_HIS_NONE, args[0]);
			}
			else
			{
				getPlugin().getMessages().send(sender, Message.BOUNTY_HIS, args[0], getPlugin().getEconomy().format(data.getBounty()));
			}
		}
		else
		{
			getSubCommand("help").execute(sender, command, label, args, argpos + 1);
		}
	}
}
