package fr.unice.polytech.startingpoint.bot;

import fr.unice.polytech.startingpoint.game.GameInteraction;
import fr.unice.polytech.startingpoint.game.board.Coordinate;
import fr.unice.polytech.startingpoint.game.mission.PandaMission;
import fr.unice.polytech.startingpoint.type.*;

/**
 * <h1>{@link PandaBot} :</h1>
 *
 * <p>This class provides a bot specialized in {@link PandaMission} missions.</p>
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

public class PandaBot extends Bot {

    public PandaBot(GameInteraction gameInteraction) {
        super(gameInteraction);
        botType = BotType.PANDA_BOT;
    }

    @Override
    public MissionType bestMissionTypeToDraw() {
        int NB_MAX_MISSION_PARCEL = 2;

        if (    gameInteraction.getResourceSize(ResourceType.PARCEL_MISSION) > 0 &&
                (gameInteraction.getInventoryParcelMissions().size() + gameInteraction.getMissionsParcelDone() < NB_MAX_MISSION_PARCEL) )
            return MissionType.PARCEL;
        else if ( gameInteraction.getResourceSize(ResourceType.PANDA_MISSION) > 0 )
            return MissionType.PANDA;
        else
            return chooseMissionTypeDrawable(MissionType.PARCEL,MissionType.PEASANT,MissionType.PANDA);
    }

    @Override
    public void stratThunderstorm() {
        for (PandaMission pandaMission : gameInteraction.getInventoryPandaMissions()) {
            Coordinate coordinateAllColor = missionPandaStrat.strategyMissionAllColor();
            Coordinate coordinateOneColor = missionPandaStrat.strategyMissionOneColor(pandaMission.getColorType());

            if (coordinateAllColor != null && pandaMission.getColorType().equals(ColorType.ALL_COLOR)){
                gameInteraction.thunderstormAction(coordinateAllColor);
                return;
            }
            else if (coordinateOneColor != null){
                gameInteraction.thunderstormAction(coordinateOneColor);
                return;
            }
        }
    }
}