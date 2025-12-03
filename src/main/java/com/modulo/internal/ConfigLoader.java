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
