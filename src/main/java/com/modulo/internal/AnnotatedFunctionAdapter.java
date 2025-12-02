package com.modulo.internal;

import java.lang.reflect.Method;

/**
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

        try {
            this.runMethod = obj.getClass().getDeclaredMethod("run", double.class);
        } catch (Exception e) {
            throw new RuntimeException("Modules using @Function must define a run(double) method");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getInsertText() {
        return insert;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double evaluate(double x) {
        try {
            return (double) runMethod.invoke(instance, x);
        } catch (Exception e) {
            throw new RuntimeException("Error running module " + name);
        }
    }
}
