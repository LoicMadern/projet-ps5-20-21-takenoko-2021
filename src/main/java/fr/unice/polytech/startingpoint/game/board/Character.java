package fr.unice.polytech.startingpoint.game.board;

import fr.unice.polytech.startingpoint.type.CharacterType;

/**
 * Classe representant un personnage : le panda ou le paysan
 * @author Manuel Enzo
 * @author Naud Eric
 * @author Madern Loic
 * @author Le Calloch Antoine
 * @version 2020.12.03
 */

class Character {
    private final CharacterType characterType;
    private Coordinate coordinate;

    public Character(CharacterType characterType){
        this.characterType = characterType;
        coordinate = new Coordinate(0,0,0);
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public CharacterType getCharacterType() {
        return characterType;
    }

    public Coordinate getCoordinate(){
        return coordinate;
    }
}