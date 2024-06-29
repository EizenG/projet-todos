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
class X_ACalculatorTest {

    private static ICalculator calculator;
    private double a, b, result, expected;


    @BeforeAll
    static void beforeAll() {
        calculator = new Calculator();
    }

    @BeforeEach
    void beforeEach() {
        a = 33;
        b = 44;
    }

    @Test
    void addShouldReturnCorrectResult() {
        //Arrange
        expected = a + b;
        //Act
        result = calculator.add(a, b);
        //Assert
        assertThat(result)
                .isEqualTo(expected);
    }

    @Test
    void substractShouldReturnCorrectResult() {
        //Arrange
        expected = a - b;
        //Act
        result = calculator.subtract(a, b);
        //Assert
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void multiplyShouldReturnCorrectResult() {
        //Arrange
        expected = a * b;
        //Act
        result = calculator.multiply(a, b);
        //Assert
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void divideShouldReturnCorrectResult() throws DivisionByZeroException {
        //Arrange
        expected = a / b;
        //Act
        result = calculator.divide(a, b);
        //Assert
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void divisionByZeroShouldThrowError() {
        //Arrange
        b = 0;
        //Assert
        Assertions.assertThrows(
                DivisionByZeroException.class,
                () -> calculator.divide(a, b)  //Act
        );
    }


    @RepeatedTest(value = 10)
    void testAddRepeated() {
        //Arrange
        expected = a * 2;
        //Act
        result = calculator.add(a, a);
        //Assert
        assertThat(result).isEqualTo(expected);
    }


    @DisplayName("ICalculator: parameterized test for add method")
    @ParameterizedTest
    @MethodSource("addTestData")
    void addWithRandomInputsShouldReturnCorrectValue(double a, double b) {
        //Arrange
        expected = a + b;
        //Act
        double result = calculator.add(a, b);
        //Assert
        assertThat(result).isEqualTo(expected);
    }

    static Stream<Arguments> addTestData() {
        return Stream.of(
                Arguments.of(1, 10),
                Arguments.of(28, 2),
                Arguments.of(37, 3),
                Arguments.of(40, 4),
                Arguments.of(55, 5),
                Arguments.of(6, 61)
        );
    }


    /**
     * Won't be run
     */
//   @Disabled
//    @Test
//    void testDisabled() {
//        assertThat(Boolean.TRUE).isTrue();
//    }

}