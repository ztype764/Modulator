package com.modulo.functions;

import com.modulo.internal.Function;

/**
 * Example Module to multiply by three
 *
 */
@Function(name = "*3",insert = "*3")
public class MultiplyByThree {
    public double run(double x) {
        return x * 3;
    }
}
