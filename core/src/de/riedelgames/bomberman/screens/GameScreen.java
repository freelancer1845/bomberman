package de.riedelgames.bomberman.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.riedelgames.bomberman.GameConstants;
import de.riedelgames.bomberman.InitialScreen;
import de.riedelgames.bomberman.map.MapFactory;
import de.riedelgames.bomberman.map.objects.ObjectFactory;

/**
 * The screen underlying the game.
 * 
 * @author Jascha Riedel
 *
 */
public class GameScreen implements Screen {

    /** The world the game exists in. */
    public static World world;

    /** Reference to the game extending class. */
    private final Game game;

    /** Top Cam. */
    private OrthographicCamera gamecam;

    /** Viewport for the game. */
    private Viewport gamePort;

    /** Box2DRenderer */
    private Box2DDebugRenderer b2dr;

    /** Constructor. */
    public GameScreen(Game game) {
        this.game = game;

        gamecam = new OrthographicCamera();

        gamePort = new FitViewport(GameConstants.WORLD_WIDTH, GameConstants.WORLD_HEIGHT, gamecam);



        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, 0), true);

        MapFactory.getInstance().createMap();

        ObjectFactory.getInstance().createSolidStack(0,0, 1, 1);
        ObjectFactory.getInstance().createSolidStack(16,16,1,1);
        ObjectFactory.getInstance().createSolidStack(16,31,1,1);
        ObjectFactory.getInstance().createSolidStack(0, 0, 31, 31);

        Gdx.app.log("mapfactory createmap", "called");
        b2dr = new Box2DDebugRenderer();



    }

    @Override
    public void show() {
        // TODO Auto-generated method stub



    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Gdx.app.log("render", "rendering");
        b2dr.render(world, gamecam.combined);

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



}
