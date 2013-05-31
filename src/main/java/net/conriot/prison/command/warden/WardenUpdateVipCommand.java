package net.conriot.prison.command.warden;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.conriot.prison.ConRiot;
import net.conriot.prison.Message;
import net.conriot.prison.command.AbstractCommand;
import net.conriot.prison.util.PlayerUtils;

public class WardenUpdateVipCommand extends AbstractCommand
{

	public WardenUpdateVipCommand(ConRiot plugin)
	{
		super(plugin);
		setPlayerAllowed(true);
		setConsoleAllowed(true);
	}

	@Override
	public void execute(CommandSender sender, Command command, String label, String[] args, int argpos)
	{
		// warden 		updatevip 	<name>		<amount>
		// command		args[0]		args[1]		args[2]
		if(sender.hasPermission("conriot.guard.warden"))
		{
			if(args.length == 3)
			{
				String name = PlayerUtils.resolveName(args[1]);
				if(name != null)
				{
					int amount = Integer.parseInt(args[2]);
					if(amount > 0)
					{
						if(!getPlugin().getVipManager().addDonation(name, amount))
						{
							getPlugin().getMessages().send(sender, Message.WARDEN_VIP_PENDING, name);
						}
					} else
					{
						getPlugin().getMessages().send(sender, Message.WARDEN_VIP_INVALID_AMOUNT);
					}
				} else
				{
					getPlugin().getMessages().send(sender, Message.WARDEN_VIP_INVALID_NAME, args[1]);
				}
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
