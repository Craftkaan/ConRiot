package net.conriot.prison;

import lombok.Getter;
import net.conriot.prison.cells.CellManager;
import net.conriot.prison.command.bounty.BountyCommand;
import net.conriot.prison.command.guard.OffDutyCommand;
import net.conriot.prison.command.guard.OnDutyCommand;
import net.conriot.prison.command.guard.PointsCommand;
import net.conriot.prison.command.guard.ShuCommand;
import net.conriot.prison.command.guard.SpotCommand;
import net.conriot.prison.economy.EconomyManager;

import org.bukkit.plugin.java.JavaPlugin;

public class ConRiot extends JavaPlugin 
{
	@Getter private PlayerDataManager playerData;
	@Getter private EconomyManager economy;
	@Getter private CellManager cells;
	
	@Override
	public void onEnable()
	{
		playerData = new PlayerDataManager();
		
		economy = new EconomyManager();
		if (economy.setup())
		{
			getLogger().info("Integrating with " + economy.getName() + " through Vault");
		} else
		{
			getLogger().warning("Failed to integrate with an economy plugin through Vault");
		}
		
		// Load up the cell rental manager
		cells = new CellManager(this);
		
		getCommand("bounty").setExecutor(new BountyCommand(this));
		getCommand("onduty").setExecutor(new OnDutyCommand(this));
		getCommand("offduty").setExecutor(new OffDutyCommand(this));
		getCommand("shu").setExecutor(new ShuCommand(this));
		getCommand("spot").setExecutor(new SpotCommand(this));
		getCommand("points").setExecutor(new PointsCommand(this));
	}
}
