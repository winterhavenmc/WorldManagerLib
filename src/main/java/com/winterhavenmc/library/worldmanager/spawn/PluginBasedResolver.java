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

package com.winterhavenmc.library.worldmanager.spawn;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.util.Optional;


public final class PluginBasedResolver implements SpawnLocationResolver
{
	private final Plugin plugin;


	public PluginBasedResolver(final Plugin plugin)
	{
		this.plugin = plugin;
	}


	public Location resolve(final World world)
	{
		if (world == null) { return null; }

		Optional<Location> result;

		if (plugin != null
				&& plugin.getDescription().getVersion().startsWith("4")
				&& plugin instanceof com.onarandombox.MultiverseCore.MultiverseCore mvPlugin)
		{
			result = new Multiverse4Retriever(mvPlugin).getSpawnLocation(world);
		}
		else if (plugin != null
				&& plugin.getDescription().getVersion().startsWith("5")
				&& plugin instanceof org.mvplugins.multiverse.core.MultiverseCore)
		{
			result = new Multiverse5Retriever().getSpawnLocation(world);
		}
		else
		{
			result = new DefaultRetriever().getSpawnLocation(world);
		}

		return result.orElseGet(world::getSpawnLocation);
	}

}
