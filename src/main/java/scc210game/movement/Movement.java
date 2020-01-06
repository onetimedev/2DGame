package scc210game.movement;

import org.jsfml.window.Keyboard;

public class Movement {
    char key;
    int direc = 0;

    //if key w, a, s, d is pressed return true, if not return false
   public boolean keyPressed(Keyboard.Key W, Keyboard.Key A, Keyboard.Key S, Keyboard.Key D) {
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
   }

   //Sets the direction the player is moving
   public int direction(char key) {
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
   }
}
