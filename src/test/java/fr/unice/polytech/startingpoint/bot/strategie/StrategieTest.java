package fr.unice.polytech.startingpoint.bot.strategie;

import fr.unice.polytech.startingpoint.game.Game;
import fr.unice.polytech.startingpoint.game.board.*;
import fr.unice.polytech.startingpoint.type.ColorType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StrategieTest {
    private Parcel parcel1;
    private Parcel parcel2;
    private Parcel parcel3;
    private Parcel parcel4;

    Coordinate coordCentral;
    Coordinate coordinate1;
    Coordinate coordinate2;
    Coordinate coordinate3;
    Coordinate coordinate4;

    Coordinate coordinate7;
    Coordinate coordinate8;
    Coordinate coordinate9;

    private Board board;

    private BoardRules boardRules;

    private Strategie strategie;

    @BeforeEach
    void setUp(){
        Game game = new Game();

        parcel1 = new Parcel(ColorType.RED);
        parcel2 = new Parcel(ColorType.RED);
        parcel3 = new Parcel(ColorType.GREEN);
        parcel4 = new Parcel(ColorType.GREEN);

        coordCentral = new Coordinate(0,0,0);
        coordinate1 = new Coordinate(1, 0, -1);
        coordinate2 = new Coordinate(1, -1, 0);
        coordinate3 = new Coordinate(0, -1, 1);
        coordinate4 = new Coordinate(-1, 0, 1);

        coordinate7 = new Coordinate(2, -1, -1);
        coordinate8 = new Coordinate(2, -2, 0);
        coordinate9 = new Coordinate(1, -2, 1);

        board = game.getBoard();

        boardRules = game.getRules();

        strategie = new MissionPandaStrat(game.getGameInteraction());
    }

    @Test
    void initializeNextCoordinatesNextToCentral(){
        List<Coordinate> nextTocentral = strategie.possibleCoordinatesParcel();

        assertEquals(6,nextTocentral.size());

        Coordinate randomco = nextTocentral.get(0);

        assertEquals(2,Coordinate.getNorm(coordCentral,randomco));

        int[] tabco = randomco.getCoordinate();
        int sumco = tabco[0] + tabco[1] + tabco[2];

        assertEquals(0,sumco);
    }

    @Test
    void initializeNextCoordinatesAwayFromCentral(){
        board.placeParcel(parcel1,coordinate2);

        List<Coordinate> awayFromCentral =  strategie.allPlaces();

        Collections.shuffle(awayFromCentral);

        Coordinate randomCo=awayFromCentral.get(0);

        int [] tabco = randomCo.getCoordinate();
        int sumco = tabco[0] + tabco[1] + tabco[2];

        assertTrue(Coordinate.getNorm(coordinate2,randomCo) < 19);
        assertTrue(Coordinate.getNorm(coordinate2,randomCo) >= 0);

        assertEquals(0,sumco);
    }

    @Test
    void possibleCoordinatesParcelTest(){
        List<Coordinate> possibleCo = strategie.possibleCoordinatesParcel();

        Collections.shuffle(possibleCo);

        assertTrue(boardRules.isPlayableParcel(possibleCo.get(0)));
    }

    @Test
    void notPossibleCoordinatesCanal(){
        List<Coordinate[]> possibleCanals = strategie.possibleCoordinatesCanal();

        assertEquals(possibleCanals.size(),0);

        board.placeParcel(parcel1,coordinate2);
        board.placeParcel(parcel2,coordinate1);
        board.placeParcel(parcel3,coordinate7);

        List<Coordinate[]>possibleCanals2 = strategie.possibleCoordinatesCanal();

        assertEquals(possibleCanals2.size(),2);
    }

    @Test
    void possibleCoordinatesCanal() {
        board.placeParcel(parcel1,coordinate2);
        board.placeParcel(parcel2,coordinate1);

        List<Coordinate[]> possibleCanals = strategie.possibleCoordinatesCanal();

        Collections.shuffle(possibleCanals);

        Coordinate[] tabco = possibleCanals.get(0);

        assertTrue(boardRules.isPlayableCanal(tabco[0],tabco[1]));
    }

    @Test
    void  getBestCanal(){
        board.placeParcel(parcel1,coordinate2);
        board.placeParcel(parcel2,coordinate1);
        board.placeParcel(parcel3,coordinate7);
        board.placeParcel(parcel4,new Coordinate(2,0,-2));

        board.placeCanal(new Canal(),coordinate2,coordinate1);

        Coordinate[] possibleCanals = strategie.getBestCanal(new Coordinate(2,0,-2));

        assertTrue(boardRules.isPlayableCanal(possibleCanals[0],possibleCanals[1]));
        assertTrue(Arrays.stream(possibleCanals).anyMatch(coordinate -> coordinate.equals(coordinate1)));
        assertTrue(Arrays.stream(possibleCanals).anyMatch(coordinate -> coordinate.equals(coordinate7)));
    }

    @Test
    void BestCanalToIrrigateCoordinate9_0CanalPut() {
        board.placeParcel(new Parcel(ColorType.RED), coordinate2);
        board.placeParcel(new Parcel(ColorType.RED), coordinate3);
        board.placeParcel(new Parcel(ColorType.RED), coordinate9);
        Coordinate[] bestCanal = strategie.getBestCanal(coordinate9);

        assertTrue(Arrays.stream(bestCanal).anyMatch(coord -> coord.equals(coordinate2)));
        assertTrue(Arrays.stream(bestCanal).anyMatch(coord -> coord.equals(coordinate3)));
        assertEquals(2,bestCanal.length);
    }

    @Test
    void BestCanalToIrrigateCoordinate3_1CanalPut() {
        board.placeParcel(new Parcel(ColorType.RED), coordinate2);
        board.placeParcel(new Parcel(ColorType.RED), coordinate3);
        board.placeParcel(new Parcel(ColorType.RED), coordinate9);
        board.placeCanal(new Canal(), coordinate2, coordinate3);
        Coordinate[] bestCanal = strategie.getBestCanal(coordinate9);

        assertTrue(Arrays.stream(bestCanal).anyMatch(coord -> coord.equals(coordinate9)));
    }

    @Test
    void notExistPossibleCoordinatesBamboo(){
        assertEquals(0,strategie.possibleCoordinatesPanda().size());
    }

    @Test
    void ExistPossibleCoordinatesBamboo(){
        board.placeParcel(parcel1,coordinate2);

        assertTrue(board.getPlacedParcels().get(coordinate2).getIrrigated());

        assertEquals(coordinate2, strategie.possibleCoordinatesPanda().get(0));
    }

    @Test
    void freePlaceInitialStates(){
        List<Coordinate> newPlaces = strategie.possibleCoordinatesParcel();

        assertEquals(coordinate2,newPlaces.get(0));
        assertEquals(coordinate3,newPlaces.get(1));
        assertEquals(new Coordinate(-1,1,0),newPlaces.get(2));
        assertEquals(new Coordinate(0,1,-1),newPlaces.get(3));
        assertEquals(coordinate1,newPlaces.get(4));
        assertEquals(coordinate4,newPlaces.get(5));
    }

    /** <h2><b>Test  posssibleCoordinatesNextToParcelsWithAColor </b></h2>
     */

    @Test
    void noParcelsSoNoPossibleCoordinatesForAnyColorGiven(){
        List<Coordinate> allPossibleCoNextToBlue = strategie.allPosssibleCoordinatesNextToParcelsWithAColor(ColorType.GREEN);
        List<Coordinate> allPossibleCoNextToRed = strategie.allPosssibleCoordinatesNextToParcelsWithAColor(ColorType.RED);

        assertEquals(0,allPossibleCoNextToBlue.size());
        assertEquals(0,allPossibleCoNextToRed.size());
    }

    @Test
    void twoRedParcelPlaced(){
        Coordinate expectedCo1 = new Coordinate(0,1,-1);//11h
        Coordinate expectedCo2 = coordinate3;//5h
        Coordinate expectedCo3 = coordinate7;//2h distant 2 du centre

        board.placeParcel(parcel1,coordinate1);//3h
        board.placeParcel(parcel2,coordinate2);//2h

        List<Coordinate> allPossibleCoNextToBlue = strategie.allPosssibleCoordinatesNextToParcelsWithAColor(ColorType.GREEN);
        List<Coordinate> allPossibleCoNextToRed = strategie.allPosssibleCoordinatesNextToParcelsWithAColor(ColorType.RED);

        assertEquals(0,allPossibleCoNextToBlue.size());
        assertEquals(3,allPossibleCoNextToRed.size());

        assertTrue(allPossibleCoNextToRed.contains(expectedCo1));
        assertTrue(allPossibleCoNextToRed.contains(expectedCo2));
        assertTrue(allPossibleCoNextToRed.contains(expectedCo3));
    }
}