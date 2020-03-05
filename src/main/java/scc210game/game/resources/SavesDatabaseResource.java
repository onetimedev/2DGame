package scc210game.game.resources;

import scc210game.engine.ecs.Resource;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class SavesDatabaseResource extends Resource {
    private final Path p = Path.of("saves.json");

    public void save(String s) {
        try {
            Files.writeString(this.p, s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String load() {
        try {
            return Files.readString(this.p);
        } catch (IOException e) {
            return "{}";
        }
    }

    @Override
    public boolean shouldKeep() {
        return false;
    }
}
