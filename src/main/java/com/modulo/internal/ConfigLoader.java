package com.modulo.internal;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;
/**
 * <p>MIT License</p>
 *
 * <p>Copyright (c) 2025 ztype764</p>
 *
 * <p>Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:</p>
 *
 * <p>The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.</p>
 *
 * <p>THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.</p>
 * <br>
 * <br>
 * Adapter class to adapt annotated functions to the {@link CalcFunction}
 * interface.
 * <p>
 * This class wraps an object annotated with {@link Function} and exposes it as
 * a {@link CalcFunction}.
 * </p>
 */
public class ConfigLoader {

    private static final Map<String, Object> config;

    static {
        config = new java.util.HashMap<>();
        // Default values
        config.put("ui.window.title", "Modulator Calculator");
        config.put("ui.window.width", 400);
        config.put("ui.window.height", 600);
        config.put("ui.colors.background", "#FFFFFF");
        config.put("ui.colors.keypad", "#F0F0F0");
        config.put("ui.colors.buttonText", "#000000");
        config.put("ui.colors.buttonBackground", "#E0E0E0");
        config.put("ui.colors.buttonHover", "#D0D0D0");
        config.put("ui.labels.defaultResult", "= 0");

        try (InputStream in = ConfigLoader.class.getClassLoader()
                .getResourceAsStream("application.yml")) {
            if (in != null) {
                Yaml yaml = new Yaml();
                Map<String, Object> loaded = yaml.load(in);
                if (loaded != null) {
                    merge(config, loaded);
                }
            }
        } catch (Exception e) {
            System.err.println("Warning: Failed to load application.yml, using defaults. " + e.getMessage());
        }
    }

    private static void merge(Map<String, Object> target, Map<String, Object> source) {
        // We want to flatten the source into the target for simplicity,
        // or we just support both.
        // Let's just flatten the source keys into dot notation and put them in target.
        flattenAndPut("", source, target);
    }

    @SuppressWarnings("unchecked")
    private static void flattenAndPut(String prefix, Map<String, Object> source, Map<String, Object> target) {
        for (Map.Entry<String, Object> entry : source.entrySet()) {
            String key = prefix.isEmpty() ? entry.getKey() : prefix + "." + entry.getKey();
            if (entry.getValue() instanceof Map) {
                flattenAndPut(key, (Map<String, Object>) entry.getValue(), target);
            } else {
                target.put(key, entry.getValue());
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(String keyPath) {
        // First check if the key exists directly (from defaults or flattened config)
        if (config.containsKey(keyPath)) {
            return (T) config.get(keyPath);
        }

        // Fallback to traversing nested maps (if any exist and weren't flattened)
        String[] parts = keyPath.split("\\.");
        Object current = config;

        for (String part : parts) {
            if (!(current instanceof Map))
                return null;
            current = ((Map<String, Object>) current).get(part);
            if (current == null)
                return null;
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
