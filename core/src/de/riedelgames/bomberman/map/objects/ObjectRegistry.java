package de.riedelgames.bomberman.map.objects;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

import de.riedelgames.bomberman.players.Player;

/**
 * Allows for the registration of objects if necessary.
 * 
 * @author Jascha Riedel
 *
 */
public class ObjectRegistry {

    private static ObjectRegistry instance = null;

    private List<Bomb> bombRegistry = new ArrayList<Bomb>();

    public void addBomb(Bomb bomb) {
        bombRegistry.add(bomb);
    }

    public void removeBomb(Bomb bomb) {
        bombRegistry.remove(bomb);
    }

    /** Checks whether there is a bomb at this location. */
    public boolean isBombAtLocation(int xgridPosition, int ygridPosition) {
        for (Bomb bomb : bombRegistry) {
            Vector2 position = bomb.getBody().getWorldCenter();
            if (Math.abs(position.x - 0.5f - xgridPosition) < 0.1f
                    && Math.abs(position.y - 0.5f - ygridPosition) < 0.1f) {
                return true;
            }
        }
        return false;
    }

    /** Checks whether the player may plant more bombs. */
    public boolean playerHasBombsLeft(Player player) {
        int bombCount = 0;
        for (Bomb bomb : bombRegistry) {
            if (bomb.getOwner().equals(player.getId())) {
                bombCount++;
            }
        }
        if (bombCount >= player.getMaxBombs()) {
            return false;
        }
        return true;
    }

    /** Returns the ObjectRegistry. */
    public static ObjectRegistry getInstance() {
        if (instance == null) {
            instance = new ObjectRegistry();
        }
        return instance;
    }

    private ObjectRegistry() {}


}
