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

import com.onarandombox.MultiverseCore.MultiverseCore;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Optional;

public class Multiverse4Retriever implements SpawnLocationRetriever
{
	private final MultiverseCore plugin;


	public Multiverse4Retriever(MultiverseCore plugin)
	{
		this.plugin = plugin;
	}


	public Optional<Location> getSpawnLocation(final World world)
	{
		if (plugin != null
			&& plugin.getMVWorldManager() != null
			&& plugin.getMVWorldManager().getMVWorld(world) != null)
		{
			return Optional.ofNullable(plugin.getMVWorldManager().getMVWorld(world).getSpawnLocation());
		}
		else {
			return Optional.empty();
		}
	}

}
