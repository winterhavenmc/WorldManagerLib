package com.winterhavenmc.util.worldmanager;

import org.bukkit.Material;
import org.bukkit.World;
import org.junit.jupiter.api.*;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.world.WorldMock;

import java.util.Collection;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WorldManagerTests {

	@SuppressWarnings("FieldCanBeLocal")
	private ServerMock server;
	private PluginMain plugin;

	@BeforeEach
	public void setUp() {
		// Start the mock server
		server = MockBukkit.mock();

		// start the mock plugin
		plugin = MockBukkit.load(PluginMain.class);
	}

	@AfterEach
	public void tearDown() {
		// Stop the mock server
		MockBukkit.unmock();
	}


	@Nested
	class WorldManagerSetupTests {
		@Test
		@DisplayName("Test worldManager is not null.")
		void worldManagerNotNull() {
			Assertions.assertNotNull(plugin.worldManager);
		}

		@Test
		@DisplayName("Test plugin.worldManager.getEnabledWorldNames() is not null.")
		void getEnabledWorldNamesNotNull() {
			Assertions.assertNotNull(plugin.worldManager.getEnabledWorldNames());
		}

		@Disabled
		@Test
		@DisplayName(("Test enabled worlds collection is not empty."))
		void getEnabledWorldNamesNotEmpty() {
			Assertions.assertFalse(plugin.worldManager.getEnabledWorldNames().isEmpty(),
					"Enabled worlds list is empty. It should contain the default mock world named 'world'.");
		}

		@Disabled
		@Test
		@DisplayName("test world name 'world' is in enabled worlds list.")
		void getEnabledWorldNamesContains() {

//			World world = new WorldMock(Material.DIRT, 3);
			//World world = server.addSimpleWorld("simple_world");
			//World world = server.getWorld("world");
			World world = server.addSimpleWorld("test");
			Assertions.assertNotNull(world, "test world is null.");

			Collection<String> names = plugin.worldManager.getEnabledWorldNames();
			for (String name : names) {
				System.out.println(name);
			}
			Assertions.assertTrue(names.contains("world"));
		}
	}

	@Nested
	class WorldManagerGetWorldTests {

		@Disabled
		@Test
		@DisplayName("get world name from world manager by string name")
		void getWorldNameByString() {
			Assertions.assertEquals("world", plugin.worldManager.getWorldName("world"));
		}

		@Disabled
		@Test
		@DisplayName("get world name from world manager by world object")
		void getWorldNameByWorld() {
			Assertions.assertEquals("world", plugin.worldManager.getWorldName(server.getWorld("world")));
		}

		@Disabled
		@Test
		@DisplayName("get world name by world uuid")
		void getWorldNameByWorldUid() {
			Assertions.assertEquals("world", plugin.worldManager.getWorldName(server.getWorld("world").getUID()));
		}
	}

}
