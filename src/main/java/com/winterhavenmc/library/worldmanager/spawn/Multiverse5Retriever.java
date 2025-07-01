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
import org.mvplugins.multiverse.core.MultiverseCoreApi;
import org.mvplugins.multiverse.core.world.MultiverseWorld;
import org.mvplugins.multiverse.core.world.WorldManager;
import org.mvplugins.multiverse.external.vavr.control.Option;

import java.util.Optional;
import java.util.logging.Logger;


public class Multiverse5Retriever implements SpawnLocationRetriever
{
	@Override
	public Optional<Location> getSpawnLocation(World world)
	{
		MultiverseCoreApi multiverseCoreApi;

		try
		{
			multiverseCoreApi = MultiverseCoreApi.get();
		}
		catch (IllegalStateException e)
		{
			Logger.getLogger(this.getClass().getName()).warning( "Multiverse threw an exception while trying to get an instance of its api.");
			return Optional.empty();
		}

		WorldManager worldManager = multiverseCoreApi.getWorldManager();

		if (worldManager == null)
		{
			return Optional.empty();
		}

		Option<MultiverseWorld> optionWorld = worldManager.getWorld(world);

		if (optionWorld.isEmpty())
		{
			return Optional.empty();
		}

		MultiverseWorld multiverseWorld = optionWorld.getOrNull();

		return (multiverseWorld != null)
				? Optional.ofNullable(multiverseWorld.getSpawnLocation())
				: Optional.empty();
	}

}
