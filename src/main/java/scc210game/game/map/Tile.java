package scc210game.game.map;

import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import java.io.IOException;
import java.nio.file.Paths;


public class Tile {
    private Texture tileTexture = new Texture();
    private Vector2i textureSize;
    private int xPos;
    private int yPos;
    private Boolean hasCollision;


    public Tile(String fileName, int x, int y, Boolean collision) {
        String assetsPath = "./src/main/assets/";
        xPos = x;
        yPos = y;
        hasCollision = collision;

        try {
            tileTexture.loadFromFile(Paths.get(assetsPath, fileName));
            textureSize = tileTexture.getSize();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public Vector2f getPosition() {
        return new Vector2f(xPos, yPos);
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

    public Texture getTexture() {
        return tileTexture;
    }

    public Boolean hasCollision() {
        return hasCollision;
    }

    public Vector2i getTextureSize() {
        return textureSize;
    }
}
