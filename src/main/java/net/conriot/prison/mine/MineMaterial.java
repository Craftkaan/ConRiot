package net.conriot.prison.mine;

import org.bukkit.Material;

public class MineMaterial {
	public Material type;
	public byte data;
	public int weight;
	
	public MineMaterial(Material type, byte data, int weight) {
		this.type = type;
		this.data = data;
		this.weight = weight;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o == null)
			return false;
		if(o instanceof MineMaterial)
		{
			MineMaterial m = (MineMaterial) o;
			if(m.type == type && m.data == data)
				return true;
		}
		return false;
	}
	
	public int hashCode()
	{
		return (16 * type.getId()) + data;
	}
}
