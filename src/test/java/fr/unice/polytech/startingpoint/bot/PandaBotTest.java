package fr.unice.polytech.startingpoint.bot;

import fr.unice.polytech.startingpoint.game.Game;
import fr.unice.polytech.startingpoint.game.GameInteraction;
import fr.unice.polytech.startingpoint.game.board.Coordinate;
import fr.unice.polytech.startingpoint.game.board.Parcel;
import fr.unice.polytech.startingpoint.game.mission.PandaMission;
import fr.unice.polytech.startingpoint.type.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PandaBotTest {



/**
 * <h2>Thunderstorm Test</h2>
 */

    @Nested
    class thunderStormTest{

        Game gamePanda;
        Parcel greenParcel;
        Parcel greenParcelEnclosure;
        Parcel redParcel;
        PandaBot pandaBot;

        @BeforeEach
        void setUp(){
            greenParcel=new Parcel(ColorType.GREEN);
            greenParcelEnclosure=new Parcel(ColorType.GREEN, ImprovementType.ENCLOSURE);
            redParcel =new Parcel(ColorType.RED);
            gamePanda=new Game(false,new BotType[]{BotType.PANDA_BOT});
            pandaBot= (PandaBot) gamePanda.getPlayerData().getBot();
            gamePanda.getPlayerData().getInventory().subMissions(gamePanda.getPlayerData().getPandaMissions());
        }


        @Test
        void stratThunderstormWithNoMission(){
            gamePanda.getBoard().placeParcel(greenParcel,new Coordinate(1,-1,0));
            gamePanda.getBoard().placeParcel(greenParcelEnclosure,new Coordinate(0,1,-1));
            pandaBot.stratThunderstorm();
            assertEquals(new Coordinate(0,0,0),gamePanda.getBoard().getPandaCoordinate());
        }

        @Test
        void stratThunderstormPandaOneColor(){
            gamePanda.getBoard().placeParcel(greenParcel,new Coordinate(1,-1,0));
            gamePanda.getBoard().placeParcel(greenParcelEnclosure,new Coordinate(0,1,-1));
            gamePanda.getBoard().placeParcel(redParcel,new Coordinate(0,-1,1));
            gamePanda.getPlayerData().addMission(new PandaMission(ColorType.GREEN,2));
            pandaBot.stratThunderstorm();
            assertEquals(new Coordinate(1,-1,0),gamePanda.getBoard().getPandaCoordinate());
        }


        @Test
        void stratThunderstormPandaAllColor(){
            gamePanda.getBoard().placeParcel(greenParcel,new Coordinate(1,-1,0));
            gamePanda.getBoard().placeParcel(greenParcelEnclosure,new Coordinate(1,0,-1));
            gamePanda.getPlayerData().addMission(new PandaMission(ColorType.GREEN,2));
            pandaBot.stratThunderstorm();
            assertEquals(new Coordinate(1,-1,0),gamePanda.getBoard().getPandaCoordinate());
        }

    }

    /**
     * <h2>Strategy drawing mission</h2>
     */

    @Nested
    class strategyDrawingMission{

        Game gamePanda;
        GameInteraction gameInteractionMock;
        PandaBot pandaBot;
        PandaBot pandaBotMock;

        @BeforeEach
        void setUp(){
            gamePanda=new Game(false,new BotType[]{BotType.PANDA_BOT});
            gameInteractionMock= Mockito.mock(GameInteraction.class);
            pandaBotMock= new PandaBot(gameInteractionMock);
            pandaBot= (PandaBot) gamePanda.getPlayerData().getBot();
        }

        @Test
        void botChooseTodrawMissionParcel(){
            assertEquals(MissionType.PARCEL,pandaBot.bestMissionTypeToDraw());
        }
        @Test
        void maxMissionParcel(){
            Mockito.when(gameInteractionMock.getMissionsParcelDone()).thenReturn(3);
            assertEquals(MissionType.PANDA,pandaBotMock.bestMissionTypeToDraw());
        }

        @Test
        void noMoreMissionsParcels(){
            Mockito.when(gameInteractionMock.getResourceSize(ResourceType.PARCEL)).thenReturn(0);
            assertEquals(MissionType.PANDA,pandaBotMock.bestMissionTypeToDraw());
        }

        @Test
        void noMoreMissionsParcelsPanda() {
            for (int i = 0; i < 14; i++) {
                gamePanda.getResource().drawParcelMission();
                gamePanda.getResource().drawPandaMission();
            }
            assertEquals(MissionType.PEASANT, pandaBot.bestMissionTypeToDraw());
        }


    }







}







