package fr.unice.polytech.startingpoint.game.board;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires
 * @author Manuel Enzo
 * @author Naud Eric
 * @author Madern Loic
 * @author Le Calloch Antoine
 * @version 2020.12.03
 */

class CoordinateTest {

    @Test
    void coorEquals(){ //new equals
        assertEquals(new Coordinate(1,-1,0), new Coordinate(1,-1,0));
        assertNotEquals(new Coordinate(0,1,-1), new Coordinate(1,-1,0));
        assertNotEquals(null, new Coordinate(1,-1,0));
    }

    @Test
    void addCoInSet(){ //new hashCode
        Set<Coordinate> freeCoordinate = new HashSet<>();
        assertTrue(freeCoordinate.add(new Coordinate(1,-1,0)));
        assertTrue(freeCoordinate.add(new Coordinate(0,1,-1)));
        assertFalse(freeCoordinate.add(new Coordinate(1,-1,0)));
        assertFalse(freeCoordinate.add(new Coordinate(0,1,-1)));
    }

    @Test
    void isAround(){
        List<Coordinate> cordAround = new Coordinate(-2,0,2).coordinatesAround();
        assertTrue(cordAround.contains(new Coordinate(-1,-1,2)));
        assertTrue(cordAround.contains(new Coordinate(-1,0,1)));
        assertTrue(cordAround.contains(new Coordinate(-2,1,1)));
        assertTrue(cordAround.contains(new Coordinate(-3,1,2)));
        assertTrue(cordAround.contains(new Coordinate(-3,0,3)));
        assertTrue(cordAround.contains(new Coordinate(-2,-1,3)));
    }

    @Test
    void isNotAround() {
        List<Coordinate> cordAround = new Coordinate(-2,0,2).coordinatesAround();
        assertFalse(cordAround.contains(new Coordinate(1, 0, -1)));
        assertFalse(cordAround.contains(new Coordinate(-1, 2, -1)));
        assertFalse(cordAround.contains(new Coordinate(0, 0, 0)));
    }

    @Test
    void goodNorm(){
        assertEquals(0,Coordinate.getNorm(new Coordinate(1,-1,0),new Coordinate(1,-1,0)));
        assertEquals(6,Coordinate.getNorm(new Coordinate(1,-1,0),new Coordinate(0,1,-1)));
        assertEquals(14,Coordinate.getNorm(new Coordinate(1,-1,0),new Coordinate(-2,0,2)));
        assertEquals(2,Coordinate.getNorm(new Coordinate(1,-1,0),new Coordinate(1,0,-1)));
        assertEquals(0,Coordinate.getNorm(new Coordinate(0,0,0),new Coordinate(0,0,0)));
    }

    @Test
    void normTesting(){
        assertEquals(2,Coordinate.getNorm(new Coordinate(1,-1,0),new Coordinate(1,0,-1)));
        assertNotEquals(17,Coordinate.getNorm(new Coordinate(),new Coordinate(3,0,-3)));
        assertEquals(0,Coordinate.getNorm(new Coordinate(),new Coordinate()));
    }

    @Test
    void onTheSameLine(){
        assertTrue(new Coordinate(0,0,0).isOnTheSameLine(new Coordinate(1,-1,0)));
        assertTrue(new Coordinate(0,0,0).isOnTheSameLine(new Coordinate(0,2,-2)));
        assertTrue(new Coordinate(3,-1,-2).isOnTheSameLine(new Coordinate(-2,-1,3)));
    }

    @Test
    void notOnTheSameLine(){
        assertFalse(new Coordinate(0,0,0).isOnTheSameLine(new Coordinate(1,-2,1)));
        assertFalse(new Coordinate(1,-1,0).isOnTheSameLine(new Coordinate(0,2,-2)));
        assertFalse(new Coordinate(-2,-1,3).isOnTheSameLine(new Coordinate(2,1,-3)));
    }

    @Test
    void goodUnitVector(){
        assertEquals(new Coordinate(1,-1,0),Coordinate.getUnitVector(new Coordinate(0,0,0),new Coordinate(5,-5,0)));
        assertEquals(new Coordinate(1,-1,0),Coordinate.getUnitVector(new Coordinate(-3,2,1),new Coordinate(2,-3,1)));
        assertEquals(new Coordinate(0,1,-1),Coordinate.getUnitVector(new Coordinate(2,-3,1),new Coordinate(2,1,-3)));
    }

    @Test
    void wrongUnitVector(){
        assertNotEquals(new Coordinate(-1,1,0),Coordinate.getUnitVector(new Coordinate(0,0,0),new Coordinate(5,-5,0)));
        assertNotEquals(new Coordinate(-1,1,0),Coordinate.getUnitVector(new Coordinate(-3,2,1),new Coordinate(2,-3,1)));
        assertNotEquals(new Coordinate(0,-1,1),Coordinate.getUnitVector(new Coordinate(2,-3,1),new Coordinate(2,1,-3)));
    }

    @Test
    void goodVector(){
        assertEquals(new Coordinate(5,-5,0),Coordinate.getVector(new Coordinate(0,0,0),new Coordinate(5,-5,0)));
        assertEquals(new Coordinate(5,-5,0),Coordinate.getVector(new Coordinate(-3,2,1),new Coordinate(2,-3,1)));
        assertEquals(new Coordinate(0,4,-4),Coordinate.getVector(new Coordinate(2,-3,1),new Coordinate(2,1,-3)));
    }

    @Test
    void wrongVector(){
        assertNotEquals(new Coordinate(4,-4,0),Coordinate.getVector(new Coordinate(0,0,0),new Coordinate(5,-5,0)));
        assertNotEquals(new Coordinate(1,-1,0),Coordinate.getVector(new Coordinate(-3,2,1),new Coordinate(2,-3,1)));
        assertNotEquals(new Coordinate(0,5,-5),Coordinate.getVector(new Coordinate(2,-3,1),new Coordinate(2,1,-3)));
    }

    @Test
    void coordinateBetween(){
        assertEquals(new ArrayList<>(),Coordinate.getAllCoordinatesBetween(new Coordinate(),new Coordinate(1,-2,1)));
        assertEquals(new ArrayList<>(Collections.singletonList(new Coordinate(1, -1, 0))), Coordinate.getAllCoordinatesBetween(new Coordinate(0,0,0),new Coordinate(2,-2,0)));
        assertEquals(new ArrayList<>(Collections.singletonList(new Coordinate(-1, -1, 2))), Coordinate.getAllCoordinatesBetween(new Coordinate(-2,0,2),new Coordinate(0,-2,2)));
        assertEquals(new ArrayList<>(Collections.singletonList(new Coordinate(-3, 1, 2))), Coordinate.getAllCoordinatesBetween(new Coordinate(-3,2,1),new Coordinate(-3,0,3)));
    }
}
