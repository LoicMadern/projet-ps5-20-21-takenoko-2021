package fr.unice.polytech.startingpoint.game;

import fr.unice.polytech.startingpoint.exception.TooManyPlayersInGameException;
import fr.unice.polytech.startingpoint.type.BotType;
import fr.unice.polytech.startingpoint.type.WeatherType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private Game game = new Game();

    @Test
    void aPlayerFinished(){
        Game game=new Game();
        for (int i = 0; i < 10; i++)
            game.getPlayerData().addMissionDone();
        assertTrue(game.isSomebodyFinished());
    }

    @Test
    void firstPlayerReceiveEmperor(){
        Game game=new Game();
        for (int i = 0; i < 10; i++)
            game.getPlayerData().addMissionDone();
        game.isSomebodyFinished();
        assertEquals(2,game.getPlayerData().getScore()[0]);
    }

    @Test
    void secondPlayerDidntReceiveEmperor(){
        BotType[] botList = new BotType[]{BotType.PARCEL_BOT,BotType.PANDA_BOT};
        Game game=new Game(false, botList);
        for (int i = 0; i < 9; i++) {
            game.getPlayerData().addMissionDone();
            game.nextBot();
        }
        game.isSomebodyFinished();
        game.nextBot();
        game.isSomebodyFinished();
        assertEquals(0,game.getPlayerData().getScore()[0]);
    }

    @Test
    void gameIsFinish(){
        BotType[] botList = new BotType[]{BotType.PARCEL_BOT,BotType.PANDA_BOT,BotType.PARCEL_BOT,BotType.PANDA_BOT};
        Game game=new Game(false, botList);
        for (int i = 0; i < 28; i++) {
            game.getPlayerData().addMissionDone();
            game.isContinue();
            game.nextBot();
        }
        assertFalse(game.isContinue());
    }

    @Test
    void gameIsnTFinishBecauseLastTurnIsnTFinish(){
        BotType[] botList = new BotType[]{BotType.PARCEL_BOT,BotType.PANDA_BOT,BotType.PARCEL_BOT,BotType.PANDA_BOT};
        Game game=new Game(false, botList);
        for (int i = 0; i < 25; i++) {
            game.getPlayerData().addMissionDone();
            game.isContinue();
            game.nextBot();
        }
        assertTrue(game.isContinue());
    }


    @Test
    void threeMissionsInInventory(){
        Game game=new Game();
        assertEquals(game.getPlayerData().getPandaMissions().size(),1);
        assertEquals(game.getPlayerData().getPeasantMissions().size(),1);
        assertEquals(game.getPlayerData().getParcelMissions().size(),1);

    }

    @Test
    void firstRoundSoNoWeather(){
        game.botPlay();
        assertNotEquals(3,game.getPlayerData().getStamina());//No weather so stamina=2
        assertFalse(game.getPlayerData().isActionCouldBeDoneTwice());//No weather so no same double action
    }

    @Test
    void atLeastTwoRoundsSoWeatherMustBePresent(){
        game.newRound();
        game.newRound();//2 round so No Weather disappear
        game.botPlay();
        assertNotEquals(WeatherType.NO_WEATHER,game.getPlayerData().getWeatherType());
        //2e le dé météo peut être roll
    }

    @Test
    void gameMissionAndScoreTest(){
        assertEquals(0,game.getScores().get(0)[0]);
        assertEquals(0, game.getMissionsDone().get(0));

        game.getPlayerData().addMissionDone(2);

        assertEquals(2,game.getScores().get(0)[0]);
        assertEquals(1,game.getMissionsDone().get(0));
    }

    @Test
    void tooManyPlayersInGame(){
        BotType[] botList = new BotType[]{BotType.RANDOM,BotType.RANDOM,BotType.RANDOM,BotType.RANDOM,BotType.RANDOM};
        Game game=new Game(false, botList);
        assertThrows(TooManyPlayersInGameException.class, game::play);
    }

    @Test
    void fourPlayersInGame(){
        BotType[] botList = new BotType[]{BotType.RANDOM,BotType.RANDOM,BotType.RANDOM,BotType.RANDOM};
        Game game=new Game(false, botList);
        game.play();
    }

    @Test
    void OnePlayersInGame(){
        BotType[] botList = new BotType[]{BotType.RANDOM};
        Game game=new Game(false, botList);
        game.play();
    }


}