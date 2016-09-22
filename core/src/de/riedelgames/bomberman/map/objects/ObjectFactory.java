package de.riedelgames.bomberman.map.objects;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import de.riedelgames.bomberman.GameConstants;
import de.riedelgames.bomberman.screens.GameScreen;

/**
 * Creates Objects and adds them to the game world.
 * 
 * @author Jascha Riedel
 *
 */
public class ObjectFactory {



    private static ObjectFactory instance = null;


    /**
     * Creates a solid block in the world.
     * 
     * @param xGridPosition position in Grid.
     * @param yGridPosition position in Grid.
     * 
     */
    public void createSolidBlock(int xGridPosition, int yGridPosition) {
        createSolidStack(xGridPosition, yGridPosition, 1, 1);
    }

    /**
     * Creates a stack of solid blocks. (Only one rectangle in the World).
     * 
     * @param xGridPosition position in Grid
     * @param yGridPosition position in Grid
     * @param xBlocks count
     * @param yBlocks count
     */
    public void createSolidStack(int xGridPosition, int yGridPosition, int xBlocks, int yBlocks) {
        createBlock(xGridPosition, yGridPosition, xBlocks, yBlocks, GameConstants.BLOCK_ID);
    }

    /**
     * Creates a destructable block in the world.
     */
    public void createDestructableBlock(int xGridPosition, int yGridPosition) {
        createBlock(xGridPosition, yGridPosition, 1, 1, GameConstants.DESTRUCTIBLE_BLOCK_ID);
    }

    /**
     * Creates a player object with a given id.
     */
    public void createPlayer(int xGridPosition, int yGridPosition, String id) {
        createPlayerObject(xGridPosition, yGridPosition, GameConstants.PLAYER_ID_PREFIX + id);
    }


    /**
     * Returns the instance.
     * 
     * @return {@link ObjectFactory} instance
     */
    public static ObjectFactory getInstance() {
        if (instance == null) {
            instance = new ObjectFactory();
        }
        return instance;
    }


    private void createBlock(int xGridPosition, int yGridPosition, int xBlocks, int yBlocks,
            String id) {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(xGridPosition + xBlocks / 2.0f, yGridPosition + yBlocks / 2.0f);


        Body body;
        body = GameScreen.world.createBody(bodyDef);

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(xBlocks / 2.0f, yBlocks / 2.0f);


        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;

        body.createFixture(fixtureDef);
        body.setUserData(id);


    }

    private void createPlayerObject(int xGridPosition, int yGridPosition, String id) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(xGridPosition + 0.5f, yGridPosition + 0.5f);

        Body body;
        body = GameScreen.world.createBody(bodyDef);

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(0.4f, 0.4f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;

        body.createFixture(fixtureDef);
        body.setUserData(id);
    }



    private ObjectFactory() {}

}
