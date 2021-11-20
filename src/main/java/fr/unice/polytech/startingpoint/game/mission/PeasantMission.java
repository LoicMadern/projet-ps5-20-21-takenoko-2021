package fr.unice.polytech.startingpoint.game.mission;

import fr.unice.polytech.startingpoint.game.board.Coordinate;
import fr.unice.polytech.startingpoint.game.board.Parcel;
import fr.unice.polytech.startingpoint.game.playerdata.Inventory;
import fr.unice.polytech.startingpoint.type.ColorType;
import fr.unice.polytech.startingpoint.type.ImprovementType;
import fr.unice.polytech.startingpoint.type.MissionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <h1>{@link PeasantMission} :</h1>
 *
 * <p>This class create and check if the {@link PeasantMission} is done.</p>
 *
 * @author Manuel Enzo
 * @author Naud Eric
 * @author Madern Loic
 * @author Le Calloch Antoine
 * @see PeasantMission
 * @see ParcelMission
 * @version 0.5
 */

public class PeasantMission extends Mission{
    private final ImprovementType improvementType;

    /**
     * <p>Set up a peasant mission. Initialize all variables.</p>
     *
     * @param colorType
     *            <b>the colorType of the mission</b>
     * @param points
     *            <b>the points of the mission</b>
     */
    public PeasantMission(ColorType colorType, ImprovementType improvementType, int points){
        super(MissionType.PEASANT,colorType,points);
        this.improvementType = improvementType;
    }

    public boolean checkMission(Map<Coordinate,Parcel> coordinateParcelMap, Inventory inventory) {
        return checkMission(coordinateParcelMap);
    }

    public boolean checkMission(Map<Coordinate,Parcel> coordinateParcelMap) {
        if(improvementType.equals(ImprovementType.WHATEVER))
            return checkMissionSpecial(new ArrayList<>(coordinateParcelMap.values()));
        else
            return checkMissionClassic(new ArrayList<>(coordinateParcelMap.values()));
    }

    private boolean checkMissionClassic(List<Parcel> parcelList) {
        int NB_BAMBOO = 4;
        for (Parcel parcel : parcelList) {
            if (parcel.getNbBamboo() == NB_BAMBOO && parcel.getColor() == colorType && parcel.getImprovement().equals(improvementType)){
                return true;
            }
        }
        return false;
    }

    private boolean checkMissionSpecial(List<Parcel> parcelList){
        switch(colorType){
            case GREEN:
                return checkMissionFewParcel(parcelList,4);
            case YELLOW:
                return checkMissionFewParcel(parcelList,3);
            case RED:
                return checkMissionFewParcel(parcelList,2);
            default:
                return false;
        }
    }

    private boolean checkMissionFewParcel(List<Parcel> parcelList, int nbParcel) {
        int NB_BAMBOO = 3;
        int cpt = 0;
        for (Parcel parcel : parcelList) {
            if (parcel.getNbBamboo() == NB_BAMBOO && parcel.getColor() == colorType)
                cpt++;
        }
        return cpt >= nbParcel;
    }

    public ImprovementType getImprovementType(){
        return improvementType;
    }
}