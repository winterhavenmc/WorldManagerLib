/*
 * Copyright (c) 2022 Tim Savage.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.winterhavenmc.util.worldmanager;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.generator.WorldInfo;
import org.bukkit.plugin.Plugin;

import java.util.*;


@SuppressWarnings("unused")
public final class WorldManager {

	// reference to main class
	private final Plugin plugin;

	// collection of enabled world names
	private final Collection<UUID> enabledWorldRegistry = new HashSet<>();

	// reference to MultiverseCore
	private final MultiverseCore mvCore;

	private final static String ENABLED_WORLDS_KEY = "enabled-worlds";
	private final static String DISABLED_WORLDS_KEY = "disabled-worlds";
	public static final String UNKNOWN_WORLD = "unknown";
	public static final String CONSOLE_SENDER = "console";


	/**
	 * Class constructor
	 *
	 * @param plugin passed reference to the plugin main class
	 */
	public WorldManager(final Plugin plugin) {

		// set reference to main class
		this.plugin = plugin;

		// get reference to Multiverse-Core if installed
		mvCore = (MultiverseCore) plugin.getServer().getPluginManager().getPlugin("Multiverse-Core");
		if (mvCore != null && mvCore.isEnabled()) {
			plugin.getLogger().info("Multiverse-Core detected.");
		}

		// populate enabled world UID list field
		this.reload();
	}


	/**
	 * update enabledWorlds collection from plugin config.yml file
	 */
	@SuppressWarnings("WeakerAccess")
	public void reload() {
		// remove all worlds from registry
		this.enabledWorldRegistry.clear();

		// if server.getWorlds() is empty, return without adding any worlds to registry and log warning
		if (plugin.getServer().getWorlds().stream().map(WorldInfo::getName).toList().isEmpty()) {
			plugin.getLogger().warning("the server has no worlds.");
			return;
		}

		// if config list of enabled worlds is empty, add all server worlds to registry
		if (plugin.getConfig().getStringList(ENABLED_WORLDS_KEY).isEmpty()) {
			addAllServerWorlds();
		}
		// otherwise, add only the worlds in the config enabled worlds list that are also server worlds
		else {
			addAllEnabledConfigWorlds();
		}

		// remove all disabled worlds from registry
		removeAllDisabledConfigWorlds();
	}


	/**
	 * Reload helper method adds all server worlds to the registry
	 */
	@SuppressWarnings("UnusedReturnValue")
	private int addAllServerWorlds() {
		int count = 0;
		for (World world : plugin.getServer().getWorlds()) {
			if (world != null) {
				this.enabledWorldRegistry.add(world.getUID());
				count++;
			}
		}
		return count;
	}


	/**
	 * Reload helper method adds all worlds to registry whose names are
	 * contained in the config enabled-worlds string list and are also current server worlds
	 */
	@SuppressWarnings("UnusedReturnValue")
	private int addAllEnabledConfigWorlds() {
		int count = 0;
		// iterate through config list of enabled worlds, and add valid world UIDs to registry
		for (String worldName : plugin.getConfig().getStringList(ENABLED_WORLDS_KEY)) {
			// get world by name
			World world = plugin.getServer().getWorld(worldName);
			// add world UID to field if it is not already in list and world exists
			if (world != null) {
				this.enabledWorldRegistry.add(world.getUID());
				count++;
			}
		}
		return count;
	}


	/**
	 * Reload helper method removes all worlds from registry whose names are
	 * contained in the config disabled-worlds string list
	 */
	@SuppressWarnings("UnusedReturnValue")
	private int removeAllDisabledConfigWorlds() {
		int count = 0;
		// remove config list of disabled worlds from enabledWorldUIDs field
		for (String worldName : plugin.getConfig().getStringList(DISABLED_WORLDS_KEY)) {
			// get world by name
			World world = plugin.getServer().getWorld(worldName);
			// if world is not null remove UID from list
			if (world != null) {
				this.enabledWorldRegistry.remove(world.getUID());
				count++;
			}
		}
		return count;
	}


	/**
	 * get collection of enabled world names from registry
	 *
	 * @return a Collection of String containing enabled world names
	 */
	public Collection<String> getEnabledWorldNames() {
		// create empty set of string for return
		Set<String> resultCollection = new HashSet<>();

		// iterate through list of enabled world UIDs
		for (UUID worldUID : enabledWorldRegistry) {

			// get world by UID
			World world = plugin.getServer().getWorld(worldUID);

			// if world is not null, add name to return set
			if (world != null) {
				resultCollection.add(world.getName());
			}
		}

		// return result list
		return resultCollection;
	}


	/**
	 * Check if a world is enabled by bukkit world UID
	 *
	 * @param worldUID Unique Identifier for world
	 * @return {@code true} if world is enabled, {@code false} if disabled
	 */
	public boolean isEnabled(final UUID worldUID) {

		// if worldUID is null return false
		if (worldUID == null) {
			return false;
		}

		return this.enabledWorldRegistry.contains(worldUID);
	}


	/**
	 * Check if a world is enabled by bukkit world object
	 *
	 * @param world bukkit world object
	 * @return {@code true} if world is enabled, {@code false} if disabled
	 */
	public boolean isEnabled(final World world) {

		// if world is null return false
		if (world == null) {
			return false;
		}

		return this.enabledWorldRegistry.contains(world.getUID());
	}


	/**
	 * Check if a world is enabled by name
	 *
	 * @param worldName name of world as string to check
	 * @return {@code true} if world is enabled, {@code false} if disabled
	 */
	public boolean isEnabled(final String worldName) {

		// if worldName is null or blank, return false
		if (worldName == null || worldName.isBlank()) {
			return false;
		}

		// get world by name
		World world = plugin.getServer().getWorld(worldName);

		// if world is null, return false
		if (world == null) {
			return false;
		}

		return this.enabledWorldRegistry.contains(world.getUID());
	}


	/**
	 * Get world name from world UID. If a Multiverse alias exists for the world, it will be returned;
	 * otherwise the bukkit world name will be returned
	 *
	 * @param worldUID the unique ID of a bukkit world
	 * @return String containing Multiverse world alias or bukkit world name
	 */
	public String getWorldName(final UUID worldUID) {

		// worldUID must be non-null
		if (worldUID == null) {
			throw new IllegalArgumentException("The argument passed is null; a valid UUID is required.");
		}

		// get world
		World world = plugin.getServer().getWorld(worldUID);

		// if world is null, return unknown world string
		if (world == null) {
			return UNKNOWN_WORLD;
		}

		// return the world name or Multiverse alias
		return getAliasOrName(world);
	}


	private String getAliasOrName(final World world) {

		String worldName = world.getName();

		// if Multiverse is enabled, get MultiverseWorld object
		if (mvCore != null && mvCore.isEnabled()) {

			MultiverseWorld mvWorld = mvCore.getMVWorldManager().getMVWorld(world);

			// if Multiverse alias is not null or empty, set worldName to alias
			if (mvWorld != null && mvWorld.getAlias() != null && !mvWorld.getAlias().isEmpty()) {
				worldName = mvWorld.getAlias();
			}
		}
		// return the world name or Multiverse alias
		return worldName;
	}


	/**
	 * Get world name from world object, using Multiverse alias if available
	 *
	 * @param world the world object to retrieve name
	 * @return bukkit world name or multiverse alias as String
	 * @throws NullPointerException if passed world is null
	 */
	public String getWorldName(final World world) {

		// passed world must be non-null
		if (world == null) {
			throw new IllegalArgumentException("The argument passed is null; a valid World is required.");
		}
		// return the world name or Multiverse alias
		return getAliasOrName(world);
	}


	/**
	 * Get world name from world name string, using Multiverse alias if available
	 *
	 * @param passedName the bukkit world name as string
	 * @return bukkit world name or multiverse alias as String
	 */
	public String getWorldName(final String passedName) {

		// if passedName is null or blank, return empty string
		if (passedName == null || passedName.isBlank()) {
			return UNKNOWN_WORLD;
		}

		// get world
		World world = plugin.getServer().getWorld(passedName);

		// if world is null, return null
		if (world == null) {
			return UNKNOWN_WORLD;
		}
		// return the world name or Multiverse alias
		return getAliasOrName(world);
	}


	/**
	 * Get world name for command sender's world, using Multiverse alias if available
	 *
	 * @param sender the command sender used to retrieve world name
	 * @return bukkit world name or multiverse alias as String
	 * @throws NullPointerException if passed sender is null
	 */
	public String getWorldName(final CommandSender sender) {

		// sender must be non-null
		if (sender == null) {
			throw new IllegalArgumentException("The argument passed is null; a valid CommandSender is required.");
		}

		// if server has no worlds, return CONSOLE_SENDER as world name
		if (plugin.getServer().getWorlds().isEmpty()) {
			plugin.getLogger().warning("The server has no enabled worlds.");
			return CONSOLE_SENDER;
		}

		// get first server world
		World world = plugin.getServer().getWorlds().getFirst();

		if (sender instanceof Entity) {
			world = ((Entity) sender).getWorld();
		}
		else if (sender instanceof ConsoleCommandSender) {
			return CONSOLE_SENDER;
		}
		// return the world name or Multiverse alias
		return getAliasOrName(world);
	}


	/**
	 * Get world name for location, using Multiverse alias if available
	 *
	 * @param location the location used to retrieve world name
	 * @return bukkit world name or multiverse alias as String
	 * @throws NullPointerException if passed location is null
	 */
	public String getWorldName(final Location location) {

		// passed location must be non-null
		if (location == null) {
			throw new IllegalArgumentException("The argument passed is null; a valid Location is required.");
		}

		// get world from location
		World world = location.getWorld();

		// get worldName from world, or default to servers first world name if world is null
		String worldName;

		if (world != null) {
			worldName = world.getName();
		}
		else {
			worldName = plugin.getServer().getWorlds().getFirst().getName();
		}

		// if Multiverse is enabled, get MultiverseWorld object
		if (mvCore != null && mvCore.isEnabled()) {

			// get MultiverseWorld object
			MultiverseWorld mvWorld = mvCore.getMVWorldManager().getMVWorld(world);

			// if Multiverse alias is not null or empty, set worldName to alias
			if (mvWorld != null && mvWorld.getAlias() != null && !mvWorld.getAlias().isEmpty()) {
				worldName = mvWorld.getAlias();
			}
		}
		// return the bukkit world name or Multiverse world alias
		return getAliasOrName(world);
	}


	/**
	 * get world spawn location, preferring Multiverse spawn location if available
	 *
	 * @param world bukkit world object to retrieve spawn location
	 * @return spawn location
	 * @throws NullPointerException if passed world is null
	 */
	public Location getSpawnLocation(final World world) {

		// passed world must be non-null
		if (world == null) {
			throw new IllegalArgumentException("The argument is null; a valid World is required.");
		}

		// if Multiverse is enabled, return Multiverse world spawn location
		if (mvCore != null && mvCore.isEnabled()) {
			return mvCore.getMVWorldManager().getMVWorld(world).getSpawnLocation();
		}

		// return bukkit world spawn location
		return world.getSpawnLocation();
	}


	/**
	 * get world spawn location for entity, preferring Multiverse spawn location if available
	 *
	 * @param entity entity to retrieve world spawn location
	 * @return {@code Location} the spawn location of the world
	 * @throws IllegalArgumentException if passed entity is null
	 */
	public Location getSpawnLocation(final Entity entity) {

		// passed entity must be non-null
		if (entity == null) {
			throw new IllegalArgumentException("The argument passed is null; a valid Entity is required.");
		}

		// if Multiverse is enabled, return Multiverse world spawn location
		if (mvCore != null && mvCore.isEnabled()) {
			return mvCore.getMVWorldManager().getMVWorld(entity.getWorld()).getSpawnLocation();
		}

		// return bukkit world spawn location
		return entity.getWorld().getSpawnLocation();
	}

}
