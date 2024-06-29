package sn.ept.git.seminaire.tp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

class JuryTest {


    private final Jury jury = new Jury();



    static Stream<Arguments> donneesPasse() {
        return Stream.of(
                Arguments.of(12),
                Arguments.of(15),
                Arguments.of(19.5),
                Arguments.of(12.01)
        );
    }

    @ParameterizedTest
    @MethodSource("donneesPasse")
    void testPasse(double moyenne) {
        boolean passe = jury.passe(moyenne);
        Assertions.assertTrue(passe);
    }




    static Stream<Arguments> donneesNePassePas() {
        return Stream.of(
                Arguments.of(11.99),
                Arguments.of(10),
                Arguments.of(9.5),
                Arguments.of(2.01)
        );
    }

    @ParameterizedTest
    @MethodSource("donneesNePassePas")
    void testNePassePase(double moyenne) {
        boolean passe = jury.passe(moyenne);
        Assertions.assertFalse(passe);
    }

    @Test
    void voiciMonTravail() {
        //Arange
        List<Double > notes = List.of(12.0,11.0,13.0);
        //Act
        double moyenne = jury.obtenirMoyenne(notes);
        //Assert
        Assertions.assertEquals(12.0, moyenne);
    }


}