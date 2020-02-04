package scc210game.items;

import org.jsfml.graphics.RectangleShape;
import org.jsfml.system.Vector2f;
import scc210game.engine.ui.spawners.DroppableBoxSpawner;

public class Square extends DroppableBoxSpawner {

        public Square(float x, float y, float width, float height ) {
            super(x, y, width, height);
        }

}
