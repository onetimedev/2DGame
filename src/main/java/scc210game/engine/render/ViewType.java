package scc210game.engine.render;

import com.github.cliftonlabs.json_simple.Jsonable;

import java.io.IOException;
import java.io.Writer;

public enum ViewType implements Jsonable {
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

    @Override
    public String toJson() {
        return this.getValue();
    }

    @Override
    public void toJson(Writer writer) throws IOException {
        writer.write(this.getValue());
    }
}
