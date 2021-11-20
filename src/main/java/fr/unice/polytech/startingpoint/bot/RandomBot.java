package fr.unice.polytech.startingpoint.bot;

import fr.unice.polytech.startingpoint.game.GameInteraction;
import fr.unice.polytech.startingpoint.game.board.Coordinate;
import fr.unice.polytech.startingpoint.game.board.ParcelInformation;
import fr.unice.polytech.startingpoint.type.*;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * <h1>{@link RandomBot} :</h1>
 *
 * <p>This class provides a bot playing randomly.</p>
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

public class RandomBot extends Bot {
    private Random random;
    private Random random2;

    public RandomBot(GameInteraction gameInteraction) {
        super(gameInteraction);
        botType = BotType.RANDOM;
        random = new Random();
        random2 = new Random();
    }

    public void setRand(Random rand1, Random rand2){
        random = rand1;
        random2 = rand2;
    }

    @Override
    public void botPlay(WeatherType weatherType){
        playWeather(weatherType);
        for (int i = gameInteraction.getStamina(); i > 0; i--) {
            stratRandom();
        }
    }

    void stratRandom(){
        int randAction = random.nextInt(5);

        if (    randAction == 0 &&
                gameInteraction.getResourceSize(ResourceType.ALL_MISSION) > 0 &&
                !gameInteraction.contains(ActionType.DRAW_MISSION) &&
                gameInteraction.getInventoryMissions().size() < 5 ){// pioche mission
            int randMission = random2.nextInt(3);

            if (    randMission == 0 &&
                    gameInteraction.getResourceSize(ResourceType.PARCEL_MISSION) > 0 )
                drawMission(MissionType.PARCEL);
            if (    randMission == 1 &&
                    gameInteraction.getResourceSize(ResourceType.PANDA_MISSION) > 0 )
                drawMission(MissionType.PANDA);
            if (    randMission == 2 &&
                    gameInteraction.getResourceSize(ResourceType.PEASANT_MISSION) > 0 )
                drawMission(MissionType.PEASANT);
        }
        else if ( randAction == 1 &&
                  gameInteraction.getResourceSize(ResourceType.CANAL) > 0 &&
                  !gameInteraction.contains(ActionType.DRAW_CANAL) ){  // place canal
            if ( missionParcelStrat.possibleCoordinatesCanal().size() > 0 ) {
                List<Coordinate[]> list = missionParcelStrat.possibleCoordinatesCanal();
                Collections.shuffle(list);
                gameInteraction.drawCanal();
                gameInteraction.placeCanal(list.get(0)[0],list.get(0)[1]);
            }
        }
        else if ( randAction == 2 &&
                  gameInteraction.getResourceSize(ResourceType.PARCEL) > 0 &&
                  !gameInteraction.contains(ActionType.DRAW_PARCELS) ){ // place parcel
            List<ParcelInformation> parcelList = gameInteraction.drawParcels();
            Collections.shuffle(parcelList);
            gameInteraction.selectParcel(parcelList.get(0));
            List<Coordinate> list = missionParcelStrat.possibleCoordinatesParcel();
            Collections.shuffle(list);
            gameInteraction.placeParcel(list.get(0));
        }
        else if ( randAction == 3 &&
                  missionParcelStrat.possibleCoordinatesPanda().size() != 0 &&
                  !gameInteraction.contains(ActionType.MOVE_PANDA) ){
            List<Coordinate> list = missionParcelStrat.possibleCoordinatesPanda();
            Collections.shuffle(list);
            gameInteraction.moveCharacter(CharacterType.PANDA,list.get(0));
        }
        else if ( missionParcelStrat.possibleCoordinatesPeasant().size() != 0 &&
                  !gameInteraction.contains(ActionType.MOVE_PEASANT) ) {
            List<Coordinate> list = missionParcelStrat.possibleCoordinatesPeasant();
            Collections.shuffle(list);
            gameInteraction.moveCharacter(CharacterType.PEASANT,list.get(0));
        }
    }

    @Override
    protected MissionType bestMissionTypeToDraw() {
        return null;
    }
}