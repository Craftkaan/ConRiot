package net.conriot.prison.guard;

import java.util.LinkedList;
import java.util.List;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GuardGear {
	private Player player;
	@Getter private List<ItemStack> locked;
	
	public GuardGear(Player player)
	{
		this.player = player;
		locked = new LinkedList<ItemStack>();
	}
	
	public void give()
	{
		// Clear current inventory
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		
		// Heal the player
		player.setHealth(20);
        player.setFoodLevel(20);
        
        // Create the gear for the player
        ItemStack stick = new ItemStack(Material.STICK, 1);
        ItemStack wstick = new ItemStack(Material.STICK, 1);
        ItemStack hat = new ItemStack(Material.CHAINMAIL_HELMET, 1);
        ItemStack chest = new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1);
        ItemStack leggings = new ItemStack(Material.CHAINMAIL_LEGGINGS, 1);
        ItemStack boots = new ItemStack(Material.CHAINMAIL_BOOTS, 1);
        
        // Name all the gear
        ItemMeta stickMeta = stick.getItemMeta();
        stickMeta.setDisplayName(ChatColor.DARK_PURPLE + "Guard stick");
        stick.setItemMeta(stickMeta);
        ItemMeta wstickMeta = wstick.getItemMeta();
        wstickMeta.setDisplayName(ChatColor.RED + "Warning stick");
        wstick.setItemMeta(wstickMeta);
        ItemMeta hatMeta = hat.getItemMeta();
        hatMeta.setDisplayName(ChatColor.DARK_PURPLE + "Ballistic Helmet");
        hat.setItemMeta(hatMeta);
        ItemMeta chestMeta = chest.getItemMeta();
        chestMeta.setDisplayName(ChatColor.DARK_PURPLE + "Ballistic Vest");
        chest.setItemMeta(chestMeta);
        ItemMeta leggingsMeta = leggings.getItemMeta();
        leggingsMeta.setDisplayName(ChatColor.DARK_PURPLE + "Ballistic Leggings");
        leggings.setItemMeta(leggingsMeta);
        ItemMeta bootsMeta = boots.getItemMeta();
        bootsMeta.setDisplayName(ChatColor.DARK_PURPLE + "Ballistic Shoes");
        boots.setItemMeta(bootsMeta);
        
        // Enchant all the gear
        stick.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 10);
        stick.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
        wstick.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 2);
        wstick.addUnsafeEnchantment(Enchantment.KNOCKBACK, 3);
        hat.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 10);
        chest.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 10);
        leggings.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 10);
        boots.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 10);
        hat.addUnsafeEnchantment(Enchantment.THORNS, 3);
        chest.addUnsafeEnchantment(Enchantment.THORNS, 3);
        leggings.addUnsafeEnchantment(Enchantment.THORNS, 3);
        boots.addUnsafeEnchantment(Enchantment.THORNS, 3);
        hat.addUnsafeEnchantment(Enchantment.DURABILITY, 50);
        chest.addUnsafeEnchantment(Enchantment.DURABILITY, 50);
        leggings.addUnsafeEnchantment(Enchantment.DURABILITY, 50);
        boots.addUnsafeEnchantment(Enchantment.DURABILITY, 50);
        
        // Add the item to the player inventory
        player.getInventory().addItem(stick);
        player.getInventory().addItem(wstick);
        player.getInventory().setHelmet(hat);
        player.getInventory().setChestplate(chest);
        player.getInventory().setLeggings(leggings);
        player.getInventory().setBoots(boots);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kit guard " + player.getName());
        
        // Lock the current inventory
        lock();
	}
	
	public void buff()
	{
		// Add potion effects
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1));
	}
	
	public void debuff()
	{
		// Remove potion effects
		player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
        player.removePotionEffect(PotionEffectType.REGENERATION);
	}
	
	public void lock()
	{
		// Clear the previous locked list
		locked.clear();
		
		// Add the current inventory contents
		for(ItemStack is : player.getInventory().getContents())
		{
			if(is != null)
				if(is.getType() != Material.AIR)
					locked.add(is);
		}
		// Add the current armor contents
		for(ItemStack is : player.getInventory().getArmorContents())
		{
			if(is != null)
				if(is.getType() != Material.AIR)
					locked.add(is);
		}
	}
	
	public boolean isLocked(ItemStack is)
	{
		return locked.contains(is);
	}
}
