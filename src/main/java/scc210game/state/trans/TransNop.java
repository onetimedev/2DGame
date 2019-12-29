package scc210game.state.trans;

/**
 * A transition that does nothing
 */
public class TransNop implements Transition {
    private static final TransNop instance = new TransNop();

    public static TransNop getInstance() {
        return TransNop.instance;
    }
}
