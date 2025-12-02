package com.modulo.functions;

import com.modulo.internal.Function;

/**
 * complex function example
 */

@Function(name = "NPrime", insert = "NPrime(")
public class NPrime {

    public double run(double x) {
        int n = (int) x;
        if (n <= 0) return 2;

        int count = 0;
        int num = 1;

        while (count < n) {
            num++;
            if (isPrime(num)) count++;
        }

        return num;
    }

    private boolean isPrime(int n) {
        if (n < 2) return false;

        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) return false;
        }

        return true;
    }
}
