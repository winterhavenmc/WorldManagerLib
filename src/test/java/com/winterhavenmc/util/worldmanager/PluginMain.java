package com.winterhavenmc.util.worldmanager;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;

/**
 * The main class for WorldManagerTest plugin
 */
@SuppressWarnings("unused")
public class PluginMain extends JavaPlugin {

	public WorldManager worldManager;


	public PluginMain() {
		super();
	}


	protected PluginMain(JavaPluginLoader loader, PluginDescriptionFile descriptionFile, File dataFolder, File file) {
		super(loader, descriptionFile, dataFolder, file);
	}


	@Override
	public void onEnable() {
		// instantiate world manager
		worldManager = new WorldManager(this);
	}

}
