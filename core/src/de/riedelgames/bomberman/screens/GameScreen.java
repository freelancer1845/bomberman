package de.riedelgames.bomberman.screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.riedelgames.bomberman.GameConstants;
import de.riedelgames.bomberman.map.MapFactory;
import de.riedelgames.bomberman.players.Player;
import de.riedelgames.bomberman.players.PlayerException;
import de.riedelgames.bomberman.players.PlayersRegistry;

/**
 * The screen underlying the game.
 * 
 * @author Jascha Riedel
 *
 */
public class GameScreen implements Screen, InputProcessor {

    /** The world the game exists in. */
    public static World world;

    /** Reference to the game extending class. */
    private final Game game;

    /** Top Cam. */
    private OrthographicCamera gamecam;

    /** Viewport for the game. */
    private Viewport gamePort;

    /** Box2DRenderer. */
    private Box2DDebugRenderer b2dr;

    /** The Player Registry. */
    private PlayersRegistry playersRegistry;

    /** Bodies scheduled to destroy. */
    private List<Body> bodiesToDestroy = new ArrayList<Body>();

    /** Constructor. */
    public GameScreen(Game game) {
        this.game = game;

        gamecam = new OrthographicCamera();

        gamePort = new FitViewport(GameConstants.WORLD_WIDTH + 5, GameConstants.WORLD_HEIGHT + 5,
                gamecam);



        gamecam.position.set(GameConstants.WORLD_WIDTH / 2.0f, GameConstants.WORLD_HEIGHT / 2.0f,
                0);

        createWorld();

        playersRegistry = PlayersRegistry.getInstance();
        try {
            playersRegistry.registerPlayer("FIRST_PLAYER");
            playersRegistry.registerPlayer("SECOND_PLAYER");
            // playersRegistry.registerPlayer("THIRD_PLAYER");
            // playersRegistry.registerPlayer("FOURTH_PLAYER");
            playersRegistry.placeRegisteredPlayersInWorld();
        } catch (PlayerException e) {
            System.out.println(e.getMessage());
        }



        MapFactory.getInstance().createMap();


        b2dr = new Box2DDebugRenderer();

        Gdx.input.setInputProcessor(this);


    }

    private void createWorld() {
        world = new World(new Vector2(0, 0), true);

        world.setContactListener(new ContactListener() {

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
                // TODO Auto-generated method stub

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
                // TODO Auto-generated method stub

            }

            @Override
            public void endContact(Contact contact) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beginContact(Contact contact) {
                if (!handlePowerUps(contact)) {
                    // Do other stuff
                }
            }
        });


    }

    private void updateMovement() {
        for (Player player : playersRegistry.getPlayers()) {
            Vector2 velocity = new Vector2(0, 0);
            if (player.getMovement()[0]) {
                velocity.y += GameConstants.STANDARD_VELOCITY;
            }
            if (player.getMovement()[1]) {
                velocity.y -= GameConstants.STANDARD_VELOCITY;
            }
            if (player.getMovement()[2]) {
                velocity.x -= GameConstants.STANDARD_VELOCITY;
            }
            if (player.getMovement()[3]) {
                velocity.x += GameConstants.STANDARD_VELOCITY;
            }
            Body body = player.getBody();
            body.setLinearVelocity(velocity);
        }
    }

    private void deleteBodiestoDestroy() {
        for (Body body : bodiesToDestroy) {
            world.destroyBody(body);
        }
        bodiesToDestroy.clear();
    }

    private boolean handlePowerUps(Contact contact) {
        if (contact.getFixtureA().getBody().getUserData() != null
                && contact.getFixtureB().getBody().getUserData() != null) {
            if (((String) contact.getFixtureA().getBody().getUserData())
                    .startsWith(GameConstants.PLAYER_ID_PREFIX)) {
                if (contact.getFixtureB().getBody().getUserData()
                        .equals(GameConstants.BOMB_COUNT_POWER_UP_ID)) {
                    playersRegistry
                            .getPlayer((String) contact.getFixtureA().getBody().getUserData())
                            .increaseMaxBombs();
                    bodiesToDestroy.add(contact.getFixtureB().getBody());
                } else if (contact.getFixtureB().getBody().getUserData()
                        .equals(GameConstants.BOMB_RANGE_POWER_UP_ID)) {
                    playersRegistry
                            .getPlayer((String) contact.getFixtureA().getBody().getUserData())
                            .increaseBombRange();
                    bodiesToDestroy.add(contact.getFixtureB().getBody());
                }
            } else if (((String) contact.getFixtureB().getBody().getUserData())
                    .startsWith(GameConstants.PLAYER_ID_PREFIX)) {
                if (contact.getFixtureA().getBody().getUserData()
                        .equals(GameConstants.BOMB_COUNT_POWER_UP_ID)) {
                    playersRegistry
                            .getPlayer((String) contact.getFixtureB().getBody().getUserData())
                            .increaseMaxBombs();
                    bodiesToDestroy.add(contact.getFixtureB().getBody());
                } else if (contact.getFixtureA().getBody().getUserData()
                        .equals(GameConstants.BOMB_RANGE_POWER_UP_ID)) {
                    playersRegistry
                            .getPlayer((String) contact.getFixtureB().getBody().getUserData())
                            .increaseBombRange();
                    bodiesToDestroy.add(contact.getFixtureA().getBody());
                }
            }
        }

        return false;
    }


    @Override
    public void show() {
        // TODO Auto-generated method stub



    }

    @Override
    public void render(float delta) {
        updateMovement();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Gdx.app.log("render", "rendering");
        b2dr.render(world, gamecam.combined);
        world.step(delta, 6, 2);

        deleteBodiestoDestroy();

    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);


    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        world.dispose();
        world = null;
        Gdx.app.log("dispose method gamescreen", "called");

    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.SPACE) {
            playersRegistry.getPlayer("FIRST_PLAYER").plantBomb();
            return true;
        } else if (keycode == Input.Keys.SHIFT_LEFT) {
            playersRegistry.getPlayer("SECOND_PLAYER").plantBomb();
            return true;
        } else if (keycode == Input.Keys.W) {
            return playersRegistry.getPlayer("SECOND_PLAYER").addDirection(Input.Keys.UP);
        } else if (keycode == Input.Keys.S) {
            return playersRegistry.getPlayer("SECOND_PLAYER").addDirection(Input.Keys.DOWN);
        } else if (keycode == Input.Keys.A) {
            return playersRegistry.getPlayer("SECOND_PLAYER").addDirection(Input.Keys.LEFT);
        } else if (keycode == Input.Keys.D) {
            return playersRegistry.getPlayer("SECOND_PLAYER").addDirection(Input.Keys.RIGHT);
        }
        return playersRegistry.getPlayer("FIRST_PLAYER").addDirection(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.W) {
            return playersRegistry.getPlayer("SECOND_PLAYER").removeDirection(Input.Keys.UP);
        } else if (keycode == Input.Keys.S) {
            return playersRegistry.getPlayer("SECOND_PLAYER").removeDirection(Input.Keys.DOWN);
        } else if (keycode == Input.Keys.A) {
            return playersRegistry.getPlayer("SECOND_PLAYER").removeDirection(Input.Keys.LEFT);
        } else if (keycode == Input.Keys.D) {
            return playersRegistry.getPlayer("SECOND_PLAYER").removeDirection(Input.Keys.RIGHT);
        }
        return playersRegistry.getPlayer("FIRST_PLAYER").removeDirection(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        // TODO Auto-generated method stub
        return false;
    }



}
