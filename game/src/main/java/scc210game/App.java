/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package scc210game;

import scc210game.engine.utils.ResourceLoader;
import scc210game.game.Main;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class App {
    public static void main(String[] args) {
        System.out.println("Hello world: " + System.getProperty("os.name"));
        if (System.getProperty("os.name").equals("Linux")) {
            var arch = System.getProperty("os.arch");
            String ext = null;
            if (arch.contains("amd64")) {
                ext = "amd64";
            } else if (arch.contains("arm")) {
                ext = "arm";
            }

            if (ext != null) {
                try {
                    var tempFile = File.createTempFile("libXinitThreads", ".so");
                    tempFile.setExecutable(true);
                    var outFile = new FileOutputStream(tempFile);
                    ResourceLoader.resolve("libXinitThreads.so." + ext).transferTo(outFile);
                    outFile.close();
                    System.load(tempFile.getAbsolutePath());
                } catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        Main.runForever();
    }
}
