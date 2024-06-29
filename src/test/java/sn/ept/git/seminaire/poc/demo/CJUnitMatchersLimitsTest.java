package sn.ept.git.seminaire.poc.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import sn.ept.git.seminaire.poc.demo.calculator.Calculator;
import sn.ept.git.seminaire.poc.demo.calculator.ICalculator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@Slf4j
class CJUnitMatchersLimitsTest {

    private static ICalculator calculator;

    @BeforeAll
    static void beforeAll() {
        log.info(">>> beforeAll");
        calculator = new Calculator();
    }


    @Test
    void testGenerateRandomInteger() {
        int min = 100;
        int max = 200;
        int result = calculator.getRandom(min, max);
        //ici
        assertTrue(min <= result);
        assertTrue(result < max);
    }

    @Test
    void testGenerateRandomIntegerV2() {
        int min = 0;
        int max = 5;
        int resultat = calculator.getRandom(min, max);
        //ou la
        assertThat(resultat)
                .isGreaterThanOrEqualTo(min)
                .isLessThan(max);
    }


}