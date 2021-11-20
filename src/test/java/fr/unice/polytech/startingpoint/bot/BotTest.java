package fr.unice.polytech.startingpoint.bot;

import fr.unice.polytech.startingpoint.game.Game;
import fr.unice.polytech.startingpoint.game.board.Coordinate;
import fr.unice.polytech.startingpoint.game.board.Parcel;
import fr.unice.polytech.startingpoint.game.mission.Mission;
import fr.unice.polytech.startingpoint.game.mission.PandaMission;
import fr.unice.polytech.startingpoint.game.mission.PeasantMission;
import fr.unice.polytech.startingpoint.type.ColorType;
import fr.unice.polytech.startingpoint.type.ImprovementType;
import fr.unice.polytech.startingpoint.type.MissionType;
import fr.unice.polytech.startingpoint.type.WeatherType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BotTest {
    Game game;
    Bot bot;

    @BeforeEach
    public void setUp(){
        game = new Game();
        bot = new RandomBot(game.getGameInteraction());
        game.getPlayerData().getInventory().subMissions(game.getPlayerData().getMissions());
    }

    /**
     * <h1><u>playMission</u></h1>
     */

    @Test
    void chooseFirstMissionTypeDrawableNotEmpty(){
        assertEquals(MissionType.PANDA,bot.chooseMissionTypeDrawable(MissionType.PANDA,MissionType.PARCEL,MissionType.PEASANT));
    }

    @Test
    void chooseSecondtMissionTypeDrawableFirstEmpty(){
        for (int i = 0; i < 14; i++) {
            game.getResource().drawPandaMission();
        }
        assertEquals(MissionType.PARCEL,bot.chooseMissionTypeDrawable(MissionType.PANDA,MissionType.PARCEL,MissionType.PEASANT));
    }

    @Test
    void chooseThirdMissionTypeDrawableFirstAndSecondEmpty(){
        for (int i = 0; i < 14; i++) {
            game.getResource().drawPandaMission();
            game.getResource().drawParcelMission();
        }
        assertEquals(MissionType.PEASANT,bot.chooseMissionTypeDrawable(MissionType.PANDA,MissionType.PARCEL,MissionType.PEASANT));
    }

    /**
     * <h1><u>isJudiciousDrawMission</u></h1>
     */

    @Test
    void isJudiciousDrawMissionAlready5Missions(){
        for (int i = 0; i < 5; i++) {
            game.getPlayerData().addMission(new PandaMission(ColorType.GREEN,0));
        }
        assertFalse(bot.isJudiciousDrawMission());
    }

    @Test
    void isJudiciousDrawMissionAlreadyMissionEmpty(){
        for (int i = 0; i < 14; i++) {
            game.getResource().drawPandaMission();
            game.getResource().drawParcelMission();
            game.getResource().drawPeasantMission();
        }
        assertFalse(bot.isJudiciousDrawMission());
    }

    @Test
    void isJudiciousDrawMissionAlreadyAlreadyDrawMission(){
        bot.drawMission(MissionType.PANDA);
        assertFalse(bot.isJudiciousDrawMission());
    }

    @Test
    void isJudiciousDrawTrue(){
        assertTrue(bot.isJudiciousDrawMission());
    }

    /**
     * <h1><u>Météo</u></h1>
     */


    @Test
    void simpleRainStratPreferFertilizer(){
        Coordinate coordinate = new Coordinate(-1,1,0);
        game.getBoard().placeParcel(new Parcel(),new Coordinate(1,-1,0));
        game.getBoard().placeParcel(new Parcel(ImprovementType.FERTILIZER),new Coordinate(-1,1,0));
        bot.stratRain();

        assertEquals(4,game.getBoard().getPlacedParcels().get(coordinate).getNbBamboo());
    }

    @Test
    void simpleRainStratWith1Parcel(){
        Coordinate coordinate = new Coordinate(1,-1,0);
        game.getBoard().placeParcel(new Parcel(),coordinate);
        bot.stratRain();
        assertEquals(2,game.getBoard().getPlacedParcels().get(coordinate).getNbBamboo());
    }

    @Test
    void simlpeThunderstormStrat(){
        Parcel parcel=new Parcel();
        Coordinate coordinate = new Coordinate(1,-1,0);
        parcel.addBamboo();
        game.getBoard().placeParcel(new Parcel(),new Coordinate(-1,1,0));
        game.getBoard().placeParcel(parcel,coordinate);
        bot.stratThunderstorm();
        assertEquals(1,game.getBoard().getPlacedParcels().get(coordinate).getNbBamboo());
    }

    @Test
    void stratCloudNoMoreWathershedSoFertizilerChosen(){
        for(int i=0;i<3;i++) {
            game.getGameInteraction().drawImprovement(ImprovementType.WATERSHED);
        }
        bot.stratCloud();
        assertFalse(game.getPlayerData().getInventory().getInventoryImprovement(ImprovementType.FERTILIZER).isEmpty());
        assertTrue(game.getPlayerData().getInventory().getInventoryImprovement(ImprovementType.ENCLOSURE).isEmpty());
    }

    @Test
    void stratCloudNoMoreImprovementSoWeatherChanged(){
        for(int i=0;i<3;i++) {
            game.getGameInteraction().drawImprovement(ImprovementType.WATERSHED);
            game.getGameInteraction().drawImprovement(ImprovementType.FERTILIZER);
            game.getGameInteraction().drawImprovement(ImprovementType.ENCLOSURE);
        }
        bot.stratCloud();
        assertEquals(WeatherType.SUN,game.getPlayerData().getTemporaryInventory().getWeatherType());
    }


    @Test
    void stratQuestionMark(){
        assertEquals(WeatherType.NO_WEATHER,game.getPlayerData().getTemporaryInventory().getWeatherType());
        bot.stratQuestionMark();
        assertEquals(WeatherType.SUN,game.getPlayerData().getTemporaryInventory().getWeatherType());
    }


    /** <h2><b>Test  determineBestMissionToDo </b></h2>
     */

    @Test
    void determineBestMissionToDoMissionMostFastMission(){
        Parcel parcelGreen = new Parcel(ColorType.GREEN, ImprovementType.NOTHING);
        Coordinate coordinate1 = new Coordinate(1, -1, 0);
        Mission peasantMissionGreen = new PeasantMission(ColorType.GREEN, ImprovementType.NOTHING, 2);

        game.getBoard().placeParcel(parcelGreen,coordinate1);
        parcelGreen.addBamboo();
        parcelGreen.addBamboo(); // mission jardinier faisable en u coup

        game.getPlayerData().getInventory().addMission(peasantMissionGreen);
        assertEquals(peasantMissionGreen,bot.determineBestMissionToDo());
    }

    @Test
    void determineBestMissionToDoMissionMostFastMissionTwoMissions(){
        Parcel parcelGreen = new Parcel(ColorType.GREEN, ImprovementType.NOTHING);
        Coordinate coordinate1 = new Coordinate(1, -1, 0);
        Mission peasantMissionGreen = new PeasantMission(ColorType.GREEN, ImprovementType.NOTHING, 2);

        game.getBoard().placeParcel(parcelGreen,coordinate1);
        parcelGreen.addBamboo();
        parcelGreen.addBamboo(); // mission jardinier faisable en un coup

        Parcel parcelGreen2 = new Parcel(ColorType.GREEN, ImprovementType.ENCLOSURE);
        Coordinate coordinate2 = new Coordinate(1, 0, -1);
        Mission peasantMissionGreen2 = new PeasantMission(ColorType.GREEN, ImprovementType.ENCLOSURE, 2);

        game.getBoard().placeParcel(parcelGreen2,coordinate2);
        parcelGreen2.addBamboo(); // mission jardinier faisable en plus d'un coup

        game.getPlayerData().getInventory().addMission(peasantMissionGreen);
        game.getPlayerData().getInventory().addMission(peasantMissionGreen2);
        assertEquals(peasantMissionGreen,bot.determineBestMissionToDo());
    }

    @Test
    void determineBestMissionToDoOneisImpossible(){
        Parcel parcelGreen = new Parcel(ColorType.GREEN, ImprovementType.NOTHING);
        Coordinate coordinate1 = new Coordinate(1, -1, 0);
        Mission peasantMissionGreen = new PeasantMission(ColorType.GREEN, ImprovementType.NOTHING, 2);

        game.getBoard().placeParcel(parcelGreen,coordinate1);
        parcelGreen.addBamboo();
        parcelGreen.addBamboo(); // mission jardinier faisable en un coup

        Mission peasantMissionGreen2 = new PeasantMission(ColorType.GREEN, ImprovementType.WHATEVER, 4);

        game.getPlayerData().getInventory().addMission(peasantMissionGreen);
        game.getPlayerData().getInventory().addMission(peasantMissionGreen2);

        assertEquals(peasantMissionGreen,bot.determineBestMissionToDo());
    }
}