package net.conriot.prison.mine.refiller;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_5_R3.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import net.conriot.prison.mine.Mine;
import net.conriot.prison.mine.MineManager;
import net.minecraft.server.v1_5_R3.Chunk;
import net.minecraft.server.v1_5_R3.ChunkCoordIntPair;

public class V1_5_R3MineRefiller implements MineRefiller
{
	// High performance implementation of MineRefiller
	// Targeting version 'v1_5_R3'
	private MineManager mm;
	private Random rand;
	
	public V1_5_R3MineRefiller(MineManager mm)
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
			Map<ChunkCoordIntPair, World> chunks = new HashMap<ChunkCoordIntPair, World>();
			// Iterate over all blocks in the mine
			net.minecraft.server.v1_5_R3.World cworld = ((CraftWorld) m.getWorld()).getHandle();
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
					            Chunk chunk = cworld.getChunkAt(x >> 4, z >> 4);
					            chunk.a(x & 0x0f, y, z & 0x0f, m.getType()[r], m.getData()[r]);
					            chunks.put(chunk.l(), m.getWorld());
							}
						}
					}
				}
			}
			// Send chunk updates
			for(Entry<ChunkCoordIntPair, World> c : chunks.entrySet())
				c.getValue().refreshChunk(c.getKey().x, c.getKey().z);
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
