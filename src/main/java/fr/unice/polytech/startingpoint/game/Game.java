package fr.unice.polytech.startingpoint.game;

import fr.unice.polytech.startingpoint.bot.*;
import fr.unice.polytech.startingpoint.exception.TooManyPlayersInGameException;
import fr.unice.polytech.startingpoint.game.board.*;
import fr.unice.polytech.startingpoint.game.playerdata.PlayerData;
import fr.unice.polytech.startingpoint.type.BotType;
import fr.unice.polytech.startingpoint.type.WeatherType;

import java.util.*;

/**
 * Moteur de jeu, creation d'une partie, fait jouer les bots, verifie les missions faites et termine la partie
 * @author Manuel Enzo
 * @author Naud Eric
 * @author Madern Loic
 * @author Le Calloch Antoine
 * @version 2020.12.03
 */

public class Game{
    private final Board board;
    private final BoardRules boardRules;
    private final Resource resource;
    private final GameInteraction gameInteraction;
    private final List<PlayerData> botData;
    private final int NB_MISSION;
    private final int FIRST_BOT = 0;
    private final WeatherDice weatherDice;
    private boolean isFirstPlayer;
    private final boolean isInformationsPrinted;
    private int numBot;
    private int round;
    private int lastRound;

    public Game(boolean isInformationsPrinted, BotType[] botTypes){
        board = new Board();
        boardRules = new BoardRules(board);
        resource = new Resource();
        gameInteraction = new GameInteraction(this);
        botData = new LinkedList<>();
        NB_MISSION = 11 - botTypes.length;
        isFirstPlayer = true;
        this.isInformationsPrinted = isInformationsPrinted;
        numBot = FIRST_BOT;
        round = 0;
        lastRound = 0;
        weatherDice = new WeatherDice(new Random());
        initializeBot(botTypes);
        initializeMissionsBot();
    }

    public Game(){
        this(false, new BotType[]{BotType.RANDOM});
    }

    /**Initialize all bots.
     *
     * @param botTypes
     *                  <b>The lists of bots to initialize.</b>
     */
    private void initializeBot(BotType[] botTypes){
        for (BotType botType : botTypes) {
            switch (botType) {
                case RANDOM:
                    botData.add( new PlayerData( new RandomBot(gameInteraction) ) );
                    break;
                case PARCEL_BOT:
                    botData.add( new PlayerData( new ParcelBot(gameInteraction) ) );
                    break;
                case PEASANT_BOT:
                    botData.add( new PlayerData( new PeasantBot(gameInteraction) ) );
                    break;
                case PANDA_BOT:
                    botData.add( new PlayerData( new PandaBot(gameInteraction) ) );
                    break;
                case INTELLIGENT_BOT:
                    botData.add( new PlayerData( new IntelligentBot(gameInteraction) ) );
                    break;
            }
        }
    }

    /**
     * <p>Initialize the 3 missions for each bot at the beginning of a game.</p>
     *
     */
    private void initializeMissionsBot(){
        for (PlayerData playerData : botData) {
            playerData.addMission(resource.drawPandaMission());
            playerData.addMission(resource.drawParcelMission());
            playerData.addMission(resource.drawPeasantMission());
        }
    }

    void newRound(){
        round++;
    }

    /**
     * <p>Run the game while the conditions of an end game false</p>
     *
     */
    public void play() {
        int NB_MAX_PLAYER = 4;
        if (botData.size() <= NB_MAX_PLAYER)
            while( isContinue() ) {
                if( numBot == FIRST_BOT )
                    newRound();
                    if ( isInformationsPrinted ) {
                        System.out.println("=========== TOUR N°" + round + " ===========\n");
                        printTurnInformations();
                        System.out.println("\n");
                }
                botPlay();
                nextBot();
            }
        else
            throw new TooManyPlayersInGameException("\nThere are more players than the allowed number.\n" +
                    "The maximum number is " + NB_MAX_PLAYER + ".");
    }

    /**
     * <p>print the informations of the game after each round</p>
     */
    private void printTurnInformations() {
        for (PlayerData botDatum : botData) {
            System.out.println("Le bot " + botDatum.getBotType() + " a complété " + botDatum.getMissionsDone()
                    + " missions (" + botDatum.getMissionsPandaDone() + " missions panda, " + botDatum.getMissionsPeasantDone()
                    + " missions jardinier, " + botDatum.getMissionsParcelDone() + " missions parcelles) pour un total de "
                    + botDatum.getScore()[0] + " points.");
            System.out.println("Au tour d'avant, il a joué les actions suivantes : " + botDatum.getActionTypeList() + "\n");
        }
        System.out.println("\nLe panda est sur la parcelle de coordonnées : " + board.getPandaCoordinate());
        System.out.println("Le jardinier est sur la parcelle de coordonnées : " + board.getPeasantCoordinate());
        System.out.println("\nLes parcelles posées sont :");
        for(Map.Entry<Coordinate,Parcel> placedParcel : board.getPlacedParcels().entrySet())
            System.out.println(placedParcel.getValue() + " | Coordonnées " + placedParcel.getKey());
    }

    /**
     * <p>allow a bot to play with a weather which appears at the second round</p>
     */
    void botPlay() {
        getPlayerData().botPlay((round < 2) ? WeatherType.NO_WEATHER : weatherDice.roll());
        getPlayerData().checkMissions(board.getPlacedParcels());
    }

    /**Set the next bot to play.
     */
    void nextBot() {
        numBot = (numBot+1) % botData.size();
    }

    /**@return <b>True if nobody has done the number of missions required to win.</b>
     */
    boolean isSomebodyFinished(){
        final int EMPEROR_POINTS = 2;
        for (int missionDoneBy1P : getMissionsDone()) {
            if ( missionDoneBy1P >= NB_MISSION ) {
                if ( isFirstPlayer ) {
                    getPlayerData().addMissionDone(EMPEROR_POINTS);
                    isFirstPlayer = false;
                }
                return true;
            }
        }
        return false;
    }

    /**@return <b>True if the game is not done because a player finished his missions and the round is finished, and when resources aren't empty.</b>
     */
    boolean isContinue(){
        if (isSomebodyFinished())
            lastRound++;
        if (lastRound >= botData.size())
            return false;
        return !resource.isEmpty();
    }

    public Board getBoard() {
        return board;
    }

    public  Resource getResource() {
        return resource;
    }

    public BoardRules getRules() {
        return boardRules;
    }

    public GameInteraction getGameInteraction() {
        return gameInteraction;
    }

    /**
     * @return <b>The current playerData in use.</b>
     */
    public PlayerData getPlayerData(){
        return botData.get(numBot);
    }

    /**
     * @return <b>The number of mission done by all bots.</b>
     */
    public List<Integer> getMissionsDone() {
        List<Integer> missionsDone = new ArrayList<>();
        for (PlayerData playerData : botData){
            missionsDone.add(playerData.getMissionsDone());
        }
        return missionsDone;
    }

    /**
     * @return <b>The score of all bots.</b>
     */
    public List<int[]> getScores(){
        List<int[]> Score = new ArrayList<>();
        for (PlayerData playerData : botData) {
            Score.add(playerData.getScore());
        }
        return Score;
    }

    public int getNumberPlayers() {
        return botData.size();
    }
}