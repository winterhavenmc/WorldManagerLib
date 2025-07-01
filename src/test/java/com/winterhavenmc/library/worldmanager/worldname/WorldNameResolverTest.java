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

package com.winterhavenmc.library.worldmanager.worldname;

import com.onarandombox.MultiverseCore.MultiverseCore;
import org.bukkit.World;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class WorldNameResolverTest
{
	@Mock PluginManager pluginManagerMock;
	@Mock World worldMock;
	@Mock MultiverseCore multiverseCoreV4Mock;
	@Mock org.mvplugins.multiverse.core.MultiverseCore multiverseCoreV5Mock;
	@Mock PluginDescriptionFile descriptionMock;


	@Test
	void getResolver()
	{
		// Arrange
		WorldNameResolver resolver = WorldNameResolver.get(pluginManagerMock);

		// Act
		assertNotNull(resolver);

		// Assert
		assertInstanceOf(DefaultResolver.class, resolver);
	}


	@Test
	void resolveWorldName()
	{
		// Arrange
		when(worldMock.getName()).thenReturn("test_world");
		WorldNameResolver resolver = WorldNameResolver.get(pluginManagerMock);

		// Act
		var result = resolver.resolve(worldMock);

		// Assert
		assertEquals("test_world", result);
	}


	@Test
	void resolveWorldName_multiverse_fail()
	{
		// Arrange
		when(multiverseCoreV4Mock.getDescription()).thenReturn(descriptionMock);
		when(descriptionMock.getVersion()).thenReturn("4.3.16");
		WorldNameResolver resolver = new PluginResolver(multiverseCoreV4Mock);

		// Act
		var result = resolver.resolve(worldMock);

		// Assert
		assertNull(result);
	}


	@Test
	void resolveWorldName_multiverse_succeed()
	{
		// Arrange
		WorldNameResolver resolver = mock(PluginResolver.class);
		when(resolver.resolve(worldMock)).thenReturn("fake alias");

		// Act
		var result = resolver.resolve(worldMock);

		// Assert
		assertEquals("fake alias", result);
	}


	@Test
	void getResolver_for_multiverse_4()
	{
		// Arrange
		when(pluginManagerMock.getPlugin("Multiverse-Core")).thenReturn(multiverseCoreV4Mock);
		when(multiverseCoreV4Mock.isEnabled()).thenReturn(true);

		// Act
		WorldNameResolver resolver = WorldNameResolver.get(pluginManagerMock);

		// Assert
		assertInstanceOf(PluginResolver.class, resolver);

		// Verify
		verify(pluginManagerMock, atLeastOnce()).getPlugin("Multiverse-Core");
		verify(multiverseCoreV4Mock, atLeastOnce()).isEnabled();
	}


	@Test
	void getResolver_for_multiverse_5()
	{
		// Arrange
		when(pluginManagerMock.getPlugin("Multiverse-Core")).thenReturn(multiverseCoreV5Mock);
		when(multiverseCoreV5Mock.isEnabled()).thenReturn(true);

		// Act
		WorldNameResolver resolver = WorldNameResolver.get(pluginManagerMock);

		// Assert
		assertInstanceOf(PluginResolver.class, resolver);

		// Verify
		verify(pluginManagerMock, atLeastOnce()).getPlugin("Multiverse-Core");
		verify(multiverseCoreV5Mock, atLeastOnce()).isEnabled();
	}

}
