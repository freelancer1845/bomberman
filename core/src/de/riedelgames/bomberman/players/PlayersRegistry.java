package de.riedelgames.bomberman.players;

import java.util.ArrayList;
import java.util.List;

import de.riedelgames.bomberman.GameConstants;
import de.riedelgames.bomberman.map.objects.ObjectFactory;

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

    /**
     * Gives a player by id.
     * 
     * @param id of the player
     * @return {@link Player} object with this id or null.
     */
    public Player getPlayer(String id) {
        for (Player player : playerList) {
            if (player.getId().equals(id)) {
                return player;
            }
        }
        return null;
    }

    /**
     * Spawns all registered Players.
     * 
     * @throws PlayerException if there are no players registered.
     */
    public void placeRegisteredPlayersInWorld() throws PlayerException {
        ObjectFactory objectFactory = ObjectFactory.getInstance();
        // Player 1 Top Left Corner
        if (playerList.size() > 0) {
            playerList.get(0).setBody(objectFactory.createPlayer(1, GameConstants.WORLD_HEIGHT - 2,
                    playerList.get(0).getId()));
        } else {
            throw new PlayerException("No Players registered!");
        }
        // Player 2 Bottom Right Corner
        if (playerList.size() > 1) {
            playerList.get(1).setBody(objectFactory.createPlayer(GameConstants.WORLD_WIDTH - 2, 1,
                    playerList.get(1).getId()));
        }
        // Player 3 Bottom Left Corner
        if (playerList.size() > 2) {
            playerList.get(2).setBody(objectFactory.createPlayer(1, 1, playerList.get(2).getId()));
        }
        // Player 4 Top Right Corner
        if (playerList.size() > 3) {
            playerList.get(3).setBody(objectFactory.createPlayer(GameConstants.WORLD_WIDTH - 2,
                    GameConstants.WORLD_HEIGHT - 2, playerList.get(3).getId()));
        }


    }

    public List<Player> getPlayers() {
        return playerList;
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
