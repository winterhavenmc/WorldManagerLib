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

import com.onarandombox.MultiverseCore.MVWorld;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class PluginBasedResolverTest
{
	@Mock World worldMock;
	@Mock Plugin pluginMock;
	@Mock com.onarandombox.MultiverseCore.MultiverseCore mv4Mock;
	@Mock PluginDescriptionFile mv4DescriptionMock;
	@Mock MVWorldManager mv4WorldManagerMock;
	@Mock MVWorld mv4WorldMock;
	@Mock Location locationMock;

	@Test
	@Disabled
	void resolve_with_valid_parameter_returns_location()
	{
		// Arrange
		when(worldMock.getSpawnLocation()).thenReturn(locationMock);
		PluginBasedResolver resolver = new PluginBasedResolver(pluginMock);

		// Act
		Location result = resolver.resolve(worldMock);

		// Assert
		assertEquals(locationMock, result);

		// Verify
		verify(worldMock, atLeastOnce()).getSpawnLocation();
	}


	@Test
	void resolve_with_null_world_returns_null()
	{
		// Arrange
		PluginBasedResolver resolver = new PluginBasedResolver(pluginMock);

		// Act
		Location result = resolver.resolve(null);

		// Assert
		assertNull(result);
	}


	@Test
	void resolve_with_null_plugin_returns_default_world_spawn()
	{
		// Arrange
		when(worldMock.getSpawnLocation()).thenReturn(locationMock);
		PluginBasedResolver resolver = new PluginBasedResolver(null);

		// Act
		Location result = resolver.resolve(worldMock);

		// Assert
		assertEquals(locationMock, result);

		// Verify
		verify(worldMock, atLeastOnce()).getSpawnLocation();
	}


	@Test
	void resolve_with_Multiverse_4_returns_location()
	{
		// Arrange
		when(mv4Mock.getDescription()).thenReturn(mv4DescriptionMock);
		when(mv4DescriptionMock.getVersion()).thenReturn("4.3.16");
		when(mv4Mock.getMVWorldManager()).thenReturn(mv4WorldManagerMock);
		when(mv4WorldManagerMock.getMVWorld(worldMock)).thenReturn(mv4WorldMock);
		when(mv4WorldMock.getSpawnLocation()).thenReturn(locationMock);
		PluginBasedResolver resolver = new PluginBasedResolver(mv4Mock);

		// Act
		Location result = resolver.resolve(worldMock);

		// Assert
		assertEquals(locationMock, result);

		// Verify
		verify(mv4Mock, atLeastOnce()).getMVWorldManager();
		verify(mv4Mock, atLeastOnce()).getDescription();
		verify(mv4DescriptionMock, atLeastOnce()).getVersion();
		verify(mv4WorldManagerMock, atLeastOnce()).getMVWorld(worldMock);
		verify(mv4WorldMock, atLeastOnce()).getSpawnLocation();
	}


//	@Test
//	void resolve_with_valid_parameter_returns_location_mv5()
//	{
//		// Arrange
//		when(mv5Mock.getApi()).thenReturn(mv5apiMock);
//		when(mv5apiMock.getWorldManager()).thenReturn(mv5WorldManagerMock);
//		when(mv5WorldManagerMock.getWorld(worldMock)).thenReturn(Option<mv5WorldMock>);
//		when(mv5WorldMock.getSpawnLocation()).thenReturn(locationMock);
//		PluginBasedResolver resolver = new PluginBasedResolver(mv4Mock);
//
//		// Act
//		Location result = resolver.resolve(worldMock);
//
//		// Assert
//		assertEquals(locationMock, result);
//
//		// Verify
//		verify(mv4Mock, atLeastOnce()).getMVWorldManager();
//		verify(mv4WorldManagerMock, atLeastOnce()).getMVWorld(worldMock);
//		verify(mv4WorldMock, atLeastOnce()).getSpawnLocation();
//	}

}
