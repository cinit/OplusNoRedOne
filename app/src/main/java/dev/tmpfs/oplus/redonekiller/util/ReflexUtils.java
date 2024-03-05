package dev.tmpfs.oplus.redonekiller.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ReflexUtils {

    @Nullable
    public static Class<?> loadClassOrNull(@NonNull ClassLoader cl, @NonNull String name) {
        try {
            return cl.loadClass(name);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

}
