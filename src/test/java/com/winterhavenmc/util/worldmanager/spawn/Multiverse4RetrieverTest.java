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
import org.bukkit.World;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class Multiverse4RetrieverTest
{
	@Mock com.onarandombox.MultiverseCore.MultiverseCore mv4;
	@Mock World worldMock;
	@Mock MVWorldManager MVWorldManagerMock;
	@Mock MVWorld MVWorldMock;

	@Test
	void getSpawnLocation()
	{
		Multiverse4Retriever retriever = new Multiverse4Retriever(mv4);
		when(mv4.getMVWorldManager()).thenReturn(MVWorldManagerMock);
		when(MVWorldManagerMock.getMVWorld(worldMock)).thenReturn(MVWorldMock);

		retriever.getSpawnLocation(worldMock);
	}

}
