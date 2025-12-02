package com.modulo.Registry;


import com.modulo.internal.AnnotatedFunctionAdapter;
import com.modulo.internal.CalcFunction;
import com.modulo.internal.Function;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
/**
 * <p>MIT License</p>

 <p>Copyright (c) 2025 ztype764</p>

 <p>Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:</p>

 <p>The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.</p>

 <p>THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.</p>

 **/
public class FunctionRegistry {

    private static final List<CalcFunction> functions = new ArrayList<>();

    public static List<CalcFunction> getFunctions() {
        return functions;
    }

    // Auto-scan all classes in com.modulo.functions
    public static void loadFunctions() {
        try {
            Reflections reflections = new Reflections("com.modulo.functions");

            // Load old-style CalcFunction modules
            Set<Class<? extends CalcFunction>> classes = reflections.getSubTypesOf(CalcFunction.class);
            for (Class<? extends CalcFunction> cls : classes) {
                if (!cls.isInterface()) {
                    functions.add(cls.getDeclaredConstructor().newInstance());
                }
            }

            // Load annotation-based modules
            Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Function.class);
            for (Class<?> cls : annotated) {
                Object obj = cls.getDeclaredConstructor().newInstance();
                Function meta = cls.getAnnotation(Function.class);
                functions.add(new AnnotatedFunctionAdapter(obj, meta));
            }
        }catch (Exception e) {
            System.err.println("Failed loading plugins: " + e.getMessage());
        }
    }
}
