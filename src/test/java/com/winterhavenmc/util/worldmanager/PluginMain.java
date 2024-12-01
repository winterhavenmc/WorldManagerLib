package com.winterhavenmc.util.worldmanager;

import org.bukkit.plugin.java.JavaPlugin;


/**
 * The main class for WorldManagerTest plugin
 */
public class PluginMain extends JavaPlugin {

	public WorldManager worldManager;

	@Override
	public void onEnable() {
		// instantiate world manager
		worldManager = new WorldManager(this);
	}

}
