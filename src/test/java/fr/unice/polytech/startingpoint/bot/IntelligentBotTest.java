package fr.unice.polytech.startingpoint.bot;

import fr.unice.polytech.startingpoint.game.Game;
import fr.unice.polytech.startingpoint.game.mission.ParcelMission;
import fr.unice.polytech.startingpoint.type.ColorType;
import fr.unice.polytech.startingpoint.type.FormType;
import fr.unice.polytech.startingpoint.type.MissionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IntelligentBotTest {
    Game game;
    Bot bot;

    @BeforeEach
    public void setUp(){
        game = new Game();
        bot = new IntelligentBot(game.getGameInteraction());
    }

    /**
     * <h1><u>bestMissionTypeToDraw</u></h1>
     */

    @Test
    void chooseParcelMission(){
        assertEquals(MissionType.PARCEL,bot.bestMissionTypeToDraw());
    }

    @Test
    void choosePandaMissionBecauseParcelMissionEmpty(){
        for (int i = 0; i < 14; i++)
            game.getResource().drawParcelMission();
        assertEquals(MissionType.PANDA,bot.bestMissionTypeToDraw());
    }

    @Test
    void choosePandaMissionBecauseAlreadyHaveTwoMissionParcel(){
        game.getPlayerData().addMission(new ParcelMission(ColorType.GREEN, FormType.LINE,0));
        game.getPlayerData().addMission(new ParcelMission(ColorType.GREEN, FormType.LINE,0));
        assertEquals(MissionType.PANDA,bot.bestMissionTypeToDraw());
    }

    @Test
    void choosePandaMissionBecauseAlreadyCompleteTwoMissionParcel(){
        game.getPlayerData().addParcelMission();
        game.getPlayerData().addParcelMission();
        assertEquals(MissionType.PANDA,bot.bestMissionTypeToDraw());
    }

    @Test
    void choosePeasantMissionBecausePandaMissionIsEmpty(){
        for (int i = 0; i < 14; i++) {
            game.getResource().drawParcelMission();
            game.getResource().drawPandaMission();
        }
        assertEquals(MissionType.PEASANT,bot.bestMissionTypeToDraw());
    }

    @Test
    void choosePeasantMissionBecauseComplete8Missions(){ //game a un joueur
        for (int i = 0; i < 14; i++)
            game.getResource().drawParcelMission();
        for (int i = 0; i < 8; i++) {
            game.getPlayerData().addMissionDone();
        }
        assertEquals(MissionType.PEASANT,bot.bestMissionTypeToDraw());
    }

    @Test
    void chooseParcelMissionBecausePandaAndPeasantAreEmpty(){
        game.getPlayerData().addParcelMission();
        game.getPlayerData().addParcelMission();
        for (int i = 0; i < 14; i++) {
            game.getResource().drawPeasantMission();
            game.getResource().drawPandaMission();
        }
        assertEquals(MissionType.PARCEL,bot.bestMissionTypeToDraw());
    }


}