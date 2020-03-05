package scc210game.game.resources;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import com.github.cliftonlabs.json_simple.Jsoner;
import org.jsfml.window.Keyboard;
import scc210game.engine.ecs.Resource;
import scc210game.engine.ecs.World;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class SavingGameResource extends Resource {
    public final ArrayList<SaveState> saves;
    public int selectedSave;
    public boolean createNewSelected;

    public SavingGameResource(World w) {
        var db = w.fetchGlobalResource(SavesDatabaseResource.class);

        try {
            var saves = (JsonObject) Jsoner.deserialize(db.load());
            this.saves = saves.keySet()
                    .stream()
                    .map(k -> {
                        var v = (Jsonable) saves.get(k);
                        return new SavingGameResource.SaveState(Integer.parseInt(k), v);
                    })
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (JsonException e) {
            throw new RuntimeException(e);
        }

        this.createNewSelected = true;
        this.selectedSave = 0;
    }

    private void saveSaves(World w) {
        var db = w.fetchGlobalResource(SavesDatabaseResource.class);

        var o = new JsonObject() {{
            saves.forEach(s ->
                    this.put(Integer.toString(s.id), s.data));
        }};

        db.save(o.toJson());
    }

    public void handleInput(Keyboard.Key k, World w) {
        if (k == Keyboard.Key.UP && selectedSave > 0) {
            if (createNewSelected) {
                createNewSelected = false;
            }
            selectedSave -= 1;
        } else if (k == Keyboard.Key.DOWN && !createNewSelected) {
            var lastIdx = saves.size() - 1;
            if (selectedSave == lastIdx) {
                createNewSelected = true;
            }
            selectedSave += 1;
        } else if (k == Keyboard.Key.RETURN) {
            if (createNewSelected) {
                var id = saves.size();
                var data = w.ecs.serialize();
                saves.add(new SaveState(id, data));
                this.saveSaves(w);
                createNewSelected = false;
                selectedSave = id;
            } else {
                var data = w.ecs.serialize();
                saves.set(selectedSave, new SaveState(selectedSave, data));
            }
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
