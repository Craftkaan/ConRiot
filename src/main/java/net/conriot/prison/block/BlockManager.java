package net.conriot.prison.block;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;

import net.conriot.prison.ConRiot;
import net.conriot.prison.util.ConfigAccessor;

public class BlockManager
{
	private ConRiot plugin;
	private ConfigAccessor config;
	private List<BlockConfig> blocks;

	public BlockManager(ConRiot plugin)
	{
		this.plugin = plugin;
		config = new ConfigAccessor(plugin, "blocks.yml");
		config.saveDefaultConfig();
		parseConfig();
	}
	
	public void parseConfig()
	{
		/*
		 * block:[data]:
		 *   'item:[data]': weight
		 */
		plugin.getLogger().info("Loading blocks.yml");
		FileConfiguration config = this.config.getConfig();
		blocks = new ArrayList<BlockConfig>();
		for (String key : config.getKeys(false))
		{
			try
			{
				blocks.add(new BlockConfig(config.getConfigurationSection(key)));
			}
			catch (InvalidBlockConfigException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public boolean doBlockDrops(Block block)
	{
		for (BlockConfig blockConfig : blocks)
		{
			if (blockConfig.matchesBlock(block))
			{
				block.getWorld().dropItemNaturally(block.getLocation(), blockConfig.randomDrop());
				return true;
			}
		}
		return false;
	}
}
