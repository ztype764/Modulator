package com.modulo.internal;

import java.lang.reflect.Method;

public class AnnotatedFunctionAdapter implements CalcFunction {

    private final String name;
    private final String insert;
    private final Object instance;
    private final Method runMethod;

    public AnnotatedFunctionAdapter(Object obj, Function meta) {
        this.name = meta.name();
        this.insert = meta.insert();
        this.instance = obj;

        try {
            this.runMethod = obj.getClass().getDeclaredMethod("run", double.class);
        } catch (Exception e) {
            throw new RuntimeException("Modules using @Function must define a run(double) method");
        }
    }

    @Override public String getName() { return name; }

    @Override public String getInsertText() { return insert; }

    @Override
    public double evaluate(double x) {
        try {
            return (double) runMethod.invoke(instance, x);
        } catch (Exception e) {
            throw new RuntimeException("Error running module " + name);
        }
    }
}
