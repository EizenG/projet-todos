package sn.ept.git.seminaire.poc.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import sn.ept.git.seminaire.poc.demo.calculator.Calculator;
import sn.ept.git.seminaire.poc.demo.calculator.ICalculator;
import sn.ept.git.seminaire.poc.demo.exception.DivisionByZeroException;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class ACalculatorTest {

    private static ICalculator calculator;
    private double a, b, result, expected;


    @BeforeAll
    static void beforeAll() {
        calculator = new Calculator();
    }

    @BeforeEach
    void beforeEach() {
        a = 2;
        b = 2;
    }

    @Test
    void addShouldReturnCorrectResult() {
//        a = 44;
//        b = 11;
        expected = a + b;
        result = calculator.add(a, b);
        // result = a + b (expected)
        assertThat(result)
                .isEqualTo(expected);
    }

    @Test
    void substractShouldReturnCorrectResult() {
        //        a = 44;
//        b = 11;
        expected = a - b;
        result = calculator.subtract(a, b);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void multiplyShouldReturnCorrectResult() {
        //        a = 44;
//        b = 11;
        expected = a * b;
        result = calculator.multiply(a, b);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void divideShouldReturnCorrectResult() throws DivisionByZeroException {
        //        a = 44;
//        b = 11;
        expected = a / b;
        result = calculator.divide(a, b);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void divisionByZeroShouldThrowException() {
        b = 0;
        Assertions.assertThrows(
                DivisionByZeroException.class,
                () -> calculator.divide(a, b)
        );
    }


    @ParameterizedTest
    @MethodSource("multiplyTestData")
    void multiplyWithRandomInputsShouldReturnCorrectValue(double a, double b) {
        expected = a * b;
        result = calculator.multiply(a, b);
        assertThat(result).isEqualTo(expected);
    }

    static Stream<Arguments>  multiplyTestData() {
        return Stream.of(
                Arguments.of(0, 0),
                Arguments.of(2, 2),
                Arguments.of(1, 10),
                Arguments.of(28, 2),
                Arguments.of(37, 3),
                Arguments.of(40, 4),
                Arguments.of(55, 5),
                Arguments.of(6, 61)
        );
    }



}