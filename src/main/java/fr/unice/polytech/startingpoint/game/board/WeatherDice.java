package fr.unice.polytech.startingpoint.game.board;

import fr.unice.polytech.startingpoint.type.WeatherType;

import java.util.Random;

public class WeatherDice {
    private Random random;
    private final int FACES= WeatherType.values().length;

    public WeatherDice(Random random){
            this.random=random;
    }

    /**<p>Roll a dice for get a weather.</p>
     *
     * @return WeatherType
     *            <b>return a random weather</b>
     */

    public WeatherType roll(){
        int result = random.nextInt(FACES-1);
        if(result>FACES)
            throw new RuntimeException("Dice return an incompatible value");
        return WeatherType.values()[result];
    }
}