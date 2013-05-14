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
}
