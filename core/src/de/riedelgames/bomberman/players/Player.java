package de.riedelgames.bomberman.players;

/**
 * A player Object. For now it only has an ID (Usually provided by the network), movement direction
 * and information about its powerUps.
 * 
 * @author Jascha Riedel
 *
 */
public class Player {

    /** Direction. */
    public static final int UP = 0;

    /** Direction. */
    public static final int DOWN = 1;

    /** Direction. */
    public static final int LEFT = 2;

    /** Direction. */
    public static final int RIGHT = 3;

    private final String id;

    private boolean[] movement = new boolean[] {false, false, false, false};

    private int maxBombs = 1;

    private int bombRange = 1;

    private int score = 0;

    public Player(String id) {
        this.id = id;
    }

    public void addDirection(int dir) {
        movement[dir] = true;
    }

    public void removeDirection(int dir) {
        movement[dir] = false;
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

    public int getBombRange() {
        return bombRange;
    }

    public void setBombRange(int bombRange) {
        this.bombRange = bombRange;
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



}
