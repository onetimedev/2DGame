package scc210game.engine.utils;

import java.io.*;
import java.util.Base64;

public class SerDeBase64 {
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

    public static <T> T deserializeFromBase64(String b64, Class<T> target) {
        var decoder = Base64.getDecoder();
        var bis = new ByteArrayInputStream(decoder.decode(b64));
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(bis);
            var obj = in.readObject();
            return target.cast(obj);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
