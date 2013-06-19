package net.conriot.prison.command.warden;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.conriot.prison.ConRiot;
import net.conriot.prison.Message;
import net.conriot.prison.command.AbstractCommand;
import net.conriot.prison.util.PlayerUtils;
import net.conriot.prison.warden.Vip;

public class ExpirationCommand extends AbstractCommand
{
	public ExpirationCommand(ConRiot plugin)
	{
		super(plugin);
		setPlayerAllowed(true);
	}

	@Override
	public void execute(CommandSender sender, Command command, String label, String[] args, int argpos)
	{
		// expiration	[name] <- Optional, for wardens only
		// command		args[0]
		if(args.length == 1)
		{
			if(sender.hasPermission("conriot.guard.warden"))
			{
				String name = PlayerUtils.resolveName(args[0]);
				if(name != null)
				{
					Vip v = getPlugin().getVipManager().getVipRecord(name);
					if((v != null) && (v.getRank() <= 2))
					{
						if((v.getExpiration() != 0) && (v.getExpiration() > System.currentTimeMillis()))
						{
							int timeleft = (int) (v.getExpiration() - System.currentTimeMillis());
							int minutes = (timeleft / 1000) / 60;
							int hours = minutes / 60;
							minutes = minutes % 60;
							getPlugin().getMessages().send(sender, Message.WARDEN_VIP_EXPIRATION_OTHER, name, ("" + hours +" hours and " + minutes + " minutes"));
						} else
						{
							getPlugin().getMessages().send(sender, Message.WARDEN_VIP_EXPIRED_OTHER, name);
						}
					} else
					{
						getPlugin().getMessages().send(sender, Message.WARDEN_VIP_NO_RECORD, name);
					}
				} else
				{
					getPlugin().getMessages().send(sender, Message.WARDEN_VIP_INVALID_NAME, args[0]);
				}
			} else
			{
				getPlugin().getMessages().send(sender, Message.WARDEN_PERMISSION);
			}
		} else if(args.length == 0)
		{
			Vip v = getPlugin().getVipManager().getVipRecord(sender.getName());
			if((v != null) && (v.getRank() <= 2))
			{
				if((v.getExpiration() != 0) && (v.getExpiration() > System.currentTimeMillis()))
				{
					int timeleft = (int) (v.getExpiration() - System.currentTimeMillis());
					int minutes = (timeleft / 1000) / 60;
					int hours = minutes / 60;
					minutes = minutes % 60;
					getPlugin().getMessages().send(sender, Message.WARDEN_VIP_EXPIRATION, ("" + hours +" hours and " + minutes + " minutes"));
				} else
				{
					getPlugin().getMessages().send(sender, Message.WARDEN_VIP_EXPIRED);
				}
			} else
			{
				getPlugin().getMessages().send(sender, Message.WARDEN_VIP_NO_RECORD, sender.getName());
			}
		} else
		{
			getPlugin().getMessages().send(sender, Message.WARDEN_ARG_COUNT);
		}
	}
}