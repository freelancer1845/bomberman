package de.riedelgames.bomberman;

public class GameConstants {



    /** Solid block identifier. */
    public static final String BLOCK_ID = "SolidBlock";

    /** Destructible block. */
    public static final String DESTRUCTIBLE_BLOCK_ID = "DesctructableBlock";

    /** Player id prefix. */
    public static final String PLAYER_ID_PREFIX = "PlayerId-";

    /** Bomb id. */
    public static final String BOMB_ID = "BombId";


    /** From here on the gameConstants are settings to be set. */

    /** Game world width. */
    public static final int WORLD_WIDTH = 17;

    /** Game world height. */
    public static final int WORLD_HEIGHT = 17;

    /** Percentage blocked with destructible blocks. */
    public static final float DESTRUCTIBLE_BLOCKS_PERCENTAGE = 0.5f;

    /** Standard Velocity. */
    public static final float STANDARD_VELOCITY = 5f;

    private GameConstants() {}
}
