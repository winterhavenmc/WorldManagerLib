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

import org.bukkit.plugin.PluginManager;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class SpawnLocationResolverTest
{
	@Mock PluginManager pluginManagerMock;
	@Mock MultiverseCore mv4PluginMock;


	@Test
	void get_returns_default_resolver_when_plugin_manager_is_null()
	{
		// Arrange
		// Act
		SpawnLocationResolver resolver = SpawnLocationResolver.get(null);

		// Assert
		assertInstanceOf(DefaultResolver.class, resolver);
	}


	@Test
	void get_returns_default_resolver_when_plugin_is_null()
	{
		// Arrange
		when(pluginManagerMock.getPlugin("Multiverse-Core")).thenReturn(null);

		// Act
		SpawnLocationResolver resolver = SpawnLocationResolver.get(pluginManagerMock);

		// Assert
		assertInstanceOf(DefaultResolver.class, resolver);

		// Verify
		verify(pluginManagerMock, atLeastOnce()).getPlugin("Multiverse-Core");
	}


	@Test
	void get_returns_default_resolver_when_plugin_is_disabled()
	{
		// Arrange
		when(pluginManagerMock.getPlugin("Multiverse-Core")).thenReturn(mv4PluginMock);
		when(mv4PluginMock.isEnabled()).thenReturn(false);

		// Act
		SpawnLocationResolver resolver = SpawnLocationResolver.get(pluginManagerMock);

		// Assert
		assertInstanceOf(DefaultResolver.class, resolver);

		// Verify
		verify(pluginManagerMock, atLeastOnce()).getPlugin("Multiverse-Core");
	}


	@Test
	void get_returns_mv4_resolver_when_plugin_is_enabled()
	{
		// Arrange
		when(pluginManagerMock.getPlugin("Multiverse-Core")).thenReturn(mv4PluginMock);
		when(mv4PluginMock.isEnabled()).thenReturn(true);

		// Act
		SpawnLocationResolver resolver = SpawnLocationResolver.get(pluginManagerMock);

		// Assert
		assertInstanceOf(PluginBasedResolver.class, resolver);

		// Verify
		verify(pluginManagerMock, atLeastOnce()).getPlugin("Multiverse-Core");
	}

}