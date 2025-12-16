package com.modulo.functions;

import com.modulo.internal.CalcFunction;

public class LogFunction implements CalcFunction {
    @Override
    public String getName() {
        return "log";
    }

    @Override
    public String getInsertText() {
        return "log(";
    }

    @Override
    public double execute(double... args) {
        return Math.log10(args[0]);
    }
}
