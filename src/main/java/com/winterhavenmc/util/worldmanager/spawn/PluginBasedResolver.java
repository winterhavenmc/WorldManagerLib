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

package com.winterhavenmc.util.worldmanager.spawn;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;


public final class PluginBasedResolver implements SpawnLocationResolver
{
	private final Plugin plugin;


	public PluginBasedResolver(final Plugin plugin)
	{
		this.plugin = plugin;
	}


	public Location resolve(final World world)
	{
		if (plugin == null)
		{
			return world.getSpawnLocation();
		}

		SpawnLocationRetriever retriever = switch (plugin)
		{
			case com.onarandombox.MultiverseCore.MultiverseCore mvPlugin -> new Multiverse4Retriever(mvPlugin);
			case org.mvplugins.multiverse.core.MultiverseCore mvPlugin -> new Multiverse5Retriever(mvPlugin);
			default -> new DefaultRetriever();
		};

		return retriever.getSpawnLocation(world);
	}

}
