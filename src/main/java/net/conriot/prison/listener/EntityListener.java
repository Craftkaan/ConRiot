package net.conriot.prison.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import net.conriot.prison.ConRiot;

public class EntityListener extends AbstractListener
{
	public EntityListener(ConRiot plugin)
	{
		super(plugin);
	}

	@EventHandler
	public void onAnimalSexyTime(CreatureSpawnEvent event)
	{
		if(event.getSpawnReason() == SpawnReason.BREEDING)
			event.setCancelled(true); // No little babbies allowed!
	}
}
