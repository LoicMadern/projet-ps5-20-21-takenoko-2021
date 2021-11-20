package fr.unice.polytech.startingpoint.game.playerdata;

import fr.unice.polytech.startingpoint.exception.OutOfResourcesException;
import fr.unice.polytech.startingpoint.game.Game;
import fr.unice.polytech.startingpoint.game.board.Canal;
import fr.unice.polytech.startingpoint.game.board.Parcel;
import fr.unice.polytech.startingpoint.game.mission.Mission;
import fr.unice.polytech.startingpoint.game.mission.PandaMission;
import fr.unice.polytech.startingpoint.game.mission.ParcelMission;
import fr.unice.polytech.startingpoint.game.mission.PeasantMission;
import fr.unice.polytech.startingpoint.type.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class PlayerDataTest {
    private Game game;
    private PlayerData playerData;

    @BeforeEach
    void Setup() {
        game = new Game();
        playerData = game.getPlayerData();
    }

    @Test
    void looseStamina() {
        playerData.looseStamina();
        playerData.looseStamina();
        assertThrows(OutOfResourcesException.class, () -> playerData.looseStamina());
    }

    @Test
    void actionList() {
        playerData.add(ActionType.DRAW_PARCELS);
        playerData.add(ActionType.SELECT_PARCEL);
        assertThrows(NoSuchElementException.class, () -> playerData.hasPlayedCorrectly());

        playerData.add(ActionType.PLACE_PARCEL);
        assertTrue(playerData.contains(ActionType.PLACE_PARCEL));
        assertDoesNotThrow(() -> playerData.hasPlayedCorrectly());

        playerData.remove(ActionType.PLACE_PARCEL);
        assertFalse(playerData.contains(ActionType.PLACE_PARCEL));
        assertThrows(NoSuchElementException.class, () -> playerData.hasPlayedCorrectly());
    }

    @Test
    void saveParcel() {
        playerData.saveParcels(new ArrayList<>(Arrays.asList(new Parcel(),new Parcel(),new Parcel())));
        assertEquals(3,playerData.getParcelsSaved().size());

        playerData.saveParcel(new Parcel());
        assertNotNull(playerData.getParcel());

        playerData.resetTemporaryInventory(WeatherType.NO_WEATHER);

        assertNull(playerData.getParcel());
        assertEquals(0,playerData.getParcelsSaved().size());
        assertEquals(0,playerData.getActionTypeList().size());
        assertEquals(2,playerData.getStamina());
    }

    @Test
    void inventory(){
        playerData.addCanal(new Canal());
        assertEquals(1,playerData.getInventory().getInventoryCanal().size());
        playerData.pickCanal();
        assertEquals(0,playerData.getInventory().getInventoryCanal().size());
        assertThrows(OutOfResourcesException.class,() -> playerData.pickCanal());

        playerData.addBamboo(ColorType.RED);
        playerData.addBamboo(ColorType.GREEN);
        playerData.addBamboo(ColorType.YELLOW);
        assertArrayEquals(new int[]{1,1,1},playerData.getInventory().getInventoryBamboo());

        playerData.addMission(new ParcelMission(ColorType.RED, FormType.TRIANGLE, 3));
        playerData.addMission(new PandaMission(ColorType.RED,2));
        playerData.addMission(new PeasantMission(ColorType.RED, ImprovementType.NOTHING, 4));
        assertEquals(2,playerData.getParcelMissions().size());
        assertEquals(2,playerData.getPandaMissions().size());
        assertEquals(2,playerData.getPeasantMissions().size());
    }

    @Test
    void score(){
        assertEquals(0,playerData.getScore()[0]);
        assertEquals(0,playerData.getMissionsDone());

        playerData.addMissionDone(3);

        assertEquals(3,playerData.getScore()[0]);
        assertEquals(1,playerData.getMissionsDone());
    }

    /*
    public void checkMissions(Map<Coordinate, Parcel> coordinateParcelMap){
        List<Mission> toRemoveMissions = new ArrayList<>();
        for(Mission mission : inventory.getMissions()){
            if(mission.checkMission(coordinateParcelMap,inventory)){
                addMissionDone(mission.getPoints());
                if (mission.getMissionType().equals(MissionType.PANDA)){
                    missionsPandaDone ++;
                }
                if (mission.getMissionType().equals(MissionType.PARCEL)){
                    missionsParcelDone ++;
                }
                if (mission.getMissionType().equals(MissionType.PEASANT)){
                    missionsPeasantDone ++;
                }
                toRemoveMissions.add(mission);
            }
        }
        inventory.checkMissions(toRemoveMissions);
    }*/



    /**
     * <h1><u>checkMissions</u></h1>
     */

    @Test
    public void checkMissionsAddMissioPandaDone(){
        //playerData.addMission(new ParcelMission(ColorType.RED, FormType.TRIANGLE, 3));
        playerData.addMission(new PandaMission(ColorType.RED,2));
        //.addMission(new PeasantMission(ColorType.RED, ImprovementType.NOTHING, 4));
        playerData.addBamboo(ColorType.RED);
        playerData.addBamboo(ColorType.RED);

        playerData.checkMissions(game.getBoard().getPlacedParcels());
        assertEquals(1,playerData.getMissionsPandaDone());
    }

    @Test
    public void checkMissionsAddMissionPandaDone(){
        Inventory inventory = playerData.getInventory();
        Mission mission = mock(PandaMission.class);
        Mockito.when(mission.checkMission(game.getBoard().getPlacedParcels(),inventory)).thenReturn(true);
        Mockito.when(mission.getMissionType()).thenReturn(MissionType.PANDA);
        playerData.addMission(mission);
        playerData.checkMissions(game.getBoard().getPlacedParcels());
        assertEquals(1,playerData.getMissionsPandaDone());
    }

    @Test
    public void checkMissionsAddMissionParcelDone(){
        Inventory inventory = playerData.getInventory();
        Mission mission = mock(PandaMission.class);
        Mockito.when(mission.checkMission(game.getBoard().getPlacedParcels(),inventory)).thenReturn(true);
        Mockito.when(mission.getMissionType()).thenReturn(MissionType.PARCEL);
        playerData.addMission(mission);
        playerData.checkMissions(game.getBoard().getPlacedParcels());
        assertEquals(1,playerData.getMissionsParcelDone());
    }

    @Test
    public void checkMissionsAddMissiPeasantDone(){
        Inventory inventory = playerData.getInventory();
        Mission mission = mock(PandaMission.class);
        Mockito.when(mission.checkMission(game.getBoard().getPlacedParcels(),inventory)).thenReturn(true);
        Mockito.when(mission.getMissionType()).thenReturn(MissionType.PEASANT);
        playerData.addMission(mission);
        playerData.checkMissions(game.getBoard().getPlacedParcels());
        assertEquals(1,playerData.getMissionsPeasantDone());
    }

}