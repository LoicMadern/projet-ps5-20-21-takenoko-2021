package fr.unice.polytech.startingpoint.game.playerdata;

import fr.unice.polytech.startingpoint.bot.*;
import fr.unice.polytech.startingpoint.exception.OutOfResourcesException;
import fr.unice.polytech.startingpoint.game.board.Canal;
import fr.unice.polytech.startingpoint.game.mission.Mission;
import fr.unice.polytech.startingpoint.game.mission.PandaMission;
import fr.unice.polytech.startingpoint.game.mission.ParcelMission;
import fr.unice.polytech.startingpoint.game.mission.PeasantMission;
import fr.unice.polytech.startingpoint.type.ColorType;
import fr.unice.polytech.startingpoint.type.ImprovementType;
import fr.unice.polytech.startingpoint.type.MissionType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <h1>{@link Inventory} :</h1>
 *
 * <p>This class provides an organised storage for different objects, from {@link PandaMission}, {@link ParcelMission}, {@link PeasantMission} and {@link Canal}
 * classes, for the {@link Bot},{@link PandaBot}, {@link ParcelBot} and {@link PeasantBot} classes.</p>
 *
 * @author Manuel Enzo
 * @author Naud Eric
 * @author Madern Loic
 * @author Le Calloch Antoine
 * @see Bot
 * @see PandaBot
 * @see ParcelBot
 * @see PeasantBot
 * @see RandomBot
 * @version 0.5
 */

public class Inventory {
    private final List<Mission> inventoryMission;
    private final List<Canal> inventoryCanal;
    private final List<ImprovementType>inventoryImprovement;
    private final int[] inventoryBamboo;

    /**
     * <p>Set up the Inventory. Initialize all variables.</p>
     */
    public Inventory(){
        inventoryMission = new ArrayList<>();
        inventoryImprovement=new ArrayList<>();
        inventoryCanal = new ArrayList<>();
        inventoryBamboo = new int[]{0,0,0};
    }

    public void addImprovement(ImprovementType improvementType){
        inventoryImprovement.add(improvementType);
    }

    /**<p>Add a {@link Canal} in the inventory.</p>
     */
    public void addCanal(Canal canal){
        inventoryCanal.add(canal);
    }

    /**@return <b>A canal and remove it from the inventory.</b>
     */
    public Canal pickCanal() throws OutOfResourcesException {
        if (!inventoryCanal.isEmpty()){
            return inventoryCanal.remove(0);
        }
        throw new OutOfResourcesException("No more Canal in the inventory.");
    }

    /**<p>Add one bamboo of the {@link ColorType} specified in parameter.</p>
     *
     * @param colorType
     *              <b>The {@link ColorType} of the bamboo we want to add.</b>
     */
    public void addBamboo(ColorType colorType){
        if (colorType != ColorType.NO_COLOR)
            inventoryBamboo[colorType.ordinal()] ++;
    }

    /**<p>Sub bamboo(s) of the {@link ColorType} specified in parameter.</p>
     *
     * @param colorType
     *          <b>The {@link ColorType} of the bamboo we want to remove.</b>
     * @param colorType
     *          <b>The {@link ColorType} of the bamboo we want to remove.</b>
     * @return
     */
    public void subTwoBamboos(ColorType colorType){
        if(inventoryBamboo[colorType.ordinal()] >= 2 && !colorType.equals(ColorType.NO_COLOR))
            inventoryBamboo[colorType.ordinal()] -= 2;
    }

    public void subOneBambooPerColor() {
        if (inventoryBamboo[0] > 0 && inventoryBamboo[1] > 0 && inventoryBamboo[2] > 0) {
            inventoryBamboo[ColorType.YELLOW.ordinal()]--;
            inventoryBamboo[ColorType.GREEN.ordinal()]--;
            inventoryBamboo[ColorType.RED.ordinal()]--;
        }
    }

    public void addMission(Mission mission) {
        inventoryMission.add(mission);
    }

    public void subMissions(List<Mission> missionList){
        inventoryMission.removeAll(missionList);
    }

    /**@param colorType
     *          <b>The {@link ColorType} of the bamboo we want the number from.</b>
     *
     * @return <b>The number of bamboo of the {@link ColorType} specified in parameter.</b>
     */
    public int getInventoryBamboo(ColorType colorType){
        return inventoryBamboo[colorType.ordinal()];
    }

    /**@return <b>The list containing the number of bamboos of each {@link ColorType}.</b>
     */
    public int[] getInventoryBamboo() {
        return inventoryBamboo.clone();
    }

    public List<Canal> getInventoryCanal() {
        return new ArrayList<>(inventoryCanal);
    }

    /**
     * @return <b>The list of {@link PandaMission} missions.</b>
     */
    public List<Mission> getPandaMissions(){
        return inventoryMission
                .stream()
                .filter(mission -> mission.getMissionType().equals(MissionType.PANDA))
                .collect(Collectors.toList());
    }

    /**@return <b>The list of {@link ParcelMission} missions.</b>
     */
    public List<Mission> getParcelMissions(){
        return inventoryMission
                .stream()
                .filter(mission -> mission.getMissionType().equals(MissionType.PARCEL))
                .collect(Collectors.toList());
    }

    /**
     * @return <b>The list of {@link PeasantMission} missions.</b>
     */
    public List<Mission> getPeasantMissions(){
        return inventoryMission
                .stream()
                .filter(mission -> mission.getMissionType().equals(MissionType.PEASANT))
                .collect(Collectors.toList());
    }

    /**
     * @return <b>The list of {@link Mission} missions.</b>
     */
    public List<Mission> getMissions(){
        return new ArrayList<>(inventoryMission);
    }

    /**
     * @return <b>The list of {@link ImprovementType} missions.</b>
     */
    public List<ImprovementType> getInventoryImprovement(ImprovementType improvementType){
        return inventoryImprovement.stream().filter(improvementType1 -> improvementType1.equals(improvementType))
                .collect(Collectors.toList());
    }

    public List<ImprovementType> getImprovementTypes() {
        return inventoryImprovement;
    }

    public boolean subImprovementType(ImprovementType improvementType) {
        return inventoryImprovement.remove(improvementType);
    }
}