package scc210game.game.resources;

import com.github.cliftonlabs.json_simple.Jsonable;
import org.jsfml.window.Keyboard;
import scc210game.engine.ecs.Resource;
import scc210game.engine.ecs.World;

import java.util.ArrayList;

public class SavingGameResource extends Resource {
    public ArrayList<SaveState> saves;
    public int selectedSave;
    public boolean createNewSelected;

    public void handleInput(Keyboard.Key k, World w) {
        if (k == Keyboard.Key.UP) {
            if (createNewSelected) {
                createNewSelected = false;
                selectedSave = saves.size();
            } else if (selectedSave > 0) {
                selectedSave -= 1;
            }
        } else if (k == Keyboard.Key.DOWN) {
            if (!createNewSelected && (selectedSave == saves.size() - 1)) {
                createNewSelected = true;
            } else if (selectedSave < saves.size() - 1) {
                selectedSave += 1;
            }
        } else if (k == Keyboard.Key.RETURN) {
            if (createNewSelected) {
                var id = saves.size();
                var data = w.ecs.serialize();
                saves.add(new SaveState(id, data));
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
