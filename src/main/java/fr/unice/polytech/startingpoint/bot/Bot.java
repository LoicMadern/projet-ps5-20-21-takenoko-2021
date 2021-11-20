package fr.unice.polytech.startingpoint.bot;

import fr.unice.polytech.startingpoint.bot.strategie.MissionPandaStrat;
import fr.unice.polytech.startingpoint.bot.strategie.MissionParcelStrat;
import fr.unice.polytech.startingpoint.bot.strategie.MissionPeasantStrat;
import fr.unice.polytech.startingpoint.game.GameInteraction;
import fr.unice.polytech.startingpoint.game.board.Coordinate;
import fr.unice.polytech.startingpoint.game.mission.Mission;
import fr.unice.polytech.startingpoint.type.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <h1>{@link Bot} :</h1>
 *
 * <p>This class provides a skeletal implementation of the {@link PandaBot},
 * {@link ParcelBot}, {@link PeasantBot} and {@link RandomBot} classes.</p>
 *
 * <p>The programmer needs only to extend this class and provide
 * implementations for the {@link #botPlay(WeatherType)} method.</p>
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

public abstract class Bot {
    BotType botType;
    final MissionPandaStrat missionPandaStrat;
    final MissionParcelStrat missionParcelStrat;
    private final MissionPeasantStrat missionPeasantStrat;

    final GameInteraction gameInteraction;

    Bot(GameInteraction gameInteraction) {
        this.gameInteraction = gameInteraction;
        this.missionPandaStrat = new MissionPandaStrat(gameInteraction);
        this.missionParcelStrat = new MissionParcelStrat(gameInteraction);
        this.missionPeasantStrat = new MissionPeasantStrat(gameInteraction);
    }

    public void botPlay(WeatherType weatherType) {
        playWeather(weatherType);

        for (int i = gameInteraction.getStamina(); i > 0; i--)
            if(isJudiciousDrawMission())
                drawMission(bestMissionTypeToDraw());
            else
                playMission(determineBestMissionToDo());
    }

    void playWeather(WeatherType weatherType){
        switch (weatherType){
            case RAIN:
                stratRain();
                break;
            case THUNDERSTORM:
                stratThunderstorm();
                break;
            case QUESTION_MARK:
                stratQuestionMark();
                break;
            case CLOUD:
                stratCloud();
                break;
            case SUN:
                gameInteraction.useSun();
                break;
            case WIND:
                gameInteraction.useWind();
                break;
            default:
                break;
        }
    }

    void stratThunderstorm(){
        List<Coordinate> irrigatedParcelsWithMoreThan1Bamboo = gameInteraction.getAllParcelsIrrigated()
                .stream()
                .filter( coordinate -> gameInteraction.getPlacedParcelsNbBamboo(coordinate) > 0 &&
                                       !gameInteraction.getPlacedParcelInformation(coordinate).getImprovementType().equals(ImprovementType.ENCLOSURE) )
                .collect(Collectors.toList());
        if(!irrigatedParcelsWithMoreThan1Bamboo.isEmpty())
            gameInteraction.thunderstormAction(irrigatedParcelsWithMoreThan1Bamboo.get(0));
    }

    void stratRain(){
        List<Coordinate> parcelsIrrigated= gameInteraction.getAllParcelsIrrigated();
        List<Coordinate> parcelsIrrigatedWithFertilizer=parcelsIrrigated.stream().
                filter(coordinate -> gameInteraction.getPlacedParcelInformation(coordinate).getImprovementType()
                        .equals(ImprovementType.FERTILIZER)).collect(Collectors.toList());

        if( !parcelsIrrigatedWithFertilizer.isEmpty() )
            gameInteraction.rainAction(parcelsIrrigatedWithFertilizer.get(0));
        else if( !parcelsIrrigated.isEmpty() )
            gameInteraction.rainAction(parcelsIrrigated.get(0));
    }

    void stratQuestionMark(){
        gameInteraction.questionMarkAction(WeatherType.SUN);
    }

    void stratCloud(){
        if( gameInteraction.getResourceSize(ResourceType.WATERSHED_IMPROVEMENT) > 0 )
            gameInteraction.cloudAction(ImprovementType.WATERSHED,WeatherType.SUN);
        else if( gameInteraction.getResourceSize(ResourceType.FERTILIZER_IMPROVEMENT) > 0 )
            gameInteraction.cloudAction(ImprovementType.FERTILIZER,WeatherType.SUN);
        else
            gameInteraction.cloudAction(ImprovementType.ENCLOSURE,WeatherType.SUN);

        ImprovementType improvementType = (gameInteraction.getInventoryImprovementTypes().isEmpty()) ? null : gameInteraction.getInventoryImprovementTypes().get(0);
        List<Coordinate> coordinateList = gameInteraction.getPlacedCoordinates().stream()
                .filter(coordinate -> gameInteraction.getPlacedParcelInformation(coordinate).getImprovementType().equals(ImprovementType.NOTHING))
                .collect(Collectors.toList());

        if (improvementType != null && !coordinateList.isEmpty())
            gameInteraction.placeImprovement(improvementType,coordinateList.get(0));
    }

    /**<b><u>MISSION HANDLING
     */

    MissionType chooseMissionTypeDrawable(MissionType missionType1, MissionType missionType2, MissionType missionType3) {
        if ( gameInteraction.getResourceSize(ResourceType.get(missionType1)) > 0 )
            return missionType1;
        else if ( gameInteraction.getResourceSize(ResourceType.get(missionType2)) > 0 )
            return missionType2;
        else
            return missionType3;
    }

    boolean isJudiciousDrawMission() {
        int NB_MAX_MISSION = 5;
        return gameInteraction.getMissionsSize() < NB_MAX_MISSION &&
                gameInteraction.getResourceSize(ResourceType.ALL_MISSION) > 0 &&
                !gameInteraction.contains(ActionType.DRAW_MISSION);
    }

    /**<p>Draw a mission with the type required in the resources.</p>
     *
     * @param missionType
     *            <b>The type of the mission the bot want to draw.</b>
     */
    void drawMission(MissionType missionType){
        gameInteraction.drawMission(missionType);
    }

    protected abstract MissionType bestMissionTypeToDraw();

    Mission determineBestMissionToDo() {
        Mission bestMission = null;
        int minNbMove = -1;
        int nbMove = 0;

        for (Mission mission : gameInteraction.getInventoryMissions()){
            switch (mission.getMissionType()){
                case PANDA:
                    nbMove = missionPandaStrat.howManyMoveToDoMission(mission);
                    break;
                case PARCEL:
                    nbMove = missionParcelStrat.howManyMoveToDoMission(mission);
                    break;
                case PEASANT:
                    nbMove = missionPeasantStrat.howManyMoveToDoMission(mission);
                    break;
            }
            if( (nbMove < minNbMove && nbMove > 0) ||
                    minNbMove == -1){
                minNbMove = nbMove;
                bestMission = mission;
            }
        }
        if ( bestMission != null)
            return bestMission;
        else
            return gameInteraction.getInventoryMissions().get(0);
    }

    private void playMission(Mission mission){
        switch (mission.getMissionType()){
            case PANDA:
                missionPandaStrat.stratOneTurn(mission);
                return;
            case PARCEL:
                missionParcelStrat.stratOneTurn(mission);
                return;
            case PEASANT:
                missionPeasantStrat.stratOneTurn(mission);
        }
    }

    public BotType getBotType(){
        return botType;
    }
}