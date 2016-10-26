package de.riedelgames.bomberman.renderer;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class BlockTextures extends Sprite {

    private final Body body;



    public BlockTextures(Body body, float x, float y, TextureRegion textureRegion) {

        super(textureRegion);

        this.setBounds(x, y, 1f, 1f);
        this.body = body;

    }



    public Body getBody() {
        return body;
    }

}
