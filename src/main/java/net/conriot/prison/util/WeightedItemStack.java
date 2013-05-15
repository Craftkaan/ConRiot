package net.conriot.prison.util;

import org.bukkit.inventory.ItemStack;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WeightedItemStack
{
	private int weight;
	private ItemStack item;
}
