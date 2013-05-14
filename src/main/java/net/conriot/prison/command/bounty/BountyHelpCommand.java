package net.conriot.prison.command.bounty;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.conriot.prison.ConRiot;
import net.conriot.prison.Message;
import net.conriot.prison.command.AbstractCommand;

public class BountyHelpCommand extends AbstractCommand
{
	public BountyHelpCommand(ConRiot plugin)
	{
		super(plugin);
		allowAll();
	}

	@Override
	public void execute(CommandSender sender, Command command, String label, String[] args, int argpos)
	{
		getPlugin().getMessages().send(sender, Message.BOUNTY_HELP_HEADER);
		getPlugin().getMessages().send(sender, Message.BOUNTY_HELP_ITEM, "/bounty", " <player>", "See the specified player's bounty");
		getPlugin().getMessages().send(sender, Message.BOUNTY_HELP_ITEM, "/bounty add", "", "Add a bounty to the specified player");
		getPlugin().getMessages().send(sender, Message.BOUNTY_HELP_ITEM, "/bounty me", "", "See the bounty on your head");
		getPlugin().getMessages().send(sender, Message.BOUNTY_HELP_ITEM, "/bounty list", " [page]", "See the list of bounties");
		getPlugin().getMessages().send(sender, Message.BOUNTY_HELP_ITEM, "/bounty top5", "", "See the top 5 bounties");
	}
}
