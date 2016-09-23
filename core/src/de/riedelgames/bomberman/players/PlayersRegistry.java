package de.riedelgames.bomberman.players;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton Object that organizes the players.
 * 
 * @author Jascha Riedel
 *
 */
public class PlayersRegistry {

    /** the instance. */
    private static PlayersRegistry instance = null;

    /** The backing list of the registry. */
    private final List<Player> playerList;


    /**
     * Adds a new player to the registry.
     * 
     * @param id A Unique id to identify the player later on.
     * @throws PlayerException thrown if a player with this id is already registered.
     */
    public void registerPlayer(String id) throws PlayerException {
        Player player = new Player(id);
        if (playerList.contains(player)) {
            throw new PlayerException();
        }
        playerList.add(player);
    }

    /**
     * Removes a player.
     * 
     * @param id of the player to remove.
     */
    public void removePlayer(String id) {
        Player playerToRemove = null;
        for (Player player : playerList) {
            if (player.getId().equals(id)) {
                playerToRemove = player;
                break;
            }
        }
        if (playerToRemove != null) {
            playerList.remove(playerToRemove);
        }
    }

    private PlayersRegistry() {
        playerList = new ArrayList<Player>();
    }

    /** Returns the the PlayersRegistry. */
    public static PlayersRegistry getInstance() {
        if (instance == null) {
            instance = new PlayersRegistry();
        }
        return instance;
    }

}
