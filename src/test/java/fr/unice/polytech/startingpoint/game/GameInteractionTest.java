package fr.unice.polytech.startingpoint.game;

import fr.unice.polytech.startingpoint.exception.BadCoordinateException;
import fr.unice.polytech.startingpoint.exception.OutOfResourcesException;
import fr.unice.polytech.startingpoint.exception.RulesViolationException;
import fr.unice.polytech.startingpoint.game.board.Canal;
import fr.unice.polytech.startingpoint.game.board.Coordinate;
import fr.unice.polytech.startingpoint.game.board.Parcel;
import fr.unice.polytech.startingpoint.game.board.ParcelInformation;
import fr.unice.polytech.startingpoint.type.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class GameInteractionTest {
    private Game game;
    private GameInteraction gameInteraction;

    @BeforeEach
    void Setup() {
        game = new Game();
        gameInteraction = game.getGameInteraction();
    }

    @Test
    void botDrawCanalLessStaminaAndAddCanalToInventory() {
        gameInteraction.drawCanal();
        assertEquals(1,game.getPlayerData().getInventory().getInventoryCanal().size());
        assertEquals(26, gameInteraction.getResourceSize(ResourceType.CANAL));
        assertEquals(1,game.getPlayerData().getStamina());
    }

    @Test
    void botDrawTooMuchCanal() {
        for (int i = 0; i < 27; i++) {
            game.getResource().drawCanal();
        }
        assertThrows(OutOfResourcesException.class,() -> gameInteraction.drawCanal());
    }

    @Test
    void botDrawTwoCanalInTheSameRound() {
        gameInteraction.drawCanal();
        assertThrows(RulesViolationException.class,() -> gameInteraction.drawCanal());
    }

    @Test
    void initializeGameInteraction() {
        assertEquals(1, gameInteraction.getInventoryParcelMissions().size());
        assertEquals(14, gameInteraction.getResourceSize(ResourceType.PARCEL_MISSION));
        assertEquals(2,game.getPlayerData().getStamina());
    }


    @Test
    void botDrawParcelMissionLessStaminaAndAddMissionToInventory() {
        gameInteraction.drawMission(MissionType.PARCEL);
        assertEquals(2, gameInteraction.getInventoryParcelMissions().size());
        assertEquals(13, gameInteraction.getResourceSize(ResourceType.PARCEL_MISSION));
        assertEquals(1,game.getPlayerData().getStamina());
    }

    @Test
    void botDrawTooMuchParcelMission() {
        for (int i = 0; i < 14; i++) {
            game.getResource().drawParcelMission();
        }
        assertThrows(OutOfResourcesException.class,() -> gameInteraction.drawMission(MissionType.PARCEL));
    }

    @Test
    void botDrawTwoParcelMissionInTheSameRound() {
        gameInteraction.drawMission(MissionType.PARCEL);
        assertThrows(RulesViolationException.class,() -> gameInteraction.drawMission(MissionType.PARCEL));
    }

    @Test
    void botDrawPandaMissionLessStaminaAndAddMissionToInventory() {
        gameInteraction.drawMission(MissionType.PANDA);
        assertEquals(2, gameInteraction.getInventoryPandaMissions().size());
        assertEquals(13, gameInteraction.getResourceSize(ResourceType.PANDA_MISSION));
        assertEquals(1,game.getPlayerData().getStamina());
    }

    @Test
    void botDrawTooMuchPandaMission() {
        for (int i = 0; i < 14; i++) {
            game.getResource().drawPandaMission();
        }
        assertThrows(OutOfResourcesException.class,() -> gameInteraction.drawMission(MissionType.PANDA));
    }

    @Test
    void botDrawTwoPandaMissionInTheSameRound() {
        gameInteraction.drawMission(MissionType.PANDA);
        assertThrows(RulesViolationException.class,() -> gameInteraction.drawMission(MissionType.PANDA));
    }

    @Test
    void botDrawPeasantMissionLessStaminaAndAddMissionToInventory() {
        gameInteraction.drawMission(MissionType.PEASANT);
        assertEquals(2, gameInteraction.getInventoryPeasantMissions().size());
        assertEquals(13, gameInteraction.getResourceSize(ResourceType.PEASANT_MISSION));
        assertEquals(1,game.getPlayerData().getStamina());
    }

    @Test
    void botDrawTooMuchPeasantMission() {
        for (int i = 0; i < 14; i++) {
            game.getResource().drawPeasantMission();
        }
        assertThrows(OutOfResourcesException.class,() -> gameInteraction.drawMission(MissionType.PEASANT));
    }

    @Test
    void botDrawTwoPeasantMissionInTheSameRound() {
        gameInteraction.drawMission(MissionType.PEASANT);
        assertThrows(RulesViolationException.class,() -> gameInteraction.drawMission(MissionType.PEASANT));
    }

    @Test
    void botDrawAndPlaceParcelLessStaminaAndAddParcelToTemporaryInventoryAndBoard() {
        assertDoesNotThrow(() -> game.getPlayerData().hasPlayedCorrectly());

        assertEquals(0,game.getPlayerData().getParcelsSaved().size());
        assertEquals(27, gameInteraction.getResourceSize(ResourceType.PARCEL));
        assertEquals(2,game.getPlayerData().getStamina());
        assertEquals(1, gameInteraction.getPlacedCoordinates().size());

        List<ParcelInformation> parcels = gameInteraction.drawParcels();

        assertThrows(NoSuchElementException.class,() -> game.getPlayerData().hasPlayedCorrectly());

        assertEquals(3,game.getPlayerData().getParcelsSaved().size());

        gameInteraction.selectParcel(parcels.get(0));

        assertThrows(NoSuchElementException.class,() -> game.getPlayerData().hasPlayedCorrectly());

        assertEquals(26, gameInteraction.getResourceSize(ResourceType.PARCEL));
        assertEquals(1,game.getPlayerData().getStamina());

        gameInteraction.placeParcel(new Coordinate(0,-1,1));

        assertDoesNotThrow(() -> game.getPlayerData().hasPlayedCorrectly());

        assertEquals(2, gameInteraction.getPlacedCoordinates().size());
    }

    @Test
    void botDrawTooMuchParcel() {
        for (int i = 0; i < 27; i++) {
            game.getResource().selectParcel(game.getResource().drawParcels().get(0));
        }
        assertThrows(OutOfResourcesException.class,() -> gameInteraction.drawParcels());
    }

    @Test
    void botDrawTwoTimesInTheSameTurn() {
        gameInteraction.drawParcels();
        assertThrows(RulesViolationException.class,() -> gameInteraction.drawParcels());
    }

    @Test
    void botSelectWithoutDrawing() {
        assertThrows(RulesViolationException.class,() -> gameInteraction.selectParcel(new ParcelInformation()));
    }

    @Test
    void botDrawWrongParcelInformation() {
        gameInteraction.drawParcels();
        assertThrows(RulesViolationException.class,() -> gameInteraction.selectParcel(new ParcelInformation()));
    }

    @Test
    void botSelectTwoTimesInTheSameTurn() {
        List<ParcelInformation> drawParcels =  gameInteraction.drawParcels();
        gameInteraction.selectParcel(drawParcels.get(0));
        assertThrows(RulesViolationException.class,() -> gameInteraction.selectParcel(drawParcels.get(1)));
    }

    @Test
    void botPlaceWithoutDrawing() {
        assertThrows(RulesViolationException.class,() -> gameInteraction.placeParcel(new Coordinate(1,-1,0)));
    }

    @Test
    void botPlaceWithoutSelecting() {
        gameInteraction.drawParcels();
        assertThrows(RulesViolationException.class,() -> gameInteraction.placeParcel(new Coordinate(1,-1,0)));
    }

    @Test
    void botPlaceTwoTimesInTheSameTurn() {
        List<ParcelInformation> drawParcels =  gameInteraction.drawParcels();
        gameInteraction.selectParcel(drawParcels.get(0));
        gameInteraction.placeParcel(new Coordinate(0,-1,1));
        assertThrows(RulesViolationException.class,() -> gameInteraction.placeParcel(new Coordinate(0,1,-1)));
    }

    @Test
    void botPlaceWrongParcelAndThenGoodAndRetryToPlace() {
        gameInteraction.selectParcel(gameInteraction.drawParcels().get(0));
        assertThrows(BadCoordinateException.class,() -> gameInteraction.placeParcel(new Coordinate(0,-2,2)));
    }

    @Test
    void botPlaceCanal() {
        game.getBoard().placeParcel(new Parcel(),new Coordinate(0,-1,1));
        game.getBoard().placeParcel(new Parcel(),new Coordinate(1,-1,0));
        gameInteraction.drawCanal();
        assertDoesNotThrow(() -> gameInteraction.placeCanal(new Coordinate(0,-1,1),new Coordinate(1,-1,0)));
    }

    @Test
    void botPlaceWrongCanalAndThenGoodAndRetryToPlace() {
        game.getBoard().placeParcel(new Parcel(),new Coordinate(0,-1,1));
        game.getBoard().placeParcel(new Parcel(),new Coordinate(1,-1,0));
        game.getBoard().placeParcel(new Parcel(),new Coordinate(1,-2,1));
        gameInteraction.drawCanal();
        game.getPlayerData().resetTemporaryInventory(WeatherType.NO_WEATHER);
        assertThrows(BadCoordinateException.class, () -> gameInteraction.placeCanal(new Coordinate(1,-1,0),new Coordinate(1,-2,1)));
    }

    @Test
    void botMovePanda(){
        game.getBoard().placeParcel(new Parcel(),new Coordinate(1,-1,0));
        assertDoesNotThrow(() -> gameInteraction.moveCharacter(CharacterType.PANDA, new Coordinate(1,-1,0)));
    }

    @Test
    void botMoveWrongPandaAndThenGoodAndRetryToMove(){
        game.getBoard().placeParcel(new Parcel(),new Coordinate(0,-1,1));
        assertThrows(BadCoordinateException.class, () -> gameInteraction.moveCharacter(CharacterType.PANDA,new Coordinate(1,-1,0)));
        assertDoesNotThrow(() -> gameInteraction.moveCharacter(CharacterType.PANDA,new Coordinate(0,-1,1)));
        assertThrows(RulesViolationException.class, () -> gameInteraction.moveCharacter(CharacterType.PANDA,new Coordinate()));
    }

    @Test
    void botMovePeasant(){
        game.getBoard().placeParcel(new Parcel(),new Coordinate(1,-1,0));
        assertDoesNotThrow(() -> gameInteraction.moveCharacter(CharacterType.PEASANT, new Coordinate(1,-1,0)));
    }

    @Test
    void botMoveWrongPeasantAndThenGoodAndRetryToMove(){
        game.getBoard().placeParcel(new Parcel(),new Coordinate(0,-1,1));
        assertThrows(BadCoordinateException.class, () -> gameInteraction.moveCharacter(CharacterType.PEASANT,new Coordinate(1,-1,0)));
        assertDoesNotThrow(() -> gameInteraction.moveCharacter(CharacterType.PEASANT,new Coordinate(0,-1,1)));
        assertThrows(RulesViolationException.class, () -> gameInteraction.moveCharacter(CharacterType.PEASANT,new Coordinate()));
    }

    @Test
    void numberMissionsDone(){
        assertEquals(0,gameInteraction.getNumberMissionsDone());
        game.getPlayerData().addMissionDone(2);
        game.getPlayerData().addMissionDone(2);
        assertEquals(2,gameInteraction.getNumberMissionsDone());
    }

    @Test
    void placedParcel(){
        game.getBoard().placeParcel(new Parcel(),new Coordinate(1,-1,0));
        game.getBoard().placeParcel(new Parcel(),new Coordinate(1,-2,1));
        assertTrue(gameInteraction.isPlacedParcel(new Coordinate()));
        assertTrue(gameInteraction.isPlacedParcel(new Coordinate(1,-1,0)));
        assertTrue(gameInteraction.isPlacedParcel(new Coordinate(1,-2,1)));
    }

    @Test
    void notPlacedParcel(){
        assertFalse(gameInteraction.isPlacedParcel(new Coordinate(1,-1,0)));
        assertFalse(gameInteraction.isPlacedParcel(new Coordinate(1,-2,1)));
    }

    @Test
    void irrigatedAndPlacedParcel(){
        game.getBoard().placeParcel(new Parcel(),new Coordinate(1,-1,0));
        game.getBoard().placeParcel(new Parcel(),new Coordinate(0,-1,1));
        game.getBoard().placeParcel(new Parcel(),new Coordinate(1,-2,1));
        game.getBoard().placeCanal(new Canal(),new Coordinate(1,-1,0),new Coordinate(0,-1,1));
        game.getBoard().placeCanal(new Canal(),new Coordinate(1,-2,1),new Coordinate(0,-1,1));
        assertTrue(gameInteraction.isPlacedAndIrrigatedParcel(new Coordinate(1,-1,0)));
        assertTrue(gameInteraction.isPlacedAndIrrigatedParcel(new Coordinate(0,-1,1)));
        assertTrue(gameInteraction.isPlacedAndIrrigatedParcel(new Coordinate(1,-2,1)));
    }

    @Test
    void notIrrigatedAndPlacedParcel(){
        game.getBoard().placeParcel(new Parcel(),new Coordinate(1,-2,1));
        assertFalse(gameInteraction.isPlacedAndIrrigatedParcel(new Coordinate(1,-1,0)));
        assertFalse(gameInteraction.isPlacedAndIrrigatedParcel(new Coordinate(1,-2,1)));
    }

    @Test
    void placedParcelInformation(){
        game.getBoard().placeParcel(new Parcel(ImprovementType.WATERSHED),new Coordinate(1,-1,0));
        game.getBoard().placeParcel(new Parcel(ColorType.RED),new Coordinate(0,-1,1));
        game.getBoard().placeParcel(new Parcel(ColorType.GREEN,ImprovementType.ENCLOSURE),new Coordinate(1,-2,1));
        assertEquals(new ParcelInformation(), gameInteraction.getPlacedParcelInformation(new Coordinate()));
        assertEquals(new ParcelInformation(ImprovementType.WATERSHED), gameInteraction.getPlacedParcelInformation(new Coordinate(1,-1,0)));
        assertEquals(new ParcelInformation(ColorType.RED), gameInteraction.getPlacedParcelInformation(new Coordinate(0,-1,1)));
        assertEquals(new ParcelInformation(ColorType.GREEN,ImprovementType.ENCLOSURE), gameInteraction.getPlacedParcelInformation(new Coordinate(1,-2,1)));
    }

    @Test
    void actionTypeList(){
        assertEquals(gameInteraction.getActionTypeList().size(),0);
        gameInteraction.drawCanal();
        assertEquals(gameInteraction.getActionTypeList().size(),1);
        gameInteraction.drawParcels();
        assertEquals(gameInteraction.getActionTypeList().size(),2);
    }

    @Test
    void testGetPlacedCoordinatesByColor(){
        game.getBoard().placeParcel(new Parcel(ColorType.RED),new Coordinate(0,-1,1));
        game.getBoard().placeParcel(new Parcel(ColorType.GREEN),new Coordinate(1,-1,0));
        ArrayList<Coordinate> placedCoordinatesByRedColor= (ArrayList<Coordinate>) gameInteraction.getPlacedCoordinatesByColor(ColorType.RED);
        ArrayList<Coordinate> placedCoordinatesByBlueColor= (ArrayList<Coordinate>) gameInteraction.getPlacedCoordinatesByColor(ColorType.GREEN);
        assertEquals(new Coordinate(0,-1,1),placedCoordinatesByRedColor.get(0));
        assertEquals(new Coordinate(1,-1,0),placedCoordinatesByBlueColor.get(0));
        assertNotEquals(new Coordinate(1,-1,0),placedCoordinatesByRedColor.get(0));
        assertNotEquals(new Coordinate(0,-1,1),placedCoordinatesByBlueColor.get(0));
    }

    @Test
    void weatherCantBePlayedTwice(){
        game.getPlayerData().add(ActionType.WEATHER);
        assertThrows(RulesViolationException.class, () ->  gameInteraction.thunderstormAction(new Coordinate(1,-1,0)));
        assertThrows(RulesViolationException.class, () ->  gameInteraction.rainAction(new Coordinate(1,-1,0)));
        //météo utilisée 2 fois
    }

    @Test
    void thunderstruckCantBeProperlyPLayed(){
        assertThrows(BadCoordinateException.class, () ->  gameInteraction.thunderstormAction(new Coordinate(1,-1,0)));
        //La parcelle n'est pas posée donc le panda ne peut pas s'y déplacer
    }

    @Test
    void rainCantBeProperlyPLayed(){
        assertThrows(BadCoordinateException.class, () ->  gameInteraction.rainAction(new Coordinate(1,-1,0)));
        //La parcelle n'est pas posée
    }

    @Test
    void changeMeteoWithCloud(){
        for(int i=0;i<3;i++) {
            gameInteraction.drawImprovement(ImprovementType.ENCLOSURE);
            gameInteraction.drawImprovement(ImprovementType.FERTILIZER);
            gameInteraction.drawImprovement(ImprovementType.WATERSHED);
        }
        assertFalse(gameInteraction.cloudAction(ImprovementType.ENCLOSURE,WeatherType.SUN));
        assertEquals(WeatherType.SUN,game.getPlayerData().getTemporaryInventory().getWeatherType());
    }

    @Test
    void changeMeteoWithQuestionMark(){
        assertNotEquals(WeatherType.SUN,game.getPlayerData().getWeatherType());
        gameInteraction.questionMarkAction(WeatherType.SUN);
        assertEquals(WeatherType.SUN,game.getPlayerData().getWeatherType());
    }

    @Test
    void chooseWeatherQuestionMarkForbidden(){
        assertThrows(RulesViolationException.class, () ->  game.getGameInteraction().chooseWeather(WeatherType.QUESTION_MARK,WeatherType.QUESTION_MARK));
        assertThrows(RulesViolationException.class, () ->  game.getGameInteraction().chooseWeather(WeatherType.CLOUD,WeatherType.QUESTION_MARK));
    }
    @Test
    void chooseWeatherCloudForbiddenWithCloud(){
        assertThrows(RulesViolationException.class, () ->  game.getGameInteraction().chooseWeather(WeatherType.CLOUD,WeatherType.CLOUD));
    }
  @Test
    void chooseWeatherWithOtherWeatherThanCloudOrQuestionMark(){
        assertThrows(RulesViolationException.class, () ->  game.getGameInteraction().chooseWeather(WeatherType.RAIN,WeatherType.THUNDERSTORM));
        assertThrows(RulesViolationException.class, () ->  game.getGameInteraction().chooseWeather(WeatherType.WIND,WeatherType.SUN));
        assertThrows(RulesViolationException.class, () ->  game.getGameInteraction().chooseWeather(WeatherType.THUNDERSTORM,WeatherType.CLOUD));
        assertThrows(RulesViolationException.class, () ->  game.getGameInteraction().chooseWeather(WeatherType.SUN,WeatherType.WIND));
    }







}