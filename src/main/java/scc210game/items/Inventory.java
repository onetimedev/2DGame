package scc210game.items;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;


import org.jsfml.window.*;
import org.jsfml.graphics.*;
import org.jsfml.window.event.Event;


public class Inventory {

    public ArrayList<Item> inventory = new ArrayList<Item>(5);
    public CircleShape circle;
    public ArrayList<Square> squares = new ArrayList<>();
    protected RenderWindow window;

    public Inventory(RenderWindow window) {
        this.window = window;
        //createSquare();
        for (int i = 0; i < squares.size(); i++) {
            window.draw(squares.get(i));

        }

    }

    public class Square extends RectangleShape {
        public Square(Vector2f size) {
            super(size);
        }


    }

    public ArrayList<Square> createSquare(float x, float y) {
        Square[][] square = new Square[1][5];
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 5; j++) {
                square[i][j] = new Square(new Vector2f(50, 50));
                square[i][j].setFillColor(Color.YELLOW);
                square[i][j].setOutlineColor(Color.BLACK);
                square[i][j].setOutlineThickness(3);
                square[i][j].setOrigin(25, 25);
                square[i][j].setPosition((j * 50) + x, y);
                squares.add(square[i][j]);

            }
            System.out.println(squares.get(i).getPosition());
        }

        return squares;

    }


    public Item addItem(int id, int level, int damage, String desc) throws IOException {

        Weapon newItem = new Weapon(id, level, damage, desc );
        inventory.add(newItem);
        squares.get(inventory.indexOf(newItem)).setTexture(newItem.getTexture());
        return newItem;

    }

    public void removeItem(int pos) {
        if (inventory.size() != 1) {
            inventory.remove(pos);
        }

    }

    public void moveItem(Event e) {
        Vector2f pos;
        Vector2f pos2;
        Texture t;
        if (e.type == Event.Type.MOUSE_BUTTON_PRESSED && Mouse.getPosition() == new Vector2i(squares.get(0).getPosition())) {
            pos = new Vector2f(Mouse.getPosition());
            if (e.type == Event.Type.MOUSE_MOVED) {
                if (e.type == Event.Type.MOUSE_BUTTON_RELEASED) {
                    pos2 = new Vector2f(Mouse.getPosition());
                    if (pos2 == squares.get(1).getPosition()) {
                        t = (Texture) squares.get(0).getTexture();
                        squares.get(0).setFillColor(Color.YELLOW);
                        squares.get(1).setTexture(t);


                    }
                }
            }
        }


    }

    public void inventoryClick() {
        //mouse button event?
        for (Event event : window.pollEvents()) {
            switch (event.type) {
                case CLOSED:
                    System.out.println("The user pressed the close button!");
                    window.close();
                    break;

                case MOUSE_BUTTON_PRESSED:

                    System.out.print(Mouse.getPosition().toString());


            }


        }
    }


    public void create() {


    }
}










