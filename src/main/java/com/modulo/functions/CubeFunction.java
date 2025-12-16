package com.modulo.functions;

import com.modulo.internal.CalcFunction;

public class CubeFunction implements CalcFunction {
    @Override
    public String getName() {
        return "cube";
    }

    @Override
    public String getInsertText() {
        return "cube(";
    }

    @Override
    public double execute(double... args) {
        return Math.pow(args[0], 3);
    }
}
