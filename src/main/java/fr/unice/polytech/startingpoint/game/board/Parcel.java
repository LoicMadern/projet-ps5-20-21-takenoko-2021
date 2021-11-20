package fr.unice.polytech.startingpoint.game.board;

import fr.unice.polytech.startingpoint.type.ColorType;
import fr.unice.polytech.startingpoint.type.ImprovementType;

/**
 * Classe representant une parcelle
 * @author Manuel Enzo
 * @author Naud Eric
 * @author Madern Loic
 * @author Le Calloch Antoine
 * @version 0.5
 */

public class Parcel {
    private final int NUMBER_MINIMAL_OF_BAMBOOS = 0;
    private final int NUMBER_MAXIMAL_OF_BAMBOOS = 4;
    private int nbBamboo = NUMBER_MINIMAL_OF_BAMBOOS;
    private boolean irrigated = false;
    private final ParcelInformation parcelInformation;

    public Parcel(ColorType colorType, ImprovementType improvementType){
        parcelInformation = new ParcelInformation(colorType,improvementType);
        if (improvementType == ImprovementType.WATERSHED)
            setIrrigated();
    }

    public Parcel(ColorType colorType){
        this(colorType,ImprovementType.NOTHING);
    }

    public Parcel(ImprovementType improvementType){
        this(ColorType.NO_COLOR,improvementType);
    }

    public Parcel(){
        this(ColorType.NO_COLOR,ImprovementType.NOTHING);
    }

    //Ajoute un bamboo à la parcelle
    public void addBamboo(){
        if (nbBamboo < NUMBER_MAXIMAL_OF_BAMBOOS){
            if (getImprovement().equals(ImprovementType.FERTILIZER))
                nbBamboo ++;
            nbBamboo ++;
        }
    }

    //Supprime un bambou de la parcelle
    public ColorType delBamboo(){
        if (nbBamboo > NUMBER_MINIMAL_OF_BAMBOOS && !getImprovement().equals(ImprovementType.ENCLOSURE)){
            nbBamboo --;
            return parcelInformation.getColorType();
        }
        return ColorType.NO_COLOR;
    }

    //Renvoie les coordonnées de la parcelle après l'avoir irrigué et lui avoir ajouté un bambou si elle ne l'était pas avant
    public void setIrrigated() {
        if(!irrigated)
            addBamboo();
        irrigated = true;
    }

    public void setImprovementType(ImprovementType improvementType) {
        parcelInformation.setImprovementType(improvementType);
        if (improvementType == ImprovementType.WATERSHED)
            setIrrigated();
    }

    //Renvoie si la parcelle est irriguée ou non
    public boolean getIrrigated(){
        return irrigated;
    }

    //Renvoie la couleur de la parcelle
    public ColorType getColor() {
        return parcelInformation.getColorType();
    }

    public ImprovementType getImprovement(){
        return parcelInformation.getImprovementType();
    }

    public ParcelInformation getParcelInformation() {
        return parcelInformation;
    }

    public int getNbBamboo(){
        return nbBamboo;
    }

    @Override
    public String toString() {
        StringBuilder displayInformationsParcel = new StringBuilder();
        displayInformationsParcel.append("Couleur : ").append(parcelInformation.getColorType()).append(" | ");
        displayInformationsParcel.append("Aménagement : ").append(parcelInformation.getImprovementType()).append(" | ");
        displayInformationsParcel.append("Nombre bambous : ").append(nbBamboo).append(" | ");
        if(irrigated)
            displayInformationsParcel.append("Irrigué");
        else
            displayInformationsParcel.append("Non irrigué");
        return displayInformationsParcel.toString();
    }
}