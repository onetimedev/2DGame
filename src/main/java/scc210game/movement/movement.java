package scc210game.movement;

import org.jsfml.window.Keyboard;

public class movement {
    char key;

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
}
