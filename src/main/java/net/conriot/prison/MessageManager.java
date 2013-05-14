package net.conriot.prison;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.conriot.prison.util.ConfigAccessor;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class MessageManager
{
	private Map<Message, String> messages = new HashMap<Message, String>();
	
	public MessageManager()
	{
		// TODO
	}
	
	public void load(Plugin plugin)
	{
		YamlConfiguration config = new YamlConfiguration();
		try
		{
			config.load(new File(plugin.getDataFolder(), "messages.yml"));
		}
		catch (Exception ex)
		{
			// No file found, copy over the default instead
			ConfigAccessor cfg = new ConfigAccessor((JavaPlugin) plugin, "messages.yml");
			cfg.saveDefaultConfig();
			plugin.getLogger().info("Default messages.yml was created!");
			// Get the messages.yml from the ConfigAccessor
			config = (YamlConfiguration) cfg.getConfig();
		}
		for (String key : config.getKeys(false))
		{
			Message message;
			try
			{
				message = Message.valueOf(key.toUpperCase());
			}
			catch (IllegalArgumentException ex)
			{
				plugin.getLogger().warning("Warning while loading messages.yml: There is no such message called " + key);
				continue;
			}
			String format = config.getString(key);
			if (format == null)
			{
				plugin.getLogger().warning("Warning while loading messages.yml: Key " + key + " could not be parsed as a string");
			}
			else
			{
				format = ChatColor.translateAlternateColorCodes('&', format);
				messages.put(message, format);
			}
		}
	}
	
	public String format(Message message, Object... args)
	{
		String str = messages.get(message);
		if (str == null)
		{
			return "Message." + message;
		}
		String[] vars = message.getVars();
		for (int i = 0; i < vars.length; i++)
		{
			str = str.replace("{" + vars[i] + "}", args[i].toString());
		}
		return str;
	}
	
	public void send(CommandSender user, Message message, Object... args)
	{
		user.sendMessage(format(message, args));
	}
	
	public void broadcast(Message message, Object... args)
	{
		Bukkit.broadcastMessage(format(message, args));
	}
}
