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
import net.conriot.prison.command.warden.OpCommand;
import net.conriot.prison.economy.EconomyManager;
import net.conriot.prison.listener.BlockListener;
import net.conriot.prison.listener.EntityListener;
import net.conriot.prison.listener.PlayerListener;
import net.conriot.prison.mine.MineManager;
import net.conriot.prison.shu.ShuManager;
import net.conriot.prison.stream.StreamManager;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.plugin.RegisteredServiceProvider;
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
	@Getter private ShuManager shuManager;
	@Getter private Permission permission;
	
	@Override
	public void onEnable()
	{
		playerData = new PlayerDataManager();
		
		playerData.getOrCreate("prplz").setGuardRank("Trainee"); // le test
		playerData.getOrCreate("Endain").setGuardRank("Trainee"); // le other test
		
		economy = new EconomyManager();
		if (economy.setup())
		{
			getLogger().info("Integrating with " + economy.getName() + " through Vault");
		} 
		else
		{
			getLogger().warning("Failed to integrate with an economy plugin through Vault");
		}
		
		// Get permission interface from vault
		RegisteredServiceProvider<Permission> rspPerm = getServer().getServicesManager().getRegistration(Permission.class);
		if (rspPerm != null)
		{
			permission = rspPerm.getProvider();
			getLogger().info("Integrating with " + permission.getName() + " through Vault");
		}
		else
		{
			getLogger().warning("Failed to integrate with a permission plugin through Vault");
			getLogger().warning("You really should get a permission plugin working or you're going to get exception spam");
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
		
		// Load up the message manager
		messages = new MessageManager();
		messages.load(this);
		
		// Load up the block manager
		blockManager = new BlockManager(this);
		
		// Load up the SHU manager
		shuManager = new ShuManager(this);
		getCommand("shu").setExecutor(new ShuCommand(this));
		
		// Register Guard commands
		getCommand("bounty").setExecutor(new BountyCommand(this));
		getCommand("onduty").setExecutor(new OnDutyCommand(this));
		getCommand("offduty").setExecutor(new OffDutyCommand(this));
		getCommand("spot").setExecutor(new SpotCommand(this));
		getCommand("points").setExecutor(new PointsCommand(this));
		
		// Register the op override command
		getCommand("op").setExecutor(new OpCommand(this));
		
		// Register listeners
		new PlayerListener(this).register();
		new BlockListener(this).register();
		new EntityListener(this).register();
	}
}
