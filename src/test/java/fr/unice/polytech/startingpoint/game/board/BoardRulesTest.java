package fr.unice.polytech.startingpoint.game.board;

import fr.unice.polytech.startingpoint.game.Game;
import fr.unice.polytech.startingpoint.type.CharacterType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoardRulesTest {
    private Game game;
    private Board board;
    private BoardRules boardRules;
    private Resource resource;

    @BeforeEach
    void initialize(){
        game = new Game();
        board = game.getBoard();
        boardRules = game.getRules();
        resource = game.getResource();
    }

    @Test
    void parcelNextToCenter(){
        assertTrue(boardRules.isPlayableParcel(new Coordinate(0,-1,1)));
    }

    @Test
    void parcelNextToTwoPlacedParcels(){
        board.placeParcel(new Parcel(),new Coordinate(0,-1,1));
        board.placeParcel(new Parcel(),new Coordinate(1,-1,0));
        assertTrue(boardRules.isPlayableParcel(new Coordinate(1,-2,1)));
    }

    @Test
    void parcelOnCenter(){
        assertFalse(boardRules.isPlayableParcel(new Coordinate()));
    }

    @Test
    void parcelAwayFromCenter(){
        assertFalse(boardRules.isPlayableParcel(new Coordinate(1,-2,1)));
    }

    @Test
    void parcelNextToOnePlacedParcel(){
        board.placeParcel(new Parcel(),new Coordinate(0,-1,1));
        assertFalse(boardRules.isPlayableParcel(new Coordinate(1,-2,1)));
    }

    @Test
    void canalOnTwoPlacedParcelsNextToCenter(){
        board.placeParcel(new Parcel(),new Coordinate(0,-1,1));
        board.placeParcel(new Parcel(),new Coordinate(1,-1,0));
        assertTrue(boardRules.isPlayableCanal(new Coordinate(0,-1,1),new Coordinate(1,-1,0)));
    }

    @Test
    void canalsOnTwoPlacedParcelsNextToAnotherCanal(){
        board.placeParcel(new Parcel(),new Coordinate(0,-1,1));
        board.placeParcel(new Parcel(),new Coordinate(1,-1,0));
        board.placeParcel(new Parcel(),new Coordinate(1,-2,1));
        board.placeParcel(new Parcel(),new Coordinate(2,-2,0));
        board.placeCanal(new Canal(),new Coordinate(0,-1,1),new Coordinate(1,-1,0));
        assertTrue(boardRules.isPlayableCanal(new Coordinate(1,-2,1),new Coordinate(1,-1,0))); //isPlacedCanal(coordinate2, coordinate)
        board.placeCanal(new Canal(),new Coordinate(1,-2,1),new Coordinate(1,-1,0));
        assertTrue(boardRules.isPlayableCanal(new Coordinate(1,-2,1),new Coordinate(2,-2,0))); //isPlacedCanal(coordinate1, coordinate)
    }

    @Test
    void canalsOnZeroPlacedParcel(){
        assertFalse(boardRules.isPlayableCanal(new Coordinate(0,-1,1),new Coordinate(1,-1,0)));
    }

    @Test
    void canalsOnOnePlacedParcel(){
        board.placeParcel(new Parcel(),new Coordinate(0,-1,1));
        assertFalse(boardRules.isPlayableCanal(new Coordinate(0,-1,1),new Coordinate(1,-1,0)));
    }

    @Test
    void canalsOnTwoPlacedParcelsNotNextToEachOther(){
        board.placeParcel(new Parcel(),new Coordinate(0,-1,1));
        board.placeParcel(new Parcel(),new Coordinate(1,0,-1));
        assertFalse(boardRules.isPlayableCanal(new Coordinate(0,-1,1),new Coordinate(1,0,-1)));
    }

    @Test
    void canalOnCenterAndOnePlacedParcel(){
        board.placeParcel(new Parcel(),new Coordinate(0,-1,1));
        assertFalse(boardRules.isPlayableCanal(new Coordinate(),new Coordinate(0,-1,1)));
    }

    @Test
    void characterMoveOnPlacedParcelNextToCenter(){
        board.placeParcel(new Parcel(),new Coordinate(0,-1,1));
        assertTrue(boardRules.isMovableCharacter(CharacterType.PANDA,new Coordinate(0,-1,1)));
    }

    @Test
    void characterMoveOnPlacedParcelAwayFromCenter(){
        board.placeParcel(new Parcel(),new Coordinate(0,-1,1));
        board.placeParcel(new Parcel(),new Coordinate(0,-2,2));
        assertTrue(boardRules.isMovableCharacter(CharacterType.PANDA,new Coordinate(0,-2,2)));
    }

    @Test
    void characterMoveOnNotSameLine(){
        board.placeParcel(new Parcel(),new Coordinate(1,-2,1));
        assertFalse(boardRules.isMovableCharacter(CharacterType.PANDA,new Coordinate(1,-2,1)));
    }

    @Test
    void characterMoveOnNPlacedParcelAwayFromCenterNoParcelBetween(){
        board.placeParcel(new Parcel(),new Coordinate(0,-2,2));
        assertFalse(boardRules.isMovableCharacter(CharacterType.PANDA,new Coordinate(0,-2,2)));
    }
}