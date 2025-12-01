package com.modulo.functions;

public interface CalcFunction {
    //make sure the Name contains the text. eg name sin will not work with sint( ,but sint will work
    String getName();        // Button label + function identifier
    String getInsertText();  // Inserted text (ex: "sin(")
    double evaluate(double x);
}
