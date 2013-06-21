package net.conriot.prison.command.guard;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.conriot.prison.ConRiot;
import net.conriot.prison.Message;
import net.conriot.prison.PlayerData;
import net.conriot.prison.command.AbstractCommand;

public class OffDutyCommand extends AbstractCommand
{
	public OffDutyCommand(ConRiot plugin)
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
		
		//Attempt to schedule the guard to go off duty
		if(getPlugin().getGuardManager().scheduleOffDuty(player))
		{
			// Announce that guard will be going off duty
			getPlugin().getMessages().broadcast(Message.GUARD_PENDING_OFF, player.getName());
		}
	}
}
