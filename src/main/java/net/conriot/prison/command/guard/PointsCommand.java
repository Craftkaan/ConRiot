package net.conriot.prison.command.guard;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.conriot.prison.ConRiot;
import net.conriot.prison.Message;
import net.conriot.prison.PlayerData;
import net.conriot.prison.command.AbstractCommand;

public class PointsCommand extends AbstractCommand
{
	public PointsCommand(ConRiot plugin)
	{
		super(plugin);
		setPlayerAllowed(true);
	}

	@Override
	public void execute(CommandSender sender, Command command, String label, String[] args, int argpos)
	{
		Player player = (Player) sender;
		
		// Validate that player is a guard
		PlayerData pd = getPlugin().getPlayerData().get(player);
		if(pd == null)
		{
			getPlugin().getMessages().send(player, Message.GUARD_NOT_GUARD);
			return;
		} else if(pd.getGuardRank() == null)
		{
			getPlugin().getMessages().send(player, Message.GUARD_NOT_GUARD);
			return;
		}
		
		// Send message with how many points the guard has
		getPlugin().getMessages().send(player, Message.GUARD_POINTS, pd.getGuardRank(), (int)pd.getGuardPoints());
	}
}
