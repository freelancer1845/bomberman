package de.riedelgames.bomberman;

public class GameConstants {



    /** Solid block identifier. */
    public static final String BLOCK_ID = "SolidBlock";

    /** Destructable block. */
    public static final String DESTRUCTIBLE_BLOCK_ID = "DesctructableBlock";

    /** Player id prefix. */
    public static final String PLAYER_ID_PREFIX = "PlayerId-";

    /** Bomb id. */
    public static final String BOMB_ID = "BombId";

    /** Bomb count power up id. */
    public static final String BOMB_COUNT_POWER_UP_ID = "BombCountPowerUpId";

    /** Bomb range power up id. */
    public static final String BOMB_RANGE_POWER_UP_ID = "BombRangePowerUpId";


    /** From here on the gameConstants are settings to be set. */

    /** Game world width. */
    public static final int WORLD_WIDTH = 33;

    /** Game world height. */
    public static final int WORLD_HEIGHT = 33;

    /** Percentage blocked with destructible blocks. */
    public static final float DESTRUCTIBLE_BLOCKS_PERCENTAGE = 0.4f;

    /** Standard Velocity. */
    public static final float STANDARD_VELOCITY = 5f;

    /** Bomb explosion timer in ms. */
    public static final int BOMB_CLOCK = 2000;

    /** Probability to drop an upgrade. */
    public static final float UPGRADE_PERCENTAGE = 0.2f;

    private GameConstants() {}
}
