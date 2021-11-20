package fr.unice.polytech.startingpoint.game.mission;

import fr.unice.polytech.startingpoint.game.board.Coordinate;
import fr.unice.polytech.startingpoint.game.board.Parcel;
import fr.unice.polytech.startingpoint.game.playerdata.Inventory;
import fr.unice.polytech.startingpoint.type.ColorType;
import fr.unice.polytech.startingpoint.type.MissionType;

import java.util.Map;

public abstract class Mission {
    private final MissionType missionType;
    final ColorType colorType;
    private final int points;

    Mission(MissionType missionType, ColorType colorType, int points){
        this.missionType = missionType;
        this.colorType = colorType;
        this.points = points;
    }

    public abstract boolean checkMission(Map<Coordinate, Parcel> coordinateParcelMap, Inventory inventory);

    public MissionType getMissionType() {
        return missionType;
    }

    public ColorType getColorType() {
        return colorType;
    }

    public int getPoints() {
        return points;
    }
}
