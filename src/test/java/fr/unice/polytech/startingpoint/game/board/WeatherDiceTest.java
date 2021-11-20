package fr.unice.polytech.startingpoint.game.board;

import fr.unice.polytech.startingpoint.type.WeatherType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;

class WeatherDiceTest {
    private WeatherDice weatherDice;

    @Test
    void rollOverNumberOfNumberOfFaces(){
        Random tooMuch= Mockito.mock(Random.class);
        Mockito.when(tooMuch.nextInt(anyInt())).thenReturn(7);
        weatherDice=new WeatherDice(tooMuch);
        assertThrows(RuntimeException.class, ()->{
            weatherDice.roll();
        });
    }

    @Test
    void rollUnderOfNumberOfFaces(){
        Random tooLow= Mockito.mock(Random.class);
        Mockito.when(tooLow.nextInt(anyInt())).thenReturn(-1);
        weatherDice=new WeatherDice(tooLow);
        assertThrows(RuntimeException.class, ()->{
            weatherDice.roll();
        });
    }

    @Test
    void goodRoll(){
        Random goodNumber= Mockito.mock(Random.class);
        Mockito.when(goodNumber.nextInt(anyInt())).thenReturn(0);//indice 0 du tableau enum Weather
        weatherDice=new WeatherDice(goodNumber);
        assertEquals(WeatherType.SUN,weatherDice.roll());
    }

    @Test void impossibleToRollNoWeather(){
        weatherDice=new WeatherDice(new Random());
        assertNotEquals(WeatherType.NO_WEATHER,weatherDice.roll());
    }
}