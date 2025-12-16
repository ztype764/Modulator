package com.modulo.internal;

import java.lang.reflect.Method;

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
 * Adapter class to adapt annotated functions to the {@link CalcFunction}
 * interface.
 * <p>
 * This class wraps an object annotated with {@link Function} and exposes it as
 * a {@link CalcFunction}.
 * </p>
 */
public class AnnotatedFunctionAdapter implements CalcFunction {

    private final String name;
    private final String insert;
    private final Object instance;
    private final Method runMethod;

    /**
     * Constructs a new {@code AnnotatedFunctionAdapter}.
     *
     * @param obj  The object instance containing the function logic.
     * @param meta The {@link Function} annotation metadata.
     * @throws RuntimeException If the object does not have a {@code run(double)}
     *                          method.
     */
    public AnnotatedFunctionAdapter(Object obj, Function meta) {
        this.name = meta.name();
        this.insert = meta.insert();
        this.instance = obj;

        Method[] methods = obj.getClass().getDeclaredMethods();
        Method target = null;
        for (Method m : methods) {
            if (m.getName().equals("run")) {
                target = m;
                break;
            }
        }

        if (target == null) {
            throw new RuntimeException("Modules using @Function must define a run(...) method");
        }
        this.runMethod = target;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getInsertText() {
        return insert;
    }

    @Override
    public double execute(double... args) {
        try {
            // Convert double[] to Object[] for reflection
            Object[] objArgs = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                objArgs[i] = args[i];
            }
            return ((Number) runMethod.invoke(instance, objArgs)).doubleValue();
        } catch (Exception e) {
            throw new RuntimeException("Error running module " + name + ": " + e.getMessage());
        }
    }

    @Override
    public int getArgCount() {
        return runMethod.getParameterCount();
    }
}
