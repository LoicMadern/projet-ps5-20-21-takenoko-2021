package fr.unice.polytech.startingpoint.bot.strategie;

import fr.unice.polytech.startingpoint.exception.OutOfResourcesException;
import fr.unice.polytech.startingpoint.exception.RulesViolationException;
import fr.unice.polytech.startingpoint.game.GameInteraction;
import fr.unice.polytech.startingpoint.game.board.Coordinate;
import fr.unice.polytech.startingpoint.game.board.ParcelInformation;
import fr.unice.polytech.startingpoint.game.mission.Mission;
import fr.unice.polytech.startingpoint.game.mission.PeasantMission;
import fr.unice.polytech.startingpoint.type.ActionType;
import fr.unice.polytech.startingpoint.type.CharacterType;
import fr.unice.polytech.startingpoint.type.ImprovementType;
import fr.unice.polytech.startingpoint.type.ResourceType;

import java.util.List;

public class MissionPeasantStrat extends Strategie {

    public MissionPeasantStrat(GameInteraction gameInteraction) {
        super(gameInteraction);
    }

    public void stratOneTurn(Mission mission){
        PeasantMission peasantMission = (PeasantMission) mission;
        if ( isJudiciousMovePeasant(peasantMission) )
            gameInteraction.moveCharacter(CharacterType.PEASANT,strategyMovePeasant(peasantMission));
        else if ( isJudiciousPlaceParcel() )
            strategyPlaceParcel(peasantMission);
        else if ( isJudiciousPlaceCanal() )
            strategyPlaceCanal(peasantMission);
    }

    /**<b><u>IS JUDICIOUS METHODS</b>
     */

    boolean isJudiciousMovePeasant(PeasantMission peasantMission){
        return  strategyMovePeasant(peasantMission) != null &&
                !gameInteraction.contains(ActionType.MOVE_PEASANT);
    }

    boolean isJudiciousPlaceCanal() {
        return  !gameInteraction.contains(ActionType.DRAW_CANAL) &&
                !gameInteraction.contains(ActionType.PLACE_CANAL) &&
                gameInteraction.getResourceSize(ResourceType.CANAL) > 0;
    }

    boolean isJudiciousPlaceParcel() {
        return  !gameInteraction.contains(ActionType.DRAW_PARCELS) &&
                gameInteraction.getResourceSize(ResourceType.PARCEL) > 0;
    }

    /**<b><u>STRATEGIES METHODS</b>
     */

    public Coordinate strategyMovePeasant(PeasantMission mission) {
        if (!mission.getImprovementType().equals(ImprovementType.WHATEVER))
            for (Coordinate coordinate : possibleCoordinatesPeasant())
                if (gameInteraction.getPlacedParcelInformation(coordinate).getColorType().equals(mission.getColorType()) && gameInteraction.getPlacedParcelInformation(coordinate).getImprovementType().equals(mission.getImprovementType()))
                    return coordinate;
        return null;
    }

    public void strategyPlaceParcel(PeasantMission peasantMission) {
        try{
            gameInteraction.selectParcel(getBestParcelInformation(gameInteraction.drawParcels(),peasantMission));
            gameInteraction.placeParcel(getBestCoordinateParcel());
        }
        catch (OutOfResourcesException | RulesViolationException e) {
            e.printStackTrace();
        }
    }

    ParcelInformation getBestParcelInformation(List<ParcelInformation> parcelInformationList,PeasantMission peasantMission){
        ParcelInformation bestParcelInformation = null;

        for (ParcelInformation parcelInformation : parcelInformationList)
            if (    parcelInformation.getColorType().equals(peasantMission.getColorType()) &&
                    parcelInformation.getImprovementType().equals(peasantMission.getImprovementType()) )
                bestParcelInformation = parcelInformation;

        if (bestParcelInformation == null)
            return parcelInformationList.get(0);
        else
            return bestParcelInformation;
    }

    Coordinate getBestCoordinateParcel(){
        Coordinate bestCoordinate = null;

        for (Coordinate c : possibleCoordinatesParcel())
            if (gameInteraction.getRules().isMovableCharacterIfCoordinateIsPlaced(CharacterType.PEASANT,c))
                bestCoordinate = c;

        if (bestCoordinate == null)
            return possibleCoordinatesParcel().get(0);
        else
            return bestCoordinate;
    }

    public void strategyPlaceCanal(PeasantMission peasantMission) {
        gameInteraction.drawCanal();
        if(!possibleCoordinatesCanal().isEmpty()){
            Coordinate[] coordinates = getBestCoordinateCanal(peasantMission);
            gameInteraction.placeCanal(coordinates[0],coordinates[1]);
        }
    }

    Coordinate[] getBestCoordinateCanal(PeasantMission peasantMission){
        ParcelInformation parcelInformation = new ParcelInformation(peasantMission.getColorType(),peasantMission.getImprovementType());
        List<Coordinate> goodPlacedParcelForMission = gameInteraction.getPlacedCoordinatesByParcelInformation(parcelInformation);
        Coordinate bestCoordinate = null;
        Coordinate[] bestCoordinateCanal = null;

        for (Coordinate c : goodPlacedParcelForMission){
            if (gameInteraction.getRules().isMovableCharacter(CharacterType.PEASANT,c))
                bestCoordinate = c;
        }

        if (bestCoordinate != null)
            bestCoordinateCanal = getBestCanal(bestCoordinate);

        if (bestCoordinateCanal != null)
            return getBestCanal(bestCoordinate);
        else
            return possibleCoordinatesCanal().get(0);
    }

    /**<b><u>NUMBER OF MOVES TO DO THE MISSION METHODS</b>
     */

    public int howManyMoveToDoMission(Mission mission) {
        PeasantMission peasantMission = (PeasantMission) mission;
        if(     !isAlreadyFinished(peasantMission) &&
                isJudiciousMovePeasant((PeasantMission) mission) &&
                !gameInteraction.contains(ActionType.MOVE_PEASANT) ){
            if (    peasantMission.getImprovementType().equals(ImprovementType.WHATEVER) ||
                    notExistGoodMovableParcel(peasantMission) )
                return -1;
            else if ( isFinishedInOneTurn(peasantMission) )
                return 1;
            return nbMovePeasant(peasantMission);
        }
        return -1;
    }

    boolean isAlreadyFinished(PeasantMission peasantMission) {
        for (Coordinate coordinate : gameInteraction.getPlacedCoordinatesByParcelInformation(new ParcelInformation(peasantMission.getColorType(),peasantMission.getImprovementType())))
            if (gameInteraction.getPlacedParcelsNbBamboo(coordinate) == 4)
                return true;
        return false;
    }

    boolean isFinishedInOneTurn(PeasantMission peasantMission) {
        if (strategyMovePeasant(peasantMission) != null)
            if (    gameInteraction.getPlacedParcelsNbBamboo(strategyMovePeasant(peasantMission)) == 2 &&
                    peasantMission.getImprovementType().equals(ImprovementType.FERTILIZER) )
                return true;
            else
                return gameInteraction.getPlacedParcelsNbBamboo(strategyMovePeasant(peasantMission)) == 3;
        return false;
    }

    boolean notExistGoodMovableParcel(PeasantMission mission){
        int nbGoodParcelPlaced = 0;
        List<Coordinate> coordinates = gameInteraction.getPlacedCoordinatesByColor(mission.getColorType());

        for (Coordinate coordinate : coordinates)
            if ( gameInteraction.getPlacedParcelInformation(coordinate).getImprovementType().equals(mission.getImprovementType()) )
                nbGoodParcelPlaced++;

        return nbGoodParcelPlaced == 0;
    }

    private int nbMovePeasant(PeasantMission peasantMission){
        int maxNbBamboo = 0;

        for (Coordinate coordinate : gameInteraction.getPlacedCoordinatesByParcelInformation(new ParcelInformation(peasantMission.getColorType(),peasantMission.getImprovementType())))
            if (gameInteraction.getPlacedParcelsNbBamboo(coordinate) > maxNbBamboo)
                maxNbBamboo = gameInteraction.getPlacedParcelsNbBamboo(coordinate);

        return 5 - maxNbBamboo;
    }
}