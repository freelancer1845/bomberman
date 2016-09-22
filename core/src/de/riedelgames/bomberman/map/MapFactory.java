package de.riedelgames.bomberman.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;

import de.riedelgames.bomberman.GameConstants;
import de.riedelgames.bomberman.map.objects.ObjectFactory;

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


    /**
     * Create a random game map.
     * 
     */
    public void createMap() {
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

    }


    private void createInnerGrid() {

        for (int i = 2; i < GameConstants.WORLD_HEIGHT - 1; i += 2) {
            for (int j = 2; j < GameConstants.WORLD_WIDTH - 1; j += 2) {
                objectFactory.createSolidBlock(j, i);
            }
        }

        Gdx.app.log("create inner grid ", "called");

    }

    private void placeDestructableBlocks() {
        List<Integer[]> usedFields = new ArrayList<Integer[]>();

        // TODO : Implement for multiple players.
        usedFields.add(new Integer[] {1, 1});
        usedFields.add(new Integer[] {2, 1});
        usedFields.add(new Integer[] {1, 2});
        usedFields
                .add(new Integer[] {GameConstants.WORLD_WIDTH - 2, GameConstants.WORLD_HEIGHT - 2});
        usedFields
                .add(new Integer[] {GameConstants.WORLD_WIDTH - 2, GameConstants.WORLD_HEIGHT - 3});
        usedFields
                .add(new Integer[] {GameConstants.WORLD_WIDTH - 3, GameConstants.WORLD_HEIGHT - 2});

        int reservedFields = usedFields.size();

        int numberOfPossibleFields =
                ((GameConstants.WORLD_WIDTH - 1) / 2 * (GameConstants.WORLD_HEIGHT - 1) / 2);


        int numberOfWantedFields =
                Math.round(numberOfPossibleFields * GameConstants.DESTRUCTABLE_BLOCKS_PERCENTAGE);
        Random randomGenerator = new Random();

        while (usedFields.size() - reservedFields < numberOfWantedFields) {
            int posX = randomGenerator.nextInt(GameConstants.WORLD_WIDTH - 2) + 1;
            int posY = randomGenerator.nextInt(GameConstants.WORLD_HEIGHT - 2) + 1;
            if (posX % 2 == 0 && posY % 2 == 0) {
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

    /**
     * TODO : Do it the right way....
     */
    private int calculateFieldsWhereBothCoordinatesAreUneven() {
        int count = 0;
        for (int i = 0; i < GameConstants.WORLD_WIDTH; i++) {
            for (int j = 0; j < GameConstants.WORLD_HEIGHT; j++) {
                if (i % 2 == 1 && j % 2 == 1) {
                    count++;
                }
            }
        }
        return count;
    }


}
