package net.conriot.prison.command.guard;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.conriot.prison.ConRiot;
import net.conriot.prison.Message;
import net.conriot.prison.PlayerData;
import net.conriot.prison.command.AbstractCommand;

public class OnDutyCommand extends AbstractCommand
{
	public OnDutyCommand(ConRiot plugin)
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
		
		//Attempt to schedule the guard to go on duty
		if(getPlugin().getGuardManager().scheduleOnDuty(player))
		{
			// Announce that guard will be going on duty
			getPlugin().getMessages().broadcast(Message.GUARD_PENDING_ON, player.getName());
		}
	}
}
