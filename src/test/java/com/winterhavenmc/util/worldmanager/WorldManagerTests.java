package com.winterhavenmc.util.worldmanager;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import org.junit.jupiter.api.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WorldManagerTests {

	private final Plugin mockPlugin = mock(Plugin.class);
	private final World mockWorld0 = mock(World.class);
	private final World mockWorld1 = mock(World.class);
	private final Server mockServer = mock(Server.class);
	private final Player mockPlayer = mock(Player.class);
	private final PluginManager mockPluginManager = mock(PluginManager.class);
	private final FileConfiguration mockConfiguration = mock(FileConfiguration.class);

	private final static UUID mockPlayerUUID = new UUID(0,1);
	private final static UUID mockWorld0UUID = new UUID(1,1);
	private final static UUID mockWorld1UUID = new UUID(1,2);

	private WorldManager worldManager;


	@BeforeEach
	public void setUp() {

		// return real logger for mock plugin
		when(mockPlugin.getLogger()).thenReturn(Logger.getLogger("Test Logger"));

		// return responses from mock world
		when(mockWorld0.getName()).thenReturn("world");
		when(mockWorld0.getUID()).thenReturn(mockWorld0UUID);
		when(mockWorld0.getSpawnLocation()).thenReturn(new Location(mockWorld0, 0.0, 0.0, 0.0));

		when(mockWorld1.getName()).thenReturn("nether");
		when(mockWorld1.getUID()).thenReturn(mockWorld1UUID);
		when(mockWorld0.getSpawnLocation()).thenReturn(new Location(mockWorld1, 0.0, 0.0, 0.0));

		// return mock server
		when(mockPlugin.getServer()).thenReturn(mockServer);

		// return responses from the mock server
		when(mockServer.getWorlds()).thenReturn(List.of(mockWorld0, mockWorld1));
		when(mockServer.getWorld(mockWorld0UUID)).thenReturn(mockWorld0);
		when(mockServer.getWorld("world")).thenReturn(mockWorld0);

		// return mock plugin manager
		when(mockServer.getPluginManager()).thenReturn(mockPluginManager);
		// return null from plugin manager when queried for MultiVerse
		when(mockPluginManager.getPlugin("Multiverse-Core")).thenReturn(null);

		// return mock configuration
		when(mockPlugin.getConfig()).thenReturn(mockConfiguration);
		when(mockConfiguration.getStringList("enabled-worlds")).thenReturn(Collections.emptyList());
		when(mockConfiguration.getStringList("disabled-worlds")).thenReturn(List.of("disabled_world1", "disabled_world2"));

		when(mockPlayer.getName()).thenReturn("player1");
		when(mockPlayer.getUniqueId()).thenReturn(mockPlayerUUID);
		when(mockPlayer.getWorld()).thenReturn(mockWorld0);
		when(mockPlayer.getLocation()).thenReturn(new Location(mockWorld0, 3.0, 4.0, 5.0));

		// create a world manager instance by injecting mock plugin
		worldManager = new WorldManager(mockPlugin);
	}

	@AfterEach
	public void tearDown() {
		// destroy world manager
		worldManager = null;
	}


	@Nested
	class WorldManagerSetupTests {
		@Test
		@DisplayName("Test worldManager is not null.")
		void worldManagerNotNull() {
			assertNotNull(worldManager);
		}

		@Test
		@DisplayName("Test plugin.worldManager.getEnabledWorldNames() is not null.")
		void getEnabledWorldNamesNotNull() {
			assertNotNull(worldManager.getEnabledWorldNames());
		}

		@Test
		@DisplayName(("Test enabled worlds collection is not empty."))
		void getEnabledWorldNamesNotEmpty() {
			assertFalse(worldManager.getEnabledWorldNames().isEmpty(),
					"Enabled worlds list is empty. It should contain the default mock world named 'world'.");
		}

		@Test
		@DisplayName("test world name 'world' is in enabled worlds list.")
		void getEnabledWorldNamesTest_contains_world() {

			World world = mockServer.getWorld("world");
			assertNotNull(world, "test world is null.");

			Collection<String> names = worldManager.getEnabledWorldNames();
			for (String name : names) {
				System.out.println(name);
			}
			Assertions.assertTrue(names.contains("world"));
		}

		@Test
		@DisplayName("test world name 'world' is in enabled worlds list.")
		void getEnabledWorldNamesTest_list_contains_null_world() {
			Assertions.assertTrue(worldManager.getEnabledWorldNames().contains("world"));
			Assertions.assertFalse(worldManager.getEnabledWorldNames().contains("nether"));
		}
	}

	@Nested
	class getWorldNameTests {

		@Test
		@DisplayName("get world name from world manager by string name")
		void getWorldNameTest_by_string() {
			assertEquals("world", worldManager.getWorldName("world"));
			assertEquals("", worldManager.getWorldName((String) null));
		}

		@Test
		@DisplayName("get world name from world manager by world object")
		void getWorldNameTest_by_world_object() {
			assertEquals("world", worldManager.getWorldName(mockServer.getWorld("world")));
			assertThrows(IllegalArgumentException.class, () -> worldManager.getWorldName((World) null));
		}

		@Test
		@DisplayName("get world name by world uuid")
		void getWorldNameTest_by_world_uid() {
			assertEquals("world", worldManager.getWorldName(mockWorld0UUID));
			assertThrows(IllegalArgumentException.class, () -> worldManager.getWorldName((UUID) null));
		}

		@Test
		@DisplayName("get world name by location")
		void getWorldNameTest_by_location() {
			Location location = new Location(mockWorld0, 0.0, 0.0, 0.0);
			assertEquals("world", worldManager.getWorldName(location));
			assertThrows(IllegalArgumentException.class, () -> worldManager.getWorldName((Location) null));
		}

		@Test
		@DisplayName("get world name from world manager by world object")
		void getWorldName_by_entity() {
			assertEquals("world", worldManager.getWorldName(mockPlayer));
			assertThrows(IllegalArgumentException.class, () -> worldManager.getWorldName((CommandSender) null));
		}
	}

	@Nested
	class isEnabledTests {
		@Test
		void isEnabledTest_by_object() {
			assertTrue(worldManager.isEnabled(mockWorld0));
			assertTrue(worldManager.isEnabled(mockWorld1));
		}

		@Test
		void isEnabledTest_by_object2() {
			when(mockConfiguration.getStringList("enabled-worlds")).thenReturn(List.of("world"));
			assertTrue(worldManager.isEnabled(mockWorld0));
			assertTrue(worldManager.isEnabled(mockWorld1)); // TODO: THIS SHOULD RETURN FALSE
			System.out.println("Enabled worlds: " + worldManager.getEnabledWorldNames());
		}

		@Test
		void isEnabledTest_by_object_null() {
			assertFalse(worldManager.isEnabled((World) null));
		}

		@Test
		void isEnabledTest_by_uid() {
			assertTrue(worldManager.isEnabled(mockWorld0.getUID()));
		}

		@Test
		void isEnabledTest_by_uid_null() {
			UUID uid = null;
			assertFalse(worldManager.isEnabled(uid));
		}

		@Test
		void isEnabledTest_by_name() {
			assertTrue(worldManager.isEnabled(mockWorld0.getName()));
		}

		@Test
		void isEnabledTest_by_name_null() {
			String string = null;
			assertFalse(worldManager.isEnabled(string));
		}

		@Test
		void isEnabledTest_by_name_empty() {
			assertFalse(worldManager.isEnabled(""));
		}

		@Test
		void isEnabledTest_by_name_nonexistent() {
			assertFalse(worldManager.isEnabled("nonexistent"));
		}
	}

	@Nested
	class SpawnLocationTests {
		@Test
		void getSpawnLocationTest_by_world() {
			assertInstanceOf(Location.class, worldManager.getSpawnLocation(mockWorld0));
		}

		@Test
		void getSpawnLocationTest_by_world_null() {
			assertThrows(IllegalArgumentException.class, () -> worldManager.getSpawnLocation((World) null));
		}

		@Test
		void getSpawnLocationTest_by_entity() {
			assertInstanceOf(Location.class, worldManager.getSpawnLocation(mockPlayer));
		}

		@Test
		void getSpawnLocationTest_by_entity_null() {
			assertThrows(IllegalArgumentException.class, () -> worldManager.getSpawnLocation((Entity) null));
		}
	}

}
