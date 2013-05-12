package net.conriot.prison.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.conriot.prison.ConRiot;

public class BlockListener extends AbstractListener
{
	public BlockListener(ConRiot plugin)
	{
		super(plugin);
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event)
	{
		final Block block = event.getBlock();
		final Material type = block.getType();
		
		// replanter for crops/potato/carrot
		if (type == Material.CROPS || type == Material.POTATO || type == Material.CARROT)
		{
			if (block.getData() < 7)
			{
				event.setCancelled(true);
			}
			else
			{
				new BukkitRunnable()
				{
					public void run()
					{
						block.setType(type);
					}
				}.runTask(getPlugin());
			}
		}
		// replanter for nether warts
		else if (type == Material.NETHER_WARTS)
		{
			if (block.getData() < 3)
			{
				event.setCancelled(true);
			}
			else
			{
				new BukkitRunnable()
				{
					public void run()
					{
						block.setType(type);
					}
				}.runTask(getPlugin());
			}
		}
	}
}
