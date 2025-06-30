/**
 * Provides integration with external world-aliasing systems, such as
 * <strong>Multiverse-Core</strong>, for resolving user-friendly world names
 * to be used in macro-based message templates.
 * <p>
 * This package defines a two-layered abstraction for world name resolution:
 *
 * <ul>
 *   <li>{@link com.winterhavenmc.util.worldmanager.worldname.WorldNameRetriever WorldNameRetriever}
 *   – A simple strategy interface for mapping a {@link org.bukkit.World} to a display name.</li>
 *   <li>{@link com.winterhavenmc.util.worldmanager.worldname.WorldNameRetriever WorldNameResolver}
 *   – A runtime-aware interface that conditionally selects between Multiverse and default
 *       implementations based on plugin availability.</li>
 * </ul>
 *
 * <h2>Runtime Integration</h2>
 * If <code>Multiverse-Core</code> is detected and enabled at runtime, the resolver system will use
 * {@link com.winterhavenmc.util.worldmanager.worldname.PluginResolver MultiverseV4WorldNameResolver}
 * to obtain world aliases. Otherwise, it falls back to
 * {@link com.winterhavenmc.util.worldmanager.worldname.DefaultResolver DefaultResolver},
 * which uses the world’s raw Bukkit name.
 *
 * <h2>Usage</h2>
 * This package supports the replacement of macros like {@code {WORLD.NAME}} in message templates
 * via MessageBuilderLib's macro resolution pipeline.
 *
 * @see com.winterhavenmc.util.worldmanager.worldname.WorldNameResolver WorldNameResolver
 * @see com.winterhavenmc.util.worldmanager.worldname.WorldNameRetriever WorldNameRetriever
 * @see org.bukkit.World
 */
package com.winterhavenmc.util.worldmanager.worldname;