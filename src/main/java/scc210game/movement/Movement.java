package scc210game.movement;

import org.jsfml.window.Keyboard;
import scc210game.ecs.Component;

public class Movement extends Component {
    private int posX;
    private int posY;
    private char key;

    Movement(int posX, int posY) {
        this.posY = posY;
        this.posY = posY;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }
    public int getPosX() {
        return this.posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }
    public int getPosY() {
        return this.posY;
    }

    public void setKey(char key) {
        this.key = key;
    }
    public char getKey() {
        return this.key;
    }




   //Sets the direction the player is moving
   /*public int direction(char key) {
       if(key == 'W') {
           direc = 0; //set the direction angle forward
       }
       else if(key == 'A') {
           direc = 270; //set the direction angle left
       }
       else if(key == 'S') {
           direc = 180; //set the direction angle backward
       }
       else if(key == 'D') {
           direc = 90; //set the direction angle right
       }
       return direc;
   }*/

    @Override
    public String serialize() {
        return null;
    }
}

//if key w, a, s, d is pressed return true, if not return false
   /*public boolean keyPressed(Keyboard.Key W, Keyboard.Key A, Keyboard.Key S, Keyboard.Key D) {
       if(Keyboard.isKeyPressed(W)) {
           key = 'W';
           return true;
       }
       else if(Keyboard.isKeyPressed(A)) {
           key = 'A';
           return true;
       }
       else if(Keyboard.isKeyPressed(S)) {
           key = 'S';
           return true;
       }
       else if(Keyboard.isKeyPressed(D)) {
           key = 'D';
           return true;
       }
       else {
           return false;
       }
   }*/
