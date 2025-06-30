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

import com.onarandombox.MultiverseCore.MVWorld;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class Multiverse4RetrieverTest
{
	@Mock com.onarandombox.MultiverseCore.MultiverseCore mv4PluginMock;
	@Mock World worldMock;
	@Mock Location locationMock;
	@Mock MVWorldManager mv4WorldManagerMock;
	@Mock MVWorld mv4WorldMock;


	@Test
	void getSpawnLocation_with_valid_parameters_returns_optional_location()
	{
		// Arrange
		Multiverse4Retriever retriever = new Multiverse4Retriever(mv4PluginMock);
		when(mv4PluginMock.getMVWorldManager()).thenReturn(mv4WorldManagerMock);
		when(mv4WorldManagerMock.getMVWorld(worldMock)).thenReturn(mv4WorldMock);
		when(mv4WorldMock.getSpawnLocation()).thenReturn(locationMock);

		// Act
		Optional<Location> result = retriever.getSpawnLocation(worldMock);

		// Assert
		assertTrue(result.isPresent());
		assertInstanceOf(Location.class, result.get());

		// Verify
		verify(mv4PluginMock, atLeastOnce()).getMVWorldManager();
		verify(mv4WorldManagerMock, atLeastOnce()).getMVWorld((worldMock));
		verify(mv4WorldMock, atLeastOnce()).getSpawnLocation();
	}


	@Test
	void getSpawnLocation_with_null_api_returns_empty_optional()
	{
		// Arrange
		Multiverse4Retriever retriever = new Multiverse4Retriever(null);

		// Act
		Optional<Location> result = retriever.getSpawnLocation(worldMock);

		// Assert
		assertTrue(result.isEmpty());
	}


	@Test
	void getSpawnLocation_with_null_world_manager_returns_empty_optional()
	{
		// Arrange
		Multiverse4Retriever retriever = new Multiverse4Retriever(mv4PluginMock);
		when(mv4PluginMock.getMVWorldManager()).thenReturn(null);

		// Act
		Optional<Location> result = retriever.getSpawnLocation(worldMock);

		// Assert
		assertTrue(result.isEmpty());

		// Verify
		verify(mv4PluginMock, atLeastOnce()).getMVWorldManager();
	}


	@Test
	void getSpawnLocation_with_null_multiverse_world_returns_empty_optional()
	{
		// Arrange
		Multiverse4Retriever retriever = new Multiverse4Retriever(mv4PluginMock);
		when(mv4PluginMock.getMVWorldManager()).thenReturn(mv4WorldManagerMock);
		when(mv4WorldManagerMock.getMVWorld(worldMock)).thenReturn(null);

		// Act
		Optional<Location> result = retriever.getSpawnLocation(worldMock);

		// Assert
		assertTrue(result.isEmpty());

		// Verify
		verify(mv4PluginMock, atLeastOnce()).getMVWorldManager();
		verify(mv4WorldManagerMock, atLeastOnce()).getMVWorld((worldMock));
	}

}
