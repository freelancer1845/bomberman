package de.riedelgames.bomberman;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.riedelgames.bomberman.screens.GameScreen;
import de.riedelgames.bomberman.screens.SettingsScreen;
import de.riedelgames.bomberman.screens.StartScreen;

public class InitialScreen extends Game {



    /** List containing all the screens. */
    private Screen[] screenArray = new Screen[3];



    private SpriteBatch batch;
    private Texture img;

    @Override
    public void create() {

        batch = new SpriteBatch();
        this.setScreen(new GameScreen(this));



    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {

    }

    /**
     * Sets the StartScreen as current Screen.
     */
    public void setStartScreen() {
        if (screenArray[0] == null) {
            screenArray[0] = new StartScreen();
        }
        this.setScreen(screenArray[0]);
    }

    /**
     * Sets the SettingsScreen as current Screen.
     */
    public void setSettingsScreen() {
        if (screenArray[1] == null) {
            screenArray[1] = new SettingsScreen();
        }
        this.setScreen(screenArray[1]);
    }

    /**
     * Starts the game loading the current settings.
     */
    public void startGame() {
        if (screenArray[2] != null) {
            screenArray[2].dispose();
            screenArray[2] = null;
        }
        screenArray[2] = new GameScreen(this);
        setScreen(screenArray[2]);
        //this.setScreen(screenArray[2]);
        Gdx.app.log("startGame", "gamefunctionexecuted " + screenArray[2]);
    }
}
