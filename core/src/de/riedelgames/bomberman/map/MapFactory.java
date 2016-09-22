package de.riedelgames.bomberman.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;

import de.riedelgames.bomberman.GameConstants;
import de.riedelgames.bomberman.map.objects.ObjectFactory;
import de.riedelgames.bomberman.screens.GameScreen;

/**
 * Factory that creates a new map based on world width and height.
 * 
 * 
 * @author Jascha Riedel
 * @author Nikolai Riedel
 *
 */
public class MapFactory {

    private static MapFactory instance = null;

    private ObjectFactory objectFactory;

    private int fieldsUsedByInnerGrid = 0;

    private int fieldsInInnerField = 0;


    /**
     * Create a random game map.
     * 
     */
    public void createMap() {
        // TODO : Move player creation outside
        objectFactory.createPlayer(GameConstants.WORLD_WIDTH - 2, 1, "2");
        objectFactory.createPlayer(1, GameConstants.WORLD_HEIGHT - 2, "1");
        objectFactory.createPlayer((GameConstants.WORLD_WIDTH - 1) / 2 + 1,
                (GameConstants.WORLD_HEIGHT - 1) / 2 + 1, "3");

        createWalls();
        createInnerGrid();
        placeDestructableBlocks();
    }

    private void createWalls() {

        /** Left Wall. */
        objectFactory.createSolidStack(0, 0, 1, GameConstants.WORLD_HEIGHT);

        /** Right Wall. */
        objectFactory.createSolidStack(GameConstants.WORLD_WIDTH - 1, 0, 1,
                GameConstants.WORLD_HEIGHT);

        /** Bottom Wall. */
        objectFactory.createSolidStack(1, 0, GameConstants.WORLD_WIDTH - 2, 1);

        /** Top Wall. */
        objectFactory.createSolidStack(1, GameConstants.WORLD_HEIGHT - 1,
                GameConstants.WORLD_WIDTH - 2, 1);

        fieldsInInnerField = (GameConstants.WORLD_WIDTH - 2) * (GameConstants.WORLD_HEIGHT - 2);

    }


    private void createInnerGrid() {

        fieldsUsedByInnerGrid = 0;
        for (int i = 2; i < GameConstants.WORLD_HEIGHT - 1; i += 2) {
            for (int j = 2; j < GameConstants.WORLD_WIDTH - 1; j += 2) {
                objectFactory.createSolidBlock(j, i);
                fieldsUsedByInnerGrid++;
            }
        }

        Gdx.app.log("create inner grid ", "called");

    }

    private void placeDestructableBlocks() {
        List<Integer[]> usedFields = new ArrayList<Integer[]>();

        addFreeFieldsAroundPlayers(usedFields);

        int reservedFields = usedFields.size();

        int numberOfWantedFields =
                Math.round((fieldsInInnerField - fieldsUsedByInnerGrid - usedFields.size())
                        * GameConstants.DESTRUCTABLE_BLOCKS_PERCENTAGE);

        Random randomGenerator = new Random();

        while (usedFields.size() - reservedFields < numberOfWantedFields) {
            int posX = randomGenerator.nextInt(GameConstants.WORLD_WIDTH - 2) + 1;
            int posY = randomGenerator.nextInt(GameConstants.WORLD_HEIGHT - 2) + 1;
            if (!isInnerFieldNotOnGrid(posX, posY)) {
                continue;
            }
            Integer[] position = new Integer[] {posX, posY};
            boolean usedPosition = false;
            for (Integer[] positionUsed : usedFields) {
                if (positionUsed[0] == position[0] && positionUsed[1] == position[1]) {
                    usedPosition = true;
                    break;
                }
            }
            if (usedPosition) {
                continue;
            }
            objectFactory.createDestructableBlock(posX, posY);
            usedFields.add(position);
            System.out.println("Block Created: [" + posX + "] [" + posY + "]");
        }

        Gdx.app.log("destructable blocks", " created");

    }


    /**
     * Gets the instance.
     */
    public static MapFactory getInstance() {
        if (instance == null) {
            instance = new MapFactory();
        }
        return instance;
    }

    /** This class should be private. */
    private MapFactory() {
        objectFactory = ObjectFactory.getInstance();
    }

    private boolean isInnerFieldNotOnGrid(int posX, int posY) {
        if (posX % 2 == 0 && posY % 2 == 0) {
            return false;
        } else if ((posX == 0 || posX == GameConstants.WORLD_WIDTH || posY == 0
                || posY == GameConstants.WORLD_HEIGHT)) {
            return false;
        } else {
            return true;
        }
    }

    private void addFreeFieldsAroundPlayers(List<Integer[]> usedFields) {
        Array<Body> bodies = new Array<Body>();

        GameScreen.world.getBodies(bodies);

        for (Body body : bodies) {
            String id = (String) body.getUserData();
            if (id.startsWith(GameConstants.PLAYER_ID_PREFIX)) {
                int posX = (int) (body.getPosition().x - 0.5f);
                int posY = (int) (body.getPosition().y - 0.5f);
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        if (isInnerFieldNotOnGrid(posX + i, posY + j)) {
                            usedFields.add(new Integer[] {posX + i, posY + j});
                        }
                    }
                }
            }
        }
    }



}
