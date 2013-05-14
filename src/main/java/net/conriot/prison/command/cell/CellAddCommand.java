package net.conriot.prison.command.cell;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.conriot.prison.ConRiot;
import net.conriot.prison.cell.CellBlock;
import net.conriot.prison.command.AbstractCommand;

public class CellAddCommand extends AbstractCommand
{
	public CellAddCommand(ConRiot plugin)
	{
		super(plugin);
		setPlayerAllowed(true);
	}

	@Override
	public void execute(CommandSender sender, Command command, String label, String[] args, int argpos)
	{
		// cell 		add 		<cellId>	<regionId>
		// command		args[0]		args[1]		args[2]
		
		if(sender.isOp()) // OP Check until I know if we need to deal with perms
		{
			// Verify we have sufficient args
			if(args.length != 3)
			{
				sender.sendMessage(ChatColor.RED + "Improper number of arguments!");
				return;
			}
			
			// Get the block the player is looking at
			Player p = (Player) sender;
			Block b = p.getTargetBlock(null, 16);
			
			// Verify it is a sign
			if(b.getType() != Material.WALL_SIGN)
			{
				sender.sendMessage(ChatColor.RED + "You must be looking at a sign to add a cell!");
				return;
			}
			
			// Verify the block is in a cell block, get the cell block
			CellBlock cb = getPlugin().getCells().getCellBlock(b.getLocation());
			if(cb == null)
			{
				sender.sendMessage(ChatColor.RED + "Sign is not in a valid cell block!");
				return;
			}
			
			// Get the correct lock location
			Location lock = null;
			switch(b.getData())
			{
			case 0x2:
				lock = b.getLocation().add(1, -1, 0);
				break;
			case 0x3:
				lock = b.getLocation().add(-1, -1, 0);
				break;
			case 0x4:
				lock = b.getLocation().add(0, -1, -1);
				break;
			case 0x5:
				lock = b.getLocation().add(0, -1, 1);
				break;
			default:
				sender.sendMessage(ChatColor.RED + "Error determining sign's cardinal direction!");
				return;
			}
			
			// Attempt to add the cell
			if(cb.addCell(args[2], args[1], lock, b.getLocation(), p))
				sender.sendMessage(ChatColor.GREEN + "You have successfully added cell: " + args[1] + "!");
		} else
		{
			sender.sendMessage(ChatColor.RED + "You do not have permission to add cells!");
		}
	}
}
