package com.modulo.functions;

import com.modulo.internal.Function;

/**
 * Example of a multi-argument function.
 * Usage: max(10, 20) -> 20
 */
@Function(name = "max", insert = "max(")
public class MaxFunction {

    public double run(double a, double b) {
        return Math.max(a, b);
    }
}
