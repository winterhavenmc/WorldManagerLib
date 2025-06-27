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
import org.mvplugins.multiverse.core.MultiverseCoreApi;
import org.mvplugins.multiverse.core.world.MultiverseWorld;


public class Multiverse5Retriever implements SpawnLocationRetriever
{
	@Override
	public Location getSpawnLocation(World world)
	{
		if (world == null) { return null; }

		Location result = null;
		MultiverseCoreApi multiverseCoreApi = null;
		boolean success;

		try
		{
			multiverseCoreApi = MultiverseCoreApi.get();
			success = true;
		}
		catch (Exception exception)
		{
			result = world.getSpawnLocation();
			success = false;
		}

		if (success)
		{
			MultiverseWorld multiverseWorld = multiverseCoreApi.getWorldManager().getWorld(world).getOrNull();
			if (multiverseWorld != null)
			{
				result = multiverseCoreApi.getWorldManager().getWorld(world).getOrNull().getSpawnLocation();
			}
			else
			{
				result = world.getSpawnLocation();
			}
		}

		return result;
	}

}
