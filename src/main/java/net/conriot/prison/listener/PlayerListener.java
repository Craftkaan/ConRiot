package net.conriot.prison.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import net.conriot.prison.ConRiot;
import net.conriot.prison.Message;
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
				// Prevent self bounty
				if (hunter != player)
				{
					try
					{
						getPlugin().getEconomy().giveMoney(hunter, data.getBounty());
						getPlugin().getMessages().broadcast(Message.BOUNTY_COLLECTED, hunter.getName(), player.getName(), data.getBounty());
						data.setBounty(0);
						data.save();
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
