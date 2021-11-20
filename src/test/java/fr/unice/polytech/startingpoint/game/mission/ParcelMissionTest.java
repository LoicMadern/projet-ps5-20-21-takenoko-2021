package fr.unice.polytech.startingpoint.game.mission;

import fr.unice.polytech.startingpoint.game.board.Board;
import fr.unice.polytech.startingpoint.game.board.Coordinate;
import fr.unice.polytech.startingpoint.game.board.Parcel;
import fr.unice.polytech.startingpoint.type.ColorType;
import fr.unice.polytech.startingpoint.type.FormType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ParcelMissionTest {
    private Board boardMock;

    private Parcel parcel1;
    private Parcel parcel2;
    private Parcel parcel3;
    private Parcel parcel4;
    private Parcel parcel5;
    private Parcel parcel6;
    private Parcel parcel7;

    private Map<Coordinate,Parcel> coordinateParcelMap;

    private ParcelMission missionTR;
    private ParcelMission missionLR;
    private ParcelMission missionDR;
    private ParcelMission missionAR;
    private ParcelMission missionDBR;
    private ParcelMission missionTG;
    private ParcelMission missionLG;
    private ParcelMission missionDG;
    private ParcelMission missionAG;
    private ParcelMission missionDBG;

    @BeforeEach
    void setUp(){
        boardMock = Mockito.mock(Board.class);

        parcel1 = new Parcel(ColorType.RED);
        parcel2 = new Parcel(ColorType.RED);
        parcel3 = new Parcel(ColorType.RED);
        parcel4 = new Parcel(ColorType.RED);
        parcel5 = new Parcel(ColorType.RED);
        parcel6 = new Parcel(ColorType.GREEN);
        parcel7 = new Parcel(ColorType.GREEN);

        coordinateParcelMap = new HashMap<>();
        coordinateParcelMap.put(new Coordinate(),new Parcel());
        coordinateParcelMap.put(new Coordinate(1, 0, -1),parcel1);
        coordinateParcelMap.put(new Coordinate(2, -1, -1),parcel2);
        coordinateParcelMap.put(new Coordinate(1, -1, 0),parcel3);
        coordinateParcelMap.put(new Coordinate(2, -2, 0),parcel4);
        coordinateParcelMap.put(new Coordinate(1, -2, 1),parcel5);

        Mockito.when(boardMock.getPlacedParcels()).thenReturn(coordinateParcelMap);

        missionTR = new ParcelMission(ColorType.RED, FormType.TRIANGLE, 1);
        missionLR = new ParcelMission(ColorType.RED, FormType.LINE, 2);
        missionDR = new ParcelMission(ColorType.RED, FormType.DIAMOND, 3);
        missionAR = new ParcelMission(ColorType.RED, FormType.ARC, 4);
        missionDBR = new ParcelMission(ColorType.GREEN,ColorType.RED, FormType.DIAMOND, 5);
        missionTG = new ParcelMission(ColorType.GREEN, FormType.TRIANGLE, 1);
        missionLG = new ParcelMission(ColorType.GREEN, FormType.LINE, 2);
        missionDG = new ParcelMission(ColorType.GREEN, FormType.DIAMOND, 3);
        missionAG = new ParcelMission(ColorType.GREEN, FormType.ARC, 4);
        missionDBG = new ParcelMission(ColorType.GREEN,ColorType.YELLOW, FormType.DIAMOND, 5);
    }

    @Test
    void newMission(){
        assertNotEquals(missionTR,null);
        assertNotEquals(missionTR, missionLR);
        assertNotEquals(missionTR.getFormType(), missionLR.getFormType());
        assertEquals(missionTR, missionTR);
        assertEquals(missionTR.getFormType(), missionTG.getFormType());
    }

    @Test
    void missionColors(){
        assertNotEquals(missionDBR.getColorType2(),missionDBR.getColorType1());
        assertNotEquals(missionDBG.getColorType2(),missionDBG.getColorType1());
        assertEquals(missionDG.getColorType2(),missionDG.getColorType1());
        assertEquals(missionDR.getColorType2(),missionDR.getColorType1());
    }

    @Test
    void missionPoints(){
        assertEquals(missionTR.getPoints(),missionTG.getPoints());
        assertEquals(missionLR.getPoints(),missionLG.getPoints());
        assertEquals(missionDR.getPoints(),missionDG.getPoints());
        assertEquals(missionAR.getPoints(),missionAG.getPoints());
        assertEquals(missionDBR.getPoints(),missionDBG.getPoints());
        assertNotEquals(missionTR.getPoints(),missionLR.getPoints());
        assertNotEquals(missionDR.getPoints(),missionDBR.getPoints());
    }

    /**
     * <h1><u>CAS TRIANGLE</u></h1>
     */

    @Test
    void checkNoMissionTriangle(){
        assertFalse(missionTR.checkMission(boardMock.getPlacedParcels()));
    }

    @Test
    void triangleOnBoardGoodColor(){ //checkTriangle
        parcel2.setIrrigated();
        parcel3.setIrrigated();
        parcel4.setIrrigated();
        assertTrue(missionTR.checkMission(boardMock.getPlacedParcels()));
    }

    @Test
    void triangleOnBoardBadColor() { //checkTriangle
        parcel2.setIrrigated();
        parcel3.setIrrigated();
        parcel4.setIrrigated();
        assertFalse(missionTG.checkMission(boardMock.getPlacedParcels()));
    }

    @Test
    void triangleNotIrrigated() { //checkTriangle
        assertFalse(missionTR.checkMission(boardMock.getPlacedParcels()));
    }

    @Test
    void wrongTriangle() { //checkTriangle
        parcel2.setIrrigated();
        parcel3.setIrrigated();
        parcel4.setIrrigated();
        coordinateParcelMap.remove(new Coordinate(1,-1,0));
        assertFalse(missionTR.checkMission(boardMock.getPlacedParcels()));
    }

    /**
     * <h1><u>CAS LINE</u></h1>
     */

    @Test
    void checkNoMissionLine(){
        assertFalse(missionLR.checkMission(boardMock.getPlacedParcels()));
    }

    @Test
    void lineOnBoardGoodColor(){ //check Line
        parcel1.setIrrigated();
        parcel3.setIrrigated();
        parcel5.setIrrigated();
        assertTrue(missionLR.checkMission(boardMock.getPlacedParcels()));
    }

    @Test
    void lineOnBoardBadColor() { //check Line
        parcel1.setIrrigated();
        parcel3.setIrrigated();
        parcel5.setIrrigated();
        assertFalse(missionLG.checkMission(boardMock.getPlacedParcels()));
    }

    @Test
    void lineNotIrrigated() { //checkLine
        assertFalse(missionLR.checkMission(boardMock.getPlacedParcels()));
    }

    @Test
    void wrongLine() { //checkLine
        parcel1.setIrrigated();
        parcel3.setIrrigated();
        parcel5.setIrrigated();
        coordinateParcelMap.remove(new Coordinate(1,-1,0));
        assertFalse(missionLR.checkMission(boardMock.getPlacedParcels()));
    }

    /**
     * <h1><u>CAS DIAMOND</u></h1>
     */

    @Test
    void checkNoMissionDiamond(){
        assertFalse(missionDR.checkMission(boardMock.getPlacedParcels()));
    }

    @Test
    void diamondOnBoardGoodColor(){ //check Line
        parcel2.setIrrigated();
        parcel3.setIrrigated();
        parcel4.setIrrigated();
        parcel5.setIrrigated();
        assertTrue(missionDR.checkMission(boardMock.getPlacedParcels()));
    }

    @Test
    void diamondOnBoardBadColor() { //check Line
        parcel2.setIrrigated();
        parcel3.setIrrigated();
        parcel4.setIrrigated();
        parcel5.setIrrigated();
        assertFalse(missionDG.checkMission(boardMock.getPlacedParcels()));
    }

    @Test
    void diamondNotIrrigated() { //checkLine
        assertFalse(missionDR.checkMission(boardMock.getPlacedParcels()));
    }

    @Test
    void wrongDiamond() { //checkLine
        parcel2.setIrrigated();
        parcel3.setIrrigated();
        parcel4.setIrrigated();
        parcel5.setIrrigated();
        coordinateParcelMap.remove(new Coordinate(1,-1,0));
        assertFalse(missionDR.checkMission(boardMock.getPlacedParcels()));
    }

    /**
     * <h1><u>CAS ARC</u></h1>
     */

    @Test
    void checkNoMissionArc(){
        assertFalse(missionDR.checkMission(boardMock.getPlacedParcels()));
    }

    @Test
    void arcOnBoardGoodColor(){ //check Line
        parcel2.setIrrigated();
        parcel3.setIrrigated();
        parcel5.setIrrigated();
        assertTrue(missionAR.checkMission(boardMock.getPlacedParcels()));
    }

    @Test
    void arcOnBoardBadColor() { //check Line
        parcel2.setIrrigated();
        parcel3.setIrrigated();
        parcel5.setIrrigated();
        assertFalse(missionAG.checkMission(boardMock.getPlacedParcels()));
    }

    @Test
    void arcNotIrrigated() { //checkLine
        assertFalse(missionAR.checkMission(boardMock.getPlacedParcels()));
    }

    @Test
    void wrongArc() { //checkLine
        parcel2.setIrrigated();
        parcel3.setIrrigated();
        parcel5.setIrrigated();
        coordinateParcelMap.remove(new Coordinate(1,-1,0));
        assertFalse(missionDR.checkMission(boardMock.getPlacedParcels()));
    }

    /**
     * <h1><u>CAS BI COLOR DIAMOND</u></h1>
     */

    @Test
    void checkNoMissionDiamondBiColor(){
        assertFalse(missionDBR.checkMission(boardMock.getPlacedParcels()));
    }

    @Test
    void diamondBiColorOnBoardGoodColor(){ //check Line
        coordinateParcelMap.replace(new Coordinate(2,-1,-1),parcel2,parcel6);
        coordinateParcelMap.replace(new Coordinate(2,-2,0),parcel4,parcel7);
        parcel6.setIrrigated();
        parcel3.setIrrigated();
        parcel7.setIrrigated();
        parcel5.setIrrigated();
        assertTrue(missionDBR.checkMission(boardMock.getPlacedParcels()));
    }

    @Test
    void diamondBiColorOnBoardBadColor() { //check Line
        coordinateParcelMap.replace(new Coordinate(2,-1,-1),parcel2,parcel6);
        coordinateParcelMap.replace(new Coordinate(2,-2,0),parcel4,parcel7);
        parcel6.setIrrigated();
        parcel3.setIrrigated();
        parcel7.setIrrigated();
        parcel5.setIrrigated();
        assertFalse(missionDBG.checkMission(boardMock.getPlacedParcels()));
    }

    @Test
    void diamondBiColorNotIrrigated() { //checkLine
        coordinateParcelMap.replace(new Coordinate(2,-1,-1),parcel2,parcel6);
        coordinateParcelMap.replace(new Coordinate(2,-2,0),parcel4,parcel7);
        assertFalse(missionDR.checkMission(boardMock.getPlacedParcels()));
    }

    @Test
    void wrongDiamondBiColor() { //checkLine
        coordinateParcelMap.replace(new Coordinate(2,-1,-1),parcel2,parcel6);
        coordinateParcelMap.replace(new Coordinate(2,-2,0),parcel4,parcel7);
        parcel6.setIrrigated();
        parcel3.setIrrigated();
        parcel7.setIrrigated();
        parcel5.setIrrigated();
        coordinateParcelMap.remove(new Coordinate(1,-1,0));
        assertFalse(missionDR.checkMission(boardMock.getPlacedParcels()));
    }
}