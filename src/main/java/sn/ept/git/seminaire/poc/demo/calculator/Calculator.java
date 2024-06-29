package sn.ept.git.seminaire.poc.demo.calculator;

import sn.ept.git.seminaire.poc.demo.exception.DivisionByZeroException;

import java.security.SecureRandom;
import java.util.List;

public class Calculator implements ICalculator {

    public static final String DIVIDE_BY_ZERO = "Can not divide by zero";
    private static final SecureRandom secureRandom = new SecureRandom();

    @Override
    public double add(final double a, final double b) {
        return a + b;
    }

    @Override
    public double subtract(final double a, final double b) {
        return a - b;
    }

    @Override
    public double multiply(final double a, final double b) {
        return a * b;
    }

    @Override
    public double divide(final double a, final double b) throws DivisionByZeroException {
        if (0 == b) {
            throw new DivisionByZeroException(DIVIDE_BY_ZERO);
        }
        return a / b;
    }

    @Override
    public long somme(final long n) {
        return (n * (n + 1)) / 2;
    }

    @Override
    public long fact(final long n) {
        if (n <= 1) {
            return 1;
        }
        return n * fact(n - 1);
    }

    @Override
    public double moyenne(final List<Double> values) {
        return values
                .stream()
                .reduce(0.0, Double::sum) / values.size();
    }

    /**
     * Returns a random integer greater than or equal to min and less than max.
     *
     * @param min min bound inclusive
     * @param max max bound exclusive
     * @return generated random integer (min <= value < max )
     */

    @Override
    public int getRandom(final int min, final int max) {
        return secureRandom.nextInt(min, max);
    }

}
