/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package scc210game;

import scc210game.game.Main;


public class App {
    public static void main(String[] args) {
        System.out.println("Hello world: " + System.getProperty("os.name"));
        if (System.getProperty("os.name").equals("Linux"))
            System.load(System.getProperty("user.dir") + "/libXinitThreads.so");
        Main.runForever();
    }
}
