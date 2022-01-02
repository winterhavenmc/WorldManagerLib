package com.winterhavenmc.util.worldmanager;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import org.junit.jupiter.api.*;

import java.util.Collection;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WorldManagerTests {

	@SuppressWarnings("FieldCanBeLocal")
	private ServerMock server;
	private PluginMain plugin;
	private WorldMock worldMock;

	@BeforeAll
	public void setUp() {
		// Start the mock server
		server = MockBukkit.mock();

		// create mock world
		worldMock = server.addSimpleWorld("world");

		// start the mock plugin
		plugin = MockBukkit.load(PluginMain.class);
	}

	@AfterAll
	public void tearDown() {
		// Stop the mock server
		MockBukkit.unmock();
	}


	@Nested
	class WorldManagerSetupTests {
		@Test
		@DisplayName("Test worldmanager is not null.")
		void worldManagerNotNull() {
			Assertions.assertNotNull(plugin.worldManager);
		}

		@Test
		@DisplayName("Test plugin.worldmanager.getEnabledWorldNames() is not null.")
		void getEnabledWorldNamesNotNull() {
			Assertions.assertNotNull(plugin.worldManager.getEnabledWorldNames());
		}

		@Test
		@DisplayName("test world name 'world' is in enabled worlds list.")
		void getEnabledWorldNamesContains() {
			Collection<String> names = plugin.worldManager.getEnabledWorldNames();
			Assertions.assertTrue(names.contains("world"));
		}
	}

	@Nested
	class WorldManagerGetWorldTests {
		@Test
		@DisplayName("Test get world name from world manager.")
		void getWorldNameByString() {
			Assertions.assertEquals("world", plugin.worldManager.getWorldName(worldMock.getName()));
		}

		@Test
		@DisplayName("Test get world name by world object.")
		void getWorldNameByWorld() {
			Assertions.assertEquals("world", plugin.worldManager.getWorldName(worldMock));
		}

		@Test
		@DisplayName("Test get world name by world uuid.")
		void getWorldNameByWorldUid() {
			Assertions.assertEquals("world", plugin.worldManager.getWorldName(worldMock.getUID()));
		}
	}
}
