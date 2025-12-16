package com.modulo.functions;

import com.modulo.internal.CalcFunction;

public class TanFunction implements CalcFunction {
    @Override
    public String getName() {
        return "tan";
    }

    @Override
    public String getInsertText() {
        return "tan(";
    }

    @Override
    public double execute(double... args) {
        return Math.tan(Math.toRadians(args[0]));
    }

}
