package fr.unice.polytech.startingpoint.bot.strategie;

import fr.unice.polytech.startingpoint.game.Game;
import fr.unice.polytech.startingpoint.game.board.*;
import fr.unice.polytech.startingpoint.game.mission.ParcelMission;
import fr.unice.polytech.startingpoint.type.ActionType;
import fr.unice.polytech.startingpoint.type.ColorType;
import fr.unice.polytech.startingpoint.type.FormType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MissionParcelStratTest {

    List<ParcelInformation> greenParcelAvailable;
    List<ParcelInformation> redParcelAvailable;

    ParcelMission missionGreenTriangle;
    ParcelMission missionRedLine;

    Coordinate coordCentral;
    Coordinate coordinate1;
    Coordinate coordinate2;
    Coordinate coordinate3;
    Coordinate coordinate4;

    Coordinate coordinate7;
    Coordinate coordinate8;
    Coordinate coordinate9;

    Game game;
    Board board;

    MissionParcelStrat stratMissionParcel;

    @BeforeEach
    public void setUp() {

        greenParcelAvailable = new ArrayList<>(Collections.singletonList(new ParcelInformation(ColorType.GREEN)));
        redParcelAvailable = new ArrayList<>(Collections.singletonList(new ParcelInformation(ColorType.RED)));

        coordCentral = new Coordinate(0,0,0);
        coordinate1 = new Coordinate(1, 0, -1);
        coordinate2 = new Coordinate(1, -1, 0);
        coordinate3 = new Coordinate(0, -1, 1);
        coordinate4 = new Coordinate(-1, 0, 1);

        coordinate7 = new Coordinate(2, -1, -1);
        coordinate8 = new Coordinate(2, -2, 0);
        coordinate9 = new Coordinate(1, -2, 1);

        game = new Game();
        board = game.getBoard();

        stratMissionParcel = new MissionParcelStrat(game.getGameInteraction());

        missionGreenTriangle = new ParcelMission(ColorType.GREEN, FormType.TRIANGLE, 1);
        missionRedLine = new ParcelMission(ColorType.RED, FormType.LINE, 1);
    }

    @Test
    void LineForm() {
        List<Coordinate> lineForm = stratMissionParcel.setForm(coordinate1, FormType.LINE);

        assertEquals(coordinate1, lineForm.get(0));
        assertEquals(coordinate2, lineForm.get(1));
        assertEquals(coordinate9, lineForm.get(2));
        assertEquals(3,lineForm.size());
    }

    @Test
    void TriangleForm() {
        List<Coordinate> triangleForm = stratMissionParcel.setForm(coordinate2, FormType.TRIANGLE);

        assertEquals(coordinate2, triangleForm.get(0));
        assertEquals(coordinate9, triangleForm.get(1));
        assertEquals(coordinate3, triangleForm.get(2));
        assertEquals(3,triangleForm.size());
    }

    @Test
    void arcForm() {
        List<Coordinate> arcForm = stratMissionParcel.setForm(coordinate7, FormType.ARC);

        assertEquals(coordinate7, arcForm.get(0));
        assertEquals(coordinate2, arcForm.get(1));
        assertEquals(coordinate9, arcForm.get(2));
        assertEquals(3,arcForm.size());
    }

    @Test
    void diamandForm() {
        List<Coordinate> diamandForm = stratMissionParcel.setForm(coordinate7, FormType.DIAMOND);

        assertEquals(coordinate7, diamandForm.get(0));
        assertEquals(coordinate8, diamandForm.get(1));
        assertEquals(coordinate9, diamandForm.get(2));
        assertEquals(coordinate2, diamandForm.get(3));
        assertEquals(4,diamandForm.size());
    }

///////////////////////////////

    @Test
    void coordAroundUse_CoordCentral() {
        Coordinate coordAroundUse = stratMissionParcel.coordAroundUse(coordinate1);

        assertEquals(coordCentral, coordAroundUse);
    }

    @Test
    void coordAroundUse_Coordinate7() {
        board.placeParcel(new Parcel(), coordinate2);
        assertTrue(board.isPlacedParcel(coordinate2));

        assertEquals(coordinate2, stratMissionParcel.coordAroundUse(coordinate7));
    }

    @Test
    void coordCentralWithoutCoordsNext() {
        assertNull(stratMissionParcel.coordAroundUse(coordCentral));
    }

///////////////////////////////

    @Test
    void coordsToDoGreenTriangle_1GreenParcelPut() {
        board.placeParcel(new Parcel(ColorType.GREEN), coordinate2);
        Map<Coordinate,Boolean> coordsToDoGreenTriangle = stratMissionParcel.coordsToDoMission(coordinate2, missionGreenTriangle);

        assertEquals(2, coordsToDoGreenTriangle.size());
        assertTrue(coordsToDoGreenTriangle.get(coordinate9));
        assertTrue(coordsToDoGreenTriangle.get(coordinate3));
    }

    @Test
    void coordsToDoGreenTriangle_1RedParcelPut() {
        board.placeParcel(new Parcel(ColorType.RED), coordinate2);
        Map<Coordinate,Boolean> coordsToDoGreenTriangle = stratMissionParcel.coordsToDoMission(coordinate2, missionGreenTriangle);

        assertNull(coordsToDoGreenTriangle);
    }

    @Test
    void coordsNeedToDoRedLine_1RedParcelPut() {
        board.placeParcel(new Parcel(ColorType.RED), coordinate1);
        Map<Coordinate,Boolean> coordsNeedToDoRedLine = stratMissionParcel.coordsToDoMission(coordinate1, missionRedLine);

        assertEquals(3, coordsNeedToDoRedLine.size());
        assertTrue(coordsNeedToDoRedLine.get(coordinate2));
        assertFalse(coordsNeedToDoRedLine.get(coordinate3));
        assertTrue(coordsNeedToDoRedLine.get(coordinate9));
    }

    @Test
    void coordsNeedToDoRedLine_1GreenParcelPut() {
        board.placeParcel(new Parcel(ColorType.GREEN), coordinate1);
        Map<Coordinate,Boolean> coordsNeedToDoRedLine = stratMissionParcel.coordsToDoMission(coordinate1, missionRedLine);

        assertNull(coordsNeedToDoRedLine);
    }

    @Test
    void coordsNeedToDoRedLine_0ParcelPut() {
        Map<Coordinate,Boolean> coordsNeedToDoRedLine = stratMissionParcel.coordsToDoMission(coordinate1, missionRedLine);

        assertEquals(4, coordsNeedToDoRedLine.size());
        assertTrue(coordsNeedToDoRedLine.get(coordinate2));
        assertFalse(coordsNeedToDoRedLine.get(coordinate3));
        assertTrue(coordsNeedToDoRedLine.get(coordinate9));
        assertTrue(coordsNeedToDoRedLine.get(coordinate1));
    }
///////////////////////////////

    @Test
    void bestCoordsToDoGreenTriangle_1GreenParcelPut() {
        board.placeParcel(new Parcel(ColorType.GREEN), coordinate2);
        Map<Coordinate,Boolean> bestCoordsToDoGreenTriangle = stratMissionParcel.bestCoordinatesForMission(missionGreenTriangle);

        assertEquals(2, bestCoordsToDoGreenTriangle.size());
        assertTrue(bestCoordsToDoGreenTriangle.get(coordinate9));
        assertTrue(bestCoordsToDoGreenTriangle.get(coordinate3));
    }

    @Test
    void bestCoordsNeedToDoRedLine_1RedParcelPut() {
        board.placeParcel(new Parcel(ColorType.RED), coordinate2);
        Map<Coordinate,Boolean> bestCoordsNeedToDoRedLine = stratMissionParcel.bestCoordinatesForMission(missionRedLine);

        assertEquals(3, bestCoordsNeedToDoRedLine.size());
        assertTrue(bestCoordsNeedToDoRedLine.get(coordinate1));
        assertFalse(bestCoordsNeedToDoRedLine.get(coordinate3));
        assertTrue(bestCoordsNeedToDoRedLine.get(coordinate9));
    }

///////////////////////////////

    @Test
    void bestCoordsPossibleBasedOnParcelDrawnToDoGreenTriangle_1GreenParcelPut_GreenColorAvailable() {
        board.placeParcel(new Parcel(ColorType.GREEN), coordinate2);
        List<Coordinate> bestCoordsToPutDoGreenTriangle = stratMissionParcel.bestCoordPossibleBasedOnParcelDrawn(greenParcelAvailable,missionGreenTriangle);

        assertEquals(1, bestCoordsToPutDoGreenTriangle.size());
        assertEquals(coordinate3, bestCoordsToPutDoGreenTriangle.get(0));
    }

    @Test
    void bestCoordsPossibleBasedOnParcelDrawnToDoRedLine_1RedParcelPut_RedColorNotAvailable() {
        board.placeParcel(new Parcel(ColorType.RED), coordinate2);
        List<Coordinate> bestCoordsToPutDoRedLine = stratMissionParcel.bestCoordPossibleBasedOnParcelDrawn(greenParcelAvailable,missionRedLine);

        assertEquals(1, bestCoordsToPutDoRedLine.size());
        assertEquals(coordinate3, bestCoordsToPutDoRedLine.get(0));
    }

    @Test
    void bestCoordsPossibleBasedOnParcelDrawnToDoGreenTriangle_1GreenParcelPut_GreenColorNotAvailable() {
        board.placeParcel(new Parcel(ColorType.GREEN), coordinate2);
        List<Coordinate> bestCoordsToPutDoGreenTriangle = stratMissionParcel.bestCoordPossibleBasedOnParcelDrawn(redParcelAvailable,missionGreenTriangle);

        assertFalse(bestCoordsToPutDoGreenTriangle.contains(coordinate9));
        assertFalse(bestCoordsToPutDoGreenTriangle.contains(coordinate3));
    }

///////////////////////////////

    @Test
    void putParcelToDo_GreenTriangle() {
        assertEquals(1, board.getPlacedParcels().size());
        stratMissionParcel.putParcel(missionGreenTriangle);
        assertEquals(2, board.getPlacedParcels().size());
    }

    @Test
    void putParcelToDo_RedLine() {
        assertEquals(1, board.getPlacedParcels().size());
        stratMissionParcel.putParcel(missionRedLine);
        assertEquals(2, board.getPlacedParcels().size());
    }

///////////////////////////////
///////////////////////////////

    @Test
    void nbMoveForMission_GreenTriangleEnd() {
        board.placeParcel(new Parcel(ColorType.GREEN), coordinate2);
        board.placeParcel(new Parcel(ColorType.GREEN), coordinate3);
        board.placeParcel(new Parcel(ColorType.GREEN), coordinate9);
        board.placeCanal(new Canal(), coordinate2,coordinate3);
        board.placeCanal(new Canal(), coordinate2,coordinate9);
        assertEquals(0,stratMissionParcel.nbMoveForMission(missionGreenTriangle));
    }

    @Test
    void nbMoveForMission_RedLine3Parcels_AllIrrigated() {
        board.placeParcel(new Parcel(ColorType.RED), coordinate2);
        board.placeParcel(new Parcel(ColorType.RED), coordinate3);
        board.placeParcel(new Parcel(ColorType.RED), coordinate9);
        board.placeCanal(new Canal(), coordinate2,coordinate3);
        board.placeCanal(new Canal(), coordinate3,coordinate9);
        assertEquals(1,stratMissionParcel.nbMoveForMission(missionRedLine));
    }

    @Test
    void nbMoveForMission_GreenTriangleEnd_NotIrrigated() {
        board.placeParcel(new Parcel(ColorType.GREEN), coordinate2);
        board.placeParcel(new Parcel(ColorType.GREEN), coordinate3);
        board.placeParcel(new Parcel(ColorType.GREEN), coordinate9);
        assertEquals(2,stratMissionParcel.nbMoveForMission(missionGreenTriangle));
    }

    @Test
    void nbMoveForMission_GreenTriangle2Parcels_2Irrigated() {
        board.placeParcel(new Parcel(ColorType.GREEN), coordinate2);
        board.placeParcel(new Parcel(ColorType.GREEN), coordinate3);
        assertEquals(3,stratMissionParcel.nbMoveForMission(missionGreenTriangle));
    }

    @Test
    void nbMoveForMission_GreenTriangle1Parcels_2Irrigated() {
        board.placeParcel(new Parcel(ColorType.GREEN), coordinate2);
        assertEquals(4,stratMissionParcel.nbMoveForMission(missionGreenTriangle));
    }

    @Test
    void nbMoveForMission_GreenTriangle0Parcel_2Irrigated() {
        assertEquals(5,stratMissionParcel.nbMoveForMission(missionGreenTriangle));
    }

    @Test
    void nbMoveForMission_RedLine0Parcel_3Irrigated() {
        assertEquals(6,stratMissionParcel.nbMoveForMission(missionRedLine));
    }

///////////////////////////////

    @Test
    void howManyMoveToDoMission_GreenTriangleEnd() {
        board.placeParcel(new Parcel(ColorType.GREEN), coordinate2);
        board.placeParcel(new Parcel(ColorType.GREEN), coordinate3);
        board.placeParcel(new Parcel(ColorType.GREEN), coordinate9);
        board.placeCanal(new Canal(), coordinate2,coordinate3);
        board.placeCanal(new Canal(), coordinate2,coordinate9);
        assertEquals(0,stratMissionParcel.howManyMoveToDoMission(missionGreenTriangle));
    }

    @Test
    void howManyMoveToDoMission_RedLine3Parcels_AllIrrigated() {
        board.placeParcel(new Parcel(ColorType.RED), coordinate2);
        board.placeParcel(new Parcel(ColorType.RED), coordinate3);
        board.placeParcel(new Parcel(ColorType.RED), coordinate9);
        board.placeCanal(new Canal(), coordinate2,coordinate3);
        board.placeCanal(new Canal(), coordinate3,coordinate9);
        assertEquals(1,stratMissionParcel.howManyMoveToDoMission(missionRedLine));
    }

    @Test
    void howManyMoveToDoMission_GreenTriangleEnd_NotIrrigated() {
        board.placeParcel(new Parcel(ColorType.GREEN), coordinate2);
        board.placeParcel(new Parcel(ColorType.GREEN), coordinate3);
        board.placeParcel(new Parcel(ColorType.GREEN), coordinate9);
        assertEquals(2,stratMissionParcel.howManyMoveToDoMission(missionGreenTriangle));
    }

    @Test
    void howManyMoveToDoMission_GreenTriangle2Parcels_2Irrigated() {
        board.placeParcel(new Parcel(ColorType.GREEN), coordinate2);
        board.placeParcel(new Parcel(ColorType.GREEN), coordinate3);
        assertEquals(3,stratMissionParcel.howManyMoveToDoMission(missionGreenTriangle));
    }

    @Test
    void howManyMoveToDoMission_RedLine0Parcel_3Irrigated() {
        assertEquals(6,stratMissionParcel.howManyMoveToDoMission(missionRedLine));
    }

    @Test
    void howManyMoveToDoMission_RedLine0Parcel_3Irrigated_NotJudiciousPutParcelAndCanal() {
        game.getPlayerData().add(ActionType.DRAW_PARCELS);
        game.getPlayerData().add(ActionType.PLACE_CANAL);
        assertEquals(-1,stratMissionParcel.howManyMoveToDoMission(missionRedLine));
    }
///////////////////////////////

    @Test
    void findEndMissionWithoutCanal_GreenTriangle_1endMission() {
        board.placeParcel(new Parcel(ColorType.GREEN), coordinate2);
        board.placeParcel(new Parcel(ColorType.GREEN), coordinate3);
        board.placeParcel(new Parcel(ColorType.GREEN), coordinate9);
        assertEquals(coordinate2, stratMissionParcel.coordEndMissionNoIrrigate(missionGreenTriangle).get(0));
        assertEquals(coordinate9, stratMissionParcel.coordEndMissionNoIrrigate(missionGreenTriangle).get(1));
        assertEquals(coordinate3, stratMissionParcel.coordEndMissionNoIrrigate(missionGreenTriangle).get(2));
        assertEquals(3, stratMissionParcel.coordEndMissionNoIrrigate(missionGreenTriangle).size());
    }

    @Test
    void findEndMissionWithoutCanal_GreenTriangle_1endMissionRed() {
        board.placeParcel(new Parcel(ColorType.RED), coordinate2);
        board.placeParcel(new Parcel(ColorType.RED), coordinate3);
        board.placeParcel(new Parcel(ColorType.RED), coordinate9);
        assertEquals(0, stratMissionParcel.coordEndMissionNoIrrigate(missionGreenTriangle).size());
    }

    @Test
    void findEndMissionWithoutCanal_GreenTriangle_0endMission() {
        assertEquals(0, stratMissionParcel.coordEndMissionNoIrrigate(missionGreenTriangle).size());
    }

///////////////////////////////

    @Test
    void putCanal_GreenTriangle_0Canal() {
        board.placeParcel(new Parcel(ColorType.GREEN), coordinate2);
        board.placeParcel(new Parcel(ColorType.GREEN), coordinate3);
        board.placeParcel(new Parcel(ColorType.GREEN), coordinate9);

        assertEquals(0, board.getPlacedCanals().size());
        assertFalse(board.isPlacedCanal(coordinate2,coordinate3));

        stratMissionParcel.putCanal(missionGreenTriangle);

        assertEquals(1, board.getPlacedCanals().size());
        assertTrue(board.isPlacedCanal(coordinate2,coordinate3));
    }

    @Test
    void putCanal_RedLine_0Canal() {
        board.placeParcel(new Parcel(ColorType.RED), coordinate1);
        board.placeParcel(new Parcel(ColorType.RED), coordinate2);
        board.placeParcel(new Parcel(ColorType.RED), coordinate3);
        board.placeParcel(new Parcel(ColorType.RED), coordinate9);

        assertEquals(0, board.getPlacedCanals().size());
        assertFalse(board.isPlacedCanal(coordinate2,coordinate3));

        stratMissionParcel.putCanal(missionRedLine);

        assertEquals(1, board.getPlacedCanals().size());
        assertTrue(board.isPlacedCanal(coordinate3,coordinate2));
    }

    @Test
    void putCanal_RedLine_1Canal() {
        board.placeParcel(new Parcel(ColorType.RED), coordinate1);
        board.placeParcel(new Parcel(ColorType.RED), coordinate2);
        board.placeParcel(new Parcel(ColorType.RED), coordinate3);
        board.placeParcel(new Parcel(ColorType.RED), coordinate9);
        board.placeCanal(new Canal(),coordinate2,coordinate3);

        assertEquals(1, board.getPlacedCanals().size());
        assertFalse(board.isPlacedAndIrrigatedParcel(coordinate9));

        stratMissionParcel.putCanal(missionRedLine);

        assertEquals(2, board.getPlacedCanals().size());
        assertTrue(board.isPlacedAndIrrigatedParcel(coordinate9));
    }

    @Test
    void putCanal_RedLineEnd() {
        board.placeParcel(new Parcel(ColorType.RED), coordinate1);
        board.placeParcel(new Parcel(ColorType.RED), coordinate2);
        board.placeParcel(new Parcel(ColorType.RED), coordinate3);
        board.placeParcel(new Parcel(ColorType.RED), coordinate9);
        board.placeCanal(new Canal(),coordinate2,coordinate3);
        board.placeCanal(new Canal(),coordinate2,coordinate9);

        Coordinate []coordCanal = stratMissionParcel.possibleCoordinatesCanal().get(0);

        assertEquals(2, board.getPlacedCanals().size());

        stratMissionParcel.putCanal(missionRedLine);

        assertEquals(3, board.getPlacedCanals().size());
    }

///////////////////////////////

    @Test
    void judiciousPutParcel() {
        game.getPlayerData().addMission(missionGreenTriangle);

        assertTrue(stratMissionParcel.isJudiciousPutParcel());
    }

    @Test
    void notJudiciousPutParcel_ActionAlreadyPlay() {
        game.getPlayerData().add(ActionType.DRAW_PARCELS);

        assertFalse(stratMissionParcel.isJudiciousPutParcel());
    }

///////////////////////////////

    @Test
    void judiciousPutCanal_PlaceToPut_MissionEnd() {
        board.placeParcel(new Parcel(ColorType.GREEN), coordinate2);
        board.placeParcel(new Parcel(ColorType.GREEN), coordinate9);
        board.placeParcel(new Parcel(ColorType.GREEN), coordinate3);

        assertTrue(stratMissionParcel.isJudiciousPutCanal(missionGreenTriangle));
    }

    @Test
    void notJudiciousPutCanal_0PossiblePlace() {
        board.placeParcel(new Parcel(ColorType.GREEN), coordinate2);
        board.placeParcel(new Parcel(ColorType.GREEN), coordinate3);
        board.placeParcel(new Parcel(ColorType.GREEN), coordinate9);
        board.placeCanal(new Canal(),coordinate2,coordinate3);
        board.placeCanal(new Canal(),coordinate3,coordinate9);
        board.placeCanal(new Canal(),coordinate2,coordinate9);

        assertFalse(stratMissionParcel.isJudiciousPutCanal(missionGreenTriangle));
    }

    @Test
    void notJudiciousPutCanal_0MissionEnd() {
        board.placeParcel(new Parcel(ColorType.GREEN), coordinate2);
        board.placeParcel(new Parcel(ColorType.GREEN), coordinate9);

        assertFalse(stratMissionParcel.isJudiciousPutCanal(missionGreenTriangle));
    }

    @Test
    void notJudiciousPutCanal_ActionAlreadyPlay() {
        game.getPlayerData().add(ActionType.PLACE_CANAL);
        board.placeParcel(new Parcel(ColorType.GREEN), coordinate2);
        board.placeParcel(new Parcel(ColorType.GREEN), coordinate9);
        board.placeParcel(new Parcel(ColorType.GREEN), coordinate3);

        assertFalse(stratMissionParcel.isJudiciousPutCanal(missionGreenTriangle));
    }

////////////////////////////////

    @Test
    void stratOneTurn_PutCanal() {
        board.placeParcel(new Parcel(ColorType.GREEN), coordinate2);
        board.placeParcel(new Parcel(ColorType.GREEN), coordinate3);
        board.placeParcel(new Parcel(ColorType.GREEN), coordinate9);

        assertEquals(0, board.getPlacedCanals().size());
        stratMissionParcel.stratOneTurn(missionGreenTriangle);
        assertEquals(1, board.getPlacedCanals().size());
    }

    @Test
    void stratOneTurn_PutParcel() {
        assertEquals(1, board.getPlacedParcels().size());
        stratMissionParcel.stratOneTurn(missionGreenTriangle);
        assertEquals(2, board.getPlacedParcels().size());
    }

    @Test
    void stratOneTurn_MovePanda() {
        board.placeParcel(new Parcel(ColorType.GREEN), coordinate1);
        game.getPlayerData().add(ActionType.DRAW_PARCELS);
        game.getPlayerData().add(ActionType.PLACE_CANAL);

        assertEquals(coordCentral, board.getPandaCoordinate());
        stratMissionParcel.stratOneTurn(missionGreenTriangle);
        assertNotEquals(coordCentral, board.getPandaCoordinate());
    }

    @Test
    void stratOneTurn_MovePeasant() {
        board.placeParcel(new Parcel(ColorType.GREEN), coordinate1);
        game.getPlayerData().add(ActionType.DRAW_PARCELS);
        game.getPlayerData().add(ActionType.PLACE_CANAL);
        game.getPlayerData().add(ActionType.MOVE_PANDA);

        assertEquals(coordCentral, board.getPeasantCoordinate());
        stratMissionParcel.stratOneTurn(missionGreenTriangle);
        assertNotEquals(coordCentral, board.getPeasantCoordinate());
    }
}