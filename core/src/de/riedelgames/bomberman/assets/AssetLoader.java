package de.riedelgames.bomberman.assets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * Loads all the textures, animation, sounds and other releveant stuff
 *
 * @author Nikolai Riedel
 */
public class AssetLoader {

    private static TextureAtlas textureAtlas;
    private static TextureRegion solidBlock, destructableBlock, bomb,
                          player1Back, player1BackWalkingLeft, player1BackWalkingRight,
                          player1Death1, player1Death2, player1Death3, player1Death4, player1Death5, player1Death6, player1Death7, player1Death8,
                          player1Front, player1FrontWalkingLeft, player1FrontWalkingRight, player1StandingSide, player1WalkingSide1, player1WalkingSide2;

    private static Animation player1BackWalking, player1FrontWalking, player1Death, player1WalkingLeft, player1WalkingRight, player1WalkingSide;

    public AssetLoader(){

        initTextures();

    }

    /** Function that loads all the textures */

    private void initTextures(){

        textureAtlas = new TextureAtlas("textures.pack");



        /*player1Back = textureAtlas.findRegion("player1back");
        player1BackWalkingLeft = textureAtlas.findRegion("player1backwalkingleft");
        player1BackWalkingRight = textureAtlas.findRegion("player1backwalkingright");

        player1Death1 = textureAtlas.findRegion("player1death1");
        player1Death2 = textureAtlas.findRegion("player1death2");
        player1Death3 = textureAtlas.findRegion("player1death3");
        player1Death4 = textureAtlas.findRegion("player1death4");
        player1Death5 = textureAtlas.findRegion("player1death5");
        player1Death6 = textureAtlas.findRegion("player1death6");
        player1Death7 = textureAtlas.findRegion("player1death7");
        player1Death8 = textureAtlas.findRegion("player1death8");

        player1Front = textureAtlas.findRegion("player1front");
        player1FrontWalkingLeft = textureAtlas.findRegion("player1frontwalkingleft");
        player1FrontWalkingRight = textureAtlas.findRegion("player1frontwalkingright");
        player1StandingSide = textureAtlas.findRegion("player1standingside");
        player1WalkingSide1 = textureAtlas.findRegion("player1walkingside1");
        player1WalkingSide2 = textureAtlas.findRegion("player1walkingside2");*/



    }


}
