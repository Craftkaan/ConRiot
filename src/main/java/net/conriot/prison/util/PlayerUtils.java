package net.conriot.prison.util;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlayerUtils {
	public static String resolveNameOnline(String name) {
		// Check if the player name belongs to any player
		// currenty online, if so return a correctly cased
		// name.
		Player p = Bukkit.getPlayer(name);
		if(p != null)
			return p.getName();
		return null;
	}
	
	public static String resolveName(String name)
	{
		// Check if the player names has ever existed
		// and if so return correctly capitalized name.
		Player p = Bukkit.getPlayer(name);
		if(p != null)
			return p.getName();
		OfflinePlayer op = Bukkit.getOfflinePlayer(name);
		if(op.hasPlayedBefore())
			return op.getName();
		return null;
	}
	
	public static Player resolvePlayerOnline(String name) {
		// Return a Player instance for an online player
		// with the given name. Return null if no player
		// with that name exists.
		return Bukkit.getPlayer(name);
	}
	
	public static OfflinePlayer resolvePlayerOffline(String name)
	{
		// Return an OfflinePlayer instance for a player
		// with the given name. Return null if no player
		// with that name ever existed.
		return Bukkit.getOfflinePlayer(name);
	}
}
