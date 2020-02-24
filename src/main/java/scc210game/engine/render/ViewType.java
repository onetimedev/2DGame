package scc210game.engine.render;

public enum ViewType {
    MAIN("main"),
    UI("ui"),
    MINIMAP("minimap");

    private final String value;

    public String getValue() {
        return this.value;
    }

    ViewType(String value) {
        this.value = value;
    }
}
