package scc210game.ui;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import com.github.cliftonlabs.json_simple.Jsoner;
import scc210game.ecs.Component;

import java.math.BigDecimal;


/**
 * Component that describes entities that have a UI position on the screen
 */
public class UITransform extends Component {
    static {
        register(UITransform.class, s -> {
            final JsonObject json = Jsoner.deserialize(s, new JsonObject());

            BigDecimal xPos = (BigDecimal) json.get("xPos");
            BigDecimal yPos = (BigDecimal) json.get("yPos");
            BigDecimal zPos = (BigDecimal) json.get("zPos");
            BigDecimal width = (BigDecimal) json.get("width");
            BigDecimal height = (BigDecimal) json.get("height");

            return new UITransform(xPos.floatValue(), yPos.floatValue(), zPos.intValue(), width.floatValue(), height.floatValue());
        });
    }

    /**
     * Offset of the centre of the transform from the left side of the screen in percentage of the screen
     * 0.0 = Left
     * 1.0 = Right
     */
    public float xPos;

    /**
     * Offset of the centre of the transform from the bottom of the screen in percentage of the screen
     * 0.0 = Bottom
     * 1.0 = Top
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

    public UITransform(float xPos, float yPos, int zPos, float width, float height) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.zPos = zPos;
        this.width = width;
        this.height = height;
    }

    @Override
    public String serialize() {
        final Jsonable json = new JsonObject() {{
            this.put("xPos", UITransform.this.xPos);
            this.put("yPos", UITransform.this.yPos);
            this.put("zPos", UITransform.this.zPos);
            this.put("width", UITransform.this.width);
            this.put("height", UITransform.this.height);
        }};

        return json.toJson();
    }

    /**
     * Test if the given coordinates are in the area described by this ui transform.
     *
     * @param x the X coordinate in percentage from the left of the screen
     * @param y the Y coordinate in percentage from the bottom of the screen
     * @return whether the given coordinates are in the area described by this ui transform.
     */
    public boolean contains(float x, float y) {
        final float halfWidth = this.width / 2f;
        final float halfHeight = this.height / 2f;

        final float xLowBound = this.xPos - halfWidth;
        final float xUppBound = this.xPos + halfWidth;

        final float yLowBound = this.yPos - halfHeight;
        final float yUppBound = this.yPos + halfHeight;

        return (x > xLowBound &&
                x < xUppBound &&
                y > yLowBound &&
                y < yUppBound);
    }
}
