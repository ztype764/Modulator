package com.modulo.internal;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class ConfigLoader {

    private static Map<String, Object> config;

    static {
        try (InputStream in = ConfigLoader.class.getClassLoader()
                .getResourceAsStream("application.yml")) {

            Yaml yaml = new Yaml();
            config = yaml.load(in);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load application.yml", e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(String keyPath) {
        String[] parts = keyPath.split("\\.");
        Object current = config;

        for (String part : parts) {
            current = ((Map<String, Object>) current).get(part);
            if (current == null) return null;
        }
        return (T) current;
    }

    public static String getString(String path) {
        return get(path);
    }

    public static Integer getInt(String path) {
        return get(path);
    }

    public static String getColorHex(String path) {
        return get(path);
    }
}
