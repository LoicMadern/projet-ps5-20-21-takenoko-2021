package fr.unice.polytech.startingpoint.game.board;

import fr.unice.polytech.startingpoint.type.CharacterType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CharacterTest {
    private fr.unice.polytech.startingpoint.game.board.Character panda;
    private fr.unice.polytech.startingpoint.game.board.Character peasant;

    @BeforeEach
    void setUp(){
        panda = new fr.unice.polytech.startingpoint.game.board.Character(CharacterType.PANDA);
        peasant = new fr.unice.polytech.startingpoint.game.board.Character(CharacterType.PEASANT);
    }

    @Test
    void samePanda(){
        assertEquals(panda,panda);
        assertEquals(panda.getCharacterType(),new fr.unice.polytech.startingpoint.game.board.Character(CharacterType.PANDA).getCharacterType());
        assertNotEquals(panda,new fr.unice.polytech.startingpoint.game.board.Character(CharacterType.PANDA));
        assertNotEquals(panda,null);
    }

    @Test
    void samePeasant(){
        assertEquals(peasant,peasant);
        assertEquals(peasant.getCharacterType(),new fr.unice.polytech.startingpoint.game.board.Character(CharacterType.PEASANT).getCharacterType());
        assertNotEquals(peasant,new fr.unice.polytech.startingpoint.game.board.Character(CharacterType.PEASANT));
        assertNotEquals(peasant,null);
    }

    @Test
    void moveCharacter(){
        assertEquals(new Coordinate(),panda.getCoordinate());
        assertEquals(new Coordinate(),peasant.getCoordinate());

        panda.setCoordinate(new Coordinate(1,-1,0));
        peasant.setCoordinate(new Coordinate(-1,1,0));

        assertEquals(new Coordinate(1,-1,0),panda.getCoordinate());
        assertEquals(new Coordinate(-1,1,0),peasant.getCoordinate());
    }
}