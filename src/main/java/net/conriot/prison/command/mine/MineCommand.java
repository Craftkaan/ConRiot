package net.conriot.prison.command.mine;

import net.conriot.prison.ConRiot;
import net.conriot.prison.Message;
import net.conriot.prison.command.AbstractCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class MineCommand extends AbstractCommand
{
	public MineCommand(ConRiot plugin)
	{
		super(plugin);
		setPlayerAllowed(true);
		
		addSubCommand(new MineCreateCommand(plugin), "create");
		addSubCommand(new MineDeleteCommand(plugin), "delete");
		addSubCommand(new MineAddCommand(plugin), "add");
		addSubCommand(new MineRemoveCommand(plugin), "remove");
		addSubCommand(new MineListCommand(plugin), "list");
	}
	
	@Override
	public void execute(CommandSender sender, Command command, String label, String[] args, int argpos)
	{
		getPlugin().getMessages().send(sender, Message.MINE_HELP_HEADER);
		getPlugin().getMessages().send(sender, Message.MINE_HELP_ITEM, "/mine create ", "<id>", "Create a mine using selected area with the given id.");
		getPlugin().getMessages().send(sender, Message.MINE_HELP_ITEM, "/mine delete ", "<id>", "Deletes the mine with the given id.");
		getPlugin().getMessages().send(sender, Message.MINE_HELP_ITEM, "/mine add ", "<id> <type> <data> <weight>", "Adds/sets the given mine material.");
		getPlugin().getMessages().send(sender, Message.MINE_HELP_ITEM, "/mine remove ", "<id> <type> <data>", "Removes the given mine material.");
		getPlugin().getMessages().send(sender, Message.MINE_HELP_ITEM, "/mine list ", "<id>", "Lists the current materials for the given mine.");
	}
}
