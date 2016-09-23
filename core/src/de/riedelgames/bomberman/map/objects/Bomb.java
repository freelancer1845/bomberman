package de.riedelgames.bomberman.map.objects;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import de.riedelgames.bomberman.GameConstants;
import de.riedelgames.bomberman.players.PlayersRegistry;
import de.riedelgames.bomberman.screens.GameScreen;

public class Bomb {

    /** Body of the bomb in the world. */
    private final Body body;


    private final int range;

    private boolean exploded = false;

    private PlayersRegistry playersRegistry;

    private final String owner;

    /**
     * Creates a new bomb that explodes after the given time in ms with the given range.
     * 
     * @param xGridPosition position
     * @param yGridPosition position
     * @param range range in grid units
     * @param clock delay in ms
     */
    public Bomb(int xGridPosition, int yGridPosition, int range, int clock, String owner) {
        body = ObjectFactory.getInstance().createBomb(xGridPosition, yGridPosition);
        body.getFixtureList().get(0).setUserData(this);
        this.range = range;
        this.owner = owner;
        Timer.instance().scheduleTask(new Task() {

            @Override
            public void run() {
                Bomb.this.explode();
            }

        }, clock / 1000.0f);
        playersRegistry = PlayersRegistry.getInstance();
        ObjectRegistry.getInstance().addBomb(this);
    }

    /**
     * This lets the bomb explode if it hasn't exploded before. It also does all the handling of the
     * explosion. (Call destroy method of players. Drop PowerUps.)
     */
    public void explode() {
        if (!exploded) {
            exploded = true;
            destroyObjectsInExplosionRadius();
            destroyPlayerIfItIsOnTheBomb();

            ObjectRegistry.getInstance().removeBomb(this);
            GameScreen.world.destroyBody(body);
        }



    }

    private void createHitCircle(float posX, float posY) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(posX, posY);

        final Body body;
        body = GameScreen.world.createBody(bodyDef);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(0.2f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.isSensor = true;

        body.createFixture(fixtureDef);
        body.setUserData("hitCircle");
        Timer.schedule(new Task() {

            @Override
            public void run() {
                GameScreen.world.destroyBody(body);

            }
        }, 2);

    }

    private void dropRandomUpgrade(Vector2 position) {
        Random randomGenerator = new Random();
        int deploySth = randomGenerator.nextInt(100);
        if (deploySth / 100.0f < GameConstants.UPGRADE_PERCENTAGE) {
            int whatToDeploy = randomGenerator.nextInt(2);
            if (whatToDeploy == 0) {
                deployBombUpgrade(position);
            } else if (whatToDeploy == 1) {
                deployRangeUpgrade(position);
            }
        }
    }

    private void deployBombUpgrade(Vector2 position) {
        ObjectFactory.getInstance().createBombCountPowerUp(Math.round(position.x - 0.5f),
                Math.round(position.y - 0.5f));
    }

    private void deployRangeUpgrade(Vector2 position) {
        ObjectFactory.getInstance().createBombRangePowerUp(Math.round(position.x - 0.5f),
                Math.round(position.y - 0.5f));
    }

    private void destroyObjectsInExplosionRadius() {
        // Do it for all 4 angles (0, 90, 180, 270)
        for (int i = 0; i < 4; i++) {
            float angle = (float) Math.toRadians((i / 4.0 * 360));
            Vector2 rayEnd = new Vector2((float) Math.sin(angle), (float) Math.cos(angle));
            rayEnd.scl(range);
            // The 0.499f assure that the next block is only almost reached.
            rayEnd.scl((rayEnd.len() + 0.499f) / rayEnd.len());
            rayEnd.add(body.getWorldCenter());

            final List<FractionObject> fractionObjectList = new ArrayList<FractionObject>();


            RayCastCallback rayCastCallback = new RayCastCallback() {

                @Override
                public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal,
                        float fraction) {
                    // createHitCircle(point.x, point.y);
                    if (fixture.getUserData() instanceof Bomb) {
                        fractionObjectList.add(new FractionObject(fraction, fixture.getBody()));
                        // Don't stop at bombs since they may have a lower blast radius
                        return -1;
                    } else if (fixture.getBody().getUserData()
                            .equals(GameConstants.DESTRUCTIBLE_BLOCK_ID)) {
                        fractionObjectList.add(new FractionObject(fraction, fixture.getBody()));
                        // stop at blocks
                        return fraction;
                    } else if (fixture.getBody().getUserData().equals(GameConstants.BLOCK_ID)) {
                        fractionObjectList.add(new FractionObject(fraction, fixture.getBody()));
                        // stop at blocks
                        return fraction;
                    } else if (fixture.getBody().getUserData() instanceof String
                            && (((String) fixture.getBody().getUserData())
                                    .startsWith(GameConstants.PLAYER_ID_PREFIX))
                            || fixture.getBody().getUserData()
                                    .equals(GameConstants.BOMB_COUNT_POWER_UP_ID)) {
                        fractionObjectList.add(new FractionObject(fraction, fixture.getBody()));
                        // Go through players and power ups
                        return -1;
                    }
                    return fraction;
                }
            };
            // The actual ray cast
            GameScreen.world.rayCast(rayCastCallback, body.getWorldCenter(), rayEnd);

            // Sort the result by fraction
            fractionObjectList.sort(new Comparator<FractionObject>() {

                @Override
                public int compare(FractionObject o1, FractionObject o2) {
                    if (o1.fraction < o2.fraction) {
                        return -1;
                    } else if (o1.fraction > o2.fraction) {
                        return 1;
                    }
                    return 0;
                }

            });

            // Destroy hit objects until a block is reached
            for (FractionObject fractionObject : fractionObjectList) {
                String id = (String) fractionObject.body.getUserData();
                if (id == null) {
                    continue;
                }
                if (id.equals(GameConstants.BLOCK_ID)) {
                    break;
                } else if (id.equals(GameConstants.DESTRUCTIBLE_BLOCK_ID)) {
                    dropRandomUpgrade(fractionObject.body.getPosition());
                    GameScreen.world.destroyBody(fractionObject.body);
                    break;
                } else if (id.startsWith(GameConstants.PLAYER_ID_PREFIX)) {
                    playersRegistry.getPlayer(id).destroy();
                } else if (id.equals(GameConstants.BOMB_COUNT_POWER_UP_ID)) {
                    GameScreen.world.destroyBody(fractionObject.body);
                } else if (id.equals(GameConstants.BOMB_ID)) {
                    ((Bomb) fractionObject.body.getFixtureList().get(0).getUserData()).explode();
                }

            }
        }
    }

    private void destroyPlayerIfItIsOnTheBomb() {
        for (Contact contact : GameScreen.world.getContactList()) {
            if (contact.getFixtureA() != null && contact.getFixtureA().getBody().equals(body)) {
                if (((String) contact.getFixtureB().getBody().getUserData())
                        .startsWith(GameConstants.PLAYER_ID_PREFIX)) {
                    playersRegistry
                            .getPlayer((String) contact.getFixtureB().getBody().getUserData())
                            .destroy();
                }

            } else if (contact.getFixtureB() != null
                    && contact.getFixtureB().getBody().equals(body)) {
                if (((String) contact.getFixtureA().getBody().getUserData())
                        .startsWith(GameConstants.PLAYER_ID_PREFIX)) {
                    playersRegistry
                            .getPlayer((String) contact.getFixtureA().getBody().getUserData())
                            .destroy();
                }

            }
        }
    }

    public String getOwner() {
        return owner;
    }

    public Body getBody() {
        return body;
    }

    private class FractionObject {
        public final float fraction;

        public final Body body;

        public FractionObject(float fraction, Body body) {
            this.fraction = fraction;
            this.body = body;
        }
    }
}
