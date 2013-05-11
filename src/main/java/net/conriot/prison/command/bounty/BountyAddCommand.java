package net.conriot.prison.command.bounty;

import net.conriot.prison.ConRiot;
import net.conriot.prison.PlayerData;
import net.conriot.prison.command.AbstractCommand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BountyAddCommand extends AbstractCommand
{
	public BountyAddCommand(ConRiot plugin)
	{
		super(plugin);
		setPlayerAllowed(true);
	}

	@Override
	public void execute(CommandSender sender, Command command, String label, String[] args, int argpos)
	{
		// bounty 		add 		<target> 	<amount>
		// command		args[0]		args[1]		args[2]
		
		Player player = (Player) sender;
		
		if (args.length == 3)
		{
			double amount;
			
			try
			{
				amount = Double.parseDouble(args[2]);
			}
			catch (NumberFormatException ex)
			{
				player.sendMessage(ChatColor.RED + "You need to type a number instead of " + args[2]);
				return;
			}
			
			// TODO: configurable minimum bounty amount
			if (amount < 250.0)
			{
				player.sendMessage(ChatColor.RED + "The minimum bounty is $250");
			}
			
			Player target = Bukkit.getPlayer(args[1]);
			if (target == null)
			{
				player.sendMessage(ChatColor.RED + "You can't set bounty on offline players");
				return;
			}
			
			PlayerData targetData = getPlugin().getPlayerData().getOrCreate(target);
			
			/*
			try
			{
				getPlugin().getEconomy().takeMoney(player, amount);
			}
			catch (EconomyException ex)
			{
				player.sendMessage(ChatColor.RED + ex.getMessage());
				return;
			}
			*/
			
			double bounty = targetData.getBounty();
			bounty += amount;
			targetData.setBounty(bounty);
			
			// TODO: configurable message
			Bukkit.broadcastMessage(player.getName() + " added $" + amount + " bounty to " + target.getName() + ". [$" + bounty + " total]");
		}
		else
		{
			player.sendMessage("/bounty add <target> <amount>");
		}
	}
}
