package sn.ept.git.seminaire.poc.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import sn.ept.git.seminaire.poc.demo.calculator.Calculator;
import sn.ept.git.seminaire.poc.demo.calculator.ICalculator;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class BTripleAPrincipalTest {

    private static ICalculator calculator;
    private double a, b;

    @BeforeAll
    static void beforeAll() {
        log.info(">>> beforeAll");
        calculator = new Calculator();
    }


    @BeforeEach
    void beforeEach() {
        log.info(">>> beforeEach");
        a = 100;
        b = 200;
    }

    @AfterAll
    static void afterAll() {
        log.info(">>> afterAll");
    }


    @AfterEach
    void afterEach() {
        log.info(">>> afterEach");
    }


    @Test
    void testAAAPrinciple() {
        //arrange; given; preparation
        //MATIERE PREMIERE
        a = 22;
        b = 44;

        //act; when; action
        double result = calculator.add(a, b);

        //assert; then; verification
        assertThat(result)
                .isEqualTo(a + b);
    }

    @Test
    void testSommeDesNPremiersEntiers() {
        //Arrange
        long n = 10;

        //Acte
        long resultat = calculator.somme(n);

        //Assert
        assertThat(resultat).isEqualTo(55);

    }

    @Test
    void testFactorielle() {
        //arrange
        long n = 4;

        //act
        long resultat = calculator.fact(n);

        //assert
        assertThat(resultat).isEqualTo(24);

    }

    @Test
    void testCalculMoyenne() {
        //arrange
        List<Double> valeurs = List.of(2.0, 6.0, 4.0);

        //act
        double resultat = calculator.moyenne(valeurs);

        //assert
        assertThat(resultat).isEqualTo(4.0);

    }

    private String getDevise() {
        return "Sagesse devoir";
    }


    @Test
    void xxx() {
 String devise = getDevise();
 assertEquals("Sagesse devoir", devise);
 assertTrue(devise.equalsIgnoreCase("Sagesse DEVOIR"));
 assertTrue(devise.startsWith("S"));
 assertFalse(devise.startsWith("age"));
 assertTrue(devise.endsWith("r"));
 assertFalse(devise.endsWith("re"));
 assertTrue(devise.toLowerCase().contains("Devoir".toLowerCase()));
 assertEquals(14,devise.length());

    }
}