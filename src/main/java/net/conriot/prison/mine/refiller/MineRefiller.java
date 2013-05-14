package net.conriot.prison.mine.refiller;

public interface MineRefiller {
	// This interface defines a standard way of refilling
	// a Mine. This allows us to have implementations that
	// use NMS code when there is NMS code written to
	// support the current server version. Otherwise, it
	// will fall back on a pure Bukkit API implementation
	// of the refiller, sacrificing compatibility for
	// performance.
	
	// Simply define a function that refills all mines 
	// managed by it with the given tag and returns the
	// number of milliseconds taken.
	public long refill(int tag);
}
