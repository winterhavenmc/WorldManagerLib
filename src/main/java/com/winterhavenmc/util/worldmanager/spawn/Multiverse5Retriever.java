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

import org.mvplugins.multiverse.core.MultiverseCore;
import org.bukkit.Location;
import org.bukkit.World;


public class Multiverse5Retriever implements SpawnLocationRetriever
{
	private final MultiverseCore multiverseCore;


	public Multiverse5Retriever(MultiverseCore plugin)
	{
		this.multiverseCore = plugin;
	}


	@Override
	public Location getSpawnLocation(World world)
	{
		return multiverseCore.getApi().getWorldManager().getWorld(world).getOrNull().getSpawnLocation();
	}

}
