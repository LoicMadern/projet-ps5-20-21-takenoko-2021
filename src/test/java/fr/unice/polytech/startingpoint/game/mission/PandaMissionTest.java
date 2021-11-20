package fr.unice.polytech.startingpoint.game.mission;

import fr.unice.polytech.startingpoint.game.playerdata.Inventory;
import fr.unice.polytech.startingpoint.type.ColorType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class PandaMissionTest {
    private Inventory inventoryMock;
    private PandaMission mission1;
    private PandaMission mission2;
    private PandaMission mission3;

    @BeforeEach
    void setUp(){
        inventoryMock = mock(Inventory.class);
        mission1 = new PandaMission(ColorType.RED, 2);
        mission2 = new PandaMission(ColorType.RED, 3);
        mission3 = new PandaMission(ColorType.ALL_COLOR, 3);
    }

    @Test
    void missionPoints(){
        assertEquals(mission2.getPoints(),mission3.getPoints());
        assertNotEquals(mission1.getPoints(),mission3.getPoints());
        assertNotEquals(mission1.getPoints(),mission2.getPoints());
    }

    @Test
    void missionCompleteOneColor(){
        Mockito.when(inventoryMock.getInventoryBamboo(ColorType.RED)).thenReturn(2);
        assertTrue(mission1.checkMission(inventoryMock));
    }

    @Test
    void missionCompleteOneColorEvenWhenMoreThanTwoBamboos(){
        Mockito.when(inventoryMock.getInventoryBamboo(ColorType.RED)).thenReturn(3);
        assertTrue(mission1.checkMission(inventoryMock));
    }

    @Test
    void missionCompleteAllColor(){
        Mockito.when(inventoryMock.getInventoryBamboo()).thenReturn(new int[]{1,1,1});
        assertTrue(mission3.checkMission(inventoryMock));
    }

    @Test
    void missionIncompleteBadColor(){
        Mockito.when(inventoryMock.getInventoryBamboo(ColorType.GREEN)).thenReturn(2);
        assertFalse(mission1.checkMission(inventoryMock));
    }

    @Test
    void missionIncompleteNotAllColor(){
        Mockito.when(inventoryMock.getInventoryBamboo()).thenReturn(new int[]{1,1,0});
        assertFalse(mission3.checkMission(inventoryMock));
    }

    @Test
    void missionIncompleteNoEnoughBamboo(){
        Mockito.when(inventoryMock.getInventoryBamboo(ColorType.RED)).thenReturn(1);
        assertFalse(mission1.checkMission(inventoryMock));
    }
}