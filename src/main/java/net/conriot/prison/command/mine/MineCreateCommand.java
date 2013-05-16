package net.conriot.prison.command.mine;

import net.conriot.prison.ConRiot;
import net.conriot.prison.Message;
import net.conriot.prison.command.AbstractCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

public class MineCreateCommand extends AbstractCommand
{
	public MineCreateCommand(ConRiot plugin)
	{
		super(plugin);
		setPlayerAllowed(true);
	}

	@Override
	public void execute(CommandSender sender, Command command, String label, String[] args, int argpos)
	{
		// mine 		create 		<id>		<tag>
		// command		args[0]		args[1]		args[2]
		if(sender.isOp()) // OP Check until I know if we need to deal with perms
		{
			if(getPlugin().getMines() != null)
			{
				if(getPlugin().getMines().isValid()) 
				{
					if(args.length == 3)
					{
						WorldEditPlugin w = getWorldEdit();
						if(w.getSelection((Player) sender) != null) {
							if(getPlugin().getMines().create(args[1], w.getSelection((Player) sender), Integer.parseInt(args[2])))
								getPlugin().getMessages().send(sender, Message.MINE_CREATE_SUCCESS, args[1]);
							else
								getPlugin().getMessages().send(sender, Message.MINE_CREATE_FAILURE, args[1]);
						} else
						{
							getPlugin().getMessages().send(sender, Message.MINE_CREATE_SELECTION);
						}
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
	
	private WorldEditPlugin getWorldEdit()
	{
	    Plugin wep = getPlugin().getServer().getPluginManager().getPlugin("WorldEdit");
	    if (wep == null || !(wep instanceof WorldEditPlugin))
	    {
	        return null;
	    }
	    return (WorldEditPlugin) wep;
	}
}
