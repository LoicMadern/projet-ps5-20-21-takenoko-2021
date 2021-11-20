package fr.unice.polytech.startingpoint.game.board;

import fr.unice.polytech.startingpoint.exception.IllegalTypeException;
import fr.unice.polytech.startingpoint.type.CharacterType;
import fr.unice.polytech.startingpoint.type.ColorType;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;

/**
 * Classe representant le plateau de jeu
 * @author Manuel Enzo
 * @author Naud Eric
 * @author Madern Loic
 * @author Le Calloch Antoine
 * @version 2020.12.03
 */

public class Board {
    private final Character panda = new Character(CharacterType.PANDA);
    private final Character peasant = new Character(CharacterType.PEASANT);
    private final Map<Coordinate, Parcel> placedParcels = new HashMap<>();
    private final Map<SortedSet<Coordinate>, Canal> placedCanals = new HashMap<>();

    public Board() {
        placedParcels.put(new Coordinate(),new Parcel());
    }

    //Place une parcelle sur le board si les conditions le permettent
    public void placeParcel(Parcel newParcel, Coordinate newCoordinate){
        placedParcels.put(newCoordinate, newParcel);
        if (newCoordinate.isNextTo(new Coordinate()))
            newParcel.setIrrigated();
    }

    //Place un canal sur le board si les conditions le permettent
    public void placeCanal(Canal canal, Coordinate coordinate1, Coordinate coordinate2){
        placedCanals.put(Coordinate.getSortedSet(coordinate1, coordinate2), canal);
        placedParcels.get(coordinate1).setIrrigated();
        placedParcels.get(coordinate2).setIrrigated();
    }

    //Fait bouger un personnage et effectue son action si les conditions le permettent
    public ColorType moveCharacter(CharacterType characterType, Coordinate coordinate){
        getCharacter(characterType).setCoordinate(coordinate);
        return characterAction(characterType);
    }

    //Effectue l’action du personnage passé en paramètre
    private ColorType characterAction(CharacterType characterType){
        if(characterType == CharacterType.PANDA)
                return actionPanda();

        actionPeasant();
        return null;
    }

    //supprime un bambou sur la case
    private ColorType actionPanda(){
        return placedParcels.get(panda.getCoordinate()).delBamboo();
    }

    //ajoute un bambou sur la case si irrigué + autour si même couleur et irrigué
    private void actionPeasant() {
        ColorType color = placedParcels.get(peasant.getCoordinate()).getColor();
        if (placedParcels.get(peasant.getCoordinate()).getIrrigated())
            placedParcels.get(peasant.getCoordinate()).addBamboo();
        for( Coordinate coordinate : peasant.getCoordinate().coordinatesAround()){
            if (placedParcels.containsKey(coordinate)) {
                if (color == placedParcels.get(coordinate).getColor() && placedParcels.get(coordinate).getIrrigated())
                    placedParcels.get(coordinate).addBamboo();
            }

        }
    }

    //Renvoie true si une parcelle est posées aux coordonnées passées en paramètre
    public boolean isPlacedParcel(Coordinate coordinate){
        return placedParcels.containsKey(coordinate);
    }

    //Renvoie true si une parcelle est posée et est irriguée aux coordonnées passées en paramètre
    public  boolean isPlacedAndIrrigatedParcel(Coordinate coordinate){
        if (isPlacedParcel(coordinate))
            return placedParcels.get(coordinate).getIrrigated();
        return false;
    }

    //Renvoie true si un canal est posé aux coordonnées passées en paramètre
    public boolean isPlacedCanal(Coordinate coordinate1, Coordinate coordinate2){
        return placedCanals.containsKey(Coordinate.getSortedSet(coordinate1,coordinate2));
    }

    //Renvoie une map des parcelles placées
    public Map<Coordinate, Parcel> getPlacedParcels(){
        return placedParcels;
    }

    //Renvoie une map des canaux placés
    public Map<SortedSet<Coordinate>, Canal> getPlacedCanals(){
        return placedCanals;
    }

    //Renvoie le
    Character getCharacter(CharacterType characterType) {
        switch (characterType){
            case PANDA:
                return panda;
            case PEASANT:
                return peasant;
            default:
                throw new IllegalTypeException("Wrong CharacterType to move.");
        }
    }

    public Coordinate getPandaCoordinate() {
        return panda.getCoordinate();
    }

    public Coordinate getPeasantCoordinate() {
        return peasant.getCoordinate();
    }
}