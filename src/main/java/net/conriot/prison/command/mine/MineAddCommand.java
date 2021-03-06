package net.conriot.prison.command.mine;

import net.conriot.prison.ConRiot;
import net.conriot.prison.Message;
import net.conriot.prison.command.AbstractCommand;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class MineAddCommand extends AbstractCommand
{
	public MineAddCommand(ConRiot plugin)
	{
		super(plugin);
		setPlayerAllowed(true);
	}

	@Override
	public void execute(CommandSender sender, Command command, String label, String[] args, int argpos)
	{
		// mine 		add 		<id>		<typeId>	<data>		<weight>
		// command		args[0]		args[1]		args[2]		args[3]		args[4]
		if(sender.isOp()) // OP Check until I know if we need to deal with perms
		{
			if(getPlugin().getMines() != null)
			{
				if(getPlugin().getMines().isValid()) 
				{
					if(args.length == 5)
					{
						if(getPlugin().getMines().addMaterial(args[1], Integer.parseInt(args[2]), (byte) Integer.parseInt(args[3]), Integer.parseInt(args[4])))
							getPlugin().getMessages().send(sender, Message.MINE_ADD_SUCCESS, args[1], Material.getMaterial(Integer.parseInt(args[2])), "0x" + Integer.toHexString(Integer.parseInt(args[3])), args[4]);
						else
							getPlugin().getMessages().send(sender, Message.MINE_ADD_FAILURE, args[1], Material.getMaterial(Integer.parseInt(args[2])), "0x" + Integer.toHexString(Integer.parseInt(args[3])));
					} else
					{
						getPlugin().getMessages().send(sender, Message.MINE_ARG_COUNT);
					}
				} else
				{
					getPlugin().getMessages().send(sender, Message.MINE_MANAGER_OFFLINE);
				}
			} else
			{
				getPlugin().getMessages().send(sender, Message.MINE_MANAGER_OFFLINE);
			}
		} else
		{
			getPlugin().getMessages().send(sender, Message.MINE_PERMISSION);
		}
	}
}
