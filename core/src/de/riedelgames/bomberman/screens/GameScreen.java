package de.riedelgames.bomberman.screens;

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
            playersRegistry.registerPlayer("THIRD_PLAYER");
            playersRegistry.registerPlayer("FOURTH_PLAYER");
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
        }
        return playersRegistry.getPlayer("FIRST_PLAYER").addDirection(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
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
