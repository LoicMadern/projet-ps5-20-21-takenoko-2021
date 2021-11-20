package fr.unice.polytech.startingpoint.type;

import fr.unice.polytech.startingpoint.game.board.Coordinate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Enumération contenant les types de formes de parcelles disponibles
 * @author Manuel Enzo
 * @author Naud Eric
 * @author Madern Loic
 * @author Le Calloch Antoine
 * @version 2020.12.03
 */


public enum FormType {
    TRIANGLE (new ArrayList<>(Arrays.asList( new Coordinate() , new Coordinate(0, -1, 1) , new Coordinate(-1, 0, 1) ))) {
        @Override
        public String toString() {
            return "Triangle";
        }
    }
    ,
    LINE (new ArrayList<>(Arrays.asList( new Coordinate() , new Coordinate(0, -1, 1) , new Coordinate(0, -2, 2) ))) {
        @Override
        public String toString() {
            return "Line";
        }
    }
    ,
    ARC (new ArrayList<>(Arrays.asList( new Coordinate() , new Coordinate(-1, 0, 1) , new Coordinate(-1, -1, 2) ))) {
        @Override
        public String toString() {
            return "Arc";
        }
    }
    ,
    DIAMOND (new ArrayList<>(Arrays.asList( new Coordinate() , new Coordinate(0, -1, 1) )), new ArrayList<>(Arrays.asList(new Coordinate(-1, -1, 2),new Coordinate(-1, 0, 1)))) {
        @Override
        public String toString() {
            return "Diamond";
        }
    }
    ;

    private final List<Coordinate> offsetsList1;
    private final List<Coordinate> offsetsList2;

    FormType(List<Coordinate> offsetsList1 , List<Coordinate> offsetsList2){
        this.offsetsList1 = offsetsList1;
        this.offsetsList2 = offsetsList2;
    }

    FormType(List<Coordinate> offsetsList){
        this.offsetsList1 = offsetsList;
        this.offsetsList2 = new ArrayList<>();
    }

    public List<Coordinate> getOffsetsList() {
        return offsetsList1;
    }

    public List<Coordinate> getOffsetsList1() {
        return offsetsList1;
    }
    public List<Coordinate> getOffsetsList2() {
        return offsetsList2;
    }
}