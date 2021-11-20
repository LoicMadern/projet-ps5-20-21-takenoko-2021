package fr.unice.polytech.startingpoint.game.mission;

import fr.unice.polytech.startingpoint.game.board.Coordinate;
import fr.unice.polytech.startingpoint.game.board.Parcel;
import fr.unice.polytech.startingpoint.type.ColorType;
import fr.unice.polytech.startingpoint.type.ImprovementType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class PeasantMissionTest {
    private PeasantMission mission;

    private Parcel parcelMock;
    private Parcel parcelMock2;
    private Parcel parcelMock3;
    private Parcel parcelMock4;

    private Map<Coordinate,Parcel> coordinateParcelMap;

    @BeforeEach
    void setUp(){
        parcelMock = mock(Parcel.class);
        parcelMock2 = mock(Parcel.class);
        parcelMock3 = mock(Parcel.class);
        parcelMock4 = mock(Parcel.class);

        coordinateParcelMap = new HashMap<>();
        coordinateParcelMap.put(new Coordinate(1,-1,0), parcelMock);

        mission = new PeasantMission(ColorType.RED, ImprovementType.NOTHING, 2);
    }

    /**
     * <h1><u>CAS UNE PARCELLE</u></h1>
     */

    @Test
    void missionComplete() {
        Mockito.when(parcelMock.getNbBamboo()).thenReturn(4);
        Mockito.when(parcelMock.getColor()).thenReturn(ColorType.RED);
        Mockito.when(parcelMock.getImprovement()).thenReturn(ImprovementType.NOTHING);

        assertTrue(mission.checkMission(coordinateParcelMap));
    }

    @Test
    void wrongColor() {
        Mockito.when(parcelMock.getNbBamboo()).thenReturn(4);
        Mockito.when(parcelMock.getColor()).thenReturn(ColorType.GREEN);
        Mockito.when(parcelMock.getImprovement()).thenReturn(ImprovementType.NOTHING);

        assertFalse(mission.checkMission(coordinateParcelMap));
    }

    @Test
    void notEnoughBamboo(){
        Mockito.when(parcelMock.getNbBamboo()).thenReturn(3);
        Mockito.when(parcelMock.getColor()).thenReturn(ColorType.RED);
        Mockito.when(parcelMock.getImprovement()).thenReturn(ImprovementType.NOTHING);

        assertFalse(mission.checkMission(coordinateParcelMap));
    }

    @Test
    void wrongImprovement(){Mockito.when(parcelMock.getNbBamboo()).thenReturn(4);
        Mockito.when(parcelMock.getColor()).thenReturn(ColorType.RED);
        Mockito.when(parcelMock.getImprovement()).thenReturn(ImprovementType.WATERSHED);

        assertFalse(mission.checkMission(coordinateParcelMap));
    }

    /**
     * <h1><u>CAS PLUSIEURS PARCELLES</u></h1>
     */

    @Test
    void missionCompleteSpecialGreen() {
        coordinateParcelMap.put(new Coordinate(1,0,-1), parcelMock2);
        coordinateParcelMap.put(new Coordinate(0,1,-1), parcelMock3);
        coordinateParcelMap.put(new Coordinate(-1,1,0), parcelMock4);

        Mockito.when(parcelMock.getNbBamboo()).thenReturn(3);
        Mockito.when(parcelMock2.getNbBamboo()).thenReturn(3);
        Mockito.when(parcelMock3.getNbBamboo()).thenReturn(3);
        Mockito.when(parcelMock4.getNbBamboo()).thenReturn(3);
        Mockito.when(parcelMock.getColor()).thenReturn(ColorType.GREEN);
        Mockito.when(parcelMock2.getColor()).thenReturn(ColorType.GREEN);
        Mockito.when(parcelMock3.getColor()).thenReturn(ColorType.GREEN);
        Mockito.when(parcelMock4.getColor()).thenReturn(ColorType.GREEN);

        PeasantMission specialMissionGreen = new PeasantMission(ColorType.GREEN, ImprovementType.WHATEVER, 2);
        assertTrue(specialMissionGreen.checkMission(coordinateParcelMap));

    }

    @Test
    void missionCompleteSpecialYellow() {
        coordinateParcelMap.put(new Coordinate(1,0,-1), parcelMock2);
        coordinateParcelMap.put(new Coordinate(0,1,-1), parcelMock3);

        Mockito.when(parcelMock.getNbBamboo()).thenReturn(3);
        Mockito.when(parcelMock2.getNbBamboo()).thenReturn(3);
        Mockito.when(parcelMock3.getNbBamboo()).thenReturn(3);
        Mockito.when(parcelMock.getColor()).thenReturn(ColorType.YELLOW);
        Mockito.when(parcelMock2.getColor()).thenReturn(ColorType.YELLOW);
        Mockito.when(parcelMock3.getColor()).thenReturn(ColorType.YELLOW);

        PeasantMission specialMissionYellow = new PeasantMission(ColorType.YELLOW, ImprovementType.WHATEVER, 2);
        assertTrue(specialMissionYellow.checkMission(coordinateParcelMap));
    }

    @Test
    void missionCompleteSpecialRed() {
        coordinateParcelMap.put(new Coordinate(1,0,-1), parcelMock2);

        Mockito.when(parcelMock.getNbBamboo()).thenReturn(3);
        Mockito.when(parcelMock2.getNbBamboo()).thenReturn(3);
        Mockito.when(parcelMock.getColor()).thenReturn(ColorType.RED);
        Mockito.when(parcelMock2.getColor()).thenReturn(ColorType.RED);

        PeasantMission specialMissionRed = new PeasantMission(ColorType.RED, ImprovementType.WHATEVER, 2);
        assertTrue(specialMissionRed.checkMission(coordinateParcelMap));
    }

    @Test
    void missionCompleteSpecialRedTooManyParcelWithBamboo() {
        coordinateParcelMap.put(new Coordinate(1,0,-1), parcelMock2);
        coordinateParcelMap.put(new Coordinate(0,1,-1), parcelMock2);

        Mockito.when(parcelMock.getNbBamboo()).thenReturn(3);
        Mockito.when(parcelMock2.getNbBamboo()).thenReturn(3);
        Mockito.when(parcelMock3.getNbBamboo()).thenReturn(3);
        Mockito.when(parcelMock.getColor()).thenReturn(ColorType.RED);
        Mockito.when(parcelMock2.getColor()).thenReturn(ColorType.RED);
        Mockito.when(parcelMock3.getColor()).thenReturn(ColorType.RED);

        PeasantMission specialMissionRed = new PeasantMission(ColorType.RED, ImprovementType.WHATEVER, 2);
        assertTrue(specialMissionRed.checkMission(coordinateParcelMap));
    }

    @Test
    void missionIncompleteSpecialRedNotEnoughParcelWithBamboo() {
        Mockito.when(parcelMock.getNbBamboo()).thenReturn(3);
        Mockito.when(parcelMock.getColor()).thenReturn(ColorType.RED);

        PeasantMission specialMissionRed = new PeasantMission(ColorType.RED, ImprovementType.WHATEVER, 2);
        assertFalse(specialMissionRed.checkMission(coordinateParcelMap));
    }

    @Test
    void missionIncompleteSpecialGreenTooManyBambooInTheParcels() {
        coordinateParcelMap.put(new Coordinate(1,0,-1), parcelMock2);

        Mockito.when(parcelMock.getNbBamboo()).thenReturn(4);
        Mockito.when(parcelMock2.getNbBamboo()).thenReturn(3);
        Mockito.when(parcelMock.getColor()).thenReturn(ColorType.RED);
        Mockito.when(parcelMock2.getColor()).thenReturn(ColorType.RED);

        PeasantMission specialMissionRed = new PeasantMission(ColorType.RED, ImprovementType.WHATEVER, 2);
        assertFalse(specialMissionRed.checkMission(coordinateParcelMap));
    }

    @Test
    void missionIncompleteSpecialGreenNotEnoughBamboo() {
        coordinateParcelMap.put(new Coordinate(1,0,-1), parcelMock2);

        Mockito.when(parcelMock.getNbBamboo()).thenReturn(2);
        Mockito.when(parcelMock2.getNbBamboo()).thenReturn(3);
        Mockito.when(parcelMock.getColor()).thenReturn(ColorType.RED);
        Mockito.when(parcelMock2.getColor()).thenReturn(ColorType.RED);

        PeasantMission specialMissionRed = new PeasantMission(ColorType.RED, ImprovementType.WHATEVER, 2);
        assertFalse(specialMissionRed.checkMission(coordinateParcelMap));
    }
}