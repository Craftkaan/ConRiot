package net.conriot.prison.listener;

import lombok.Getter;

import net.conriot.prison.ConRiot;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class AbstractListener implements Listener
{
	@Getter private ConRiot plugin;

	public AbstractListener(ConRiot plugin)
	{
		this.plugin = plugin;
	}
	
	public void register()
	{
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
}
