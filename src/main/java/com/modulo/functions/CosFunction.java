package com.modulo.functions;

import com.modulo.internal.CalcFunction;

public class CosFunction implements CalcFunction {
    @Override
    public String getName() {
        return "cos";
    }

    @Override
    public String getInsertText() {
        return "cos(";
    }

    @Override
    public double execute(double... args) {
        return Math.cos(Math.toRadians(args[0]));
    }
}
