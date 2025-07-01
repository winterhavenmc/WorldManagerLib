/*
 * Copyright (c) 2025 Tim Savage.
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

package com.winterhavenmc.library.worldmanager.worldname;

import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;


/**
 * A runtime-resolvable strategy interface for obtaining a display-friendly name
 * for a {@link World}, optionally integrating with external plugins
 * such as <strong>Multiverse-Core</strong>.
 * <p>
 * This interface builds upon {@link WorldNameRetriever} by providing a static
 * factory method that selects an appropriate implementation based on the server
 * environment at runtime.
 * <p>
 * When {@code Multiverse-Core} is installed and enabled, the returned
 * implementation uses Multiverse APIs to obtain world aliases. Otherwise,
 * a fallback implementation is used that simply returns the default
 * world name from {@link World#getName()}.
 *
 * @see WorldNameRetriever
 * @see DefaultResolver
 * @see World
 */
public sealed interface WorldNameResolver permits DefaultResolver, PluginResolver
{
	/**
	 * Resolves the user-facing name of the given {@link World}, using
	 * either the native Bukkit name or a plugin-provided alias.
	 *
	 * @param world the {@link World} whose name should be resolved
	 * @return the display or alias name for the world
	 */
	String resolve(World world);


	/**
	 * Returns an appropriate {@link WorldNameResolver} implementation based on
	 * the availability of the {@code Multiverse-Core} plugin.
	 * <p>
	 * If Multiverse is installed and enabled, this method returns a
	 * {@link PluginResolver}; otherwise, it falls back to
	 * a {@link DefaultResolver}.
	 *
	 * @param pluginManager the server's {@link PluginManager}
	 * @return a {@link WorldNameResolver} appropriate for the current server environment
	 */
	static WorldNameResolver get(final PluginManager pluginManager)
	{
		Plugin plugin = pluginManager.getPlugin("Multiverse-Core");

		return (plugin != null && plugin.isEnabled())
				? new PluginResolver(plugin)
				: new DefaultResolver();
	}

}
