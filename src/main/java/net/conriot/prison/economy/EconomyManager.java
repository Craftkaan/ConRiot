package net.conriot.prison.economy;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class EconomyManager
{
	private Economy economy;
	
	public EconomyManager()
	{
	}
	
	public boolean setup()
	{
		RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
		if (rsp == null)
		{
			return false;
		}
		economy = rsp.getProvider();
		return true;
	}
	
	private void assertEconomySetup() throws EconomyException
	{
		if (economy == null)
		{
			throw new EconomyException("Economy has not been set up");
		}
	}
	
	public String format(double amount)
	{
		if (economy == null)
		{
			return Double.toString(amount);
		}
		return economy.format(amount);
	}
	
	public String getName()
	{
		return economy.getName();
	}
	
	public void takeMoney(Player player, double amount) throws EconomyException
	{
		takeMoney(player.getName(), amount);
	}
	
	public void takeMoney(String name, double amount) throws EconomyException
	{
		assertEconomySetup();
		if (!economy.has(name, amount))
		{
			throw new EconomyException("Can't afford " + format(amount));
		}
		EconomyResponse res = economy.withdrawPlayer(name, amount);
		if (!res.transactionSuccess())
		{
			throw new EconomyException(res.errorMessage);
		}
	}
	
	public void giveMoney(Player player, double amount) throws EconomyException
	{
		giveMoney(player.getName(), amount);
	}
	
	public void giveMoney(String name, double amount) throws EconomyException
	{
		assertEconomySetup();
		EconomyResponse res = economy.depositPlayer(name, amount);
		if (!res.transactionSuccess())
		{
			throw new EconomyException(res.errorMessage);
		}
	}
}
