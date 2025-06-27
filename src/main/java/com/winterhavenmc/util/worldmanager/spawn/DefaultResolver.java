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


public final class DefaultResolver implements SpawnLocationResolver
{
	/**
	 * Returns the default Bukkit world name by calling {@link org.bukkit.World#getName()}.
	 *
	 * @param world the {@link org.bukkit.World} to resolve a name for
	 * @return the world's raw name as defined by Bukkit
	 */
	@Override
	public Location resolve(final World world)
	{
		return (world != null)
				? world.getSpawnLocation()
				: null;
	}

}
