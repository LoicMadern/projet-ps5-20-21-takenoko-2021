package fr.unice.polytech.startingpoint.bot;

import fr.unice.polytech.startingpoint.game.GameInteraction;
import fr.unice.polytech.startingpoint.type.BotType;
import fr.unice.polytech.startingpoint.type.MissionType;
import fr.unice.polytech.startingpoint.type.ResourceType;
import fr.unice.polytech.startingpoint.type.WeatherType;

/**
 * <h1>{@link IntelligentBot} :</h1>
 *
 * <p>This class provides an intelligent bot that adapt.</p>
 *
 * <p>The programmer needs only to provide implementations for the {@link Bot#botPlay(WeatherType)} and {@link Bot#bestMissionTypeToDraw()} methods from the {@link Bot}.</p>
 *
 * @author Manuel Enzo
 * @author Naud Eric
 * @author Madern Loic
 * @author Le Calloch Antoine
 * @see Bot
 * @see PandaBot
 * @see ParcelBot
 * @see PeasantBot
 * @see RandomBot
 * @version 0.5
 */

public class IntelligentBot extends Bot {
    
    public IntelligentBot(GameInteraction gameInteraction) {
        super(gameInteraction);
        botType = BotType.INTELLIGENT_BOT;
    }

    @Override
    public MissionType bestMissionTypeToDraw() {
        int NB_MAX_MISSION_PARCEL = 2;
        int NB_MISSION_DONE = 7 - gameInteraction.getNumberPlayers();
        if (    gameInteraction.getResourceSize(ResourceType.PARCEL_MISSION) > 0 &&
                (gameInteraction.getInventoryParcelMissions().size() + gameInteraction.getMissionsParcelDone()) < NB_MAX_MISSION_PARCEL)
            return MissionType.PARCEL;
        else if ( gameInteraction.getResourceSize(ResourceType.PANDA_MISSION) > 0 &&
                  gameInteraction.getNumberMissionsDone() < NB_MISSION_DONE)
            return MissionType.PANDA;
        else if ( gameInteraction.getResourceSize(ResourceType.PEASANT_MISSION) > 0 )
            return MissionType.PEASANT;
        else
            return chooseMissionTypeDrawable(MissionType.PARCEL,MissionType.PEASANT,MissionType.PANDA);
    }
}