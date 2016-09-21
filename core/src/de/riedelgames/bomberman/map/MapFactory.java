package de.riedelgames.bomberman.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        //createWalls();
        //createInnerGrid();
        //placeDestructableBlocks();
    }

    private void createWalls() {

        /** Create the left wall from the top to the bottom. */
        //objectFactory.createSolidStack(0 , 0, 1, GameConstants.WORLD_HEIGHT);

        /** Create the right wall from the top to the bottom */
        //objectFactory.createSolidStack(GameConstants.WORLD_WIDTH - 1 , 0, 1, GameConstants.WORLD_HEIGHT);

        /** Create the top wall from (left plus one block) to (right - one block) */
        //objectFactory.createSolidStack(1, 0, GameConstants.WORLD_WIDTH - 1 - 1, 1);

        /** Create the bottom wall from (left plus one block) to (right - one block) */
        //objectFactory.createSolidStack(1, GameConstants.WORLD_HEIGHT - 1, GameConstants.WORLD_WIDTH - 1 - 1, 1);

        Gdx.app.log("create walls", " called");
    }


    private void createInnerGrid() {

        for (int i = 2; i < GameConstants.WORLD_HEIGHT; i += 2){
            for (int j = 2; j < GameConstants.WORLD_WIDTH; j += 2) {
                objectFactory.createSolidBlock(j, i);
            }
        }

        Gdx.app.log("create inner grid ", "called");

    }

    private void placeDestructableBlocks() {
        List<Integer[]> usedFields = new ArrayList<Integer[]>();

        // TODO : Implement for multiple players.
        usedFields.add(new Integer[]{ 1 , 1 });
        usedFields.add(new Integer[]{ 2 , 1 });
        usedFields.add(new Integer[]{ 1 , 2 });
        usedFields.add(new Integer[] { GameConstants.WORLD_WIDTH - 1 , GameConstants.WORLD_HEIGHT - 1 });
        usedFields.add(new Integer[] { GameConstants.WORLD_WIDTH - 1 , GameConstants.WORLD_HEIGHT - 2 });
        usedFields.add(new Integer[] { GameConstants.WORLD_WIDTH - 2 , GameConstants.WORLD_HEIGHT - 1 });

        int reservedFields = usedFields.size();

        int numberOfPossibleFields = calculateFieldsWhereBothCoordinatesAreUneven();
        numberOfPossibleFields = numberOfPossibleFields - (2 * GameConstants.WORLD_WIDTH - 4 + 2 * GameConstants.WORLD_HEIGHT);


        int numberOfWantedFields = Math.round(numberOfPossibleFields * GameConstants.DESTRUCTABLE_BLOCKS_PERCENTAGE);
        Random randomGenerator = new Random();

        while (usedFields.size() - reservedFields < numberOfWantedFields) {
            int xPos = randomGenerator.nextInt(GameConstants.WORLD_WIDTH - 1) + 1;
            int yPos = randomGenerator.nextInt(GameConstants.WORLD_HEIGHT - 1) + 1;
            if (xPos % 2 == 0 && yPos % 2 == 0) {
                continue;
            }
            Integer[] position = new Integer[]{xPos, yPos};
            if (usedFields.contains(position)) {
                continue;
            }
            objectFactory.createDestructableBlock(xPos, yPos);
            usedFields.add(position);
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
                if (i%2 == 1 && j%2 == 1) {
                    count++;
                }
            }
        }
        return count;
    }


}
