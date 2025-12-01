package com.modulo.functions;

/**
 * an example for complex implementation
 */
public class PrimeNumbers implements CalcFunction {

    @Override
    public String getName() {
        return "NPrime";
    }

    @Override
    public String getInsertText() {
        return "NPrime(";
    }

    @Override
    public double evaluate(double x) {
        int n = (int) x + 1;

        while (true) {
            if (isPrime(n)) {
                return n;
            }
            n++;
        }
    }

    private boolean isPrime(int n) {
        if (n < 2) return false;

        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) return false;
        }

        return true;
    }
}
