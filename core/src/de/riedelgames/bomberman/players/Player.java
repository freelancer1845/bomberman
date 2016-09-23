package de.riedelgames.bomberman.players;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import de.riedelgames.bomberman.GameConstants;
import de.riedelgames.bomberman.map.objects.Bomb;
import de.riedelgames.bomberman.map.objects.ObjectFactory;
import de.riedelgames.bomberman.map.objects.ObjectRegistry;
import de.riedelgames.bomberman.screens.GameScreen;

/**
 * A player Object. For now it only has an ID (Usually provided by the network), movement direction
 * and information about its powerUps.
 * 
 * @author Jascha Riedel
 *
 */
public class Player {

    private final String id;

    private Body body;

    private boolean[] movement = new boolean[] {false, false, false, false};

    private int maxBombs = 1;

    private int bombRange = 1;

    private int score = 0;

    public Player(String id) {
        this.id = id;
    }

    /**
     * Adds a movement direction.
     * 
     * @param dir of the direction from {@link Input.Keys}.
     */
    public boolean addDirection(int dir) {
        switch (dir) {
            case Input.Keys.UP:
                movement[0] = true;
                break;
            case Input.Keys.DOWN:
                movement[1] = true;
                break;
            case Input.Keys.LEFT:
                movement[2] = true;
                break;
            case Input.Keys.RIGHT:
                movement[3] = true;
                break;
            default:
                return false;
        }
        return true;
    }

    /**
     * Removes a movement direction.
     * 
     * @param dir of the direction from {@link Input.Keys}.
     */
    public boolean removeDirection(int dir) {
        switch (dir) {
            case Input.Keys.UP:
                movement[0] = false;
                break;
            case Input.Keys.DOWN:
                movement[1] = false;
                break;
            case Input.Keys.LEFT:
                movement[2] = false;
                break;
            case Input.Keys.RIGHT:
                movement[3] = false;
                break;
            default:
                return false;
        }
        return true;
    }

    /**
     * Plants a bomb at the players location if he is allowed to do so (max bombs not reached +
     * already bomb at location).
     */
    public void plantBomb() {
        if (!ObjectRegistry.getInstance().isBombAtLocation(Math.round(body.getPosition().x - 0.5f),
                Math.round(body.getPosition().y - 0.5f))) {
            if (ObjectRegistry.getInstance().playerHasBombsLeft(this)) {
                new Bomb(Math.round(body.getPosition().x - 0.5f),
                        Math.round(body.getPosition().y - 0.5f), bombRange,
                        GameConstants.BOMB_CLOCK, id);
            }
        }
    }

    /**
     * Destroys the player.
     */
    public void destroy() {
        GameScreen.world.destroyBody(body);

        // TODO : Destroy logic
        Timer.schedule(new Task() {

            @Override
            public void run() {
                body = ObjectFactory.getInstance().createPlayer(1, GameConstants.WORLD_HEIGHT - 2,
                        "FIRST_PLAYER");
            }

        }, 0.5f);

    }

    public boolean[] getMovement() {
        return movement;
    }

    public String getId() {
        return id;
    }

    public int getMaxBombs() {
        return maxBombs;
    }

    public void setMaxBombs(int maxBombs) {
        this.maxBombs = maxBombs;
    }

    public void increaseMaxBombs() {
        this.maxBombs++;
    }

    public int getBombRange() {
        return bombRange;
    }

    public void setBombRange(int bombRange) {
        this.bombRange = bombRange;
    }

    public void increaseBombRange() {
        this.bombRange++;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Player)) {
            return false;
        }
        if (this.id.equals(((Player) obj).getId())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }



}
