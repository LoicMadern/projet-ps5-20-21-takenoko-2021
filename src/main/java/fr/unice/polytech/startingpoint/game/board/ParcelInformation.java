package fr.unice.polytech.startingpoint.game.board;

import fr.unice.polytech.startingpoint.type.ColorType;
import fr.unice.polytech.startingpoint.type.ImprovementType;

public final class ParcelInformation {
    private final ColorType colorType;
    private ImprovementType improvementType;

    public ParcelInformation(ColorType colorType, ImprovementType improvementType){
        this.colorType = colorType;
        this.improvementType = improvementType;
    }

    public ParcelInformation(ColorType colorType){
        this.colorType = colorType;
        this.improvementType = ImprovementType.NOTHING;
    }
    public ParcelInformation(ImprovementType improvementType){
        this.colorType = ColorType.NO_COLOR;
        this.improvementType = improvementType;
    }

    public ParcelInformation(){
        this.colorType = ColorType.NO_COLOR;
        this.improvementType = ImprovementType.NOTHING;
    }

    void setImprovementType(ImprovementType improvementType) {
        this.improvementType = improvementType;
    }

    public ColorType getColorType() {
        return colorType;
    }

    public ImprovementType getImprovementType() {
        return improvementType;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj)
            return true;
        if (!(obj instanceof ParcelInformation))
            return false;
        ParcelInformation pi = (ParcelInformation) obj;
        return (colorType == pi.colorType && improvementType == pi.improvementType);
    }
}