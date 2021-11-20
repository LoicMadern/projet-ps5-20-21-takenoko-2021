package fr.unice.polytech.startingpoint.game;

import fr.unice.polytech.startingpoint.exception.BadCoordinateException;
import fr.unice.polytech.startingpoint.exception.IllegalTypeException;
import fr.unice.polytech.startingpoint.exception.OutOfResourcesException;
import fr.unice.polytech.startingpoint.exception.RulesViolationException;
import fr.unice.polytech.startingpoint.game.board.BoardRules;
import fr.unice.polytech.startingpoint.game.board.Coordinate;
import fr.unice.polytech.startingpoint.game.board.Parcel;
import fr.unice.polytech.startingpoint.game.board.ParcelInformation;
import fr.unice.polytech.startingpoint.game.mission.Mission;
import fr.unice.polytech.startingpoint.game.mission.PandaMission;
import fr.unice.polytech.startingpoint.game.mission.ParcelMission;
import fr.unice.polytech.startingpoint.game.mission.PeasantMission;
import fr.unice.polytech.startingpoint.game.playerdata.PlayerData;
import fr.unice.polytech.startingpoint.type.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe permettant au bot d'interagir avec le jeu
 * @author Manuel Enzo
 * @author Naud Eric
 * @author Madern Loic
 * @author Le Calloch Antoine
 * @version 0.10
 */

public class GameInteraction {
    private final int NB_MAX_MISSION = 5;
    private final Game game;

    GameInteraction(Game game){
        this.game = game;
    }

    private PlayerData getPlayerData() {
        return game.getPlayerData();
    }

    /**
     * <p>Allow the bot to draw a canal, when it's the deck canal is not empty</p>
     */
    public void drawCanal() {
        if (getPlayerData().add(ActionType.DRAW_CANAL)){
            getPlayerData().looseStamina();
            getPlayerData().addCanal(game.getResource().drawCanal());
        }
        else
            throw new RulesViolationException("Already used this method.");
    }

    /**
     * <p>Allow the bot to use the cloud Action</p>
     *
     * @param improvementType, weatherType
     *                         <p> The improvement is the one that's the player wants</p>
     *                         <p> The weather is the one that's the player wants if there is no more improvement in the deck </p>
     *
     * @return <p>if the weather has been changed return true</p>
     */
    public boolean cloudAction(ImprovementType improvementType,WeatherType weatherType){
        if(game.getPlayerData().add(ActionType.WEATHER) && !(getResourceSize(ResourceType.ALL_IMPROVEMENT) == 0) ) {
            drawImprovement(improvementType);
            return true;
        }
        chooseWeather(WeatherType.CLOUD,weatherType);
        return false;
    }

    /**
     * <p>Allow the bot to place an imrpovement on a parcel</p>
     *
     * @param improvementType, coordinate
     *                         <p> The improvement is the one that the player has selected</p>
     *                         <p> The coordinate is where the player to place the improvement </p>

     */
    public void placeImprovement(ImprovementType improvementType, Coordinate coordinate){
        if (getPlayerData().add(ActionType.PLACE_IMPROVEMENT)){
            if (game.getBoard().isPlacedParcel(coordinate) &&
                    game.getBoard().getPlacedParcels().get(coordinate).getParcelInformation().getImprovementType().equals(ImprovementType.NOTHING)) {
                if (getPlayerData().subImprovementType(improvementType))
                    game.getBoard().getPlacedParcels().get(coordinate).setImprovementType(improvementType);
                else{
                    getPlayerData().remove(ActionType.PLACE_PARCEL);
                    throw new OutOfResourcesException("No improvement " + improvementType.toString() + " was found in the inventory.");
                }
            }
            else{
                getPlayerData().remove(ActionType.PLACE_PARCEL);
                throw new RulesViolationException("The parcel with the coordinate : " + coordinate.toString() + " is not placed.");
            }
        }
        else
            throw new RulesViolationException("Already used this method.");
    }

    /**
     * <p>Allow the bot to use the question mark Action</p>
     *
     * @param weatherType
     *                         <p> The weather is the one that will be change</p>
     *
     */
    public void questionMarkAction(WeatherType weatherType){
        chooseWeather(WeatherType.QUESTION_MARK,weatherType);
    }

    /**
     * <p>Allow the bot to change the weather</p>
     *
     * @param weatherType,weatherTypeChosen
     *                         <p> The weatherType is the ones which allow to change the weather</p>
     *                         <p> The weatherTypeChosen will be the weather for the game</p>
     *
     */
    public void chooseWeather(WeatherType weatherType, WeatherType weatherTypeChosen){
        if(weatherType.equals(WeatherType.QUESTION_MARK) && !weatherTypeChosen.equals(WeatherType.QUESTION_MARK)){
            game.getPlayerData().botPlay(weatherTypeChosen);//question mark can't obtain question mark weather
        }
        else if(weatherType.equals(WeatherType.CLOUD) && !(weatherTypeChosen.equals(WeatherType.CLOUD) || weatherTypeChosen.equals(WeatherType.QUESTION_MARK))) {
            game.getPlayerData().botPlay(weatherTypeChosen);//cloud weather can't obtain weather question mark or cloud
        }
        else{
            throw new RulesViolationException("You can't choose" + weatherTypeChosen +" weather" );
        }
    }

    /**
     * <p>Allow the bot to draw an Improvement</p>
     *
     * @param improvementType
     *                         <p> The improvementType is the one that the bot wants to draw</p>
     *
     *
     */
    public void drawImprovement(ImprovementType improvementType){
        getPlayerData().addImprovement(game.getResource().drawImprovement(improvementType));
    }

    /**
     * <p>Allow the bot to draw a Mission</p>
     *
     * @param missionType
     *                         <p> The missionType is the one that the bot wants to draw</p>
     *
     *
     */
    public void drawMission(MissionType missionType) {
        if (getPlayerData().add(ActionType.DRAW_MISSION))
            if (getPlayerData().getMissionsSize() <= NB_MAX_MISSION){
                getPlayerData().looseStamina();
                switch (missionType){
                    case PANDA:
                        getPlayerData().addMission(game.getResource().drawPandaMission());
                        break;
                    case PARCEL:
                        getPlayerData().addMission(game.getResource().drawParcelMission());
                        break;
                    case PEASANT:
                        getPlayerData().addMission(game.getResource().drawPeasantMission());
                        break;
                }
            }
            else
                throw new RulesViolationException("You already have five missions in your inventory.");
        else
            throw new RulesViolationException("Already used this method.");
    }

    /**
     * <p>Allow the bot to draw a Parcel</p>
     *
     * @return <p>return the information of each parcel in the selection </p>
     */
    public List<ParcelInformation> drawParcels() {
        if (getPlayerData().add(ActionType.DRAW_PARCELS)){
            getPlayerData().looseStamina();
            getPlayerData().saveParcels(game.getResource().drawParcels());
            List<ParcelInformation> parcelInformationList = new ArrayList<>();
            for(Parcel parcel : getPlayerData().getParcelsSaved())
                parcelInformationList.add(parcel.getParcelInformation());
            return parcelInformationList;
        }
        else
            throw new RulesViolationException("Already used this method.");
    }

    /**
     * <p>Allow the bot to select parcel among 3 parcels proposed</p>
     *
     * @param parcelInformation
     *          <p>parcelInformation allows to distinguish</p>
     *
     */
    public void selectParcel(ParcelInformation parcelInformation){
        if (getPlayerData().add(ActionType.SELECT_PARCEL))
            if (getPlayerData().contains(ActionType.DRAW_PARCELS)) {
                for (Parcel parcel : getPlayerData().getParcelsSaved())
                    if (parcel.getParcelInformation() == parcelInformation) {
                        game.getResource().selectParcel(parcel);
                        getPlayerData().saveParcel(parcel);
                        return;
                    }
                throw new RulesViolationException("Wrong Parcel asked.");
            }
            else
                throw new RulesViolationException("You haven’t drawn.");
        else
            throw new RulesViolationException("Already used this method.");
    }

    /**
     * <p>Allow the bot to do the action thunderstorm from the weather</p>
     *
     * @param coordinate
     *          <p>Move the panda at the coordinate passed</p>
     *
     */
    public void thunderstormAction(Coordinate coordinate){
        if(getPlayerData().add(ActionType.WEATHER)){
             if(getPlacedCoordinates().contains(coordinate))
                 game.getBoard().moveCharacter(CharacterType.PANDA,coordinate);
             else
                 throw new BadCoordinateException("The character can't move to this coordinate : " + coordinate.toString());
        }
        else
            throw new RulesViolationException("Already used this method.");
    }

    /**
     * <p>Allow the bot to do the action rain from the weather</p>
     *
     * @param coordinate
     *          <p>Add a bamboo at a parcel chosen</p>
     *
     */
    public void rainAction(Coordinate coordinate){
        if(getPlayerData().add(ActionType.WEATHER)){
            if (game.getBoard().isPlacedAndIrrigatedParcel(coordinate))
                    game.getBoard().getPlacedParcels().get(coordinate).addBamboo();
            else
                throw new BadCoordinateException("The parcel with the coordinate : " + coordinate.toString() + " is not placed.");
        }
        else
            throw new RulesViolationException("Already used this method.");
    }

    public void useSun(){
        getPlayerData().add(ActionType.SUN);
    }

    public void useWind(){
        getPlayerData().add(ActionType.WIND);
    }

    /**
     * <p>Allow the bot to place a parcel </p>
     *
     * @param coordinate
     *          <p>Place at the coordinate given if it's possible</p>
     *
     */
    public void placeParcel(Coordinate coordinate){
        if (getPlayerData().add(ActionType.PLACE_PARCEL)){
            if (getPlayerData().contains(ActionType.DRAW_PARCELS) && getPlayerData().contains(ActionType.SELECT_PARCEL))
                if(game.getRules().isPlayableParcel(coordinate)){
                    game.getBoard().placeParcel(getPlayerData().getParcel(),coordinate);
                }
                else
                    throw new BadCoordinateException("The parcel can't be place on this coordinate : " + coordinate.toString());
            else
                throw new RulesViolationException("You haven’t drawn or selected a parcel.");
        }
        else
            throw new RulesViolationException("Already used this method.");
    }

    /**
     * <p>Allow the bot to place a canal </p>
     *
     * @param coordinate1,coordinate2
     *          <p>Place a canal at the coordinates given</p>
     *
     */
    public void placeCanal(Coordinate coordinate1, Coordinate coordinate2){
        if (getPlayerData().add(ActionType.PLACE_CANAL)) {
            if (game.getRules().isPlayableCanal(coordinate1, coordinate2))
                game.getBoard().placeCanal(getPlayerData().pickCanal(), coordinate1, coordinate2);
            else
                throw new BadCoordinateException("The canal can't be place on these coordinates : " + coordinate1.toString() + " " + coordinate2.toString());
        }
        else
            throw new RulesViolationException("Already used this method.");
    }

    /**
     * <p>Allow the bot to move a character when it's possible </p>
     *
     * @param characterType,coordinate
     *          <p>Move a characterType given at the coordinates given</p>
     *
     */
    public void moveCharacter(CharacterType characterType, Coordinate coordinate){
        ColorType ColorBambooEat;
        if (getPlayerData().add(ActionType.getCharacterAction(characterType))) {
            if (game.getRules().isMovableCharacter(characterType, coordinate)) {
                getPlayerData().looseStamina();
                if((ColorBambooEat = game.getBoard().moveCharacter(characterType, coordinate)) != null)
                    getPlayerData().addBamboo(ColorBambooEat);
            }
            else{
                getPlayerData().remove(ActionType.getCharacterAction(characterType));
                throw new BadCoordinateException("The character can't move to this coordinate : " + coordinate.toString());
            }
        }
        else
            throw new RulesViolationException("Already used this method.");
    }

    /**
     * <h1><u>BOT GETTERS</u></h1>
     */

    public List<ImprovementType> getInventoryImprovementTypes(){
        return getPlayerData().getImprovementTypes();
    }

    public List<Coordinate> getAllParcelsIrrigated(){
        return getPlacedCoordinates()
                .stream()
                .filter(this::isPlacedAndIrrigatedParcel)
                .collect(Collectors.toList());
    }

    public int getNumberMissionsDone(){
        return getPlayerData().getMissionsDone();
    }

    public boolean isPlacedParcel(Coordinate coordinate) {
        return game.getBoard().isPlacedParcel(coordinate);
    }

    public boolean isPlacedAndIrrigatedParcel(Coordinate coordinate) {
        return game.getBoard().isPlacedAndIrrigatedParcel(coordinate);
    }

    public ParcelInformation getPlacedParcelInformation(Coordinate coordinate) {
        return game.getBoard().getPlacedParcels().get(coordinate).getParcelInformation();
    }

    public int getPlacedParcelsNbBamboo(Coordinate coordinate) {
        return game.getBoard().getPlacedParcels().get(coordinate).getNbBamboo();
    }

    public List<ActionType> getActionTypeList() {
        return game.getPlayerData().getActionTypeList();
    }

    public boolean contains(ActionType action){
        return game.getPlayerData().contains(action);
    }

    public int getStamina(){
        return game.getPlayerData().getStamina();
    }

    public BoardRules getRules(){
        return  game.getRules();
    }

    public List<Coordinate> getPlacedCoordinates(){
        return new ArrayList<>(game.getBoard().getPlacedParcels().keySet());
    }

    public List<Coordinate> getPlacedCoordinatesByColor(ColorType color){
        return (getPlacedCoordinates()
               .stream()
               .filter(coordinate -> getPlacedParcelInformation(coordinate).getColorType().equals(color))
               .collect(Collectors.toList()));
    }

    public List<Coordinate> getPlacedCoordinatesByParcelInformation(ParcelInformation parcelInformation){
        return (getPlacedCoordinates()
                .stream()
                .filter(coordinate -> getPlacedParcelInformation(coordinate).equals(parcelInformation))
                .collect(Collectors.toList()));
    }

    /**
     * @return <b>The {@link ParcelMission} list of the current bot.</b>
     */
    public List<ParcelMission> getInventoryParcelMissions(){
        return getPlayerData().getParcelMissions().stream().map(parcelMission -> (ParcelMission) parcelMission).collect(Collectors.toList());
    }

    /**
     * @return <b>The {@link PandaMission} list of the current bot.</b>
     */
    public List<PandaMission> getInventoryPandaMissions(){
        return getPlayerData().getPandaMissions().stream().map(pandaMission -> (PandaMission) pandaMission).collect(Collectors.toList());
    }

    /**
     * @return <b>The {@link PeasantMission} list of the current bot.</b>
     */
    public List<PeasantMission> getInventoryPeasantMissions(){
        return getPlayerData().getPeasantMissions().stream().map(peasantMission -> (PeasantMission) peasantMission).collect(Collectors.toList());
    }

    /**
     * @return <b>The {@link Mission} list of the current bot.</b>
     */
    public List<Mission> getInventoryMissions(){
        return getPlayerData().getMissions();
    }

    public int getMissionsSize(){
        return getPlayerData().getMissionsSize();
    }

    public int[] getInventoryBamboo() {
        return getPlayerData().getInventoryBamboo();
    }

    public int getMissionsPandaDone(){
        return getPlayerData().getMissionsPandaDone();
    }

    public int getMissionsParcelDone() {
        return getPlayerData().getMissionsParcelDone();
    }

    public int getMissionsPeasantDone() {
        return getPlayerData().getMissionsPeasantDone();
    }

    public int getResourceSize(ResourceType resourceType){
        switch (resourceType){
            case PEASANT_MISSION:
                return game.getResource().getDeckPeasantMission().size();
            case PANDA_MISSION:
                return game.getResource().getDeckPandaMission().size();
            case PARCEL_MISSION:
                return game.getResource().getDeckParcelMission().size();
            case PARCEL:
                return game.getResource().getDeckParcel().size();
            case CANAL:
                return game.getResource().getDeckCanal().size();
            case ALL_IMPROVEMENT:
                return game.getResource().getDeckImprovementType().size();
            case ENCLOSURE_IMPROVEMENT:
                return (int) game.getResource().getDeckImprovementType().stream()
                        .filter(improvementType -> improvementType.equals(ImprovementType.ENCLOSURE)).count();
            case WATERSHED_IMPROVEMENT:
                return (int) game.getResource().getDeckImprovementType().stream()
                        .filter(improvementType -> improvementType.equals(ImprovementType.WATERSHED)).count();
            case FERTILIZER_IMPROVEMENT:
                return (int) game.getResource().getDeckImprovementType().stream()
                        .filter(improvementType -> improvementType.equals(ImprovementType.FERTILIZER)).count();
            case ALL_MISSION:
                return game.getResource().getNbMission();
            default:
                throw new IllegalTypeException("Wrong ResourceType.");
        }
    }

    public int getNumberPlayers() {
        return game.getNumberPlayers();
    }
}