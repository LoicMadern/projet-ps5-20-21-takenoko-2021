package fr.unice.polytech.startingpoint.game.playerdata;

import fr.unice.polytech.startingpoint.bot.Bot;
import fr.unice.polytech.startingpoint.game.board.Canal;
import fr.unice.polytech.startingpoint.game.board.Coordinate;
import fr.unice.polytech.startingpoint.game.board.Parcel;
import fr.unice.polytech.startingpoint.game.mission.Mission;
import fr.unice.polytech.startingpoint.type.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlayerData {
    private final Bot bot;
    private final BotType botType;
    private final Inventory inventory;

    public TemporaryInventory getTemporaryInventory() {
        return temporaryInventory;
    }

    private final TemporaryInventory temporaryInventory;
    private int score;
    private int missionsDone;
    private int missionsPandaDone;
    private int missionsParcelDone;
    private int missionsPeasantDone;

    public PlayerData(Bot bot){
        this.bot = bot;
        this.botType = bot.getBotType();
        inventory = new Inventory();
        temporaryInventory = new TemporaryInventory();
        score = 0;
        missionsDone = 0;
        missionsPandaDone = 0;
        missionsParcelDone = 0;
        missionsPeasantDone = 0;
    }

    public void botPlay(WeatherType weatherType) {
        resetTemporaryInventory(weatherType);
        bot.botPlay(weatherType);
        hasPlayedCorrectly();
    }

    public void checkMissions(Map<Coordinate, Parcel> coordinateParcelMap){
        List<Mission> toRemoveMissions = new ArrayList<>();

        for(Mission mission : inventory.getMissions())
            if(mission.checkMission(coordinateParcelMap,inventory)){
                addMissionDone(mission.getPoints());

                if (mission.getMissionType().equals(MissionType.PANDA))
                    missionsPandaDone ++;
                else if (mission.getMissionType().equals(MissionType.PARCEL))
                    missionsParcelDone ++;
                else if (mission.getMissionType().equals(MissionType.PEASANT))
                    missionsPeasantDone ++;

                toRemoveMissions.add(mission);
            }

        inventory.subMissions(toRemoveMissions);
    }


    public void addImprovement(ImprovementType improvementType){
        inventory.addImprovement(improvementType);
    }

    public boolean add(ActionType actionType) {
        return temporaryInventory.add(actionType);
    }

    public void remove(ActionType actionType) {
        temporaryInventory.remove(actionType);
    }

    public boolean contains(ActionType actionType) {
        return temporaryInventory.contains(actionType);
    }

    public void looseStamina(){
        temporaryInventory.looseStamina();
    }

    public void saveParcels(List<Parcel> parcelList) {
        temporaryInventory.saveParcels(parcelList);
    }

    public void saveParcel(Parcel parcel) {
        temporaryInventory.saveParcel(parcel);
    }

    public Parcel getParcel(){
        return temporaryInventory.getParcel();
    }

    public List<Parcel> getParcelsSaved() {
        return temporaryInventory.getParcelsSaved();
    }

    public void hasPlayedCorrectly() {
        temporaryInventory.hasPlayedCorrectly();
    }

    public void resetTemporaryInventory(WeatherType weatherType) {
        temporaryInventory.reset(weatherType);
    }

    public void addCanal(Canal canal) {
        inventory.addCanal(canal);
    }

    public Canal pickCanal(){
        return inventory.pickCanal();
    }

    public void addBamboo(ColorType colorType){
        inventory.addBamboo(colorType);
    }

    public void addMission(Mission mission){
        inventory.addMission(mission);
    }

    public void addMissionDone(int score){
        this.score += score;
        missionsDone++;
    }

    public void addParcelMission(){
        missionsParcelDone++;
    }

    public void addMissionDone(){
        addMissionDone(0);
    }

    public int[] getScore() {
        return new int[]{score,missionsPandaDone};
    }

    public Bot getBot() {
        return bot;
    }

    public BotType getBotType() { return botType;}

    public List<ActionType> getActionTypeList() {
        return temporaryInventory.getActionTypeList();
    }

    public int getStamina() {
        return temporaryInventory.getStamina();
    }

    public boolean isActionCouldBeDoneTwice() {
        return temporaryInventory.isActionCouldBeDoneTwice();
    }

    public WeatherType getWeatherType() {
        return temporaryInventory.getWeatherType();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public List<Mission> getParcelMissions() {
        return inventory.getParcelMissions();
    }

    public List<Mission> getPandaMissions() {
        return inventory.getPandaMissions();
    }

    public List<Mission> getPeasantMissions() {
        return inventory.getPeasantMissions();
    }

    public List<Mission> getMissions() {
        return inventory.getMissions();
    }

    public int getMissionsSize() {
        return inventory.getMissions().size();
    }

    public int getMissionsPandaDone(){
        return missionsPandaDone;
    }

    public int getMissionsParcelDone() {
        return missionsParcelDone;
    }

    public int getMissionsPeasantDone() {
        return missionsPeasantDone;
    }

    public int getMissionsDone() {
        return missionsDone;
    }

    public int[] getInventoryBamboo() {
        return inventory.getInventoryBamboo();
    }

    public List<ImprovementType> getImprovementTypes() {
        return inventory.getImprovementTypes();
    }

    public boolean subImprovementType(ImprovementType improvementType) {
        return inventory.subImprovementType(improvementType);
    }
}