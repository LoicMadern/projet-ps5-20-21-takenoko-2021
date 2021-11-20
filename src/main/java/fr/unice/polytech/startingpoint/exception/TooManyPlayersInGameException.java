package fr.unice.polytech.startingpoint.exception;

public class TooManyPlayersInGameException extends RuntimeException {

    public TooManyPlayersInGameException(String message){
        super(message);
    }

}
