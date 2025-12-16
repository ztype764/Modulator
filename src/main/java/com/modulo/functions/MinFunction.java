package com.modulo.functions;

import com.modulo.internal.Function;

/**
 * Example of a multi-argument function.
 * Usage: min(10, 20) -> 10
 */
@Function(name = "min", insert = "min(")
public class MinFunction {

    public double run(double a, double b) {
        return Math.min(a, b);
    }
}
