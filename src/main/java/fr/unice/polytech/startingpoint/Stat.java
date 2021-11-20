package fr.unice.polytech.startingpoint;

import fr.unice.polytech.startingpoint.type.BotType;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>{@link Stat} :</h1>
 *
 * <p>This class provides a treatment of the scores given at the end of each game.</p>
 *
 * @author Manuel Enzo
 * @author Naud Eric
 * @author Madern Loic
 * @author Le Calloch Antoine
 * @version 0.5
 */

final class Stat {
    private final List<List<int[]>> gameData;
    private final BotType[] botList;
    private final int[][] botScores;

    /**Initialize all variables.
     */
    public Stat(BotType[] botList){
        gameData = new ArrayList<>();
        this.botList = botList;
        botScores = new int[botList.length][3];
        for (int i = 0; i < botList.length; i++){
            for (int j = 0; j < 3; j++){
                botScores[i][j] = 0;
            }
        }
    }

    /**Add the scores of a game to {@link #botScores}.
     */
    void add(List<int[]> integerList) {
        gameData.add(integerList);
        setWinner(getWinner(integerList));
        for (int i = 0; i < integerList.size(); i++){
            botScores[i][0] += integerList.get(i)[0];
        }
    }

    /**Add a win to the winner, and add an equality to the winners .
     */
    private void setWinner(List<Integer> winner){
        if (winner.size() == 1)
            botScores[winner.get(0)][1]++;
        else
            for(int win : winner){
                botScores[win][2]++;
            }
    }

    /**@return <b>A list containing the bot(s) that have the highest score.</b>
     */
    private List<Integer> getWinner(List<int[]> scoreList){
        int[] bestScore = new int[]{0,0};
        List<Integer> winner = new ArrayList<>();

        for(int[] score : scoreList) {
            if (score[0] > bestScore[0]) {
                bestScore[0] = score[0];
                bestScore[1] = score[1];
            }
            else if (score[0] == bestScore[0] && score[1] > bestScore[1])
                bestScore[1] = score[1];
        }
        for(int i = 0; i < scoreList.size(); i++) {
            if (scoreList.get(i)[0] == bestScore[0] && scoreList.get(i)[1] == bestScore[1])
                winner.add(i);
        }

        return winner;
    }

    /**@param player
     *              <b>The player we want the points’ average from.</b>
     *
     * @return <b>The average points from the player.</b>
     */
    public double getPointsAverage(int player){
        return Math.round((botScores[player][0]*1.0)/(gameData.size())*100.0)/100.0;
    }

    /**@param player
     *              <b>The player we want the win rate from.</b>
     *
     * @return <b>The win rate from the player.</b>
     */
    public double getWinRate(int player){
        return Math.round(botScores[player][1]/((gameData.size())/100.0)*100.0)/100.0;
    }

    /**@param player
     *              <b>The player we want the loss rate from.</b>
     *
     * @return <b>The loss rate from the player.</b>
     */
    public double getLoosRate(int player){
        return Math.round((gameData.size()-botScores[player][1]-botScores[player][2])/((gameData.size())/100.0)*100.0)/100.0;
    }

    /**@param player
     *              <b>The player we want the equality rate from.</b>
     *
     * @return <b>The equality rate from the player.</b>
     */
    public double getEqualityRate(int player){
        return Math.round(botScores[player][2]/((gameData.size())/100.0)*100.0)/100.0;
    }

    /**
     * @return <b>The stats from all players.</b>
     */
    @Override
    public String toString(){
        StringBuilder displayStat = new StringBuilder();

        for (int i = 0; i < botScores.length; i++)
            displayStat.append("Joueur ")
                       .append(botList[i])
                       .append(" ")
                       .append(i+1)
                       .append(" : ")
                       .append(getWinRate(i))
                       .append("% win rate, ")
                       .append(getLoosRate(i))
                       .append("% loss rate and ")
                       .append(getEqualityRate(i))
                       .append("% equality rate with a ")
                       .append(getPointsAverage(i))
                       .append(" points average\n");

        return displayStat.toString();
    }
}