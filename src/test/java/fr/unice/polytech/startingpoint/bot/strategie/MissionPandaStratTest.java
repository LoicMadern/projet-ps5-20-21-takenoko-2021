package fr.unice.polytech.startingpoint.bot.strategie;


import fr.unice.polytech.startingpoint.game.Game;
import fr.unice.polytech.startingpoint.game.GameInteraction;
import fr.unice.polytech.startingpoint.game.board.Coordinate;
import fr.unice.polytech.startingpoint.game.board.Parcel;
import fr.unice.polytech.startingpoint.game.board.ParcelInformation;
import fr.unice.polytech.startingpoint.game.mission.Mission;
import fr.unice.polytech.startingpoint.game.mission.PandaMission;
import fr.unice.polytech.startingpoint.type.ActionType;
import fr.unice.polytech.startingpoint.type.CharacterType;
import fr.unice.polytech.startingpoint.type.ColorType;
import fr.unice.polytech.startingpoint.type.ImprovementType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MissionPandaStratTest {
    private Game game;
    private MissionPandaStrat missionPandaStrat;
    private Mission missionGreenOneColor;
    private Mission missionAllColor;

    @BeforeEach
    void setUp() {
        game = new Game(); // bot random
        missionPandaStrat = new MissionPandaStrat(game.getGameInteraction());
        missionGreenOneColor = new PandaMission(ColorType.GREEN, 0);
        missionAllColor = new PandaMission(ColorType.ALL_COLOR, 0);

    }

    /**
     * <h1><u>strategieMissionOneColor</u></h1>
     */


    @Test
    void strategieMissionOneColor() {
        Coordinate coordinate = new Coordinate(1, -1, 0);
        Parcel parcel = new Parcel(ColorType.GREEN, ImprovementType.NOTHING);
        game.getBoard().placeParcel(parcel, coordinate);
        assertEquals(coordinate, missionPandaStrat.strategyMovePanda(ColorType.GREEN));
    }

    @Test
    void strategieMissionOneColorNoParcel() {
        assertNull(missionPandaStrat.strategyMovePanda(ColorType.GREEN));
    }

    @Test
    void strategieMissionOneColorNoParcelWithNoBamboo() {
        Coordinate coordinate = new Coordinate(1, -1, 0);
        Parcel parcel = new Parcel(ColorType.GREEN, ImprovementType.NOTHING);
        game.getBoard().placeParcel(parcel, coordinate);
        game.getBoard().moveCharacter(CharacterType.PANDA, coordinate);
        assertNull(missionPandaStrat.strategyMovePanda(ColorType.GREEN));
    }

    @Test
    void strategieMissionOneColorNoParcelWithoutImprovementInclosure() {
        Coordinate coordinate = new Coordinate(1, -1, 0);
        Parcel parcel = new Parcel(ColorType.GREEN, ImprovementType.ENCLOSURE);
        game.getBoard().placeParcel(parcel, coordinate);
        assertNull(missionPandaStrat.strategyMovePanda(ColorType.GREEN));
    }

    @Test
    void strategieMissionOneColorNoParcelWithGoodColor() {
        Coordinate coordinate = new Coordinate(1, -1, 0);
        Parcel parcel = new Parcel(ColorType.RED, ImprovementType.NOTHING);
        game.getBoard().placeParcel(parcel, coordinate);
        assertNull(missionPandaStrat.strategyMovePanda(ColorType.GREEN));
    }

    /**
     * <h1><u>strategieMissionAllColor</u></h1>
     */

    @Test
    void strategyMissionAllColor() {
        Coordinate coordinate = new Coordinate(1, -1, 0);
        Parcel parcel = new Parcel(ColorType.GREEN, ImprovementType.NOTHING);
        game.getBoard().placeParcel(parcel, coordinate);
        assertEquals(coordinate, missionPandaStrat.strategyMovePanda(ColorType.ALL_COLOR));
    }

    @Test
    void strategyMissionAllColorNoParcel() {
        assertNull(missionPandaStrat.strategyMovePanda(ColorType.ALL_COLOR));
    }

    @Test
    void strategyMissionAllColorNoParcelWithBamboo() {
        Coordinate coordinate = new Coordinate(1, -1, 0);
        Parcel parcel = new Parcel(ColorType.GREEN, ImprovementType.NOTHING);
        game.getBoard().placeParcel(parcel, coordinate);
        game.getBoard().moveCharacter(CharacterType.PANDA, coordinate);
        assertNull(missionPandaStrat.strategyMovePanda(ColorType.ALL_COLOR));

    }

    @Test
    void strategyMissionAllColorNoParcelWithoutEnclosure() {
        Coordinate coordinate = new Coordinate(1, -1, 0);
        Parcel parcel = new Parcel(ColorType.GREEN, ImprovementType.ENCLOSURE);
        game.getBoard().placeParcel(parcel, coordinate);
        assertNull(missionPandaStrat.strategyMovePanda(ColorType.ALL_COLOR));

    }

    @Test
    void strategyMissionAllColorNoParcelWithColorThatBotDontHave() {
        game.getPlayerData().addBamboo(ColorType.GREEN);
        Coordinate coordinate = new Coordinate(1, -1, 0);
        Parcel parcel = new Parcel(ColorType.GREEN, ImprovementType.NOTHING);
        game.getBoard().placeParcel(parcel, coordinate);
        assertNull(missionPandaStrat.strategyMovePanda(ColorType.ALL_COLOR));
    }

    @Test
    void coordWhereMovePanda_FirstParcelWithBamboo() {
        Coordinate coordParcel1 = new Coordinate(1, -1, 0);//parcel entre 2-4h
        Coordinate coordParcel2 = new Coordinate(0, -1, 1);//parcel entre 4-6h
        Coordinate coordParcel3 = new Coordinate(1, -2, 1);//parcel a 4h éloigné de 1
        Coordinate coordParcel4 = new Coordinate(0, -2, 2);//parcel a 5h éloigné de 1
        game.getBoard().placeParcel(new Parcel(ColorType.GREEN), coordParcel1);//place la parcel (un bamboo pousse)
        game.getBoard().placeParcel(new Parcel(ColorType.GREEN), coordParcel2);//place la parcel (un bamboo pousse)
        game.getBoard().placeParcel(new Parcel(ColorType.GREEN), coordParcel3);//place la parcel (aucun bamboo pour car éloigné du centre donc pas irrigé)
        game.getBoard().placeParcel(new Parcel(ColorType.GREEN), coordParcel4);//place la parcel (aucun bamboo pour car éloigné du centre donc pas irrigé)
        game.getPlayerData().addMission(new PandaMission(ColorType.GREEN, 1));
        assertEquals(coordParcel1, missionPandaStrat.strategyMovePanda(ColorType.ALL_COLOR));
    }


    /**
     * <h1><u>nbMoveOneColor</u></h1>
     */

    @Test
    void nbMoveOneColorColorInInvetoryAndParcelPlaced() {
        game.getPlayerData().addBamboo(ColorType.GREEN);

        Coordinate coordinate = new Coordinate(1, -1, 0);
        Parcel parcel = new Parcel(ColorType.GREEN, ImprovementType.NOTHING);
        game.getBoard().placeParcel(parcel, coordinate); //place une parcelle

        assertEquals(1, missionPandaStrat.howManyMoveToDoMission(missionGreenOneColor));
    }

    @Test
    void nbMoveOneColorColorInInvetory() {
        game.getPlayerData().addBamboo(ColorType.GREEN);
        assertEquals(-1, missionPandaStrat.howManyMoveToDoMission(missionGreenOneColor));
    }

    @Test
    void nbMoveOneColorColorParcelPlaced() {
        Coordinate coordinate = new Coordinate(1, -1, 0);
        Parcel parcel = new Parcel(ColorType.GREEN, ImprovementType.NOTHING);
        game.getBoard().placeParcel(parcel, coordinate); //place une parcelle

        assertEquals(3, missionPandaStrat.howManyMoveToDoMission(missionGreenOneColor));
    }

    @Test
    void nbMoveOneColorColorNoBambooInInventoryNoParcelPlaced() {
        assertEquals(-1, missionPandaStrat.howManyMoveToDoMission(missionGreenOneColor));
    }

    /**
     * <h1><u>nbMoveAllColor</u></h1>
     */

    @Test
    void nbMoveAllColorOneBambooInInventory() {
        game.getPlayerData().addBamboo(ColorType.GREEN);
        assertEquals(-1, missionPandaStrat.howManyMoveToDoMission(missionAllColor));
    }

    @Test
    void nbMoveAllColorTwoBambooInInventory() {
        game.getPlayerData().addBamboo(ColorType.GREEN);
        game.getPlayerData().addBamboo(ColorType.RED);
        assertEquals(-1, missionPandaStrat.howManyMoveToDoMission(missionAllColor));
    }

    @Test
    void nbMoveAllColorOneBambooInInventoryOneParcelPlaced() {
        game.getPlayerData().addBamboo(ColorType.GREEN);

        Coordinate coordinate = new Coordinate(1, -1, 0);
        Parcel parcel = new Parcel(ColorType.RED, ImprovementType.NOTHING);
        game.getBoard().placeParcel(parcel, coordinate); //place une parcelle

        assertEquals(4, missionPandaStrat.howManyMoveToDoMission(missionAllColor));
    }

    @Test
    void nbMoveAllColorOneBambooInInventoryOneParcelPlacedSameColor() {
        game.getPlayerData().addBamboo(ColorType.GREEN);

        Coordinate coordinate = new Coordinate(1, -1, 0);
        Parcel parcel = new Parcel(ColorType.GREEN, ImprovementType.NOTHING);
        game.getBoard().placeParcel(parcel, coordinate); //place une parcelle

        assertEquals(-1, missionPandaStrat.howManyMoveToDoMission(missionAllColor));
    }

    /**
     * <h1><u>nbMoveAllColor</u></h1>
     */

    @Test
    void isAlreadyFinishedIsFinishMissionOneColor() {
        game.getPlayerData().addBamboo(ColorType.GREEN);
        game.getPlayerData().addBamboo(ColorType.GREEN);
        assertTrue(missionPandaStrat.isAlreadyFinished(missionGreenOneColor));
    }

    @Test
    void isAlreadyFinishedTooManyBambooMissionOneColor() {
        game.getPlayerData().addBamboo(ColorType.GREEN);
        game.getPlayerData().addBamboo(ColorType.GREEN);
        game.getPlayerData().addBamboo(ColorType.GREEN);
        assertTrue(missionPandaStrat.isAlreadyFinished(missionGreenOneColor));
    }

    @Test
    void isAlreadyFinishedNotEnoughBambooMissionOneColor() {
        game.getPlayerData().addBamboo(ColorType.GREEN);
        assertFalse(missionPandaStrat.isAlreadyFinished(missionGreenOneColor));
    }

    @Test
    void isAlreadyFinishedNotGoodColorMissionOneColor() {
        game.getPlayerData().addBamboo(ColorType.RED);
        game.getPlayerData().addBamboo(ColorType.RED);
        assertFalse(missionPandaStrat.isAlreadyFinished(missionGreenOneColor));
    }

    @Test
    void isAlreadyFinishedIsFinishMissionAllColor() {
        game.getPlayerData().addBamboo(ColorType.GREEN);
        game.getPlayerData().addBamboo(ColorType.RED);
        game.getPlayerData().addBamboo(ColorType.YELLOW);
        assertTrue(missionPandaStrat.isAlreadyFinished(missionAllColor));
    }

    @Test
    void isAlreadyFinishedTooManyBambooMissionAllColor() {
        game.getPlayerData().addBamboo(ColorType.GREEN);
        game.getPlayerData().addBamboo(ColorType.GREEN);
        game.getPlayerData().addBamboo(ColorType.RED);
        game.getPlayerData().addBamboo(ColorType.YELLOW);
        assertTrue(missionPandaStrat.isAlreadyFinished(missionAllColor));
    }

    @Test
    void isAlreadyFinishedNotEnoughBambooMissionAllColor() {
        game.getPlayerData().addBamboo(ColorType.GREEN);
        game.getPlayerData().addBamboo(ColorType.RED);
        assertFalse(missionPandaStrat.isAlreadyFinished(missionAllColor));
    }


    /**
     * <h1><u>isFinishedInOneTurn</u></h1>
     */

    @Test
    void isFinishedInOneTurnOneBambooInInventoryPlacedParcelWithGoodColor() {
        game.getPlayerData().addBamboo(ColorType.GREEN);

        Coordinate coordinate = new Coordinate(1, -1, 0);
        Parcel parcel = new Parcel(ColorType.GREEN, ImprovementType.NOTHING);
        game.getBoard().placeParcel(parcel, coordinate); //place une parcelle

        assertTrue(missionPandaStrat.isFinishedInOneTurn(missionGreenOneColor));
    }

    @Test
    void isFinishedInOneTurnOneBambooInInventoryPlacedParcelWithAllColor() {
        game.getPlayerData().addBamboo(ColorType.YELLOW);
        game.getPlayerData().addBamboo(ColorType.RED);

        Coordinate coordinate = new Coordinate(1, -1, 0);
        Parcel parcel = new Parcel(ColorType.GREEN, ImprovementType.NOTHING);
        game.getBoard().placeParcel(parcel, coordinate); //place une parcelle

        assertTrue(missionPandaStrat.isFinishedInOneTurn(missionAllColor));
    }

    @Test
    void isFinishedInOneTurnNoParcel() {
        assertFalse(missionPandaStrat.isFinishedInOneTurn(missionGreenOneColor));
    }

    @Test
    void isFinishedInOneBambooNoParcel() {
        game.getPlayerData().addBamboo(ColorType.GREEN);
        assertFalse(missionPandaStrat.isFinishedInOneTurn(missionGreenOneColor));
    }

    @Test
    void isFinishedInOneTurnAlreadyFinishMissionOneColor() {
        game.getPlayerData().addBamboo(ColorType.GREEN);
        game.getPlayerData().addBamboo(ColorType.GREEN);
        assertFalse(missionPandaStrat.isFinishedInOneTurn(missionGreenOneColor));
    }

    @Test
    void isFinishedInOneTurnAlreadyFinishMissionAllColor() {
        game.getPlayerData().addBamboo(ColorType.GREEN);
        game.getPlayerData().addBamboo(ColorType.RED);
        game.getPlayerData().addBamboo(ColorType.YELLOW);
        assertFalse(missionPandaStrat.isFinishedInOneTurn(missionAllColor));
    }


    /**
     * <h1><u>strategyMovePeasant</u></h1>
     */

    @Test
    void strategyMovePeasantGoodColor(){
        Coordinate coordinate = new Coordinate(1, -1, 0);
        Parcel parcel = new Parcel(ColorType.GREEN, ImprovementType.NOTHING);
        game.getBoard().placeParcel(parcel, coordinate);

        assertEquals(coordinate,missionPandaStrat.strategyMovePeasant(ColorType.GREEN));
    }

    @Test
    void strategyMovePeasantNotGoodColorButOneParcel(){
        Coordinate coordinate = new Coordinate(1, -1, 0);
        Parcel parcel = new Parcel(ColorType.RED, ImprovementType.NOTHING);
        game.getBoard().placeParcel(parcel, coordinate);

        assertEquals(coordinate,missionPandaStrat.strategyMovePeasant(ColorType.GREEN));
    }

    @Test
    void strategyMovePeasantTwoColors(){
        Coordinate coordinate = new Coordinate(1, -1, 0);
        Parcel parcel = new Parcel(ColorType.GREEN, ImprovementType.NOTHING);
        game.getBoard().placeParcel(parcel, coordinate);

        Coordinate coordinate2 = new Coordinate(1, -1, 0);
        Parcel parcel2 = new Parcel(ColorType.RED, ImprovementType.NOTHING);
        game.getBoard().placeParcel(parcel2, coordinate2);

        assertEquals(coordinate2,missionPandaStrat.strategyMovePeasant(ColorType.GREEN));
    }


    /**
     * <h1><u>strategyPlaceCanal</u></h1>
     */

    @Test
    void strategyPlaceCanal(){
        Coordinate coordinate1 = new Coordinate(1,-1,0);
        Coordinate coordinate2 = new Coordinate(1,0,-1);
        game.getBoard().placeParcel(new Parcel(),coordinate1);
        game.getBoard().placeParcel(new Parcel(),coordinate2);
        missionPandaStrat.strategyPlaceCanal();
        assertTrue(game.getBoard().getPlacedCanals().containsKey(Coordinate.getSortedSet(coordinate1, coordinate2)));
    }


    /**
     * <h1><u>isJudiciousMovePanda</u></h1>
     */

    @Test
    void isJudiciousMovePandaActionAlreadyDone(){
        Coordinate coordinate = new Coordinate(1, -1, 0);
        Parcel parcel = new Parcel(ColorType.GREEN, ImprovementType.NOTHING);
        game.getBoard().placeParcel(parcel, coordinate);

        game.getPlayerData().getTemporaryInventory().add(ActionType.MOVE_PANDA);
        assertFalse(missionPandaStrat.isJudiciousMovePanda(ColorType.GREEN));
    }

    @Test
    void isJudiciousMovePandaActionNoParcel(){
        assertFalse(missionPandaStrat.isJudiciousMovePanda(ColorType.GREEN));
    }

    @Test
    void isJudiciousMovePandaTrue(){
        Coordinate coordinate = new Coordinate(1, -1, 0);
        Parcel parcel = new Parcel(ColorType.GREEN, ImprovementType.NOTHING);
        game.getBoard().placeParcel(parcel, coordinate);

        assertTrue(missionPandaStrat.isJudiciousMovePanda(ColorType.GREEN));
    }

    /**
     * <h1><u>isJudiciousMovePeasant</u></h1>
     */

    @Test
    void isJudiciousMovePeasantActionAlreadyDone(){
        Coordinate coordinate = new Coordinate(1, -1, 0);
        Parcel parcel = new Parcel(ColorType.GREEN, ImprovementType.NOTHING);
        game.getBoard().placeParcel(parcel, coordinate);

        game.getPlayerData().getTemporaryInventory().add(ActionType.MOVE_PEASANT);
        assertFalse(missionPandaStrat.isJudiciousMovePeasant());
    }

    @Test
    void isJudiciousMovePeasantActionNoParcel(){
        assertFalse(missionPandaStrat.isJudiciousMovePeasant());
    }

    @Test
    void isJudiciousMovePeasantTrue(){
        Coordinate coordinate = new Coordinate(1, -1, 0);
        Parcel parcel = new Parcel(ColorType.GREEN, ImprovementType.NOTHING);
        game.getBoard().placeParcel(parcel, coordinate);

        assertTrue(missionPandaStrat.isJudiciousMovePeasant());
    }

    /**
     * <h1><u>isJudiciousPlaceParcel</u></h1>
     */

    @Test
    void isJudiciousPlaceParcelActionAlreadyDone(){
        game.getPlayerData().getTemporaryInventory().add(ActionType.DRAW_PARCELS);
        assertFalse(missionPandaStrat.isJudiciousPlaceParcel());
    }

    @Test
    void isJudiciousPlaceParcelTrue(){
        assertTrue(missionPandaStrat.isJudiciousPlaceParcel());
    }

    /**
     * <h1><u>isJudiciousPlaceCanal</u></h1>
     */

    @Test
    void isJudiciousPlaceCanalActionAlreadyDoneDrawCanal(){
        game.getPlayerData().getTemporaryInventory().add(ActionType.DRAW_CANAL);
        assertFalse(missionPandaStrat.isJudiciousPlaceCanal());
    }

    @Test
    void isJudiciousPlaceCanalActionAlreadyDonePlaceCanal(){
        game.getPlayerData().getTemporaryInventory().add(ActionType.PLACE_CANAL);
        assertFalse(missionPandaStrat.isJudiciousPlaceCanal());
    }
    @Test
    void isJudiciousPlaceCanal(){
        assertTrue(missionPandaStrat.isJudiciousPlaceCanal());
    }


    @Test
    void drawParcelWithStrat(){
        GameInteraction gameInteractionMock= Mockito.mock(GameInteraction.class);
        MissionPandaStrat missionPandaStratMock=new MissionPandaStrat(gameInteractionMock);
        List<ParcelInformation> parcelInformations=new ArrayList<>();
        parcelInformations.add(new ParcelInformation(ColorType.RED,ImprovementType.NOTHING));
        parcelInformations.add(new ParcelInformation(ColorType.GREEN,ImprovementType.NOTHING));
        Mockito.when(gameInteractionMock.drawParcels()).thenReturn(parcelInformations);
        assertEquals(parcelInformations.get(1), missionPandaStratMock.drawParcelStrategy(ColorType.GREEN));
    }

    @Test
    void drawParcelWithNoStrat(){
        GameInteraction gameInteractionMock= Mockito.mock(GameInteraction.class);
        MissionPandaStrat missionPandaStratMock=new MissionPandaStrat(gameInteractionMock);
        List<ParcelInformation> parcelInformations=new ArrayList<>();
        parcelInformations.add(new ParcelInformation(ColorType.RED,ImprovementType.NOTHING));
        Mockito.when(gameInteractionMock.drawParcels()).thenReturn(parcelInformations);
        assertEquals(parcelInformations.get(0), missionPandaStratMock.drawParcelStrategy(ColorType.GREEN));
    }

    @Test
    void strategyPlaceParcel(){
        assertTrue(game.getRules().isMovableCharacter(CharacterType.PANDA,missionPandaStrat.strategyPlaceParcel(missionPandaStrat.drawParcelStrategy(ColorType.GREEN))));
    }

////////////////////////////////

    @Test
    void stratOneTurn_MovePanda() {
        game.getBoard().placeParcel(new Parcel(ColorType.GREEN), new Coordinate(1,0,-1));

        assertEquals(new Coordinate(0,0,0), game.getBoard().getPandaCoordinate());
        missionPandaStrat.stratOneTurn(missionGreenOneColor);
        assertNotEquals(new Coordinate(0,0,0), game.getBoard().getPandaCoordinate());
    }

    @Test
    void stratOneTurn_MovePeasant() {
        game.getBoard().placeParcel(new Parcel(ColorType.GREEN), new Coordinate(1,0,-1));
        game.getPlayerData().add(ActionType.MOVE_PANDA);

        assertEquals(new Coordinate(0,0,0), game.getBoard().getPeasantCoordinate());
        missionPandaStrat.stratOneTurn(missionGreenOneColor);
        assertNotEquals(new Coordinate(0,0,0), game.getBoard().getPeasantCoordinate());
    }

    @Test
    void stratOneTurn_PutParcel() {
        game.getPlayerData().add(ActionType.MOVE_PANDA);
        game.getPlayerData().add(ActionType.MOVE_PEASANT);

        assertEquals(1,game.getBoard().getPlacedParcels().size());
        missionPandaStrat.stratOneTurn(missionGreenOneColor);
        assertEquals(2,game.getBoard().getPlacedParcels().size());
    }

    @Test
    void stratOneTurn_PlaceCanal() {
        game.getPlayerData().add(ActionType.MOVE_PANDA);
        game.getPlayerData().add(ActionType.MOVE_PEASANT);
        game.getPlayerData().add(ActionType.DRAW_PARCELS);
        game.getBoard().placeParcel(new Parcel(ColorType.GREEN), new Coordinate(1,0,-1));
        game.getBoard().placeParcel(new Parcel(ColorType.GREEN), new Coordinate(1,-1,0));

        assertEquals(0,game.getBoard().getPlacedCanals().size());
        missionPandaStrat.stratOneTurn(missionGreenOneColor);
        assertEquals(1,game.getBoard().getPlacedCanals().size());
    }
}



