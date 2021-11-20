package fr.unice.polytech.startingpoint.bot.strategie;

import fr.unice.polytech.startingpoint.exception.OutOfResourcesException;
import fr.unice.polytech.startingpoint.exception.RulesViolationException;
import fr.unice.polytech.startingpoint.game.GameInteraction;
import fr.unice.polytech.startingpoint.game.board.Coordinate;
import fr.unice.polytech.startingpoint.game.board.ParcelInformation;
import fr.unice.polytech.startingpoint.game.mission.Mission;
import fr.unice.polytech.startingpoint.game.mission.ParcelMission;
import fr.unice.polytech.startingpoint.type.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MissionParcelStrat extends Strategie {

    /**
     * <p>Set up the MissionParcelStrat. Call the constructor from {@link Strategie} superclass.</p>
     * @param gameInteraction
     *          <b>The Bot object used by MissionParcelStrat to access information.</b>
     */
    public MissionParcelStrat(GameInteraction gameInteraction) {
        super(gameInteraction);
    }

    /**
     * <p>Choose the strat to do for each turn. Put one canal or put one parcel</p>
     * @param mission
     *          <b>The Mission object which is need to be done at this time.</b>
     */
    public void stratOneTurn(Mission mission){
        ParcelMission parcelMission = (ParcelMission) mission;
        if (isJudiciousPutCanal(parcelMission))
            putCanal(parcelMission);
        else if(isJudiciousPutParcel())
            putParcel(parcelMission);
        else if (!gameInteraction.contains(ActionType.MOVE_PANDA) && !possibleCoordinatesPanda().isEmpty())
            gameInteraction.moveCharacter(CharacterType.PANDA,possibleCoordinatesPanda().get(0));
        else if (!gameInteraction.contains(ActionType.MOVE_PEASANT) && !possibleCoordinatesPeasant().isEmpty())
            gameInteraction.moveCharacter(CharacterType.PEASANT,possibleCoordinatesPeasant().get(0));
    }

    /**
     * <p>Check if it's possible to draw and place a canal and if a end form need to be irrigate</p>
     * @param parcelMission
     *          <b>ParcelMission object which is need to be done at this time.</b>
     * @return <b>True if it is judicious to put a canal.</b>
     * @see GameInteraction
     */
    boolean isJudiciousPutCanal(ParcelMission parcelMission){
        if (bestCoordinatesForMission(parcelMission).size() == 0)
            return gameInteraction.getResourceSize(ResourceType.CANAL) > 0 &&
                    possibleCoordinatesCanal().size() > 0 &&
                    !gameInteraction.contains(ActionType.PLACE_CANAL);
        return false;
    }

    /**
     * <p>Check if it's possible to draw and place a parcel</p>
     * @return <b>True if it is judicious to put a parcel.</b>
     * @see GameInteraction
     */
    boolean isJudiciousPutParcel(){
        return gameInteraction.getResourceSize(ResourceType.PARCEL) > 0 && !gameInteraction.contains(ActionType.DRAW_PARCELS);
    }

    /**<b><u>STRATEGIES METHODS</b>
     */

    void putParcel(ParcelMission parcelmission) {
        try {
            List<ParcelInformation> parcelInformationList = gameInteraction.drawParcels();
            List<Coordinate> bestCoords = bestCoordPossibleBasedOnParcelDrawn(parcelInformationList, parcelmission);

            gameInteraction.selectParcel(parcelInformationList.get(0));
            gameInteraction.placeParcel(bestCoords.get(0));

        }
        catch (OutOfResourcesException | RulesViolationException e) {
            e.printStackTrace();
        }
    }

    List<Coordinate> bestCoordPossibleBasedOnParcelDrawn(List<ParcelInformation> parcelInformationList, ParcelMission parcelmission) {
        Map<Coordinate, Boolean> bestCoords = bestCoordinatesForMission(parcelmission);
        List<Coordinate> coordsNeeded= new ArrayList<>();
        List<Coordinate> coordsUsed= new ArrayList<>();
        ColorType NeededColor = parcelmission.getColorType();

        bestCoords.forEach((coord, Needed) -> {
            if(boardRules.isPlayableParcel(coord))
                if(Needed && boardRules.isPlayableParcel(coord))
                    coordsNeeded.add(coord);
                else
                    coordsUsed.add(coord);
        });

        if(parcelInformationList.stream().anyMatch(parcelInfo -> parcelInfo.getColorType().equals(NeededColor)) && coordsNeeded.size() > 0) {
            parcelInformationList.removeIf(parcelInfo -> !parcelInfo.getColorType().equals(NeededColor));
            return coordsNeeded;
        }

        if(coordsUsed.size() > 0) {
            return coordsUsed;
        }

        List<Coordinate> OtherCoords = possibleCoordinatesParcel();
        OtherCoords.removeAll(bestCoords.keySet());
        return OtherCoords;
    }

    /**@param mission
     *            <b>The mission wanted to be completed.</b>
     * @return <b>The best coordinates where a parcel need to be placed to complete the mission.</b>
     */
    Map<Coordinate, Boolean> bestCoordinatesForMission(ParcelMission mission){
        Map<Coordinate, Boolean> bestCoordinates = new LinkedHashMap<>();
        int minTurnToEndForm = -1;

        for (Coordinate coordinate : allPlaces()) {
            Map<Coordinate, Boolean> parcelsToPlaceToDoForm = coordsToDoMission(coordinate, mission);

            if(parcelsToPlaceToDoForm != null && (minTurnToEndForm == -1 || parcelsToPlaceToDoForm.size() < minTurnToEndForm)){
                minTurnToEndForm = parcelsToPlaceToDoForm.size();
                bestCoordinates = parcelsToPlaceToDoForm;
            }
        }
        return bestCoordinates;
    }

    /**@param coordinate
     *            <b>The higth coordinate of a form.</b>
     * @param mission
     *            <b>The mission wanted to be completed.</b>
     * @return <b>The list of required coordinates where parcels need to be placed to complete the mission.</b>
     */
    Map<Coordinate, Boolean> coordsToDoMission(Coordinate coordinate, ParcelMission mission){
        List<Coordinate> distantCoord = new ArrayList<>();
        Map<Coordinate, Boolean> coordToDoMission = new LinkedHashMap<>();
        List<Coordinate> form = setForm(coordinate, mission.getFormType());

        if(form.contains(new Coordinate(0,0,0)) || form.stream().anyMatch(coord -> gameInteraction.isPlacedParcel(coord) &&
                !gameInteraction.getPlacedParcelInformation(coord).getColorType().equals(mission.getColorType())))
            return null;

        form.removeIf(gameInteraction::isPlacedParcel);

        for (Coordinate coord : form) {
            coordToDoMission.put(coord, true);
            if (!boardRules.isPlayableParcel(coord) && coordAroundUse(coord) != null) {
                List<Coordinate> laidCoord = Coordinate.getInCommonAroundCoordinates(coord, coordAroundUse(coord));
                if (boardRules.isPlayableParcel(laidCoord.get(0)))
                    coordToDoMission.put(laidCoord.get(0), false);
                else if (boardRules.isPlayableParcel(laidCoord.get(1)))
                    coordToDoMission.put(laidCoord.get(1), false);
                else
                    return null;
            }
            else if (!boardRules.isPlayableParcel(coord))
                distantCoord.add(coord);
        }

        for (Coordinate coord : distantCoord)
            treatDistantCoord(coord,coordToDoMission);

        return coordToDoMission;
    }

    void treatDistantCoord(Coordinate distantCoord, Map<Coordinate, Boolean> coordToDoMission){
        if(distantCoord.coordinatesAround().stream().filter(coordToDoMission.keySet()::contains).count() < 2){
            List<Coordinate>  coordsNeed =  Coordinate.getInCommonAroundCoordinates(distantCoord,distantCoord.coordinatesAround().stream().filter(coordToDoMission.keySet()::contains).collect(Collectors.toList()).get(0));
            if(boardRules.isPlayableParcel(coordsNeed.get(0)))
                coordToDoMission.put(coordsNeed.get(0),false);
            else if(boardRules.isPlayableParcel(coordsNeed.get(1)))
                coordToDoMission.put(coordsNeed.get(1),false);
            else if(coordAroundUse(coordsNeed.get(0)) != null)
                coordToDoMission.put(coordsNeed.get(0),false);
            else if(coordAroundUse(coordsNeed.get(1)) != null)
                coordToDoMission.put(coordsNeed.get(1),false);
            else {
                coordToDoMission.put(coordsNeed.get(0), false);
                coordToDoMission.put(coordsNeed.get(1), false);
            }
        }
    }

    Coordinate coordAroundUse(Coordinate coordinate){
        for (Coordinate coordAround : coordinate.coordinatesAround())
            if(gameInteraction.isPlacedParcel(coordAround))
                return coordAround;
        return null;
    }

    List<Coordinate> setForm(Coordinate coordinate, FormType form){
        List<Coordinate> coordForm = new ArrayList<>(Coordinate.coordinatesOfOffsets(coordinate, form.getOffsetsList1()));
        coordForm.addAll(Coordinate.coordinatesOfOffsets(coordinate, form.getOffsetsList2()));
        return coordForm;
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////

    void putCanal(ParcelMission mission) {
        try {
            List<Coordinate> fullForm = coordEndMissionNoIrrigate(mission);
            Coordinate[] bestCoordinatesCanal = null;

            for (Coordinate coordinateForm : fullForm)
                if (!gameInteraction.isPlacedAndIrrigatedParcel(coordinateForm))
                    bestCoordinatesCanal = getBestCanal(coordinateForm);

            if (bestCoordinatesCanal != null && fullForm.size() != 0) {
                gameInteraction.drawCanal();
                gameInteraction.placeCanal(bestCoordinatesCanal[0], bestCoordinatesCanal[1]);
            } else {
                Coordinate []coordCanal = possibleCoordinatesCanal().get(0);
                gameInteraction.drawCanal();
                gameInteraction.placeCanal(coordCanal[0], coordCanal[1]);
            }
        } catch (OutOfResourcesException | RulesViolationException e) {
            e.printStackTrace();
        }
    }

    List<Coordinate> coordEndMissionNoIrrigate(ParcelMission mission) {
        for (Coordinate coordinate : gameInteraction.getPlacedCoordinates()) {
            Map<Coordinate, Boolean> coordinateList = coordsToDoMission(coordinate,mission);
            if(coordinateList != null)
                if(coordinateList.size() == 0)
                    return setForm(coordinate, mission.getFormType());
        }
        return new ArrayList<>();
    }

    /**
     * <p>Count the number of move needed to end one mission</p>
     * @param mission
     *          <b>Mission object whose number of movements we are looking for.</b>
     * @return <b>The number of move or -1 if something wrong</b>
     * @see GameInteraction
     */
    public int howManyMoveToDoMission(Mission mission) {
        ParcelMission parcelMission = (ParcelMission) mission;
        if(isJudiciousPutParcel() || isJudiciousPutCanal(parcelMission)){
            if (bestCoordinatesForMission(parcelMission).size() > gameInteraction.getResourceSize(ResourceType.PARCEL))
                return -1;
            return nbMoveForMission(parcelMission);
        }
        return -1;
    }

    /**
     * <p>Count the number of move (putCanal or putParcel) needed to end one mission</p>
     * @param parcelMission
     *          <b>ParcelMission object whose number of movements we are looking for.</b>
     * @return <b>The number of move or -1 if something wrong</b>
     * @see GameInteraction
     */
    int nbMoveForMission(ParcelMission parcelMission) {
        Map<Coordinate,Boolean> bestCoordinatesForMission;
        int nbMove = 0;

        if((bestCoordinatesForMission = bestCoordinatesForMission(parcelMission)).size() == 0){
            for (Coordinate coord : coordEndMissionNoIrrigate(parcelMission))
                if (!gameInteraction.isPlacedAndIrrigatedParcel(coord))
                    nbMove += 2;
            return nbMove;
        }

        for (Map.Entry<Coordinate,Boolean> line : bestCoordinatesForMission.entrySet()) {
            if (line.getValue() && !line.getKey().isNextTo(new Coordinate(0, 0, 0)))
                nbMove += 2;
        }

        return nbMove + bestCoordinatesForMission.size();
    }
}