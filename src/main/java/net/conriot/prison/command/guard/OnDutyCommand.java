package net.conriot.prison.command.guard;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.conriot.prison.ConRiot;
import net.conriot.prison.Message;
import net.conriot.prison.PlayerData;
import net.conriot.prison.command.AbstractCommand;
import net.milkbowl.vault.permission.Permission;

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
		PlayerData playerData = getPlugin().getPlayerData().get(player);
		
		if (playerData == null || playerData.getGuardRank() == null)
		{
			getPlugin().getMessages().send(sender, Message.GUARD_NOT_GUARD);
			return;
		}
		
		if (playerData.isOnGuardDuty())
		{
			getPlugin().getMessages().send(sender, Message.GUARD_ALREADY_ON);
			return;
		}
		
		Permission perms = getPlugin().getPermission();
		
		List<String> groups = Arrays.asList(perms.getPlayerGroups(player));
		
		playerData.setNormalRanks(groups);
		
		// log just in case
		getPlugin().getLogger().info("[OnDutyCommand] " + player.getName() + " started at " + groups);
		
		playerData.setOnGuardDuty(true);
		
		// add to guard group
		perms.playerAddGroup(player, playerData.getGuardRank());
		
		// remove all other groups
		for (String group : groups)
		{
			perms.playerRemoveGroup(player, group);
		}
		
		//
		getPlugin().getLogger().info("[OnDutyCommand] " + player.getName() + " now has " + Arrays.asList(perms.getPlayerGroups(player)));
		
		getPlugin().getMessages().broadcast(Message.GUARD_ON, player.getName());
	}
}
