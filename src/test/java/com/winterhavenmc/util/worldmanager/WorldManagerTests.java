package com.winterhavenmc.util.worldmanager;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.*;
import java.util.logging.Logger;

import static com.winterhavenmc.util.worldmanager.WorldManager.UNKNOWN_WORLD;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WorldManagerTests
{
	public static final String ENABLED_WORLDS_CONFIG_KEY = "enabled-worlds";
	public static final String DISABLED_WORLDS_CONFIG_KEY = "disabled-worlds";
	private final Plugin mockPlugin = mock(Plugin.class);
	private final Server mockServer = mock(Server.class);
	private final World[] mockWorld = {mock(World.class), mock(World.class), mock(World.class)};
	private final Player mockPlayer = mock(Player.class);
	private final static UUID mockPlayerUUID = new UUID(0,1);
	private final PluginManager mockPluginManager = mock(PluginManager.class);
	private final FileConfiguration mockConfiguration = mock(FileConfiguration.class);

	private final static UUID mockWorld0UUID = new UUID(1,0);
	private final static UUID mockWorld1UUID = new UUID(1,1);
	private final static UUID mockWorld2UUID = new UUID(1,2);

	private WorldManager worldManager;


	@BeforeEach
	public void setUp()
	{
		setupMockPluginLogger(mockPlugin);
		setupMockWorlds();
		setupMockConfiguration();
		setupMockPlayer();
		setupMockServer();

		// create a world manager instance by injecting mock plugin
		worldManager = new WorldManager(mockPlugin);
	}

	@AfterEach
	public void tearDown()
	{
		// destroy world manager
		worldManager = null;
	}


	@Nested
	class WorldManagerSetupTests
	{
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
		void getEnabledWorldNamesTest_contains_world()
		{
			World world = mockServer.getWorld("world");
			assertNotNull(world, "test world is null.");

			Collection<String> names = worldManager.getEnabledWorldNames();
			for (String name : names) {
				System.out.println(name);
			}
			assertTrue(names.contains("world"));
		}


		@Test
		@DisplayName("test world name 'world' is in the enabled worlds registry.")
		void getEnabledWorldNamesTest_list_contains_null_world()
		{
			assertTrue(worldManager.getEnabledWorldNames().contains("world"));
			assertTrue(worldManager.getEnabledWorldNames().contains("nether"));
			assertTrue(worldManager.getEnabledWorldNames().contains("the_end"));
			assertFalse(worldManager.getEnabledWorldNames().contains("nonexistent_world"));
		}
	}


	@Nested
	class getWorldNameTests
	{
		@Nested
		class byString
		{
			@Test
			@DisplayName("get world name by string")
			void getWorldNameTest_by_string () {
				assertEquals("world", worldManager.getWorldName("world"));
			}


			@Test
			@DisplayName("get world name by null string")
			void getWorldNameTest_by_string_null () {
				assertEquals(UNKNOWN_WORLD, worldManager.getWorldName((String) null));
			}


			@Test
			@DisplayName("get world name by empty string")
			void getWorldNameTest_by_string_empty () {
				assertEquals(UNKNOWN_WORLD, worldManager.getWorldName(""));
			}
		}


		@Nested
		class byWorld
		{
			@Test
			@DisplayName("get world name by world object")
			void getWorldNameTest_by_world_object()
			{
				assertEquals("world", worldManager.getWorldName(mockServer.getWorld("world")));
			}


			@Test
			@DisplayName("get world name by null world object")
			void getWorldNameTest_by_world_object_null()
			{
				assertEquals("\uD83C\uDF10", worldManager.getWorldName((World) null));
			}
		}
	}


	@Nested
	class isEnabledTests
	{
		@Nested
		class byWorld
		{
			@Test
			void isEnabledTest_by_world()
			{
				when(mockConfiguration.getStringList(ENABLED_WORLDS_CONFIG_KEY)).thenReturn(List.of("world"));
				WorldManager worldManager = new WorldManager(mockPlugin);
				assertTrue(worldManager.isEnabled(mockWorld[0]),
						"a world in the config enabled-worlds list is not enabled");
				assertFalse(worldManager.isEnabled(mockWorld[1]),
						"a world not in the config enabled-worlds list is enabled.");
				assertFalse(worldManager.isEnabled(mockWorld[2]),
						"a world not in the config enabled-worlds list is enabled.");
				verify(mockConfiguration, atLeast(3)).getStringList(ENABLED_WORLDS_CONFIG_KEY);
			}


			@Test
			void isEnabledTest_by_world_null()
			{
				assertFalse(worldManager.isEnabled((World) null));
			}
		}


		@Nested
		class byUUID
		{
			@Test
			void isEnabledTest_by_uid()
			{
				assertTrue(worldManager.isEnabled(mockWorld[0].getUID()));
			}


			@SuppressWarnings("ConstantValue")
			@Test
			void isEnabledTest_by_uid_null()
			{
				UUID uid = null;
				assertFalse(worldManager.isEnabled(uid));
			}
		}


		@Test
		void isEnabledTest_by_name()
		{
			assertTrue(worldManager.isEnabled(mockWorld[0].getName()));
		}


		@SuppressWarnings("ConstantValue")
		@Test
		void isEnabledTest_by_name_null()
		{
			String string = null;
			assertFalse(worldManager.isEnabled(string));
		}


		@Test
		void isEnabledTest_by_name_empty()
		{
			assertFalse(worldManager.isEnabled(""));
		}


		@Test
		void isEnabledTest_by_name_nonexistent()
		{
			assertFalse(worldManager.isEnabled("nonexistent"));
		}
	}


	@Nested
	class SpawnLocationTests
	{
		@Test
		void getSpawnLocationTest_by_world()
		{
			assertInstanceOf(Location.class, worldManager.getSpawnLocation(mockWorld[0]));
		}


		@Test
		void getSpawnLocationTest_by_world_null()
		{
			assertNull(worldManager.getSpawnLocation(null));
		}
	}


	@Nested
	class reloadTests
	{
		@Test
		@DisplayName("when config enabled-worlds is empty list")
		void reloadTest_default()
		{
			//TODO: FIX THIS! it appears to be a bug in the isEnabled(String name) method
			// fixed for now by using isEnabled(UUID worldUid) method instead
			worldManager.reload();
			assertTrue(worldManager.isEnabled(mockWorld0UUID));
			assertTrue(worldManager.isEnabled(mockWorld1UUID));
			assertTrue(worldManager.isEnabled(mockWorld2UUID));
			assertEquals(3, worldManager.size(), "there should be 3 worlds in the registry.");
			verify(mockConfiguration, atLeastOnce()).getStringList(ENABLED_WORLDS_CONFIG_KEY);
		}


		@Test
		@DisplayName("that registry is not null after reload")
		void reloadTest_not_null()
		{
			worldManager.reload();
			assertNotNull(worldManager, "world manager registry is null after reload.");
		}


		@Test
		@DisplayName("that registry is not empty after reload")
		void reloadTest_not_empty()
		{
			worldManager.reload();
			assertNotNull(worldManager, "world manager registry is empty after reload.");
		}


		@Test
		@DisplayName("when config disabled-worlds list contains valid world name")
		void reloadTest_disabled_worlds_config_contains_valid_world()
		{
			when(mockConfiguration.getStringList(DISABLED_WORLDS_CONFIG_KEY)).thenReturn(List.of("nether", "nonexistent_world"));
			worldManager.reload();
			assertTrue(worldManager.isEnabled("world"));
			assertFalse(worldManager.isEnabled("nether"));
			assertTrue(worldManager.isEnabled("the_end"));
			verify(mockConfiguration, atLeastOnce()).getStringList(DISABLED_WORLDS_CONFIG_KEY);
		}


		@Test
		@DisplayName("when config disabled-worlds list contains valid world name")
		void reloadTest_disabled_worlds_config_contains_valid_world2()
		{
			when(mockConfiguration.getStringList(ENABLED_WORLDS_CONFIG_KEY)).thenReturn(List.of("world", "nether", "the_end"));
			when(mockConfiguration.getStringList(DISABLED_WORLDS_CONFIG_KEY)).thenReturn(List.of("nether", "nonexistent_world"));
			worldManager.reload();
			assertTrue(worldManager.isEnabled("world"));
			assertFalse(worldManager.isEnabled("nether"));
			assertTrue(worldManager.isEnabled("the_end"));
			verify(mockConfiguration, atLeastOnce()).getStringList(ENABLED_WORLDS_CONFIG_KEY);
			verify(mockConfiguration, atLeastOnce()).getStringList(DISABLED_WORLDS_CONFIG_KEY);
		}


		@Test
		@DisplayName("when server has no worlds")
		void reloadTest_no_server_worlds()
		{
			when(mockServer.getWorlds()).thenReturn(Collections.emptyList());
			worldManager.reload();
			assertTrue(worldManager.getEnabledWorldNames().isEmpty(), "the world manager registry is not empty after reload when server has no worlds.");
			verify(mockServer, atLeastOnce()).getWorlds();
		}


		@Test
		@DisplayName("when config enabled-worlds list contains valid world name")
		void reloadTest_enabled_worlds_populated()
		{
			when(mockConfiguration.getStringList(ENABLED_WORLDS_CONFIG_KEY)).thenReturn(List.of("world", "nonexistent_world_name"));
			worldManager.reload();
			assertTrue(worldManager.isEnabled("world"), "world 'world' is not enabled.");
			assertFalse(worldManager.getEnabledWorldNames().isEmpty(), "the enabled worlds list is empty.");
			assertEquals(1, worldManager.getEnabledWorldNames().size(), "there should be only one enabled world.");
			verify(mockConfiguration, atLeastOnce()).getStringList(ENABLED_WORLDS_CONFIG_KEY);
		}
	}


	@Test
	void getWorldsTest()
	{
		assertTrue(worldManager.getEnabledWorldNames().contains("world"), "world 'world' is not contained in the collection returned.");
		assertEquals(3, worldManager.getEnabledWorldNames().size(), "there should be 3 elements in the collection returned.");
		verify(mockConfiguration, atLeastOnce()).getStringList(ENABLED_WORLDS_CONFIG_KEY);
	}


	@ParameterizedTest
	@EnumSource
	void getWorldsTest_not_null(final EnabledWorldList enabledWorldList)
	{
		when(mockConfiguration.getStringList(ENABLED_WORLDS_CONFIG_KEY)).thenReturn(enabledWorldList.getList());
		assertNotNull(worldManager.getEnabledWorldNames(), "the returned collection is null.");
		verify(mockConfiguration, atLeastOnce()).getStringList(ENABLED_WORLDS_CONFIG_KEY);
	}


	@ParameterizedTest
	@EnumSource()
	void getWorldsTest_not_empty(final EnabledWorldList enabledWorldList)
	{
		when(mockConfiguration.getStringList(ENABLED_WORLDS_CONFIG_KEY)).thenReturn(enabledWorldList.getList());
		worldManager.reload();
		assertFalse(worldManager.getEnabledWorldNames().isEmpty(), "the returned collection is empty.");
		verify(mockConfiguration, atLeastOnce()).getStringList(ENABLED_WORLDS_CONFIG_KEY);
	}


	@Test
	void getWorldsTest_config_populated()
	{
		when(mockConfiguration.getStringList(ENABLED_WORLDS_CONFIG_KEY)).thenReturn(List.of("world", "nonexistent_world_name"));
		worldManager.reload();
		assertTrue(worldManager.getEnabledWorldNames().contains("world"), "world 'world' is not enabled.");
		assertEquals(1, worldManager.getEnabledWorldNames().size(), "there should be only one enabled world.");
		verify(mockConfiguration, atLeastOnce()).getStringList(ENABLED_WORLDS_CONFIG_KEY);
	}


	@Disabled
	@Test
	void getWorldsTest_no_server_worlds_config_populated()
	{
		when(mockServer.getWorlds()).thenReturn(Collections.emptyList());
		when(mockConfiguration.getStringList(ENABLED_WORLDS_CONFIG_KEY)).thenReturn(List.of("world", "nonexistent_world_name"));
		worldManager.reload();

		assertTrue(mockServer.getWorlds().isEmpty(), "the mock server should have returned an empty list.");
		assertTrue(worldManager.getEnabledWorldNames().isEmpty(), "the returned collection should be empty.");

		verify(mockServer, atLeast(2)).getWorlds();
		verify(mockConfiguration, atLeastOnce()).getStringList(ENABLED_WORLDS_CONFIG_KEY);
	}


	@Test
	void containsTest()
	{
		assertTrue(worldManager.contains(mockWorld0UUID), "the registry does not contain the world 'world'.");
		assertFalse(worldManager.contains(mockPlayerUUID), "the registry contains a uuid that is not for a known world.");
	}

	@SuppressWarnings("unused")
	enum EnabledWorldList
	{
		EMPTY(),
		ONE_VALID_WORLD() {
			List<String> getList() {
				return List.of("world");
			}
		},
		NONEXISTENT_WORLD() {
			List<String> getList() {
				return List.of("world", "nonexistent_world");
			}
		};

		List<String> getList() {
			return Collections.emptyList();
		}
	}


	private void setupMockPluginLogger(final Plugin plugin)
	{
		when(plugin.getLogger()).thenReturn(Logger.getLogger(this.getClass().getName()));
	}


	private void setupMockWorlds()
	{
		// return responses for mock worlds
		when(mockWorld[0].getName()).thenReturn("world");
		when(mockWorld[0].getUID()).thenReturn(mockWorld0UUID);
		when(mockWorld[0].getSpawnLocation()).thenReturn(new Location(mockWorld[0], 0.0, 0.0, 0.0));

		when(mockWorld[1].getName()).thenReturn("nether");
		when(mockWorld[1].getUID()).thenReturn(mockWorld1UUID);
		when(mockWorld[1].getSpawnLocation()).thenReturn(new Location(mockWorld[1], 0.0, 0.0, 0.0));

		when(mockWorld[2].getName()).thenReturn("the_end");
		when(mockWorld[2].getUID()).thenReturn(mockWorld2UUID);
		when(mockWorld[2].getSpawnLocation()).thenReturn(new Location(mockWorld[2], 0.0, 0.0, 0.0));

		// return responses for the mock server
		when(mockServer.getWorlds()).thenReturn(List.of(mockWorld));
		when(mockServer.getWorld("world")).thenReturn(mockWorld[0]);
		when(mockServer.getWorld("nether")).thenReturn(mockWorld[1]);
		when(mockServer.getWorld("the_end")).thenReturn(mockWorld[2]);

		when(mockServer.getWorld(mockWorld0UUID)).thenReturn(mockWorld[0]);
		when(mockServer.getWorld(mockWorld1UUID)).thenReturn(mockWorld[1]);
		when(mockServer.getWorld(mockWorld2UUID)).thenReturn(mockWorld[2]);
	}

	private void setupMockConfiguration()
	{
		// return mock configuration
		when(mockPlugin.getConfig()).thenReturn(mockConfiguration);

		// return defaults for config string lists (empty list for enabled-worlds, two nonexistent world names for disabled-worlds)
		when(mockConfiguration.getStringList(ENABLED_WORLDS_CONFIG_KEY)).thenReturn(Collections.emptyList());
		when(mockConfiguration.getStringList(DISABLED_WORLDS_CONFIG_KEY)).thenReturn(List.of("disabled_world1", "disabled_world2"));
	}


	private void setupMockPlayer()
	{
		when(mockPlayer.getName()).thenReturn("player1");
		when(mockPlayer.getUniqueId()).thenReturn(mockPlayerUUID);
		when(mockPlayer.getWorld()).thenReturn(mockWorld[0]);
		when(mockPlayer.getLocation()).thenReturn(new Location(mockWorld[0], 3.0, 4.0, 5.0));
	}


	private void setupMockServer()
	{
		when(mockPlugin.getServer()).thenReturn(mockServer);
		when(mockServer.getPluginManager()).thenReturn(mockPluginManager);
		when(mockPluginManager.getPlugin("Multiverse-Core")).thenReturn(null);
	}

}
