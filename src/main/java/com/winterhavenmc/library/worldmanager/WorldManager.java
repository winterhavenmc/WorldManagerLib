/*
 * Copyright (c) 2022-2025 Tim Savage.
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

package com.winterhavenmc.library.worldmanager;

import com.winterhavenmc.library.worldmanager.spawn.SpawnLocationResolver;
import com.winterhavenmc.library.worldmanager.worldname.WorldNameResolver;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.WorldInfo;
import org.bukkit.plugin.Plugin;

import org.jetbrains.annotations.Contract;

import java.util.*;


@SuppressWarnings("unused")
public final class WorldManager
{
	private final Plugin plugin;

	// collection of enabled world names
	private final Collection<UUID> enabledWorldRegistry = new HashSet<>();

	private final static String ENABLED_WORLDS_KEY = "enabled-worlds";
	private final static String DISABLED_WORLDS_KEY = "disabled-worlds";
	public static final String UNKNOWN_WORLD = "\uD83C\uDF10";
	public static final String CONSOLE_SENDER = "console";


	/**
	 * Class constructor
	 *
	 * @param plugin passed reference to the plugin main class
	 */
	public WorldManager(final Plugin plugin)
	{
		// set reference to main class
		this.plugin = plugin;

		// populate enabled world UID list field
		this.reload();
	}


	/**
	 * update enabledWorlds collection from plugin config.yml file
	 */
	@SuppressWarnings("WeakerAccess")
	public void reload()
	{
		// remove all worlds from registry
		this.enabledWorldRegistry.clear();

		// if server.getWorlds() is empty, return without adding any worlds to registry and log warning
		if (plugin.getServer().getWorlds().stream().map(WorldInfo::getName).toList().isEmpty())
		{
			plugin.getLogger().warning("the server has no worlds.");
			return;
		}

		// if config list of enabled worlds is empty, add all server worlds to registry
		if (plugin.getConfig().getStringList(ENABLED_WORLDS_KEY).isEmpty())
		{
			addAllServerWorlds();
		}

		// otherwise, add only the worlds in the config enabled worlds list that are also server worlds
		else
		{
			addAllEnabledConfigWorlds();
		}

		// remove all disabled worlds from registry
		removeAllDisabledConfigWorlds();
	}


	/**
	 * Reload helper method adds all server worlds to the registry
	 */
	@SuppressWarnings("UnusedReturnValue")
	private int addAllServerWorlds()
	{
		int count = 0;
		for (World world : plugin.getServer().getWorlds())
		{
			if (world != null)
			{
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
	private int addAllEnabledConfigWorlds()
	{
		int count = 0;

		for (String worldName : plugin.getConfig().getStringList(ENABLED_WORLDS_KEY))
		{
			World world = plugin.getServer().getWorld(worldName);

			if (world != null)
			{
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
	private int removeAllDisabledConfigWorlds()
	{
		int count = 0;

		for (String worldName : plugin.getConfig().getStringList(DISABLED_WORLDS_KEY))
		{
			World world = plugin.getServer().getWorld(worldName);

			if (world != null)
			{
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
	public Collection<String> getEnabledWorldNames()
	{
		Set<String> resultCollection = new HashSet<>();

		for (UUID worldUID : enabledWorldRegistry)
		{
			World world = plugin.getServer().getWorld(worldUID);

			if (world != null)
			{
				resultCollection.add(world.getName());
			}
		}

		return resultCollection;
	}


	/**
	 * Check if a world is enabled by bukkit world UID
	 *
	 * @param worldUID Unique Identifier for world
	 * @return {@code true} if world is enabled, {@code false} if disabled
	 */
	public boolean isEnabled(final UUID worldUID)
	{
		return worldUID != null && this.enabledWorldRegistry.contains(worldUID);
	}


	/**
	 * Check if a world is enabled by bukkit world object
	 *
	 * @param world bukkit world object
	 * @return {@code true} if world is enabled, {@code false} if disabled
	 */
	public boolean isEnabled(final World world)
	{
		return world != null && this.enabledWorldRegistry.contains(world.getUID());
	}


	/**
	 * Check if a world is enabled by name
	 *
	 * @param worldName name of world as string to check
	 * @return {@code true} if world is enabled, {@code false} if disabled
	 */
	public boolean isEnabled(final String worldName)
	{
		if (worldName == null || worldName.isBlank())
		{
			return false;
		}

		World world = plugin.getServer().getWorld(worldName);

		return world != null && this.enabledWorldRegistry.contains(world.getUID());
	}


	/**
	 * Get world name from world object, using Multiverse alias if available
	 *
	 * @param world the world object to retrieve name
	 * @return bukkit world name or multiverse alias as String
	 */
	public String getWorldName(final World world)
	{
		return getAliasOrName(world);
	}


	/**
	 * Get world name from world object, using Multiverse alias if available
	 *
	 * @param world the world object to retrieve name
	 * @return bukkit world name or multiverse alias as String
	 */
	private String getAliasOrName(final World world)
	{
		if (world == null) { return UNKNOWN_WORLD; }

		WorldNameResolver resolver = WorldNameResolver.get(plugin.getServer().getPluginManager());

		return resolver.resolve(world);
	}


	/**
	 * Get world name from world name string, using Multiverse alias if available
	 *
	 * @param worldName the bukkit world name as string
	 * @return bukkit world name or multiverse alias as String
	 */
	public String getAliasOrName(final String worldName)
	{
		if (worldName == null || worldName.isBlank())
		{
			return UNKNOWN_WORLD;
		}

		// get world
		World world = plugin.getServer().getWorld(worldName);

		// if world is null, return null
		if (world == null)
		{
			return UNKNOWN_WORLD;
		}

		return getAliasOrName(world);
	}


	/**
	 * get world spawn location, preferring Multiverse spawn location if available
	 *
	 * @param world bukkit world object to retrieve spawn location
	 * @return spawn location, or null if world is null
	 */
	public Location getSpawnLocation(final World world)
	{
		SpawnLocationResolver resolver = SpawnLocationResolver.get(plugin.getServer().getPluginManager());
		return resolver.resolve(world);
	}


	/**
	 * get the current size of the registry. used for testing
	 *
	 * @return {@code int} the size of the registry
	 */
	@Contract(pure = true)
	int size()
	{
		return this.enabledWorldRegistry.size();
	}


	/**
	 * check if uuid is present in the registry
	 *
	 * @param uuid the uuid of a world
	 * @return {@code boolean} true if the world uuid is present in the registry, or false if not
	 */
	@Contract(pure = true)
	boolean contains(final UUID uuid)
	{
		return this.enabledWorldRegistry.contains(uuid);
	}

}
