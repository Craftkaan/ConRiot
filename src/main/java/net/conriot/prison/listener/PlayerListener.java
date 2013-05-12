package net.conriot.prison.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import net.conriot.prison.ConRiot;
import net.conriot.prison.PlayerData;
import net.conriot.prison.economy.EconomyException;

public class PlayerListener extends AbstractListener
{
	public PlayerListener(ConRiot plugin)
	{
		super(plugin);
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event)
	{
		Player player = event.getEntity();
		PlayerData data = getPlugin().getPlayerData().get(player);
		if (data != null && data.getBounty() > 0)
		{
			if (player.getKiller() instanceof Player)
			{
				Player hunter = (Player) player.getKiller();
				// no self bounty
				if (hunter != player)
				{
					try
					{
						getPlugin().getEconomy().giveMoney(hunter, data.getBounty());
						// TODO: configurable message
						Bukkit.broadcastMessage(hunter.getName() + " collected a bounty of " + getPlugin().getEconomy().format(data.getBounty()) + " for killing " + player.getName());
						data.setBounty(0);
					}
					catch (EconomyException ex)
					{
						getPlugin().getLogger().warning("Error giving " + hunter.getName() + " the bounty for " + player.getName());
						getPlugin().getLogger().warning(ex.getMessage());
					}
				}
			}
		}
	}
}
