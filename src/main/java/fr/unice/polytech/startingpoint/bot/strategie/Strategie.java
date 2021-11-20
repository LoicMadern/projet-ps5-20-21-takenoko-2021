package fr.unice.polytech.startingpoint.bot.strategie;

import fr.unice.polytech.startingpoint.game.GameInteraction;
import fr.unice.polytech.startingpoint.game.board.BoardRules;
import fr.unice.polytech.startingpoint.game.board.Coordinate;
import fr.unice.polytech.startingpoint.game.mission.Mission;
import fr.unice.polytech.startingpoint.type.CharacterType;
import fr.unice.polytech.startingpoint.type.ColorType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class Strategie {
    final GameInteraction gameInteraction;
    final BoardRules boardRules;

    Strategie(GameInteraction gameInteraction) {
        this.gameInteraction = gameInteraction;
        this.boardRules = gameInteraction.getRules();
    }

    /**
     * <p>The actions of the bot during his turn.</p>
     *
     * @param mission
     */
    public abstract void stratOneTurn(Mission mission);

    public abstract int howManyMoveToDoMission(Mission mission);

    public Coordinate[] getBestCanal(Coordinate coordinateToIrrigate) {
        Coordinate[] bestCanal = null;
        for (Coordinate[] coordinatesCanal : possibleCoordinatesCanal())
            if (bestCanal == null ||
                    (Coordinate.getNorm(coordinateToIrrigate, coordinatesCanal[0]) + Coordinate.getNorm(coordinateToIrrigate, coordinatesCanal[1])) <
                            (Coordinate.getNorm(coordinateToIrrigate, bestCanal[0]) + Coordinate.getNorm(coordinateToIrrigate, bestCanal[1])))
                bestCanal = coordinatesCanal;
        return bestCanal;
    }

    /**
     * @return <b>A list of all parcels’ coordinates present on the board and one layer of coordinates around.</b>
     */
    public List<Coordinate> allPlaces() {
        Set<Coordinate> possibleCoordinates = new HashSet<>();
        for (Coordinate c : gameInteraction.getPlacedCoordinates()) {
            possibleCoordinates.add(c);
            for (Coordinate offSet : Coordinate.offSets())
                possibleCoordinates.add(new Coordinate(c, offSet));
        }
        return new ArrayList<>(possibleCoordinates);
    }

    /**
     * @return <b>A list of coordinates for all placeable parcels on the board.</b>
     */
    public List<Coordinate> possibleCoordinatesParcel() {
        Set<Coordinate> possibleCoordinates = new HashSet<>();
        for (Coordinate c : gameInteraction.getPlacedCoordinates())
            for (Coordinate offSet : Coordinate.offSets())
                if (boardRules.isPlayableParcel(new Coordinate(c, offSet)))
                    possibleCoordinates.add(new Coordinate(c, offSet));
        return new ArrayList<>(possibleCoordinates);
    }

    /**
     * @return <b>A list of coordinates for all placeable canals on the board.</b>
     */
    public List<Coordinate[]> possibleCoordinatesCanal() {
        Set<Coordinate[]> possibleCoordinates = new HashSet<>();
        for (Coordinate coordinate1 : gameInteraction.getPlacedCoordinates())
            for (Coordinate coordinate2 : gameInteraction.getPlacedCoordinates())
                if (boardRules.isPlayableCanal(coordinate1, coordinate2))
                    possibleCoordinates.add(new Coordinate[]{coordinate1, coordinate2});
        return new ArrayList<>(possibleCoordinates);
    }

    /**
     * @return <b>A list of coordinates where the Panda can be moved on the board.</b>
     */
    public List<Coordinate> possibleCoordinatesPanda() {
        Set<Coordinate> possibleCoordinates = new HashSet<>();
        for (Coordinate coordinate : gameInteraction.getPlacedCoordinates())
            if (boardRules.isMovableCharacter(CharacterType.PANDA, coordinate))
                possibleCoordinates.add(coordinate);
        return new ArrayList<>(possibleCoordinates);
    }

    /**
     * @return <b>A list of coordinates  where the Peasant can be moved on the board.</b>
     */
    public List<Coordinate> possibleCoordinatesPeasant() {
        Set<Coordinate> possibleCoordinates = new HashSet<>();
        for (Coordinate c : gameInteraction.getPlacedCoordinates())
            if (boardRules.isMovableCharacter(CharacterType.PEASANT, c))
                possibleCoordinates.add(c);
        return new ArrayList<>(possibleCoordinates);
    }

    /**
     * @return <b>A list of playable coordinates around a coordinate given </b>
     **/
    private List<Coordinate> playableCoordinatesAroundACoordinateGivenCo(Coordinate coordinate) {
        return coordinate.coordinatesAround().stream()
                .filter(c -> boardRules.isPlayableParcel(c) && !gameInteraction.isPlacedParcel(c))
                .collect(Collectors.toList());
    }

    /**
     * @return <b>A list of all possible coordinates next to all parcels with the color given </b>
     **/
    public List<Coordinate> allPosssibleCoordinatesNextToParcelsWithAColor(ColorType colorGiven) {
        Set<Coordinate> posssibleCoNextToParcelsWithAColor = new HashSet<>();
        List<Coordinate> placedCoordinatesByColor = gameInteraction.getPlacedCoordinatesByColor(colorGiven);
        for (Coordinate placedCoordinate : placedCoordinatesByColor)
            posssibleCoNextToParcelsWithAColor.addAll(playableCoordinatesAroundACoordinateGivenCo(placedCoordinate));
        return new ArrayList<>(posssibleCoNextToParcelsWithAColor);
    }
}