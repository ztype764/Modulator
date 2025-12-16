package com.modulo.Registry;

import com.modulo.internal.AnnotatedFunctionAdapter;
import com.modulo.internal.CalcFunction;
import com.modulo.internal.Function;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * MIT License
 * </p>
 *
 * <p>
 * Copyright (c) 2025 ztype764
 * </p>
 *
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * </p>
 * 
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all
 * copies or substantial portions of the Software.
 * </p>
 * 
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * </p>
 * <br>
 * <br>
 * Registry for managing and loading calculator functions.
 * <p>
 * This class handles the discovery and registration of both
 * {@link CalcFunction} implementations
 * and methods annotated with {@link Function}.
 * </p>
 */
public class FunctionRegistry {

    private static final List<CalcFunction> functions = new ArrayList<>();

    /**
     * Retrieves the list of registered calculator functions.
     *
     * @return A list of {@link CalcFunction} instances.
     */
    public static List<CalcFunction> getFunctions() {
        return functions;
    }

    /**
     * Scans the {@code com.modulo.functions} package and loads all available
     * functions.
     * <p>
     * It supports two types of function definitions:
     * <ul>
     * <li>Classes implementing {@link CalcFunction}</li>
     * <li>Classes annotated with {@link Function}</li>
     * </ul>
     * </p>
     */
    /**
     * Reloads all functions by clearing the current registry and rescanning.
     */
    public static void reload() {
        functions.clear();
        loadFunctions();
    }

    /**
     * Scans the {@code com.modulo.functions} package and loads all available
     * functions.
     * <p>
     * It supports two types of function definitions:
     * <ul>
     * <li>Classes implementing {@link CalcFunction}</li>
     * <li>Classes annotated with {@link Function}</li>
     * </ul>
     * </p>
     */
    public static void loadFunctions() {
        // Ensure we don't duplicate if called directly without reload()
        // But reload() clears it. Let's make loadFunctions safe too or just rely on
        // reload.
        // If we clear here, we might break if loadFunctions is called multiple times
        // for additive loading?
        // The javadoc says "Scans... and loads". Usually implies fresh state or
        // additive.
        // Let's keep loadFunctions additive (or idempotent if we checked dupes) but
        // reload() clears.
        // Actually, I'll just rely on reload() clearing it.
        // Wait, if I use reload() I need to make sure loadFunctions doesn't clear if I
        // want it to be additive.
        // But for "Hot Reload" we want a fresh state.
        // So reload() { functions.clear(); loadFunctions(); } is correct.

        try {
            Reflections reflections = new Reflections("com.modulo.functions");

            // Load old-style CalcFunction modules
            Set<Class<? extends CalcFunction>> classes = reflections.getSubTypesOf(CalcFunction.class);
            for (Class<? extends CalcFunction> cls : classes) {
                if (!cls.isInterface() && !java.lang.reflect.Modifier.isAbstract(cls.getModifiers())) {
                    CalcFunction f = cls.getDeclaredConstructor().newInstance();
                    registerFunction(f);
                }
            }

            // Load annotation-based modules
            Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Function.class);
            for (Class<?> cls : annotated) {
                Object obj = cls.getDeclaredConstructor().newInstance();
                Function meta = cls.getAnnotation(Function.class);
                registerFunction(new AnnotatedFunctionAdapter(obj, meta));
            }
        } catch (Exception e) {
            System.err.println("Failed loading plugins: " + e.getMessage());
        }
    }

    private static void registerFunction(CalcFunction f) {
        for (CalcFunction existing : functions) {
            if (existing.getName().equals(f.getName())) {
                System.err.println("Warning: Duplicate function name '" + f.getName() + "'. Skipping.");
                return;
            }
        }
        functions.add(f);
    }
}
