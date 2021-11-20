package fr.unice.polytech.startingpoint.bot;

import fr.unice.polytech.startingpoint.game.Game;
import fr.unice.polytech.startingpoint.game.GameInteraction;
import fr.unice.polytech.startingpoint.game.board.Coordinate;
import fr.unice.polytech.startingpoint.game.board.Parcel;
import fr.unice.polytech.startingpoint.game.mission.PeasantMission;
import fr.unice.polytech.startingpoint.type.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PeasantBotTest {



        /**
         * <h2>Strategy drawing mission</h2>
         */

        @Nested
        class strategyDrawingMission{

            Game gamePeasant;
            GameInteraction gameInteractionMock;
            PeasantBot peasantBot;
            PeasantBot peasantBotMock;

            @BeforeEach
            void setUp(){
                gamePeasant=new Game(false,new BotType[]{BotType.PEASANT_BOT});
                gameInteractionMock= Mockito.mock(GameInteraction.class);
                peasantBotMock= new PeasantBot(gameInteractionMock);
                peasantBot= (PeasantBot) gamePeasant.getPlayerData().getBot();
            }

            @Test
            void botChooseTodrawMissionParcel(){
                assertEquals(MissionType.PARCEL,peasantBot.bestMissionTypeToDraw());
            }
            @Test
            void maxMissionParcel(){
                Mockito.when(gameInteractionMock.getInventoryParcelMissions()).thenReturn(new ArrayList<>());
                Mockito.when(gameInteractionMock.getMissionsParcelDone()).thenReturn(3);
                assertEquals(MissionType.PEASANT,peasantBotMock.bestMissionTypeToDraw());
            }

            @Test
            void noMoreMissionsParcels(){
                Mockito.when(gameInteractionMock.getResourceSize(ResourceType.PARCEL)).thenReturn(0);
                assertEquals(MissionType.PEASANT,peasantBotMock.bestMissionTypeToDraw());
            }

            @Test
            void noMoreMissionsParcelsPeasant() {
                for (int i = 0; i < 14; i++) {
                    gamePeasant.getResource().drawParcelMission();
                    gamePeasant.getResource().drawPeasantMission();
                }
                assertEquals(MissionType.PANDA, peasantBot.bestMissionTypeToDraw());

            }



        }



    /**
     * <h2>Weather strat</h2>
     */

    @Nested
    class rainTest{

        Game gamePeasant;
        Parcel greenParcel;
        Parcel redParcel;
        PeasantBot peasantBot;

        @BeforeEach
        void setUp(){
            greenParcel=new Parcel(ColorType.GREEN);
            redParcel =new Parcel(ColorType.RED);
            gamePeasant=new Game(false,new BotType[]{BotType.PEASANT_BOT});
            peasantBot= (PeasantBot) gamePeasant.getPlayerData().getBot();
        }


        @Test
        void stratRainWithNoMission(){
            gamePeasant.getPlayerData().getInventory().subMissions(gamePeasant.getPlayerData().getPeasantMissions());
            peasantBot.stratRain();
            gamePeasant.getBoard().placeParcel(greenParcel,new Coordinate(1,-1,0));
            gamePeasant.getBoard().placeParcel(redParcel,new Coordinate(0,1,-1));
            assertEquals(new Coordinate(0,0,0),gamePeasant.getBoard().getPeasantCoordinate());
        }

        @Test
        void stratRainPeasant(){
            gamePeasant.getPlayerData().getInventory().subMissions(gamePeasant.getPlayerData().getPeasantMissions());
            gamePeasant.getBoard().placeParcel(greenParcel,new Coordinate(1,-1,0));
            gamePeasant.getBoard().placeParcel(redParcel,new Coordinate(0,1,-1));
            gamePeasant.getPlayerData().addMission(new PeasantMission(ColorType.GREEN,ImprovementType.NOTHING,2));
            peasantBot.stratRain();
            assertEquals(2,gamePeasant.getBoard().getPlacedParcels().get(new Coordinate(1,-1,0)).getNbBamboo());
        }

        @Test
        void stratRainWhenNoMission(){
            gamePeasant.getPlayerData().getInventory().subMissions(gamePeasant.getPlayerData().getPeasantMissions());
            gamePeasant.getBoard().placeParcel(greenParcel,new Coordinate(1,-1,0));
            peasantBot.stratRain();
            assertEquals(1,gamePeasant.getBoard().getPlacedParcels().get(new Coordinate(1,-1,0)).getNbBamboo());
        }

        @Test
        void stratThunderstormDoNothing(){
            gamePeasant.getBoard().placeParcel(redParcel,new Coordinate(0,1,-1));
            peasantBot.stratThunderstorm();
            assertEquals(new Coordinate(0,0,0),gamePeasant.getBoard().getPandaCoordinate());
        }


    }


}

