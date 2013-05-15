package net.conriot.prison.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.conriot.prison.util.WeightedItemStack;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class BlockConfig
{
	private static Random random = new Random();
	
	private MaterialData blockType;
	private List<WeightedItemStack> drops = new ArrayList<WeightedItemStack>();
	private int weightSum = 0;
	
	public BlockConfig(ConfigurationSection config) throws InvalidBlockConfigException
	{
		blockType = parseMaterialData(config.getName(), (byte) -1);
		for (String key : config.getKeys(false))
		{
			int weight = config.getInt(key);
			if (weight == 0)
			{
				// also catches non-integer keys since they return 0
				throw new InvalidBlockConfigException("Invalid weight, must be integer greater than 0");
			}
			drops.add(new WeightedItemStack(weight, parseMaterialData(key, (byte) 0).toItemStack()));
			weightSum += weight;
		}
	}
	
	private MaterialData parseMaterialData(String str, byte data) throws InvalidBlockConfigException
	{
		String[] args = str.split(":");
		if (args.length > 2)
		{
			throw new InvalidBlockConfigException("Expected material:[data], got " + str);
		}
		Material material = Material.matchMaterial(args[0]);
		if (material == null)
		{
			throw new InvalidBlockConfigException("Unknown material " + args[0]);
		}
		if (args.length == 2)
		{
			try
			{
				data = Byte.parseByte(args[1]);
			}
			catch (NumberFormatException ex)
			{
				throw new InvalidBlockConfigException("Expected byte integer, got " + args[1]);
			}
		}
		return new MaterialData(material, data);
	}
	
	public boolean matchesBlock(Block block)
	{
		// -1 data in blocktype matches any data, "wool:-1" or "wool" will match all wools for example
		return (blockType.getData() == -1 || blockType.getData() == block.getData()) && blockType.getItemType() == block.getType();
	}
	
	public ItemStack randomDrop()
	{
		int i = random.nextInt(weightSum) + 1;
		for (WeightedItemStack drop : drops)
		{
			i -= drop.getWeight();
			if (i <= 0)
			{
				ItemStack item = drop.getItem().clone();
				item.setAmount(1);
				return item;
			}
		}
		return null;
	}
}
