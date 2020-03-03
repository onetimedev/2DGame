package scc210game.engine.ui.components;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import org.jsfml.system.Vector2f;
import scc210game.engine.ecs.Component;


/**
 * Component that describes entities that have a UI position on the screen
 */
public class UITransform extends Component {
    static {
        register(UITransform.class, j -> {
            var json = (JsonObject) j;

            Float originXPos = (Float) json.get("originXPos");
            Float originYPos = (Float) json.get("originYPos");
            Float xPos = (Float) json.get("xPos");
            Float yPos = (Float) json.get("yPos");
            Integer zPos = (Integer) json.get("zPos");
            Float width = (Float) json.get("width");
            Float height = (Float) json.get("height");
            Float rotation = (Float) json.get("rotation");

            return new UITransform(originXPos, originYPos, xPos, yPos, zPos, width, height, rotation);

        });
    }

    /**
     * Original offset of the left of the transform from the left side of the screen in percentage of the screen
     * <p>
     * When being dragged, this is the original place of the element
     * <p>
     * 0.0 = Left
     * 1.0 = Right
     */
    public float originXPos;

    /**
     * Original of the top of the transform from the top of the screen in percentage of the screen
     * <p>
     * When being dragged, this is the original place of the element
     * <p>
     * 0.0 = Top
     * 1.0 = Bottom
     */
    public float originYPos;

    /**
     * Offset of the left of the transform from the left side of the screen in percentage of the screen
     * <p>
     * When being dragged, this is updated to the current offset
     * <p>
     * 0.0 = Left
     * 1.0 = Right
     */
    public float xPos;

    /**
     * Offset of the top of the transform from the top of the screen in percentage of the screen
     *
     * When being dragged, this is updated to the current offset
     *
     * 0.0 = Top
     * 1.0 = Bottom
     */
    public float yPos;

    /**
     * The depth of the ui element
     */
    public int zPos;

    /**
     * Width on the screen in percentage
     */
    public float width;

    /**
     * Height on the screen in percentage
     */
    public float height;

    /**
     * rotation in degrees
     */
    public float rotation;

    public UITransform(UITransform uiTransform)
    {
        this.originXPos = uiTransform.originXPos;
        this.originYPos = uiTransform.originYPos;
        this.xPos = uiTransform.xPos;
        this.yPos = uiTransform.yPos;
        this.zPos = uiTransform.zPos;
        this.width = uiTransform.width;
        this.height = uiTransform.height;
        this.rotation = uiTransform.rotation;
    }

    public UITransform(float originXPos, float originYPos, float xPos, float yPos, int zPos, float width, float height) {
        this.originXPos = originXPos;
        this.originYPos = originYPos;
        this.xPos = xPos;
        this.yPos = yPos;
        this.zPos = zPos;
        this.width = width;
        this.height = height;
    }

    public UITransform(float xPos, float yPos, int zPos, float width, float height) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.originXPos = xPos;
        this.originYPos = yPos;
        this.zPos = zPos;
        this.width = width;
        this.height = height;
    }


    public UITransform(float xPos, float yPos, int zPos, float width, float height, float rotation) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.originXPos = xPos;
        this.originYPos = yPos;
        this.zPos = zPos;
        this.width = width;
        this.height = height;
        this.rotation = rotation;
    }

    public UITransform(float originXPos, float originYPos, float xPos, float yPos, int zPos, float width, float height, float rotation) {
        this.originXPos = originXPos;
        this.originYPos = originYPos;
        this.xPos = xPos;
        this.yPos = yPos;
        this.zPos = zPos;
        this.width = width;
        this.height = height;
        this.rotation = rotation;
    }


    public Vector2f pos() {
        return new Vector2f(this.xPos, this.yPos);
    }

    public Vector2f size() {
        return new Vector2f(this.width, this.height);
    }

    /**
     * Update the position of the transform and set the origin positions too.
     *
     * @param x the new x position
     * @param y the new y position
     */
    public void updateOrigin(float x, float y) {
        this.originXPos = x;
        this.xPos = x;
        this.originYPos = y;
        this.yPos = y;
    }

    @Override
    public Jsonable serialize() {
        final Jsonable json = new JsonObject() {{
            this.put("originXPos", UITransform.this.originXPos);
            this.put("originYPos", UITransform.this.originYPos);
            this.put("xPos", UITransform.this.xPos);
            this.put("yPos", UITransform.this.yPos);
            this.put("zPos", UITransform.this.zPos);
            this.put("width", UITransform.this.width);
            this.put("height", UITransform.this.height);
            this.put("rotation", UITransform.this.rotation);
        }};

        return json;
    }

    /**
     * Test if the given coordinates are in the area described by this ui transform.
     *
     * @param x the X coordinate in percentage from the left of the screen
     * @param y the Y coordinate in percentage from the bottom of the screen
     * @return whether the given coordinates are in the area described by this ui transform.
     */
    public boolean contains(float x, float y) {
        final float xLowBound = this.xPos;
        final float xUppBound = this.xPos + this.width;

        final float yLowBound = this.yPos;
        final float yUppBound = this.yPos + this.height;

        return (x > xLowBound &&
                x < xUppBound &&
                y > yLowBound &&
                y < yUppBound);
    }

    @Override
    public UITransform copy() {
        return new UITransform(this.originXPos, this.originYPos, this.xPos, this.yPos, this.zPos, this.width, this.height);
    }
}
