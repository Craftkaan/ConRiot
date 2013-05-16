package net.conriot.prison.mine.refiller;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import net.conriot.prison.mine.Mine;
import net.conriot.prison.mine.MineManager;

public class BukkitMineRefiller implements MineRefiller
{
	// Fall-back implementation of MineRefiller
	// Targeting version 'Bukkit API'
	private MineManager mm;
	private Random rand;
	
	public BukkitMineRefiller(MineManager mm)
	{
		this.mm = mm;
		rand = new Random();
	}

	@Override
	public long refill(int tag)
	{
		long start, end;
		// Grab the time we started
		start = System.currentTimeMillis();
		// Iterate over all the mines
		for(Mine m : mm.getMines().values())
		{
			// Don't refill if tags don't match
			if(tag != m.getTag())
				continue;
			// Iterate over all blocks in the mine
			if(m.getType() != null)
			{
				int max = m.getType().length;
				if(max > 0)
				{
					for(int x = m.getMin().getBlockX(); x < m.getMax().getBlockX(); x++)
					{
						for(int y = m.getMin().getBlockY(); y < m.getMax().getBlockY(); y++)
						{
							for(int z = m.getMin().getBlockZ(); z < m.getMax().getBlockZ(); z++)
							{
								int r = rand.nextInt(max);
					            m.getWorld().getBlockAt(x, y, z).setTypeIdAndData(m.getType()[r], m.getData()[r], false);
							}
						}
					}
				}
			}
			// Iterate over all the players
			for (Player player : m.getWorld().getPlayers())
			{
				Location loc = player.getLocation();
				if (loc.getX() >= m.getMin().getBlockX() && loc.getX() < m.getMax().getBlockX() &&
					loc.getZ() >= m.getMin().getBlockZ() && loc.getZ() < m.getMax().getBlockZ() &&
					loc.getY() >= m.getMin().getBlockY() && loc.getY() < m.getMax().getBlockY())
				{
					loc.setY(m.getMax().getBlockY());
					player.teleport(loc);
				}
			}
			// Iterate over all items
			for (Entity item : m.getWorld().getEntities())
			{
				if (item instanceof Item)
				{
					Location loc = item.getLocation();
					if (loc.getX() >= m.getMin().getBlockX() && loc.getX() < m.getMax().getBlockX() &&
						loc.getZ() >= m.getMin().getBlockZ() && loc.getZ() < m.getMax().getBlockZ() &&
						loc.getY() >= m.getMin().getBlockY() && loc.getY() < m.getMax().getBlockY())
					{
						item.remove();
					}
				}
			}
		}
		// Grab the time we ended
		end = System.currentTimeMillis();
		// Return the difference in times
		return end - start;
	}
}
