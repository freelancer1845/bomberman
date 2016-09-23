package de.riedelgames.bomberman.players;

/**
 * Exception for concerning a player.
 * 
 * @author Jascha Riedel
 *
 */
public class PlayerException extends Exception {

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = -1973205677060011170L;

    public PlayerException() {}

    public PlayerException(String message) {
        super(message);
    }

    public PlayerException(Throwable throwable) {
        super(throwable);
    }

    public PlayerException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
