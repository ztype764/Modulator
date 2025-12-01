package com.modulo.functions;


/**
 * Example Module to multiply by three
 *
 */
public class MultiplyByThree implements CalcFunction{
    @Override public String getName() { return "*3"; }
    @Override public String getInsertText() { return "*3"; }
    @Override public double evaluate(double x) { return x * 3; }
}
