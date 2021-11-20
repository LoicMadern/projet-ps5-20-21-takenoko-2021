package fr.unice.polytech.startingpoint.game.board;

import fr.unice.polytech.startingpoint.type.CharacterType;
import fr.unice.polytech.startingpoint.type.ColorType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    private Board board;

    @BeforeEach
    void initialize(){
        board = new Board();
    }

    @Test
    void parcelIncrease(){
        board.placeParcel(new Parcel(),new Coordinate(1,-1,0));
        assertEquals(2,board.getPlacedParcels().size());
    }

    @Test void irrigationFromCentral(){
        board.placeParcel(new Parcel(),new Coordinate(0,-1,1));
        assertTrue(board.getPlacedParcels().get(new Coordinate(0,-1,1)).getIrrigated());
    }

    @Test void noIrrigationFromCentral(){
        board.placeParcel(new Parcel(),new Coordinate(0,-1,1));
        board.placeParcel(new Parcel(),new Coordinate(1,-1,0));
        board.placeParcel(new Parcel(),new Coordinate(1,-2,1));
        assertFalse(board.getPlacedParcels().get(new Coordinate(1,-2,1)).getIrrigated());
    }

    @Test void irrigationByCanals(){
        board.placeParcel(new Parcel(),new Coordinate(0,-1,1));
        board.placeParcel(new Parcel(),new Coordinate(1,-1,0));
        board.placeParcel(new Parcel(),new Coordinate(1,-2,1));
        board.placeCanal(new Canal(),new Coordinate(0,-1,1),new Coordinate(1,-1,0));
        board.placeCanal(new Canal(),new Coordinate(0,-1,1),new Coordinate(1,-2,1));
        assertTrue(board.getPlacedParcels().get(new Coordinate(1,-2,1)).getIrrigated());
    }

    //le paysan fait pousser un bambou ou il est
    @Test
    void goodGrow() {
        board.placeParcel(new Parcel(), new Coordinate(1, -1, 0));
        board.moveCharacter(CharacterType.PEASANT,new Coordinate(1, -1, 0));
        assertEquals(2,board.getPlacedParcels().get(new Coordinate(1, -1, 0)).getNbBamboo());
    }

    //4 bambous max
    @Test
    void maxGrow() {
        board.placeParcel(new Parcel(), new Coordinate(1, -1, 0));
        for (int i = 0; i < 5; i++) {
            board.getPlacedParcels().get(new Coordinate(1,-1,0)).addBamboo();
        }
        assertEquals(4,board.getPlacedParcels().get(new Coordinate(1,-1,0)).getNbBamboo());
    }

    //bambous pousse autour si irrigué + même couleur
    @Test
    void actionPeasantSameColorAroundAndIrrigated(){
        board.placeParcel(new Parcel(ColorType.GREEN), new Coordinate(1, -1, 0));
        board.placeParcel(new Parcel(ColorType.GREEN), new Coordinate(1,0,-1));
        board.moveCharacter(CharacterType.PEASANT, new Coordinate(1, -1, 0));
        assertEquals(2,board.getPlacedParcels().get(new Coordinate(1,0,-1)).getNbBamboo());
    }

    //bambous pousse pas autour si couleur diff
    @Test
    void actionPeasantDifferentColorAround(){
        board.placeParcel(new Parcel(ColorType.GREEN), new Coordinate(1, -1, 0));
        board.placeParcel(new Parcel(ColorType.RED), new Coordinate(1,0,-1));
        board.moveCharacter(CharacterType.PEASANT, new Coordinate(1, -1, 0));
        assertEquals(1,board.getPlacedParcels().get(new Coordinate(1,0,-1)).getNbBamboo());
    }

    //bambous pousse pas autour si non irriguée
    @Test
    void actionPeasantNotIrrigatedAround(){
        board.placeParcel(new Parcel(ColorType.GREEN), new Coordinate(1, -1, 0));
        board.placeParcel(new Parcel(ColorType.GREEN), new Coordinate(1, 0, -1));
        board.placeParcel(new Parcel(ColorType.RED), new Coordinate(2,-1,-1));
        board.moveCharacter(CharacterType.PEASANT, new Coordinate(1, -1, 0));
        assertEquals(0,board.getPlacedParcels().get(new Coordinate(2,-1,-1)).getNbBamboo());
    }

    // panda mange un bambou
    @Test
    void goodEat(){
        board.placeParcel(new Parcel(ColorType.RED), new Coordinate(1, -1, 0));
        board.moveCharacter(CharacterType.PANDA, new Coordinate(1, -1, 0));
        assertEquals(0, board.getPlacedParcels().get(new Coordinate(1, -1, 0)).getNbBamboo());
    }

    //il mange pas si 0 bambous sur la parcelle
    @Test
    void minEat(){
        board.placeParcel(new Parcel(), new Coordinate(1, -1, 0));
        board.placeParcel(new Parcel(), new Coordinate(1,0,-1));
        board.moveCharacter(CharacterType.PANDA, new Coordinate(1, -1, 0));
        board.moveCharacter(CharacterType.PANDA, new Coordinate(1,0,-1));
        assertEquals(ColorType.NO_COLOR,board.moveCharacter(CharacterType.PANDA,new Coordinate(1,-1,0)));
    }
}