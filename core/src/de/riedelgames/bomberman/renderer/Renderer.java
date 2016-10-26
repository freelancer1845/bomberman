package de.riedelgames.bomberman.renderer;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import de.riedelgames.bomberman.GameConstants;
import de.riedelgames.bomberman.screens.GameScreen;

public class Renderer {

    private Array<Body> bodies = new Array<Body>();
    private List<PlayerTextures> playerTexturesList = new ArrayList<PlayerTextures>(1);
    private List<BlockTextures> blockTexturesList = new ArrayList<BlockTextures>();

    private PlayerTextures currentPlayerTexture;
    private BlockTextures currentBlockTexture;

    private World world;



    public Renderer(World world) {

        this.world = world;


    }


    public void render() {



        removeDestroyedTextures();
        world.getBodies(bodies);


        for (Body body : bodies) {

            addNewTextures(body);

            if (((String) body.getUserData()).startsWith(GameConstants.BLOCK_ID)) {
                for (BlockTextures blockTexture : blockTexturesList) {
                    if (blockTexture.getBody().equals(body)) {
                        currentBlockTexture = blockTexture;
                        break;
                    }

                    renderBlock(blockTexture);
                }



            }

            if ((((String) body.getUserData()).startsWith(GameConstants.PLAYER_ID_PREFIX))) {
                for (PlayerTextures playerTexture : playerTexturesList) {
                    if (playerTexture.getBody().equals(body)) {
                        currentPlayerTexture = playerTexture;
                        break;
                    }
                }
                if (currentPlayerTexture != null) {

                    renderPlayer(currentPlayerTexture);

                }
            }



        }

    }

    public static void renderBlock(BlockTextures blockTexture) {
        blockTexture.draw(GameScreen.getSpriteBatch());

    }

    public static void renderPlayer(PlayerTextures playerTexture) {
        playerTexture.draw(GameScreen.getSpriteBatch());
    }


    private void removeDestroyedTextures() {
        List<PlayerTextures> toRemove = new ArrayList<PlayerTextures>();
        for (PlayerTextures playerTexture : playerTexturesList) {
            if (!bodies.contains(playerTexture.getBody(), true)) {
                toRemove.add(playerTexture);
            }
        }
        playerTexturesList.removeAll(toRemove);

        List<BlockTextures> toRemoveBlocks = new ArrayList<BlockTextures>();
        for (BlockTextures blockTexture : blockTexturesList) {
            if (!bodies.contains(blockTexture.getBody(), true)) {
                toRemoveBlocks.add(blockTexture);
            }
        }
        blockTexturesList.removeAll(toRemoveBlocks);


    }

    private void addNewTextures(Body body) {
        if (((String) body.getUserData()).startsWith(GameConstants.PLAYER_ID_PREFIX)) {
            boolean alreadyListed = false;
            /*
             * if (playerTexturesList != null) { for (PlayerTextures playerTexture :
             * playerTexturesList) { if (playerTexture.getBody().equals(body)) { alreadyListed =
             * true; break; } } }
             */


            if (!alreadyListed) {
                playerTexturesList.add(new PlayerTextures(body, body.getPosition().x - 0.5f,
                        body.getPosition().y - 0.5f));
            }

        }

        if (((String) body.getUserData()).startsWith(GameConstants.BLOCK_ID)) {
            boolean alreadyListed = false;
            for (BlockTextures blockTexture : blockTexturesList) {
                if (blockTexture.getBody().equals(body)) {
                    alreadyListed = true;
                    break;
                }
            }

            if (!alreadyListed) {
                blockTexturesList.add(new BlockTextures(body, body.getPosition().x - 0.5f,
                        body.getPosition().y - 0.5f,
                        AssetLoader.textureAtlas.findRegion("SolidBlock")));
            }

        }

        if (((String) body.getUserData()).startsWith(GameConstants.DESTRUCTIBLE_BLOCK_ID)) {
            boolean alreadyListed = false;
            for (BlockTextures blockTexture : blockTexturesList) {
                if (blockTexture.getBody().equals(body)) {
                    alreadyListed = true;
                    break;
                }
            }

            if (!alreadyListed) {
                blockTexturesList.add(new BlockTextures(body, body.getPosition().x - 0.5f,
                        body.getPosition().y - 0.5f,
                        AssetLoader.textureAtlas.findRegion("DestructableBlock")));
            }

        }



    }

    /*
     * private List<Body> orderList(Array<Body> unOrderedList) {
     * 
     * List<Body> orderedList = new ArrayList<Body>();
     * 
     * for (Body body : unOrderedList) { if (body.getUserData().equals(GameConstants.BOMB_ID)) {
     * orderedList.add(body); } }
     * 
     * for (Body body : unOrderedList) { if (body.getUserData().equals(GameConstants.BLOCK_ID)) {
     * orderedList.add(body); } }
     * 
     * for (Body body : unOrderedList) { if
     * (body.getUserData().equals(GameConstants.DESTRUCTIBLE_BLOCK_ID)) { orderedList.add(body); } }
     * 
     * for (Body body : unOrderedList) { if
     * (body.getUserData().equals(GameConstants.BOMB_COUNT_POWER_UP_ID)) { orderedList.add(body); }
     * }
     * 
     * for (Body body : unOrderedList) { if
     * (body.getUserData().equals(GameConstants.BOMB_COUNT_POWER_UP_ID)) { orderedList.add(body); }
     * }
     * 
     * for (Body body : unOrderedList) { if
     * (body.getUserData().equals(GameConstants.BOMB_RANGE_POWER_UP_ID)) { orderedList.add(body); }
     * }
     * 
     * for (Body body : unOrderedList) { if
     * (body.getUserData().equals(GameConstants.PLAYER_ID_PREFIX)) { orderedList.add(body); } }
     * 
     * return orderedList; }
     */


    private void renderBomb(Body body) {};

    private void renderDestructableBlock(Body body) {}


    public List<PlayerTextures> getPlayerTexturesList() {
        return playerTexturesList;
    }


    public void setPlayerTexturesList(List<PlayerTextures> playerTexturesList) {
        this.playerTexturesList = playerTexturesList;
    };



}
