package net.conriot.prison.command;

import java.util.HashMap;
import java.util.Map;

import net.conriot.prison.ConRiot;

import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class AbstractCommand implements CommandExecutor
{
	private ConRiot plugin;
	private boolean playerAllowed;
	private boolean consoleAllowed;
	private boolean blockAllowed;
	private Map<String, AbstractCommand> subCommands = new HashMap<String, AbstractCommand>();

	public AbstractCommand(ConRiot plugin)
	{
		this.plugin = plugin;
		playerAllowed = false;
		consoleAllowed = false;
		blockAllowed = false;
	}

	protected void setPlayerAllowed(boolean allowed)
	{
		playerAllowed = allowed;
	}

	protected void setConsoleAllowed(boolean allowed)
	{
		consoleAllowed = allowed;
	}

	protected void setBlockAllowed(boolean allowed)
	{
		blockAllowed = allowed;
	}
	
	protected void allowAll()
	{
		playerAllowed = true;
		consoleAllowed = true;
		blockAllowed = true;
	}

	protected void addSubCommand(AbstractCommand command, String... aliases)
	{
		for (String alias : aliases)
		{
			subCommands.put(alias.toLowerCase(), command);
		}
	}

	protected AbstractCommand getSubCommand(String name)
	{
		return subCommands.get(name);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		preExecute(sender, command, label, args, 0);
		return true;
	}

	private void preExecute(CommandSender sender, Command command, String label, String[] args, int argpos)
	{
		if (sender instanceof Player)
		{
			if (!playerAllowed)
			{
				sender.sendMessage(ChatColor.RED
						+ "This command can not be used by in-game players");
				return;
			}
		}
		else if (sender instanceof BlockCommandSender)
		{
			if (!blockAllowed)
			{
				BlockCommandSender blockSender = (BlockCommandSender) sender;
				plugin.getLogger().warning("Command block at " + blockSender.getBlock().getLocation() + " was denied access to command /" + label);
				return;
			}
		}
		else
		{
			if (!consoleAllowed)
			{
				plugin.getLogger().warning("/" + label + " cannot be used by the console");
				return;
			}
		}

		AbstractCommand subCommand = null;
		
		if (argpos < args.length)
		{
			subCommand = getSubCommand(args[argpos].toLowerCase());
		}
		
		if (subCommand == null)
		{
			execute(sender, command, label, args, argpos);
		}
		else
		{
			subCommand.preExecute(sender, command, label, args, argpos + 1);
		}
	}

	public abstract void execute(CommandSender sender, Command command, String label, String[] args, int argpos);

	public ConRiot getPlugin()
	{
		return plugin;
	}
}