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

package com.winterhavenmc.library.worldmanager.spawn;

import org.bukkit.Location;
import org.bukkit.World;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class DefaultRetrieverTest
{
	@Mock World worldMock;
	@Mock Location locationMock;


	@Test
	void getSpawnLocation_with_valid_parameter_returns_location()
	{
		// Arrange
		when(worldMock.getSpawnLocation()).thenReturn(locationMock);
		SpawnLocationRetriever defaultRetriever = new DefaultRetriever();

		// Act
		Optional<Location> result = defaultRetriever.getSpawnLocation(worldMock);

		// Assert
		assertEquals(Optional.of(locationMock), result);
	}


	@Test
	void getSpawnLocation_with_null_parameter_returns_null()
	{
		// Arrange
		SpawnLocationRetriever defaultRetriever = new DefaultRetriever();

		// Act
		Optional<Location> result = defaultRetriever.getSpawnLocation(null);

		// Assert
		assertTrue(result.isEmpty());
	}

}
