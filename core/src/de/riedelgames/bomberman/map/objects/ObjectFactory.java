package de.riedelgames.bomberman.map.objects;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
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
     * @return {@link Body} the created body;
     */
    public Body createSolidBlock(int xGridPosition, int yGridPosition) {
        return createSolidStack(xGridPosition, yGridPosition, 1, 1);
    }

    /**
     * Creates a stack of solid blocks. (Only one rectangle in the World).
     * 
     * @param xGridPosition position in Grid
     * @param yGridPosition position in Grid
     * @param xBlocks count
     * @param yBlocks count
     * @return {@link Body} the created body;
     */
    public Body createSolidStack(int xGridPosition, int yGridPosition, int xBlocks, int yBlocks) {
        return createBlock(xGridPosition, yGridPosition, xBlocks, yBlocks, GameConstants.BLOCK_ID);
    }

    /**
     * Creates a destructible block in the world.
     * 
     * @return {@link Body} the created body;
     */
    public Body createDestructibleBlock(int xGridPosition, int yGridPosition) {
        return createBlock(xGridPosition, yGridPosition, 1, 1, GameConstants.DESTRUCTIBLE_BLOCK_ID);
    }

    /**
     * Creates a player object with a given id.
     * 
     * @return {@link Body} the created body;
     */
    public Body createPlayer(int xGridPosition, int yGridPosition, String id) {
        if (id.startsWith(GameConstants.PLAYER_ID_PREFIX)) {
            return createPlayerObject(xGridPosition, yGridPosition, id);
        } else {
            return createPlayerObject(xGridPosition, yGridPosition,
                    GameConstants.PLAYER_ID_PREFIX + id);
        }

    }

    /**
     * Creates a Bomb that will explode after the given time.
     * 
     * @param xGridPosition of the bomb.
     * @param yGridPosition of the bomb.
     * @return {@link Body} the created body;
     */
    public Body createBomb(int xGridPosition, int yGridPosition) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(xGridPosition + 0.5f, yGridPosition + 0.5f);

        final Body body;
        body = GameScreen.world.createBody(bodyDef);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(0.1f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.isSensor = true;

        body.createFixture(fixtureDef);
        body.setUserData(GameConstants.BOMB_ID);

        return body;
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


    private Body createBlock(int xGridPosition, int yGridPosition, int xBlocks, int yBlocks,
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

        return body;

    }

    private Body createPlayerObject(int xGridPosition, int yGridPosition, String id) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(xGridPosition + 0.5f, yGridPosition + 0.5f);

        Body body;
        body = GameScreen.world.createBody(bodyDef);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(0.45f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.filter.categoryBits = 0x0002;
        fixtureDef.filter.maskBits = 0x0001;

        body.createFixture(fixtureDef);
        body.setUserData(id);
        return body;
    }



    private ObjectFactory() {}

}
