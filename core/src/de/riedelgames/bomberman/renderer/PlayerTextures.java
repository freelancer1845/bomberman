package de.riedelgames.bomberman.renderer;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;


public class PlayerTextures extends Sprite {


    /**
     * An Enum that contains all the possible movement states of One Player. For SIDEWALKING exists
     * only the texture for SIDEWALKINGRIGHT, which will be flipped for SIDEWALKINGLEFT.
     */
    public enum State {
        BACKSTANDING, FRONTSTANDING, SIDESTANDINGRIGHT, SIDESTANDINGLEFT, BACKWALKING, FRONTWALKING, SIDEWALKINGRIGHT, SIDEWALKINGLEFT, DEATH;
    }

    /**
     * State variables to reach the correct textures. E.g. when walking in the FRONTWALKING texture,
     * FRONTSTANDING must be reached, instead of BACKSTANDING.
     */
    public State currentState;
    public State previousState;


    private TextureRegion playerStandBack;
    private TextureRegion playerStandFront;
    private TextureRegion playerStandSideRight;
    private TextureRegion playerStandSideLeft;

    private TextureRegion player1walkingside1;
    private TextureRegion player1walkingside2;

    private Animation walkingBack;
    private Animation walkingFront;
    private Animation sideWalkingLeft;
    private Animation sideWalkingRight;
    private Animation death;

    private String animationRegion;

    private final Body body;

    private float stateTime;
    private boolean runningRight;



    public PlayerTextures(Body body, float x, float y) {

        super(AssetLoader.textureAtlas.findRegion("player1front"));
        this.body = body;


        currentState = State.FRONTSTANDING;
        previousState = State.BACKSTANDING;

        stateTime = 0;
        runningRight = true;

        initializeTextureRegions();
        initializeAnimations();


        setBounds(x, y, 1f, 1f);
        setRegion(playerStandFront);
    }

    private void initializeTextureRegions() {


        player1walkingside1 =
                new TextureRegion(AssetLoader.textureAtlas.findRegion("player1walkingside1"));
        player1walkingside2 =
                new TextureRegion(AssetLoader.textureAtlas.findRegion("player1walkingside2"));



        playerStandBack = new TextureRegion(AssetLoader.textureAtlas.findRegion("player1back"));
        playerStandFront = new TextureRegion(AssetLoader.textureAtlas.findRegion("player1front"));
        playerStandSideRight =
                new TextureRegion(AssetLoader.textureAtlas.findRegion("player1standingside"));

        playerStandSideRight.flip(true, false);
        playerStandSideLeft = playerStandSideRight;
        playerStandSideRight.flip(true, false);

    }

    private void initializeAnimations() {

        Array<TextureRegion> frames = new Array<TextureRegion>();


        frames.add(
                new TextureRegion(AssetLoader.textureAtlas.findRegion("player1backwalkingright")));
        frames.add(
                new TextureRegion(AssetLoader.textureAtlas.findRegion("player1backwalkingleft")));
        walkingBack = new Animation(0.1f, frames);
        frames.clear();

        frames.add(
                new TextureRegion(AssetLoader.textureAtlas.findRegion("player1frontwalkingright")));
        frames.add(
                new TextureRegion(AssetLoader.textureAtlas.findRegion("player1backwalkingleft")));
        walkingFront = new Animation(0.1f, frames);
        frames.clear();

        frames.add(player1walkingside1);
        frames.add(player1walkingside2);
        sideWalkingRight = new Animation(0.1f, frames);
        frames.clear();

        player1walkingside1.flip(true, false);
        player1walkingside2.flip(true, false);
        frames.add(player1walkingside1);
        frames.add(player1walkingside2);
        sideWalkingLeft = new Animation(0.1f, frames);
        frames.clear();

        for (int i = 1; i < 9; i++) {
            animationRegion = "player1death" + i;
            frames.add(new TextureRegion(AssetLoader.textureAtlas.findRegion(animationRegion)));
        }
        death = new Animation(0.1f, frames);
        frames.clear();

    }

    public void update(float dt) {

        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));

    }

    public Body getBody() {
        return body;
    }


    public TextureRegion getFrame(float dt) {

        currentState = getState();

        TextureRegion region = null;
        switch (currentState) {
            case BACKSTANDING:
                region = playerStandBack;
                break;
            case FRONTSTANDING:
                region = playerStandFront;
                break;
            case SIDESTANDINGRIGHT:
                region = playerStandSideRight;
                break;
            case SIDESTANDINGLEFT:
                region = playerStandSideLeft;
                break;
            case BACKWALKING:
                region = walkingBack.getKeyFrame(stateTime);
                break;
            case FRONTWALKING:
                region = walkingFront.getKeyFrame(stateTime);
                break;
            case SIDEWALKINGRIGHT:
                region = sideWalkingLeft.getKeyFrame(stateTime);
                break;
            case SIDEWALKINGLEFT:
                region = sideWalkingRight.getKeyFrame(stateTime);
                break;
            case DEATH:
                region = death.getKeyFrame(stateTime);
                break;
            default:
                break;
        }
        stateTime = currentState == previousState ? stateTime + dt : 0;
        previousState = currentState;
        return region;
    }

    public State getState() {

        if (body.getLinearVelocity().x < 0)
            return State.SIDEWALKINGLEFT;
        else if (body.getLinearVelocity().x > 0)
            return State.SIDEWALKINGRIGHT;
        else if (body.getLinearVelocity().x == 0 && previousState == State.SIDEWALKINGLEFT)
            return State.SIDESTANDINGLEFT;
        else if (body.getLinearVelocity().x == 0 && previousState == State.SIDEWALKINGRIGHT)
            return State.SIDESTANDINGRIGHT;
        else if (body.getLinearVelocity().x == 0 && previousState == State.BACKWALKING)
            return State.BACKSTANDING;
        else if (body.getLinearVelocity().x == 0 && previousState == State.FRONTWALKING)
            return State.FRONTSTANDING;
        else if (body.getLinearVelocity().y > 0)
            return State.FRONTWALKING;
        else if (body.getLinearVelocity().y < 0)
            return State.BACKWALKING;
        else
            return State.FRONTSTANDING;

    }

}
