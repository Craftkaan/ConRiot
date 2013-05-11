package net.conriot.prison.command.bounty;

import net.conriot.prison.ConRiot;
import net.conriot.prison.PlayerData;
import net.conriot.prison.command.AbstractCommand;
import net.conriot.prison.economy.EconomyException;
import net.conriot.prison.economy.EconomyManager;

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
			int amount;
			
			try
			{
				amount = Integer.parseInt(args[2]);
			}
			catch (NumberFormatException ex)
			{
				player.sendMessage(ChatColor.RED + "You need to type a non-decimal number instead of " + args[2]);
				return;
			}
			
			EconomyManager eco = getPlugin().getEconomy();
			
			// TODO: configurable minimum bounty amount
			double min = 250.0;
			if (amount < min)
			{
				player.sendMessage(ChatColor.RED + "The minimum bounty is " + eco.format(min));
				return;
			}
			
			Player target = Bukkit.getPlayer(args[1]);
			if (target == null)
			{
				player.sendMessage(ChatColor.RED + "You can't set bounty on offline players");
				return;
			}
			
			PlayerData targetData = getPlugin().getPlayerData().getOrCreate(target);
			
			try
			{
				getPlugin().getEconomy().takeMoney(player, amount);
			}
			catch (EconomyException ex)
			{
				player.sendMessage(ChatColor.RED + ex.getMessage());
				return;
			}
			
			int bounty = targetData.getBounty();
			bounty += amount;
			targetData.setBounty(bounty);
			
			// TODO: configurable message
			Bukkit.broadcastMessage(player.getName() + " added " + eco.format(amount) + " bounty to " + target.getName() + ". [" + eco.format(bounty) + " total]");
		}
		else
		{
			player.sendMessage("/bounty add <target> <amount>");
		}
	}
}
