package de.riedelgames.bomberman.map.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import de.riedelgames.bomberman.players.Player;
import de.riedelgames.bomberman.screens.GameScreen;

/**
 * Allows for the registration of objects if necessary.
 * 
 * @author Jascha Riedel
 *
 */
public class ObjectRegistry {

    private static ObjectRegistry instance = null;

    private List<Bomb> bombRegistry = new ArrayList<Bomb>();

    private Queue<Object> objectsToCreate = new LinkedBlockingQueue<Object>();

    /**
     * Queues an object for creation.
     * 
     * @param bodyDef of the body.
     * @param fixtureDef of the body.
     * @param id of the object.
     */
    public void queueObjectForCreation(BodyDef bodyDef, FixtureDef fixtureDef, String id) {
        objectsToCreate.add(bodyDef);
        objectsToCreate.add(fixtureDef);
        objectsToCreate.add(id);
    }

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

    /** Creates objects in queue. */
    public void createObjectsInQueue() {
        while (!objectsToCreate.isEmpty()) {
            Body body = GameScreen.world.createBody((BodyDef) objectsToCreate.poll());
            body.createFixture((FixtureDef) objectsToCreate.poll());
            body.setUserData(objectsToCreate.poll());
        }
    }


    private ObjectRegistry() {}


}
