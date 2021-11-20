package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.type.BotType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StatTest {
    private Stat statGame1P;
    private Stat statGame2P;
    private Stat statGame4P;

    private List<int[]> p1w;
    private List<int[]> p1w2Player;
    private List<int[]> p2w2Player;
    private List<int[]> equality2Player;
    private List<int[]> p1w4Player;
    private List<int[]> p2w4Player;

    private BotType[] p1 = new BotType[]{BotType.RANDOM};
    private BotType[] p2 = new BotType[]{BotType.RANDOM,BotType.PARCEL_BOT};
    private BotType[] p4 = new BotType[]{BotType.RANDOM,BotType.PARCEL_BOT,BotType.PEASANT_BOT,BotType.PANDA_BOT};

    @BeforeEach
    void initialize(){
        statGame1P = new Stat(p1);
        statGame2P = new Stat(p2);
        statGame4P = new Stat(p4);
        p1w = new ArrayList<>(Collections.singletonList(new int[]{2, 0}));
        p1w2Player = new ArrayList<>(Arrays.asList(new int[]{2,0},new int[]{1,0}));
        p2w2Player = new ArrayList<>(Arrays.asList(new int[]{1,0},new int[]{2,0}));
        equality2Player = new ArrayList<>(Arrays.asList(new int[]{2,0},new int[]{2,0}));
        p1w4Player = new ArrayList<>(Arrays.asList(new int[]{6,1},new int[]{6,0},new int[]{2,2},new int[]{2,0}));
        p2w4Player = new ArrayList<>(Arrays.asList(new int[]{6,0},new int[]{6,1},new int[]{2,0},new int[]{2,2}));
    }

    @Test
    void p1Winner2Player(){
        statGame2P.add(p1w2Player);

        assertEquals(100,statGame2P.getWinRate(0));
        assertEquals(0,statGame2P.getWinRate(1));

        assertEquals(0,statGame2P.getEqualityRate(0));
        assertEquals(0,statGame2P.getEqualityRate(1));

        assertEquals(2,statGame2P.getPointsAverage(0));
        assertEquals(1,statGame2P.getPointsAverage(1));
    }

    @Test
    void p2Winner2Player(){
        statGame2P.add(p2w2Player);
        assertEquals(0,statGame2P.getWinRate(0));
        assertEquals(100,statGame2P.getWinRate(1));

        assertEquals(0,statGame2P.getEqualityRate(0));
        assertEquals(0,statGame2P.getEqualityRate(1));

        assertEquals(1,statGame2P.getPointsAverage(0));
        assertEquals(2,statGame2P.getPointsAverage(1));
    }

    @Test
    void equalityCase2Player(){
        statGame2P.add(equality2Player);
        assertEquals(0,statGame2P.getWinRate(0));
        assertEquals(0,statGame2P.getWinRate(1));

        assertEquals(100,statGame2P.getEqualityRate(0));
        assertEquals(100,statGame2P.getEqualityRate(1));

        assertEquals(2,statGame2P.getPointsAverage(0));
        assertEquals(2,statGame2P.getPointsAverage(1));
    }

    @Test
    void p1Win700GameLose3002Player(){
        for (int i = 0; i < 1000; i++) {
            if(i<700)
                statGame2P.add(p1w2Player);
            else
                statGame2P.add(p2w2Player);
        }
        assertEquals(70,statGame2P.getWinRate(0));
        assertEquals(30,statGame2P.getWinRate(1));

        assertEquals(0,statGame2P.getEqualityRate(0));
        assertEquals(0,statGame2P.getEqualityRate(1));

        assertEquals(1.7,statGame2P.getPointsAverage(0));
        assertEquals(1.3,statGame2P.getPointsAverage(1));
    }

    @Test
    void p1Win700GameLose3004Player(){
        for (int i = 0; i < 1000; i++) {
            if(i<700)
                statGame4P.add(p1w4Player);
            else
                statGame4P.add(p2w4Player);
        }

        assertEquals(70,statGame4P.getWinRate(0));
        assertEquals(30,statGame4P.getWinRate(1));
        assertEquals(0,statGame4P.getWinRate(2));
        assertEquals(0,statGame4P.getWinRate(3));

        assertEquals(0,statGame4P.getEqualityRate(0));
        assertEquals(0,statGame4P.getEqualityRate(1));
        assertEquals(0,statGame4P.getEqualityRate(2));
        assertEquals(0,statGame4P.getEqualityRate(3));

        assertEquals(6.0,statGame4P.getPointsAverage(0));
        assertEquals(6.0,statGame4P.getPointsAverage(1));
        assertEquals(2.0,statGame4P.getPointsAverage(2));
        assertEquals(2.0,statGame4P.getPointsAverage(3));
    }

    @Test
    void p1W1PToString(){
        statGame1P.add(p1w);
        assertEquals("Joueur Random Bot 1 : 100.0% win rate, 0.0% loss rate and 0.0% equality rate with a 2.0 points average\n",statGame1P.toString());
    }
}