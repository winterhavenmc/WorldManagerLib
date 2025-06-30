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

package com.winterhavenmc.util.worldmanager.worldname;

import org.bukkit.World;

import java.util.Optional;


/**
 * Strategy interface for retrieving a user-friendly name for a given {@link World}.
 * <p>
 * This is used to abstract world name resolution logic so that plugins can optionally
 * integrate with external world-aliasing plugins like <strong>Multiverse</strong>, without
 * hard dependencies on them.
 * <p>
 * Implementations may:
 * <ul>
 *   <li>Return the default world name from {@code World.getName()}</li>
 *   <li>Return an alias or display name from an external plugin such as Multiverse</li>
 * </ul>
 * <p>
 * Used internally by the {@code worldname} macro resolver to substitute values like
 * {@code {WORLDNAME}} in messages.
 *
 * @see World
 * @see DefaultResolver
 * @see PluginResolver
 */
public interface WorldNameRetriever
{
	Optional<String> getWorldName(World world);
}
