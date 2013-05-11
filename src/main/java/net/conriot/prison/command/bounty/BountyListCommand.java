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

public class BountyListCommand extends AbstractCommand
{

	public BountyListCommand(ConRiot plugin)
	{
		super(plugin);
		setPlayerAllowed(true);
		setConsoleAllowed(true);
	}

	@Override
	public void execute(CommandSender sender, Command command, String label, String[] args, int argpos)
	{
		// bounty 		list 		[page]
		// command		args[0]		args[1]

		// extract page argument if exists or default to 1
		int page = 1;
		if (args.length == 2)
		{
			try
			{
				page = Integer.parseInt(args[1]);
			}
			catch (NumberFormatException ex)
			{
				// fallback to page 1 on non-integer input
			}
		}
		
		// collect all players with more than 0 bounty into a list
		List<NameComparableBounty> list = new ArrayList<>();
		for (PlayerData data : getPlugin().getPlayerData().getAll())
		{
			if (data.getBounty() > 0.0)
			{
				list.add(new NameComparableBounty(data));
			}
		}
		
		if (list.isEmpty())
		{
			sender.sendMessage(ChatColor.RED + "No bounties have been entered yet");
			return;
		}
		
		Collections.sort(list);

		int pagesize = 10;
		int pages = list.size() / pagesize + 1;
		
		if (page > pages)
		{
			page = pages;
		}
		
		int start = (page - 1) * pagesize;
		int end = start + pagesize;
		
		if (end > list.size())
		{
			end = list.size();
		}
		
		// TODO: output format configurable
		sender.sendMessage("Bounty list page " + page + " of " + pages);
		for (int i = start; i < end; i++)
		{
			sender.sendMessage(String.format("%02d. %s $%,d", i + 1, list.get(i).name, list.get(i).bounty));
		}
	}
	
	private static class NameComparableBounty implements Comparable<NameComparableBounty> 
	{
        public final int bounty;
        public final String name;
        private final String lowerName;
        
        public NameComparableBounty(PlayerData data) 
        {
            bounty = data.getBounty();
            name = data.getName();
            lowerName = name.toLowerCase();
        }
        
        @Override
        public int compareTo(NameComparableBounty o) 
        {
            return lowerName.compareTo(o.lowerName);
        }
    }
}
