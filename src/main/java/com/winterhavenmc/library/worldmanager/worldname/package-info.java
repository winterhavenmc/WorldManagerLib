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

/**
 * Provides integration with external world-aliasing systems, such as
 * <strong>Multiverse-Core</strong>, for resolving user-friendly world names
 * to be used in macro-based message templates.
 * <p>
 * This package defines a two-layered abstraction for world name resolution:
 *
 * <ul>
 *   <li>{@link com.winterhavenmc.library.worldmanager.worldname.WorldNameRetriever WorldNameRetriever}
 *   – A simple strategy interface for mapping a {@link org.bukkit.World} to a display name.</li>
 *   <li>{@link com.winterhavenmc.library.worldmanager.worldname.WorldNameRetriever WorldNameResolver}
 *   – A runtime-aware interface that conditionally selects between Multiverse and default
 *       implementations based on plugin availability.</li>
 * </ul>
 *
 * <h2>Runtime Integration</h2>
 * If <code>Multiverse-Core</code> is detected and enabled at runtime, the resolver system will use
 * {@link com.winterhavenmc.library.worldmanager.worldname.PluginResolver MultiverseV4WorldNameResolver}
 * to obtain world aliases. Otherwise, it falls back to
 * {@link com.winterhavenmc.library.worldmanager.worldname.DefaultResolver DefaultResolver},
 * which uses the world’s raw Bukkit name.
 *
 * <h2>Usage</h2>
 * This package supports the replacement of macros like {@code {WORLD.NAME}} in message templates
 * via MessageBuilderLib's macro resolution pipeline.
 *
 * @see com.winterhavenmc.library.worldmanager.worldname.WorldNameResolver WorldNameResolver
 * @see com.winterhavenmc.library.worldmanager.worldname.WorldNameRetriever WorldNameRetriever
 * @see org.bukkit.World
 */
package com.winterhavenmc.library.worldmanager.worldname;