package com.modulo.internal;

import java.lang.annotation.*;

/**
 * Annotation to define a calculator function.
 * <p>
 * Classes annotated with this annotation are automatically discovered and
 * registered by the {@link com.modulo.Registry.FunctionRegistry}.
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Function {
    /**
     * The name of the function.
     *
     * @return The function name.
     */
    String name();

    /**
     * The text to insert into the input field.
     *
     * @return The insert text.
     */
    String insert();
}
