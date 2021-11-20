package fr.unice.polytech.startingpoint.bot;

import fr.unice.polytech.startingpoint.game.Game;
import fr.unice.polytech.startingpoint.game.GameInteraction;
import fr.unice.polytech.startingpoint.type.BotType;
import fr.unice.polytech.startingpoint.type.MissionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParcelBotTest {


    /**
     * <h2>Strategy drawing mission</h2>
     */

    @Nested
    class strategyDrawingMission {

        Game game;
        GameInteraction gameInteractionMock;
        ParcelBot parcelBot;

        @BeforeEach
        void setUp() {
            game = new Game(false, new BotType[]{BotType.PARCEL_BOT});
            gameInteractionMock = Mockito.mock(GameInteraction.class);
            parcelBot = new ParcelBot(gameInteractionMock);
            parcelBot = (ParcelBot) game.getPlayerData().getBot();
        }

        @Test
        void botChooseTodrawMissionParcel() {
            assertEquals(MissionType.PARCEL, parcelBot.bestMissionTypeToDraw());
        }

        @Test
        void noMoreMissionsParcels() {
            for (int i = 0; i < 14; i++) {
                game.getResource().drawParcelMission();
            }
            assertEquals(MissionType.PANDA, parcelBot.bestMissionTypeToDraw());

        }
        @Test
        void noMoreMissionsParcelsPanda() {
            for (int i = 0; i < 14; i++) {
                game.getResource().drawParcelMission();
                game.getResource().drawPandaMission();
            }
            assertEquals(MissionType.PEASANT, parcelBot.bestMissionTypeToDraw());

        }


    }

}
