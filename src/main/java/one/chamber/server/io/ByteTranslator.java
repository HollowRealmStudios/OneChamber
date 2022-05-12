package one.chamber.server.io;

import lombok.SneakyThrows;
import lombok.val;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ByteTranslator {

    @SneakyThrows
    public static byte[] toByteArray(Object o) {
        val baos = new ByteArrayOutputStream();
        val oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        oos.flush();
        return baos.toByteArray();
    }

    @SneakyThrows
    public static <T> T toObject(byte[] array, Class<T> clazz) {
        val bais = new ByteArrayInputStream(array);
        val ois = new ObjectInputStream(bais);
        return clazz.cast(ois.readObject());
    }

    @SneakyThrows
    public static Class<?> getObjectClass(byte[] array) {
        val bais = new ByteArrayInputStream(array);
        val ois = new ObjectInputStream(bais);
        return ois.readObject().getClass();
    }

}
