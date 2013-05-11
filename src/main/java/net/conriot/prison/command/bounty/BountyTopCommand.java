package net.conriot.prison.command.bounty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.conriot.prison.ConRiot;
import net.conriot.prison.PlayerData;
import net.conriot.prison.command.AbstractCommand;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class BountyTopCommand extends AbstractCommand
{
	public BountyTopCommand(ConRiot plugin)
	{
		super(plugin);
		setPlayerAllowed(true);
		setConsoleAllowed(true);
	}

	@Override
	public void execute(CommandSender sender, Command command, String label, String[] args, int argpos)
	{
		List<ComparableBounty> list = new ArrayList<ComparableBounty>();
		
		for (PlayerData data : getPlugin().getPlayerData().getAll())
		{
			if (data.getBounty() > 0)
			{
				list.add(new ComparableBounty(data));
			}
		}
		
		if (list.isEmpty())
		{
			sender.sendMessage(ChatColor.RED + "No bounties have been entered yet");
			return;
		}
		
		Collections.sort(list);
		
		int end = list.size() < 5 ? list.size() : 5;
		
		// TODO: message config
		sender.sendMessage("Top 5 bounties");
		for (int i = 0; i < end; i++)
		{
			sender.sendMessage((i + 1) + ". " + list.get(i).name + " " + getPlugin().getEconomy().format(list.get(i).bounty));
		}
	}
	
	private static class ComparableBounty implements Comparable<ComparableBounty>
	{
		private final String name;
		private final int bounty;

		public ComparableBounty(PlayerData data)
		{
			this.name = data.getName();
			this.bounty = data.getBounty();
		}
		
		@Override
		public int compareTo(ComparableBounty o)
		{
			if (bounty > o.bounty) return -1;
            if (bounty < o.bounty) return 1;
            return 0;
		}
	}
}
