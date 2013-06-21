package net.conriot.prison;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryBackup {
	private Player player;
	private ItemStack[] contents;
	private ItemStack[] armor;
	private int slot;

	public InventoryBackup(Player player)
	{
		this.player = player;
		// Automatically back up the player's inventory
		this.contents = player.getInventory().getContents().clone();
		this.armor = player.getInventory().getArmorContents().clone();
		this.slot = player.getInventory().getHeldItemSlot();
		// Automatically clear the player's inventory
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
	}
	
	public void restore()
	{
		// Clear the current inventory
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		// Restore the old inventory items
		player.getInventory().setContents(contents);
		player.getInventory().setArmorContents(armor);
		player.getInventory().setHeldItemSlot(slot);
	}
}
