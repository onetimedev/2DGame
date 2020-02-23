package scc210game.engine.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;

public class SerializeToBase64 {
    public static String serializeToBase64(Serializable toSer) {
        var bos = new ByteArrayOutputStream();
        try {
            var out = new ObjectOutputStream(bos);
            out.writeObject(toSer);
            out.flush();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        var encoder = Base64.getEncoder();
        return encoder.encodeToString(bos.toByteArray());
    }
}
