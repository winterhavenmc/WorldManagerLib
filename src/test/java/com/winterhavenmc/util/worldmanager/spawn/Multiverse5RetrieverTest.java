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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mvplugins.multiverse.core.MultiverseCoreApi;
import org.mvplugins.multiverse.core.world.MultiverseWorld;
import org.mvplugins.multiverse.core.world.WorldManager;
import org.mvplugins.multiverse.external.vavr.control.Option;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class Multiverse5RetrieverTest
{
	@Mock World worldMock;
	@Mock Location locationMock;
	@Mock MultiverseCoreApi multiverseCoreApiMock;
	@Mock WorldManager worldManagerMock;
	@Mock MultiverseWorld multiverseWorldMock;


	@Test
	void getSpawnLocation_returns_empty_optional_when_MultiverseApi_fails()
	{
		// Act
		Multiverse5Retriever retriever = new Multiverse5Retriever();
		Optional<Location> result = retriever.getSpawnLocation(worldMock);

		// Assert
		assertEquals(Optional.empty(), result);
	}

	@Test
	void getSpawnLocation_returns_empty_optional_when_WorldManager_is_null()
	{
		// Arrange
		try (MockedStatic<MultiverseCoreApi> mocked = mockStatic(MultiverseCoreApi.class)) {
			mocked.when(MultiverseCoreApi::get).thenReturn(multiverseCoreApiMock);

			when(multiverseCoreApiMock.getWorldManager()).thenReturn(null);

			// Act
			Multiverse5Retriever retriever = new Multiverse5Retriever();
			Optional<Location> result = retriever.getSpawnLocation(worldMock);

			// Assert
			assertEquals(Optional.empty(), result);
		}
	}

	@Test
	void getSpawnLocation_returns_empty_optional_when_Option_world_is_empty()
	{
		// Arrange
		try (MockedStatic<MultiverseCoreApi> mocked = mockStatic(MultiverseCoreApi.class)) {
			mocked.when(MultiverseCoreApi::get).thenReturn(multiverseCoreApiMock);

			when(multiverseCoreApiMock.getWorldManager()).thenReturn(worldManagerMock);
			when(worldManagerMock.getWorld(worldMock)).thenReturn(Option.none());

			// Act
			Multiverse5Retriever retriever = new Multiverse5Retriever();
			Optional<Location> result = retriever.getSpawnLocation(worldMock);

			// Assert
			assertEquals(Optional.empty(), result);
		}
	}

	@Test
	void getSpawnLocation_returns_empty_optional_when_Option_world_is_null()
	{
		// Arrange
		try (MockedStatic<MultiverseCoreApi> mocked = mockStatic(MultiverseCoreApi.class)) {
			mocked.when(MultiverseCoreApi::get).thenReturn(multiverseCoreApiMock);

			when(multiverseCoreApiMock.getWorldManager()).thenReturn(worldManagerMock);
			when(worldManagerMock.getWorld(worldMock)).thenReturn(Option.of(multiverseWorldMock));

			// Act
			Multiverse5Retriever retriever = new Multiverse5Retriever();
			Optional<Location> result = retriever.getSpawnLocation(worldMock);

			// Assert
			assertEquals(Optional.empty(), result);
		}
	}

	@Test
	void getSpawnLocation_returns_empty_optional_when_MultiverseWorld_getSpawn_location_is_null()
	{
		// Arrange
		try (MockedStatic<MultiverseCoreApi> mocked = mockStatic(MultiverseCoreApi.class)) {
			mocked.when(MultiverseCoreApi::get).thenReturn(multiverseCoreApiMock);

			when(multiverseCoreApiMock.getWorldManager()).thenReturn(worldManagerMock);
			when(worldManagerMock.getWorld(worldMock)).thenReturn(Option.of(multiverseWorldMock));
			when(multiverseWorldMock.getSpawnLocation()).thenReturn(null);

			// Act
			Multiverse5Retriever retriever = new Multiverse5Retriever();
			Optional<Location> result = retriever.getSpawnLocation(worldMock);

			// Assert
			assertEquals(Optional.empty(), result);
		}
	}

	@Test
	void getSpawnLocation_returns_optional_location()
	{
		// Arrange
		try (MockedStatic<MultiverseCoreApi> mocked = mockStatic(MultiverseCoreApi.class)) {
			mocked.when(MultiverseCoreApi::get).thenReturn(multiverseCoreApiMock);

			when(multiverseCoreApiMock.getWorldManager()).thenReturn(worldManagerMock);
			when(worldManagerMock.getWorld(worldMock)).thenReturn(Option.of(multiverseWorldMock));
			when(multiverseWorldMock.getSpawnLocation()).thenReturn(locationMock);

			// Act
			Multiverse5Retriever retriever = new Multiverse5Retriever();
			Optional<Location> result = retriever.getSpawnLocation(worldMock);

			// Assert
			assertEquals(Optional.of(locationMock), result);
		}
	}

}
