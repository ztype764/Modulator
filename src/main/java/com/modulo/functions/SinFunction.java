package com.modulo.functions;

import com.modulo.internal.CalcFunction;

public class SinFunction implements CalcFunction {
    @Override
    public String getName() {
        return "sin";
    }

    @Override
    public String getInsertText() {
        return "sin(";
    }

    @Override
    public double execute(double... args) {
        return Math.sin(Math.toRadians(args[0]));
    }
}
