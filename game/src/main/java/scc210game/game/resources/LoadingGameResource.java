package scc210game.game.resources;

import com.github.cliftonlabs.json_simple.*;
import org.jsfml.window.Keyboard;
import scc210game.engine.ecs.Resource;
import scc210game.engine.ecs.World;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class LoadingGameResource extends Resource {
    public final ArrayList<SaveState> saves;
    public int selectedSave;

    public LoadingGameResource(World w) {
        var db = w.fetchGlobalResource(SavesDatabaseResource.class);

        try {
            var saves = (JsonObject) Jsoner.deserialize(db.load());
            this.saves = saves.keySet()
                    .stream()
                    .map(k -> {
                        var v = (Jsonable) saves.get(k);
                        return new SaveState(Integer.parseInt(k), v);
                    })
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (JsonException e) {
            throw new RuntimeException(e);
        }

        this.selectedSave = 0;
    }

    public void handleInput(Keyboard.Key k, World w) {
        if (k == Keyboard.Key.UP && selectedSave > 0) {
            selectedSave -= 1;
        } else if (k == Keyboard.Key.DOWN) {
            var lastIdx = saves.size() - 1;
            if (selectedSave < lastIdx) {
                selectedSave += 1;
            }
        } else if (k == Keyboard.Key.RETURN) {
            var save = this.saves.get(this.selectedSave);
            w.ecs.deserializeAndReplace(save.data);
        }
    }

    public static class SaveState {
        public final int id;
        public final Jsonable data;

        public SaveState(int id, Jsonable data) {
            this.id = id;
            this.data = data;
        }
    }

    @Override
    public boolean shouldKeep() {
        return false;
    }
}
