package net.conriot.prison.command.guard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.conriot.prison.ConRiot;
import net.conriot.prison.Message;
import net.conriot.prison.PlayerData;
import net.conriot.prison.command.AbstractCommand;
import net.milkbowl.vault.permission.Permission;

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
		PlayerData data = getPlugin().getPlayerData().get(player);
		
		if (data == null || !data.isOnGuardDuty())
		{
			getPlugin().getMessages().send(player, Message.GUARD_NOT_ON);
			return;
		}
		
		Permission perms = getPlugin().getPermission();
		
		// add normal groups to the player
		// groups must be added in reverse, pex add groups to the front
		List<String> groups = new ArrayList<String>(data.getNormalRanks());
		Collections.reverse(groups);
		for (String group : groups)
		{
			perms.playerAddGroup(player, group);
		}
		
		// remove guard group
		perms.playerRemoveGroup(player, data.getGuardRank());
		
		data.setOnGuardDuty(false);
		
		getPlugin().getMessages().broadcast(Message.GUARD_OFF, player.getName());
		
		getPlugin().getLogger().info("[OffDutyCommand] " + player.getName() + " is now " + Arrays.asList(perms.getPlayerGroups(player)));
	}
}
