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
import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mvplugins.multiverse.core.MultiverseCore;
import org.mvplugins.multiverse.core.MultiverseCoreApi;
import org.mvplugins.multiverse.core.world.MultiverseWorld;
import org.mvplugins.multiverse.core.world.WorldManager;
import org.mvplugins.multiverse.external.vavr.control.Option;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MultiverseV5WorldNameResolverTest
{
	@Mock PluginDescriptionFile descriptionFileMock;
	@Mock World worldMock;
	@Mock MultiverseCore multiverseCoreMock;
	@Mock MultiverseCoreApi multiverseCoreApiMock;
	@Mock WorldManager worldManagerMock;
	@Mock MultiverseWorld mvWorldMock;
	@Mock Option<MultiverseWorld> optionWorldMock;

	@Test
	@Disabled
	void testResolveWorldName_valid_world()
	{
		// Arrange
		when(worldMock.getName()).thenReturn("test_world");

		// Act
		WorldNameResolver resolver = new PluginResolver(multiverseCoreMock);
		String result = resolver.resolve(worldMock);

		// Assert
		assertEquals("test_world", result);
	}


	@Test
	void testResolveWorldName_null_world()
	{
		// Act
		WorldNameResolver resolver = new PluginResolver(multiverseCoreMock);
		String result = resolver.resolve(null);

		// Assert
		assertEquals("NULL", result);
	}


	@Test
	@Disabled
	void getWorldName_when_plugin_null()
	{
		// Arrange
		// Act
		WorldNameResolver resolver = new PluginResolver(null);
		String result = resolver.resolve(worldMock);

		// Assert
		assertNull(result);
	}


	@Test
	void getWorldName_blank_string_returned()
	{
		// Arrange
		try (MockedStatic<MultiverseCoreApi> mocked = mockStatic(MultiverseCoreApi.class))
		{
			mocked.when(MultiverseCoreApi::get).thenReturn(multiverseCoreApiMock);
			when(multiverseCoreMock.getDescription()).thenReturn(descriptionFileMock);
			when(descriptionFileMock.getVersion()).thenReturn("5.0.2");
			when(multiverseCoreApiMock.getWorldManager()).thenReturn(worldManagerMock);
			when(worldManagerMock.getWorld(worldMock)).thenReturn(Option.of(mvWorldMock));
			when(mvWorldMock.getAliasOrName()).thenReturn("");

			// Act
			WorldNameResolver resolver = new PluginResolver(multiverseCoreMock);
			String result = resolver.resolve(worldMock);

			// Assert
			assertNull(result);

			// Verify
			verify(multiverseCoreApiMock, atLeastOnce()).getWorldManager();
			verify(worldManagerMock, atLeastOnce()).getWorld(worldMock);
			verify(mvWorldMock, atLeastOnce()).getAliasOrName();
		}
	}


	@Test
	@Disabled
	void getWorldName_null_api()
	{
		// Arrange
		try (MockedStatic<MultiverseCoreApi> mocked = mockStatic(MultiverseCoreApi.class))
		{
			mocked.when(MultiverseCoreApi::get).thenReturn(null);
			when(multiverseCoreMock.getDescription()).thenReturn(descriptionFileMock);
			when(descriptionFileMock.getVersion()).thenReturn("5.0.2");

			// Act
			WorldNameResolver resolver = new PluginResolver(multiverseCoreMock);
			String result = resolver.resolve(worldMock);

			// Assert
			assertNull(result);

			// Verify
			verify(multiverseCoreMock, atLeastOnce()).getDescription();
			verify(descriptionFileMock, atLeastOnce()).getVersion();
		}
	}


	@Test
	void getWorldName_null_WorldManager()
	{
		// Arrange
		try (MockedStatic<MultiverseCoreApi> mocked = mockStatic(MultiverseCoreApi.class))
		{
			mocked.when(MultiverseCoreApi::get).thenReturn(multiverseCoreApiMock);
			when(multiverseCoreMock.getDescription()).thenReturn(descriptionFileMock);
			when(descriptionFileMock.getVersion()).thenReturn("5.0.2");
			when(multiverseCoreApiMock.getWorldManager()).thenReturn(null);

			// Act
			WorldNameResolver resolver = new PluginResolver(multiverseCoreMock);
			String result = resolver.resolve(worldMock);

			// Assert
			assertNull(result);

			// Verify
			verify(multiverseCoreMock, atLeastOnce()).getDescription();
			verify(descriptionFileMock, atLeastOnce()).getVersion();
			verify(multiverseCoreApiMock, atLeastOnce()).getWorldManager();
		}
	}


	@Test
	@Disabled
	void getWorldName_when_WorldManager_returns_Option_none()
	{
		// Arrange
		try (MockedStatic<MultiverseCoreApi> mocked = mockStatic(MultiverseCoreApi.class))
		{
			mocked.when(MultiverseCoreApi::get).thenReturn(multiverseCoreApiMock);
			when(multiverseCoreMock.getDescription()).thenReturn(descriptionFileMock);
			when(descriptionFileMock.getVersion()).thenReturn("5.0.2");
			when(multiverseCoreApiMock.getWorldManager()).thenReturn(worldManagerMock);
			when(worldManagerMock.getWorld(worldMock)).thenReturn(Option.none());

			// Act
			WorldNameResolver resolver = new PluginResolver(multiverseCoreMock);
			String result = resolver.resolve(worldMock);

			// Assert
			assertNull(result);

			// Verify
			verify(multiverseCoreMock, atLeastOnce()).getDescription();
			verify(descriptionFileMock, atLeastOnce()).getVersion();
			verify(multiverseCoreApiMock, atLeastOnce()).getWorldManager();
			verify(worldManagerMock, atLeastOnce()).getWorld(worldMock);
		}
	}

	@Test
	void getWorldName_when_Option_world_returns_null()
	{
		// Arrange
		try (MockedStatic<MultiverseCoreApi> mocked = mockStatic(MultiverseCoreApi.class))
		{
			mocked.when(MultiverseCoreApi::get).thenReturn(multiverseCoreApiMock);
			when(multiverseCoreMock.getDescription()).thenReturn(descriptionFileMock);
			when(descriptionFileMock.getVersion()).thenReturn("5.0.2");
			when(multiverseCoreApiMock.getWorldManager()).thenReturn(worldManagerMock);
			when(worldManagerMock.getWorld(worldMock)).thenReturn(optionWorldMock);
			when(optionWorldMock.getOrNull()).thenReturn(null);

			// Act
			WorldNameResolver resolver = new PluginResolver(multiverseCoreMock);
			String result = resolver.resolve(worldMock);

			// Assert
			assertNull(result);

			// Verify
			verify(multiverseCoreMock, atLeastOnce()).getDescription();
			verify(descriptionFileMock, atLeastOnce()).getVersion();
			verify(multiverseCoreApiMock, atLeastOnce()).getWorldManager();
			verify(worldManagerMock, atLeastOnce()).getWorld(worldMock);
			verify(optionWorldMock, atLeastOnce()).getOrNull();
		}
	}

	@Test
	void getWorldName_when_Option_world_returns_valid()
	{
		// Arrange
		try (MockedStatic<MultiverseCoreApi> mocked = mockStatic(MultiverseCoreApi.class))
		{
			mocked.when(MultiverseCoreApi::get).thenReturn(multiverseCoreApiMock);
			when(multiverseCoreMock.getDescription()).thenReturn(descriptionFileMock);
			when(descriptionFileMock.getVersion()).thenReturn("5.0.2");
			when(multiverseCoreApiMock.getWorldManager()).thenReturn(worldManagerMock);
			when(worldManagerMock.getWorld(worldMock)).thenReturn(optionWorldMock);
			when(optionWorldMock.getOrNull()).thenReturn(mvWorldMock);
			when(mvWorldMock.getAliasOrName()).thenReturn("resolved name");

			// Act
			WorldNameResolver resolver = new PluginResolver(multiverseCoreMock);
			String result = resolver.resolve(worldMock);

			// Assert
			assertEquals("resolved name", result);

			// Verify
			verify(multiverseCoreMock, atLeastOnce()).getDescription();
			verify(descriptionFileMock, atLeastOnce()).getVersion();
			verify(multiverseCoreApiMock, atLeastOnce()).getWorldManager();
			verify(worldManagerMock, atLeastOnce()).getWorld(worldMock);
			verify(optionWorldMock, atLeastOnce()).getOrNull();
			verify(mvWorldMock, atLeastOnce()).getAliasOrName();
		}
	}

}
