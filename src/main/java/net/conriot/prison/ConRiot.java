package net.conriot.prison;

import lombok.Getter;
import net.conriot.prison.block.BlockManager;
import net.conriot.prison.cell.CellManager;
import net.conriot.prison.command.bounty.BountyCommand;
import net.conriot.prison.command.cell.CellCommand;
import net.conriot.prison.command.guard.OffDutyCommand;
import net.conriot.prison.command.guard.OnDutyCommand;
import net.conriot.prison.command.guard.PointsCommand;
import net.conriot.prison.command.guard.ShuCommand;
import net.conriot.prison.command.guard.SpotCommand;
import net.conriot.prison.command.mine.MineCommand;
import net.conriot.prison.command.stream.StreamCommand;
import net.conriot.prison.economy.EconomyManager;
import net.conriot.prison.listener.BlockListener;
import net.conriot.prison.listener.PlayerListener;
import net.conriot.prison.mine.MineManager;
import net.conriot.prison.stream.StreamManager;

import org.bukkit.plugin.java.JavaPlugin;

public class ConRiot extends JavaPlugin 
{
	@Getter private PlayerDataManager playerData;
	@Getter private EconomyManager economy;
	@Getter private CellManager cells;
	@Getter private MessageManager messages;
	@Getter private BlockManager blockManager;
	@Getter private MineManager mines;
	@Getter private StreamManager streamManager;
	
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
		getCommand("cell").setExecutor(new CellCommand(this));
		
		// Load up the mine manager
		mines = new MineManager(this);
		getCommand("mine").setExecutor(new MineCommand(this));
		
		// Load up the stream manager
		streamManager = new StreamManager(this);
		getCommand("stream").setExecutor(new StreamCommand(this));
		
		messages = new MessageManager();
		messages.load(this);
		
		blockManager = new BlockManager(this);
		
		getCommand("bounty").setExecutor(new BountyCommand(this));
		getCommand("onduty").setExecutor(new OnDutyCommand(this));
		getCommand("offduty").setExecutor(new OffDutyCommand(this));
		getCommand("shu").setExecutor(new ShuCommand(this));
		getCommand("spot").setExecutor(new SpotCommand(this));
		getCommand("points").setExecutor(new PointsCommand(this));
		
		new PlayerListener(this).register();
		new BlockListener(this).register();
	}
}
