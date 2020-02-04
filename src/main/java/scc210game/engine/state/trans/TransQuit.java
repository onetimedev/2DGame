package scc210game.engine.state.trans;

/**
 * Shut down the game
 * <p>
 * Bye! (=ﾟωﾟ)ﾉ
 */
public class TransQuit implements Transition {
    private static final TransQuit instance = new TransQuit();

    public static TransQuit getInstance() {
        return TransQuit.instance;
    }
}
