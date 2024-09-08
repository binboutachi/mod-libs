package io.github.binboutachi.libs.java;

import java.io.File;

public final class JavaUtils {
    private JavaUtils() {}

    public static String toStringButBetter(Object o) {
        StringBuilder builder = new StringBuilder("Fields:\n");
        for(java.lang.reflect.Field field : o.getClass().getFields()) {
            try {
                builder.append(field.getName());
                builder.append(": ");
                builder.append(field.get(o));
                builder.append('\n');
            } catch(Exception e) {}
        }
		return builder.toString();
	}

    public static String canonizePathString(String path) {
        return path.replace('\\', File.separatorChar).replace('/', File.separatorChar);
    }
}
