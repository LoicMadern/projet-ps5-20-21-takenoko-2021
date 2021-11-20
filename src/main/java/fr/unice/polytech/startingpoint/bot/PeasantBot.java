package fr.unice.polytech.startingpoint.bot;

import fr.unice.polytech.startingpoint.game.GameInteraction;
import fr.unice.polytech.startingpoint.game.board.Coordinate;
import fr.unice.polytech.startingpoint.game.mission.PeasantMission;
import fr.unice.polytech.startingpoint.type.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <h1>{@link PeasantBot} :</h1>
 *
 * <p>This class provides a bot specialized in {@link PeasantMission} missions.</p>
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

public class PeasantBot extends Bot {

    public PeasantBot(GameInteraction gameInteraction) {
        super(gameInteraction);
        botType = BotType.PEASANT_BOT;
    }

    @Override
    public MissionType bestMissionTypeToDraw() {
        int NB_MAX_MISSION_PARCEL = 2;
        if (gameInteraction.getResourceSize(ResourceType.PARCEL_MISSION) > 0
                && (gameInteraction.getInventoryParcelMissions().size() + gameInteraction.getMissionsParcelDone() < NB_MAX_MISSION_PARCEL))
            return MissionType.PARCEL;
        else if (gameInteraction.getResourceSize(ResourceType.PEASANT_MISSION) > 0)
            return MissionType.PEASANT;
        else
            return chooseMissionTypeDrawable(MissionType.PARCEL,MissionType.PANDA,MissionType.PEASANT);
    }

    @Override
    public void stratThunderstorm() {

    }

    @Override
    public void stratRain() {

        for(PeasantMission peasantMission:gameInteraction.getInventoryPeasantMissions()) {
            ColorType peasantMissionColor = peasantMission.getColorType();

            List<Coordinate> parcelsIrrigatedSameColorAsMission = gameInteraction.
                    getPlacedCoordinatesByColor(peasantMissionColor).stream()
                    .filter(gameInteraction::isPlacedAndIrrigatedParcel)
                    .collect(Collectors.toList());
            if (!parcelsIrrigatedSameColorAsMission.isEmpty()) {
                gameInteraction.rainAction(parcelsIrrigatedSameColorAsMission.get(0));
                break;
            }
        }
    }








}