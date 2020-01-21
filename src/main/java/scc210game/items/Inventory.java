import java.util.ArrayList;

import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;
import org.jsfml.window.*;
import org.jsfml.graphics.*;
import org.jsfml.window.event.Event;

public class Inventory{

    public static ArrayList<Item> inventory = new ArrayList<Item>(5);
    public static CircleShape circle;
    public static ArrayList<RectangleShape> squares;

    public Inventory() {



    }
    public class Square extends RectangleShape{
        public Square(Vector2f size){
            super(size);
        }
    }

    public ArrayList<Square> createSquare(){
        Square[][] square = new Square[1][5];
        ArrayList<Square> squares1 = new ArrayList<>();
        for (int i = 0; i < 1; i++){
            int y = i * 50;
            for (int j = 0; j < 5; j++){
                square[i][j] = new Square( new Vector2f(50, 50));
                square[i][j].setPosition(j * 50, y);
                squares1.add(square[i][j]);

            }
        }
        return squares1;

    }




    public static Item addItem(String name, int id, int level){
        Item newItem = new Item(name, id, level);
        inventory.add(newItem);
        circle = new CircleShape(10);
        circle.setFillColor(Color.RED);
        return newItem;

    }

    public void removeItem(int pos){
        if (inventory.size() != 1) {
            inventory.remove(pos);
        }

    }

    public void moveItem(Event e){
        if (Keyboard.isKeyPressed(Keyboard.Key.Z)){

        }


    }

    public void inventoryClick(){
       //mouse button event?

    }


    public static void create(){


    }
    public static void main(String[] args){
        //Create the window
        RenderWindow window = new RenderWindow();
        window.create(new VideoMode(640, 480), "Hello JSFML!");

//Limit the framerate
        window.setFramerateLimit(30);
        addItem("cir", 1, 1);


        //ArrayList<Square> newsquare = createSquare();

        while(window.isOpen()) {
            window.clear(Color.WHITE);

            window.draw(circle);


            //Display what was drawn (... the red color!)
            window.display();

            //Handle events
            for(Event event : window.pollEvents()) {
                if(event.type == Event.Type.CLOSED) {
                    //The user pressed the close button
                    window.close();
                }
            }
        }

    }






}
