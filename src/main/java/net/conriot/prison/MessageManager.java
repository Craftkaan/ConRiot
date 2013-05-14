package net.conriot.prison;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class MessageManager
{
	private Map<Message, String> messages = new HashMap<Message, String>();
	
	public MessageManager()
	{
		// TODO Auto-generated constructor stub
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
			// TODO: create default when not existing
			plugin.getLogger().warning("Exception while loading messages.yml");
			ex.printStackTrace();
			return;
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
