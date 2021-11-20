package fr.unice.polytech.startingpoint.game.mission;

import fr.unice.polytech.startingpoint.game.board.Coordinate;
import fr.unice.polytech.startingpoint.game.board.Parcel;
import fr.unice.polytech.startingpoint.game.playerdata.Inventory;
import fr.unice.polytech.startingpoint.type.ColorType;
import fr.unice.polytech.startingpoint.type.FormType;
import fr.unice.polytech.startingpoint.type.MissionType;

import java.util.List;
import java.util.Map;

import static fr.unice.polytech.startingpoint.type.FormType.*;

/**
 * <h1>{@link ParcelMission} :</h1>
 *
 * <p>This class create and check if the {@link ParcelMission} is done.</p>
 *
 *
 * @author Manuel Enzo
 * @author Naud Eric
 * @author Madern Loic
 * @author Le Calloch Antoine
 * @see PeasantMission
 * @see ParcelMission
 * @version 0.5
 */

public class ParcelMission extends Mission{
    private final ColorType colorType2;
    private final FormType formType;

    /**
     * <p>Set up a parcel mission. Initialize all variables.</p>
     *
     * @param colorType1
     *            <b>the colorType of the mission</b>
     * @param points
     *            <b>the points of the mission</b>
     */
    public ParcelMission(ColorType colorType1, FormType formType,int points) {
        this(colorType1, colorType1, formType, points);
    }

    /**
     * <p>Set up a parcel mission. Initialize all variables.</p>
     *
     * @param colorType1
     *            <b>the colorType of the mission</b>
     * @param colorType2
     *            <b>the second colorType of the mission</b>
     * @param points
     *            <b>the points of the mission</b>
     */
    public ParcelMission(ColorType colorType1, ColorType colorType2, FormType formType, int points) {
        super(MissionType.PARCEL,colorType1,points);
        this.colorType2 = colorType2;
        this.formType = formType;
    }

    /**
     * <p>check if a parcel mission is done, use checkFormIrrigateWithColor as a function of the form</p>
     *
     * @return <b>the number of point if the mission is done, if not , return 0</b>
     */
    public boolean checkMission(Map<Coordinate,Parcel> coordinateParcelMap, Inventory inventory) {
        return checkMission(coordinateParcelMap);
    }

    public boolean checkMission(Map<Coordinate,Parcel> coordinateParcelMap) {
        switch (formType) {
            case TRIANGLE:
                return checkOneColorForm(coordinateParcelMap, TRIANGLE.getOffsetsList());
            case LINE:
                return checkOneColorForm(coordinateParcelMap, LINE.getOffsetsList());
            case ARC:
                return checkOneColorForm(coordinateParcelMap, ARC.getOffsetsList());
            case DIAMOND:
                return checkTwoColorsForm(coordinateParcelMap, DIAMOND.getOffsetsList1(),DIAMOND.getOffsetsList2());
            default:
                return false;
        }
    }

    private boolean checkTwoColorsForm(Map<Coordinate, Parcel> coordinateParcelMap, List<Coordinate> coordinateList1, List<Coordinate> coordinateList2) {
        for (Coordinate coordinate : coordinateParcelMap.keySet())
            if (checkBasicForm(coordinateParcelMap, colorType,Coordinate.coordinatesOfOffsets(coordinate,coordinateList1)))
                if (checkBasicForm(coordinateParcelMap, colorType2,Coordinate.coordinatesOfOffsets(coordinate,coordinateList2)))
                    return true;
        return false;
    }

    private boolean checkOneColorForm(Map<Coordinate, Parcel> coordinateParcelMap, List<Coordinate> coordinateList) {
        for (Coordinate coordinate : coordinateParcelMap.keySet())
            if (checkBasicForm(coordinateParcelMap, colorType,Coordinate.coordinatesOfOffsets(coordinate,coordinateList)))
                return true;
        return false;
    }

    private boolean checkBasicForm(Map<Coordinate, Parcel> coordinateParcelMap, ColorType colorType, List<Coordinate> coordinateList){
        int correctParcels = 0;
        for (Coordinate coordinate : coordinateList)
            if (coordinateParcelMap.containsKey(coordinate))
                if (coordinateParcelMap.get(coordinate).getIrrigated() && coordinateParcelMap.get(coordinate).getColor().equals(colorType))
                    correctParcels++;
        return correctParcels == coordinateList.size();
    }

    public ColorType getColorType1() {
        return colorType;
    }

    public ColorType getColorType2() {
        return colorType2;
    }

    public FormType getFormType() {
        return formType;
    }
}