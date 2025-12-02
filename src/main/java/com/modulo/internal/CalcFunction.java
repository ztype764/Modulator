package com.modulo.internal;

/**
 * <p>MIT License</p>

 <p>Copyright (c) 2025 ztype764</p>

 <p>Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:</p>

 <p>The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.</p>

 <p>THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.</p>

 **/
/**
 * Interface representing a calculator function.
 * <p>
 * Implementations of this interface define a function that can be used in the
 * calculator.
 * </p>
 */
public interface CalcFunction {
    // make sure the Name contains the text. e.g. name sin will not work with sint(
    // ,but sint will work
    /**
     * Gets the name of the function.
     * <p>
     * This name is displayed on the calculator button.
     * </p>
     *
     * @return The function name.
     */
    String getName(); // Button label + function identifier

    /**
     * Gets the text to insert into the input field when the function button is
     * clicked.
     * <p>
     * For example, for a sine function, this might be "sin(".
     * </p>
     *
     * @return The insert text.
     */
    String getInsertText(); // Inserted text (ex: "sin(")

    /**
     * Evaluates the function for a given input value.
     *
     * @param x The input value.
     * @return The result of the function.
     */
    double evaluate(double x);
}
