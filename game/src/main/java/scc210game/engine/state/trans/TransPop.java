package scc210game.engine.state.trans;

/**
 * Pops the current state from the state stack
 */
public class TransPop implements Transition {
    private static final TransPop instance = new TransPop();

    public static TransPop getInstance() {
        return TransPop.instance;
    }
}
