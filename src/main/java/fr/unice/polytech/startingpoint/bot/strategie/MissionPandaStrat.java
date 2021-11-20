package fr.unice.polytech.startingpoint.bot.strategie;

import fr.unice.polytech.startingpoint.exception.OutOfResourcesException;
import fr.unice.polytech.startingpoint.exception.RulesViolationException;
import fr.unice.polytech.startingpoint.game.GameInteraction;
import fr.unice.polytech.startingpoint.game.board.Coordinate;
import fr.unice.polytech.startingpoint.game.board.ParcelInformation;
import fr.unice.polytech.startingpoint.game.mission.Mission;
import fr.unice.polytech.startingpoint.type.*;

import java.util.List;

public class MissionPandaStrat extends Strategie {

    public MissionPandaStrat(GameInteraction gameInteraction) {
        super(gameInteraction);
    }

    public void stratOneTurn(Mission mission){
        if (isJudiciousMovePanda(mission.getColorType()))
            gameInteraction.moveCharacter(CharacterType.PANDA,strategyMovePanda(mission.getColorType()));
        else if (isJudiciousMovePeasant())
            gameInteraction.moveCharacter(CharacterType.PEASANT,strategyMovePeasant(mission.getColorType()));
        else if (isJudiciousPlaceParcel())
            strategyPlaceParcel(drawParcelStrategy(mission.getColorType()));
        else if (isJudiciousPlaceCanal())
            strategyPlaceCanal();
        else if (!gameInteraction.contains(ActionType.MOVE_PANDA) && !possibleCoordinatesPanda().isEmpty())
            gameInteraction.moveCharacter(CharacterType.PANDA,possibleCoordinatesPanda().get(0));
    }

    /**<b><u>IS JUDICIOUS METHODS</u></b>
     */

    public boolean isJudiciousMovePanda(ColorType colorTypeMission){
        return strategyMovePanda(colorTypeMission) != null && !gameInteraction.contains(ActionType.MOVE_PANDA);
    }

    public boolean isJudiciousMovePeasant(){
        return !gameInteraction.contains(ActionType.MOVE_PEASANT) && !possibleCoordinatesPeasant().isEmpty();
    }

    public boolean isJudiciousPlaceParcel() {
        return !gameInteraction.contains(ActionType.DRAW_PARCELS) && (gameInteraction.getResourceSize(ResourceType.PARCEL) > 0) ;
    }

    boolean isJudiciousPlaceCanal() {
        return !gameInteraction.contains(ActionType.DRAW_CANAL) && !gameInteraction.contains(ActionType.PLACE_CANAL)
                && (gameInteraction.getResourceSize(ResourceType.CANAL) > 0) ;
    }

    /**<b><u>STRATEGIES METHODS</b>
     */

    public Coordinate strategyMovePanda(ColorType colorTypeMission) {
        if (colorTypeMission.equals(ColorType.ALL_COLOR))
            return strategyMissionAllColor();
        else
            return strategyMissionOneColor(colorTypeMission);
    }

    public Coordinate strategyMissionOneColor(ColorType colorTypeMission) {
        for (Coordinate coordinate : possibleCoordinatesPanda()) {
            if ( gameInteraction.getPlacedParcelsNbBamboo(coordinate) > 0 &&
                    !gameInteraction.getPlacedParcelInformation(coordinate).getImprovementType().equals(ImprovementType.ENCLOSURE) &&
                    gameInteraction.getPlacedParcelInformation(coordinate).getColorType().equals(colorTypeMission) )
                return coordinate;
        }
        return null;
    }

    public Coordinate strategyMissionAllColor(){
        for (Coordinate coordinate : possibleCoordinatesPanda()) {
            if ( gameInteraction.getPlacedParcelsNbBamboo(coordinate) > 0 &&
                    !gameInteraction.getPlacedParcelInformation(coordinate).getColorType().equals(ColorType.NO_COLOR) &&
                    !gameInteraction.getPlacedParcelInformation(coordinate).getImprovementType().equals(ImprovementType.ENCLOSURE) &&
                    gameInteraction.getInventoryBamboo()[gameInteraction.getPlacedParcelInformation(coordinate).getColorType().ordinal()] == 0 )
                return coordinate;
        }
        return null;
    }

    public Coordinate strategyMovePeasant(ColorType colorTypeMission) {
        for (Coordinate coordinate : possibleCoordinatesPeasant()) {
            if (gameInteraction.getPlacedParcelInformation(coordinate).getColorType().equals(colorTypeMission)) {
                return coordinate;
            }
        }
        return possibleCoordinatesPeasant().get(0);
    }

    public void strategyPlaceCanal() {
        gameInteraction.drawCanal();
        if(!possibleCoordinatesCanal().isEmpty()){
            Coordinate[] coordinates = possibleCoordinatesCanal().get(0);
            gameInteraction.placeCanal(coordinates[0],coordinates[1]);
        }
    }


    public ParcelInformation drawParcelStrategy(ColorType colorType){
        List<ParcelInformation> parcelInformations=gameInteraction.drawParcels();
        for(ParcelInformation parcelInformation:parcelInformations)
            if(colorType.equals(parcelInformation.getColorType())) {
                return parcelInformation;
            }
        return parcelInformations.get(0);
    }

    Coordinate strategyPlaceParcel(ParcelInformation parcelInformation) {
        try {
            Coordinate coordinate = null;
            for (Coordinate c : possibleCoordinatesParcel()) {
                if (gameInteraction.getRules().isMovableCharacter(CharacterType.PANDA, c))
                    coordinate = c;

            }
            if (coordinate == null){
                coordinate = possibleCoordinatesParcel().get(0);
            }

            gameInteraction.selectParcel(parcelInformation);
            gameInteraction.placeParcel(coordinate);
            return coordinate;
        }
        catch (OutOfResourcesException | RulesViolationException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**<b><u>NUMBER OF MOVES TO DO THE MISSION METHODS</b>
     */

    public int howManyMoveToDoMission(Mission mission) {
        if (!isAlreadyFinished(mission) &&
                isJudiciousMovePanda(mission.getColorType()) &&
                        isJudiciousMovePeasant() &&
                        !gameInteraction.contains(ActionType.MOVE_PANDA) && !possibleCoordinatesPanda().isEmpty()){
            if (!isFinishedInOneTurn(mission)){
                if (mission.getColorType().equals(ColorType.ALL_COLOR)){
                    if (notExistGoodMovableParcel(ColorType.GREEN) && notExistGoodMovableParcel(ColorType.YELLOW) && notExistGoodMovableParcel(ColorType.RED))
                        return -1;
                    return nbMoveAllColor();
                }
                else{
                    if (notExistGoodMovableParcel(mission.getColorType()))
                        return -1;
                    return nbMoveOneColor(mission.getColorType());
                }
            }
            return 1;
        }
        return -1;
    }

    boolean isAlreadyFinished(Mission mission) {
        if (mission.getColorType().equals(ColorType.ALL_COLOR)){
            for (int nbBamboo : gameInteraction.getInventoryBamboo()){
                if (nbBamboo == 0)
                    return false;
            }
            return true;
        }
        else
            return gameInteraction.getInventoryBamboo()[mission.getColorType().ordinal()] >= 2;
    }

    boolean isFinishedInOneTurn(Mission mission) {
        if (strategyMovePanda(mission.getColorType()) != null){
            if (mission.getColorType() == ColorType.ALL_COLOR){
                int nbColorInInventory = 0;
                for (int i = 0; i < gameInteraction.getInventoryBamboo().length ; i++) {
                    if (gameInteraction.getInventoryBamboo()[i] >= 1)
                        nbColorInInventory ++;
                    else if ( ColorType.values()[i].equals(gameInteraction.getPlacedParcelInformation(strategyMovePanda(mission.getColorType())).getColorType()) )
                        nbColorInInventory ++;
                }
                return nbColorInInventory == gameInteraction.getInventoryBamboo().length;
            }
            else
                return  gameInteraction.getInventoryBamboo()[mission.getColorType().ordinal()] == 1 &&
                    gameInteraction.getPlacedParcelsNbBamboo(strategyMovePanda(mission.getColorType())) >= 1;
        }
        return false;
    }

    private boolean notExistGoodMovableParcel(ColorType colorTypeMission){
        int nbEnclosure = 0;
        List<Coordinate> coordinates = gameInteraction.getPlacedCoordinatesByColor(colorTypeMission);
        for (Coordinate coordinate : coordinates){
            if (!gameInteraction.getPlacedParcelInformation(coordinate).getImprovementType().equals(ImprovementType.ENCLOSURE))
                nbEnclosure++;
        }
        return nbEnclosure == 0;
    }

    private int nbMoveOneColor(ColorType colorTypeMission){
        if (strategyMovePanda(colorTypeMission) != null){
            if (gameInteraction.getInventoryBamboo()[colorTypeMission.ordinal()] == 1)
                return 1;
            return 3;
        }
        return -1;
    }

    private int nbMoveAllColor(){
        int nbBamboos = 0;
        for (int i = 0; i < gameInteraction.getInventoryBamboo().length; i++) {
            if (gameInteraction.getInventoryBamboo()[i] == 0)
                if (strategyMovePanda(ColorType.values()[i]) == null)
                    nbBamboos ++;
                nbBamboos ++;
        }
        return nbBamboos;
    }
}