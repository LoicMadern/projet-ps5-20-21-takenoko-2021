package fr.unice.polytech.startingpoint.bot.strategie;

import fr.unice.polytech.startingpoint.game.Game;
import fr.unice.polytech.startingpoint.game.GameInteraction;
import fr.unice.polytech.startingpoint.game.board.Coordinate;
import fr.unice.polytech.startingpoint.game.board.Parcel;
import fr.unice.polytech.startingpoint.game.board.ParcelInformation;
import fr.unice.polytech.startingpoint.game.mission.PeasantMission;
import fr.unice.polytech.startingpoint.type.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MissionPeasantStratTest {

    private Parcel parcelGreen;
    private Parcel parcelRed;
    private Parcel parcelFertiziled;
    private Coordinate coordinate1;
    private Game game;
    private MissionPeasantStrat stratMissionPeasant;
    private PeasantMission peasantMissionGreen;
    private PeasantMission peasantMissionGreenFerti;

    @BeforeEach
    void setUp() {
        game = new Game();
        parcelGreen = new Parcel(ColorType.GREEN,ImprovementType.NOTHING);
        parcelRed = new Parcel(ColorType.RED);
        parcelFertiziled = new Parcel(ColorType.GREEN,ImprovementType.FERTILIZER);
        coordinate1 = new Coordinate(1, -1, 0);
        peasantMissionGreen=new PeasantMission(ColorType.GREEN,ImprovementType.NOTHING,2);
        peasantMissionGreenFerti=new PeasantMission(ColorType.GREEN,ImprovementType.FERTILIZER,2);
        GameInteraction gameInteraction = game.getGameInteraction();
        stratMissionPeasant = new MissionPeasantStrat(gameInteraction);
        game.getPlayerData().getInventory().subMissions(game.getPlayerData().getPeasantMissions()); //supprime la mission donner au debut

    }
    /**
     <h2><u>Strategy Move Peasant</u></h2>

     */

    @Test
    void coordWhereMovePeasant_0parcel() {

        assertNull(stratMissionPeasant.strategyMovePeasant(peasantMissionGreen));//Pas de parcel, il ne bouge pas
    }


    @Test
    void noMissionSoPeasentNotMove() {
        game.getBoard().placeParcel(parcelGreen, coordinate1);//place la parcel (un bamboo pousse)
        assertEquals(coordinate1,stratMissionPeasant.strategyMovePeasant(peasantMissionGreen));
    }

    @Test
    void possibleParcelColorDifferentFromTheActualMission() {
        game.getBoard().placeParcel(parcelRed, coordinate1);
        assertNull(stratMissionPeasant.strategyMovePeasant(peasantMissionGreen));
    }


    @Test
    void missionColorSameAsMission() {
        game.getBoard().placeParcel(parcelGreen,coordinate1);
        assertEquals(coordinate1,stratMissionPeasant.strategyMovePeasant(peasantMissionGreen));
    }

    @Test
    void movePeasant_1Parcel2Bamboo()  {
        game.getPlayerData().add(ActionType.DRAW_MISSION);//empêche de piocher une mission
        game.getBoard().placeParcel(parcelGreen, coordinate1);//pose la parcel (cela ajoute un autre bamboo)
        assertEquals(1,game.getBoard().getPlacedParcels().get(coordinate1).getNbBamboo());
        stratMissionPeasant.stratOneTurn(peasantMissionGreen);//deplace le paysan sur une parcel avec plus de 1 bamboo (parcel1Bamboo), cela ajoute un bamboo
        assertEquals(2,game.getBoard().getPlacedParcels().get(coordinate1).getNbBamboo());//2 bamboo sur la parcel
    }

    @Test
    void stratOneTurn_PutParcel() {
        game.getPlayerData().add(ActionType.MOVE_PEASANT);

        assertEquals(1,game.getBoard().getPlacedParcels().size());
        stratMissionPeasant.stratOneTurn(peasantMissionGreen);
        assertEquals(2,game.getBoard().getPlacedParcels().size());
    }

    @Test
    void stratOneTurn_PlaceCanal() {
        game.getPlayerData().add(ActionType.MOVE_PEASANT);
        game.getPlayerData().add(ActionType.DRAW_PARCELS);
        game.getBoard().placeParcel(new Parcel(ColorType.GREEN), new Coordinate(1,0,-1));
        game.getBoard().placeParcel(new Parcel(ColorType.GREEN), new Coordinate(1,-1,0));

        assertEquals(0,game.getBoard().getPlacedCanals().size());
        stratMissionPeasant.stratOneTurn(peasantMissionGreen);
        assertEquals(1,game.getBoard().getPlacedCanals().size());
    }

    /**
     <h2><u>IS JUDICIOUS</u></h2>
     */

    @Test
    void noPossibleCoordinatesForPeasent(){
        assertFalse(stratMissionPeasant.isJudiciousMovePeasant(peasantMissionGreen));
    }

    @Test
    void peasentAlreadyMoved(){
        game.getPlayerData().add(ActionType.MOVE_PEASANT);
        assertFalse(stratMissionPeasant.isJudiciousMovePeasant(peasantMissionGreen));
    }

    @Test
    void judiciousToMovePeasent(){
        game.getBoard().placeParcel(parcelGreen,coordinate1);
        assertTrue(stratMissionPeasant.isJudiciousMovePeasant(peasantMissionGreen));
    }

    @Test
    void noMoreCanals(){
        GameInteraction gameInteractionMock = Mockito.mock(GameInteraction.class);
        Mockito.when(gameInteractionMock.getResourceSize(ResourceType.CANAL)).thenReturn(0);
        MissionPeasantStrat missionPeasantStratWithMock=new MissionPeasantStrat(gameInteractionMock);
        assertFalse(missionPeasantStratWithMock.isJudiciousPlaceCanal());
    }

    @Test
    void noMoreParcels(){
        GameInteraction gameInteractionMock = Mockito.mock(GameInteraction.class);
        Mockito.when(gameInteractionMock.getResourceSize(ResourceType.PARCEL)).thenReturn(0);
        MissionPeasantStrat missionPeasantStratWithMock=new MissionPeasantStrat(gameInteractionMock);
        assertFalse(missionPeasantStratWithMock.isJudiciousPlaceParcel());
    }

    @Test
    void noParcelInTheHandSoCantPLaceAParcel(){
        game.getPlayerData().add(ActionType.DRAW_PARCELS);
        assertFalse(stratMissionPeasant.isJudiciousPlaceParcel());
    }

    /**
     <h2><u>Strats Missions</u></h2>
     */

    @Test
    void missionNotFinishedCauseNothing(){
        stratMissionPeasant.isAlreadyFinished(peasantMissionGreen);
    }

    @Test
    void missionNotFinished(){
        for(int i = 0 ; i < 2 ; i++){
            parcelGreen.addBamboo();
        }
        game.getBoard().placeParcel(parcelGreen,coordinate1);
        assertFalse(stratMissionPeasant.isAlreadyFinished(peasantMissionGreen));
    }

    @Test
    void missionFinished(){
        for(int i=0;i<3;i++){
            parcelGreen.addBamboo();
        }
        game.getBoard().placeParcel(parcelGreen,coordinate1);
        assertTrue(stratMissionPeasant.isAlreadyFinished(peasantMissionGreen));
    }

    @Test
    void getBestParcelInformation(){
        List<ParcelInformation> parcelInformationList = new ArrayList<>(Arrays.asList(new ParcelInformation(),new ParcelInformation(ColorType.GREEN),new ParcelInformation(ImprovementType.FERTILIZER)));
        PeasantMission peasantMission = new PeasantMission(ColorType.NO_COLOR,ImprovementType.NOTHING,0);
        assertEquals(new ParcelInformation(),stratMissionPeasant.getBestParcelInformation(parcelInformationList,peasantMission));
    }

    @Test
    void noBestParcelInformation(){
        List<ParcelInformation> parcelInformationList = new ArrayList<>(Arrays.asList(new ParcelInformation(),new ParcelInformation(ColorType.GREEN),new ParcelInformation(ImprovementType.FERTILIZER)));
        PeasantMission peasantMission = new PeasantMission(ColorType.RED,ImprovementType.WATERSHED,0);
        assertEquals(new ParcelInformation(),stratMissionPeasant.getBestParcelInformation(parcelInformationList,peasantMission));
    }

    @Test
    void getBestCoordinateParcel(){
        game.getBoard().placeParcel(new Parcel(),new Coordinate(1,-1,0));
        game.getBoard().placeParcel(new Parcel(),new Coordinate(2,-2,0));
        game.getBoard().placeParcel(new Parcel(),new Coordinate(3,-3,0));
        game.getBoard().moveCharacter(CharacterType.PEASANT,new Coordinate(3,-3,0));
        assertEquals(new Coordinate(-1,1,0),stratMissionPeasant.getBestCoordinateParcel());
    }

    @Test
    void strategyPlaceCanal(){
        Coordinate coordinate1 = new Coordinate(1,-1,0);
        Coordinate coordinate2 = new Coordinate(1,0,-1);
        game.getBoard().placeParcel(new Parcel(),coordinate1);
        game.getBoard().placeParcel(new Parcel(),coordinate2);
        stratMissionPeasant.strategyPlaceCanal(new PeasantMission(ColorType.NO_COLOR,ImprovementType.NOTHING,0));
        assertTrue(game.getBoard().getPlacedCanals().containsKey(Coordinate.getSortedSet(coordinate1, coordinate2)));
    }

    @Test
    void oneTurnLeftGreenMissionSameColorParcel(){
        parcelGreen.addBamboo();
        parcelGreen.addBamboo();
        game.getBoard().placeParcel(parcelGreen,coordinate1);
        assertTrue(stratMissionPeasant.isFinishedInOneTurn(peasantMissionGreen));
    }
    @Test
    void oneTurnLeftGreenMissionFertiSameColorParcel(){
        game.getBoard().placeParcel(parcelFertiziled,coordinate1);
        assertTrue(stratMissionPeasant.isFinishedInOneTurn(peasantMissionGreenFerti));
    }

    @Test
    void noTurnLeftMissionRedParcelGreenMIssion(){
        parcelRed.addBamboo();
        parcelRed.addBamboo();
        game.getBoard().placeParcel(parcelRed,coordinate1);
        assertFalse(stratMissionPeasant.isFinishedInOneTurn(peasantMissionGreen));
    }

    @Test
    void notExistGoodMOvableParcel(){
        game.getBoard().placeParcel(parcelRed,coordinate1);
        assertTrue(stratMissionPeasant.notExistGoodMovableParcel(peasantMissionGreen));
    }

    @Test
    void goodMOvableParcel(){
        game.getBoard().placeParcel(parcelGreen,coordinate1);
        assertFalse(stratMissionPeasant.notExistGoodMovableParcel(peasantMissionGreen));
    }

    @Test
    void missionNotTakenCauseToolong(){
        assertEquals(-1,stratMissionPeasant.howManyMoveToDoMission(new PeasantMission(ColorType.GREEN,ImprovementType.WHATEVER,-2)));
    }

    @Test
    void missionTakenCauseCanBeFinishedIn1Turn(){
        game.getBoard().placeParcel(parcelGreen,coordinate1);
        parcelGreen.addBamboo();
        parcelGreen.addBamboo();
        assertEquals(1,stratMissionPeasant.howManyMoveToDoMission(peasantMissionGreen));
    }

    @Test
    void missionNotTakenCauseCanBeFinishedIn3Turn(){
        game.getBoard().placeParcel(parcelGreen,coordinate1);
        assertEquals(4,stratMissionPeasant.howManyMoveToDoMission(peasantMissionGreen));
    }

   @Test
    void cantMoveSoChooseToDontDoMission(){
        assertEquals(-1,stratMissionPeasant.howManyMoveToDoMission(peasantMissionGreen));
    }
}