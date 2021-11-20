package fr.unice.polytech.startingpoint.game.board;

import fr.unice.polytech.startingpoint.exception.OutOfResourcesException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceTest {
    private Board board;
    private Resource resource;

    @BeforeEach
    void initialize(){
        board = new Board();
        resource = new Resource();
    }

    @Test
    void goodInitializeParcel(){
        assertEquals(27, resource.getDeckParcel().size());
    }

    @Test
    void goodInitializeCanal(){
        assertEquals(27, resource.getDeckCanal().size());
    }


    @Test
    void goodInitializeMission(){
        assertEquals(45, resource.getNbMission());
    }

    @Test
    void parcelDecrease() throws OutOfResourcesException {
        resource.selectParcel(resource.drawParcels().get(0));
        assertEquals(26,resource.getDeckParcel().size());
    }

    @Test
    void canalDecrease() throws OutOfResourcesException {
        resource.drawCanal();
        assertEquals(26,resource.getDeckCanal().size());
    }

    @Test
    void missionDecreasePeasant() throws OutOfResourcesException {
        resource.drawPeasantMission();
        assertEquals(44,resource.getNbMission());
    }

    @Test
    void missionDecreasePanda() throws OutOfResourcesException {
        resource.drawPandaMission();
        assertEquals(44,resource.getNbMission());
    }

    @Test
    void missionDecreaseParcel() throws OutOfResourcesException {
        resource.drawParcelMission();
        assertEquals(44,resource.getNbMission());
    }

    @Test
    void notOutOfResources(){
        assertFalse(resource.isEmpty());
    }

    @Test
    void outOfCanals() throws OutOfResourcesException {
        for (int i = 0; i < 27; i++) {
            resource.drawCanal();
        }
        for (int i = 0; i < 27; i++) {
            resource.selectParcel(resource.drawParcels().get(0));
        }
        assertTrue(resource.isEmpty());
        assertThrows(OutOfResourcesException.class, () -> resource.drawCanal());
    }

    @Test
    void outOfMissions() throws OutOfResourcesException {
        for (int i = 0; i < 15; i++) {
            resource.drawPandaMission();
            resource.drawParcelMission();
            resource.drawPeasantMission();
        }
        assertFalse(resource.isEmpty());
        assertThrows(OutOfResourcesException.class, () -> resource.drawPandaMission());
        assertThrows(OutOfResourcesException.class, () -> resource.drawPeasantMission());
        assertThrows(OutOfResourcesException.class, () -> resource.drawParcelMission());
    }
}