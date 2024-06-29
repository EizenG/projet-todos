package sn.ept.git.seminaire.poc.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import sn.ept.git.seminaire.poc.demo.calculator.Calculator;
import sn.ept.git.seminaire.poc.demo.calculator.ICalculator;
import sn.ept.git.seminaire.poc.demo.exception.BadPhoneException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
class EFirstPrincipalTest {


    private static ICalculator calculator;
    private static double resultOne, resultTwo;
    private double a, b;

    @BeforeAll
    static void beforeAll() {
        calculator = new Calculator();
    }

    @BeforeEach
    void beforeEach() {
        a = 11;
        b = 22;
    }


    /**
     * Fast
     * A developer should not hesitate to run the tests as they are slow; they should run very fast
     * You should be aiming for many hundreds or thousands of tests per second.
     * ===> Avoid depending on network or external services
     */
    @Nested
    class Fast {

        @RepeatedTest(1000)
        void addShouldReturnTheSumOfPositiveNumbers() {
            double result = calculator.add(b, a);
            double expected = a + b;
            assertThat(result).isEqualTo(expected);
        }
    }

    /**
     * Isolation/Independent
     * You can isolate them from interfering with one another
     * No order-of-run dependency => They should pass or fail the same way in suite or when run individually.
     * ===>  not test should prepare data for others
     */
    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Isolation {

        @Order(0)
        @Test
        void addShouldReturnTheSumOfTwoPositiveNumbers() {
            a = 11;
            b = 22;
            resultOne = calculator.add(a, b); //resultOne =33
            assertThat(resultOne).isEqualTo(33);
        }

        @Order(1)
        @Test
        void givenTwoPositiveIntegers_whenMultiply_thenCorrectResult() {
            a = 11;
            resultOne = 33;
            resultTwo = calculator.multiply(a, resultOne);
            //resultTwo = 11*33 = 363
            assertThat(resultTwo).isEqualTo(363);
        }
    }

    /**
     * Repeatable
     * No matter how often or where you run it, it should produce the same result (Deterministic results ).
     * Each test should set up or arrange its own data.
     * ===>  What if a set of tests need some common data? Use Data Helper classes that can setup this data for re-usability.
     */
    @Nested
    class Repeatable {

        MyFileReader fileReader = new MyFileReader();


        @Test
        void readFileShouldReturnCorrectLines() throws IOException {

            try (MockedStatic<Files> reader = Mockito.mockStatic(Files.class)) {

                List<String> lines = Arrays.asList("Hello", "all", "from", "my", "mocked", "reader", "!");
                reader.when(() -> Files.readAllLines(ArgumentMatchers.any(Path.class)))
                        .thenReturn(lines);

                //tests doubles
                String path = "any_directory" + File.separator + "file_to_read.txt";
                List<String> result = fileReader.read(path);
                assertThat(result).isNotNull().isNotEmpty()
                        .hasSize(lines.size())
                        .containsExactlyInAnyOrderElementsOf(lines);
            }

        }


    }

    /**
     * SelfValidating
     * what it means is that running your test leaves it perfectly clear whether it passed or failed.
     * JUnit does this and fails with red, which lets you red-green-refactor.
     * By using a testing framework like JUnit, utilizing assertion libraries, and writing specific tests,
     * you can ensure that if a test fails, there will be clear and unambiguous reporting that tells
     * you exactly what passed or failed.
     */
    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class SelfValidating {

        @Test
        void addShouldReturnTheSumOfTwoPositiveNumbers() {
            //arrange
            a = 10;
            b = 34;
            double expected = a + b;
            //act
            resultTwo = calculator.add(b, a);
            //assert
            assertThat(resultTwo)
                    .as("Les deux resultats doivent etre egaux")
                    .isEqualTo(expected);
        }


        @Test
        void addShouldReturnTheSumOfTwoNegativeNumbers() {
            //  double expected = a+b;
            double expected = -(a + b);
            resultTwo = calculator.add(-b, -a);
            assertThat(resultTwo).isEqualTo(expected);
        }


    }


    /**
     * ThoroughAndTimely
     * ==> Timely
     * Practically, You can write unit tests at any time.
     * You can wait up to code is production-ready or you’re better off focusing on writing unit tests in a timely fashion.
     * The idea was that your tests should be written as close to when you write your code as possible (TDD: Before the code).
     * ==>  Thorough (exhaustive)
     * Should cover every use case scenario and NOT just aim for 100% coverage.
     * Tests for large data sets - this will test runtime and space complexity.
     * Tests for security with users having different roles - behavior may be different based on user's role.
     * Tests for large values - overflow and underflow errors for data types like integer.
     * Tests for exceptions and errors.
     * Tests for illegal arguments or bad inputs.
     */
    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class ThoroughAndTimely {


        String indicatif;
        String operator;
        static String groupe7Chifffres = "9876543";
        //indicatif-code operateur-groupe de 7
        String template = "%s%s%s";
        String phone;
        Operator result;


//
//        @Test
//        void getMobileOperator_withPlusIndicatifAnd77_shouldReturnOrange() throws BadPhoneException {
//            indicatif = "+221";
//            operator = "77";
//            phone = template.formatted(indicatif, operator, groupe7Chifffres);
//            //phone =+221779876543
//
//            //ACT
//             result = Validator.getSnMobileOperator(phone);
//             //ASSERT
//            assertThat(result).isEqualTo(Operator.ORANGE);
//        }
//
//        @Test
//        void getMobileOperator_withPlusIndicatifAnd78_shouldReturnOrange() throws BadPhoneException {
//            indicatif = "+221";
//            operator = "78";
//            phone = template.formatted( indicatif, operator, groupe7Chifffres);
//            result = Validator.getSnMobileOperator(phone);
//            assertThat(result).isEqualTo(Operator.ORANGE);
//        }
//
//        @Test
//        void getMobileOperator_with00IndicatifAnd77_shouldReturnOrange() throws BadPhoneException {
//            indicatif = "00221";
//            operator = "77";
//            phone = template.formatted( indicatif, operator, groupe7Chifffres);
//            result = Validator.getSnMobileOperator(phone);
//            assertThat(result).isEqualTo(Operator.ORANGE);
//        }
//
//        @Test
//        void getMobileOperator_with00IndicatifAnd78_shouldReturnOrange() throws BadPhoneException {
//            indicatif = "00221";
//            operator = "78";
//            phone = template.formatted( indicatif, operator, groupe7Chifffres);
//            result = Validator.getSnMobileOperator(phone);
//            assertThat(result).isEqualTo(Operator.ORANGE);
//        }
//
//        @Test
//        void getMobileOperator_withoutIndicatifAnd77_shouldReturnOrange() throws BadPhoneException {
//            indicatif = "";
//            operator = "77";
//            phone = template.formatted( indicatif, operator, groupe7Chifffres);
//            result = Validator.getSnMobileOperator(phone);
//            assertThat(result).isEqualTo(Operator.ORANGE);
//        }
//
//        @Test
//        void getMobileOperator_withoutIndicatifAnd78_shouldReturnOrange() throws BadPhoneException {
//            indicatif = "";
//            operator = "78";
//            phone = template.formatted( indicatif, operator, groupe7Chifffres);
//            result = Validator.getSnMobileOperator(phone);
//            assertThat(result).isEqualTo(Operator.ORANGE);
//        }
//
//        @Test
//        void getMobileOperator_withPlusIndicatifAnd76_shouldReturnFree() throws BadPhoneException {
//            indicatif = "+221";
//            operator = "76";
//            phone = template.formatted( indicatif, operator, groupe7Chifffres);
//            result = Validator.getSnMobileOperator(phone);
//            assertThat(result).isEqualTo(Operator.FREE);
//        }
//
//        @Test
//        void getMobileOperator_with00IndicatifAnd76_shouldReturnFree() throws BadPhoneException {
//            indicatif = "00221";
//            operator = "76";
//            phone = template.formatted( indicatif, operator, groupe7Chifffres);
//            result = Validator.getSnMobileOperator(phone);
//            assertThat(result).isEqualTo(Operator.FREE);
//        }
//
//        @Test
//        void getMobileOperator_withoutIndicatifAnd76_shouldReturnFree() throws BadPhoneException {
//            indicatif = "";
//            operator = "76";
//            phone = template.formatted( indicatif, operator, groupe7Chifffres);
//            result = Validator.getSnMobileOperator(phone);
//            assertThat(result).isEqualTo(Operator.FREE);
//        }
//
//        @Test
//        void getMobileOperator_withPlusIndicatifAnd70_shouldReturnExpresso() throws BadPhoneException {
//            indicatif = "+221";
//            operator = "70";
//            phone = template.formatted( indicatif, operator, groupe7Chifffres);
//            result = Validator.getSnMobileOperator(phone);
//            assertThat(result).isEqualTo(Operator.EXPRESSO);
//        }
//
//        @Test
//        void getMobileOperator_with00IndicatifAnd70_shouldReturnExpresso() throws BadPhoneException {
//            indicatif = "00221";
//            operator = "70";
//            phone = template.formatted( indicatif, operator, groupe7Chifffres);
//            result = Validator.getSnMobileOperator(phone);
//            assertThat(result).isEqualTo(Operator.EXPRESSO);
//        }
//
//        @Test
//        void getMobileOperator_withoutIndicatifAnd70_shouldReturnExpresso() throws BadPhoneException {
//            indicatif = "";
//            operator = "70";
//            phone = template.formatted( indicatif, operator, groupe7Chifffres);
//            result = Validator.getSnMobileOperator(phone);
//            assertThat(result).isEqualTo(Operator.EXPRESSO);
//        }
//
//        @Test
//        void getMobileOperator_withPlusIndicatifAnd75_shouldReturnPromobile() throws BadPhoneException {
//            indicatif = "+221";
//            operator = "75";
//            phone = template.formatted( indicatif, operator, groupe7Chifffres);
//            result = Validator.getSnMobileOperator(phone);
//            assertThat(result).isEqualTo(Operator.PROMOBILE);
//        }
//
//        @Test
//        void getMobileOperator_with00IndicatifAnd75_shouldReturnPromobile() throws BadPhoneException {
//            indicatif = "00221";
//            operator = "75";
//            phone = template.formatted( indicatif, operator, groupe7Chifffres);
//            result = Validator.getSnMobileOperator(phone);
//            assertThat(result).isEqualTo(Operator.PROMOBILE);
//        }
//
//        @Test
//        void getMobileOperator_withoutIndicatifAnd75_shouldReturnPromobile() throws BadPhoneException {
//            indicatif = "";
//            operator = "75";
//            phone = template.formatted( indicatif, operator, groupe7Chifffres);
//            result = Validator.getSnMobileOperator(phone);
//            assertThat(result).isEqualTo(Operator.PROMOBILE);
//        }
//
//
//
//        //jeu avec les mauvais numeros
//        @Test
//        void getMobileOperator_withBadIndicatif_shouldThrowError() throws BadPhoneException {
//            indicatif = "+222";
//            operator = "77";
//            //+222 77 987 65 43
//            phone = template.formatted( indicatif, operator, groupe7Chifffres);
//            assertThrows(
//                    BadPhoneException.class,
//                    () -> Validator.getSnMobileOperator(phone)
//            );
//        }
//
//        @Test
//        void getMobileOperator_withBadOperator_shouldThrowError() throws BadPhoneException {
//            indicatif = "+221";
//            operator = "79";
//            //+221 79 987 65 43
//            phone = template.formatted( indicatif, operator, groupe7Chifffres);
//            assertThrows(
//                    BadPhoneException.class,
//                    () -> Validator.getSnMobileOperator(phone)
//            );
//        }
//
//        @Test
//        void getMobileOperator_withNumberLessThan7digits_shouldThrowError() throws BadPhoneException {
//            indicatif = "+221";
//            operator = "77";
//            String nouveauGroupe6Chifffres = "987654";
//            //+221 77 987 65 4
//            phone = String.format(template, indicatif, operator, nouveauGroupe6Chifffres);
//            assertThrows(
//                    BadPhoneException.class,
//                    () -> Validator.getSnMobileOperator(phone)
//            );
//        }
//
//        @Test
//        void getMobileOperator_withNumberMorThan7digits_shouldThrowError() throws BadPhoneException {
//            indicatif = "+221";
//            operator = "77";
//            String nouveauGroupe8Chifffres = "98765432";
//            //+221 77 987 65 43 2
//            phone = String.format(template, indicatif, operator, nouveauGroupe8Chifffres);
//            assertThrows(
//                    BadPhoneException.class,
//                    () -> Validator.getSnMobileOperator(phone)
//            );
//        }
//
//        @Test
//        void getMobileOperator_withBadNumber_shouldThrowError() throws BadPhoneException {
//            indicatif = "+221";
//            operator = "77";
//            String nouveauGroupe7Chifffres = "987n543";
//            phone = String.format(template, indicatif, operator, nouveauGroupe7Chifffres);
//            //+221 77 987 n5 43
//            assertThrows(
//                    BadPhoneException.class,
//                    () -> Validator.getSnMobileOperator(phone)
//            );
//        }
//
//
//        @Test
//        void ousmane() throws BadPhoneException {
//            indicatif = "+221";
//            operator = "";
//            String nouveauGroupe7Chifffres = "9876543";
//            phone = String.format(template, indicatif, operator, nouveauGroupe7Chifffres);
//            //+221 987 65 43
//            assertThrows(
//                    BadPhoneException.class,
//                    () -> Validator.getSnMobileOperator(phone)
//            );
//        }


// FAMILLE DES NUMERO VALIDE
// FAMILLE DES NUMEROS INVALIDES


        //happy paths
        static Stream<Arguments> argumentsValideMobilePhone() {
            return Stream.of(
                    Arguments.of("+221", "77", Operator.ORANGE),
                    Arguments.of("+221", "78", Operator.ORANGE),
                    Arguments.of("00221", "77", Operator.ORANGE),
                    Arguments.of("00221", "78", Operator.ORANGE),
                    Arguments.of("", "77", Operator.ORANGE),
                    Arguments.of("", "78", Operator.ORANGE),

                    Arguments.of("+221", "76", Operator.FREE),
                    Arguments.of("00221", "76", Operator.FREE),
                    Arguments.of("", "76", Operator.FREE),

                    Arguments.of("+221", "70", Operator.EXPRESSO),
                    Arguments.of("00221", "70", Operator.EXPRESSO),
                    Arguments.of("", "70", Operator.EXPRESSO),

                    Arguments.of("+221", "75", Operator.PROMOBILE),
                    Arguments.of("00221", "75", Operator.PROMOBILE),
                    Arguments.of("", "75", Operator.PROMOBILE)
            );
        }


        @ParameterizedTest
        @MethodSource("argumentsValideMobilePhone")
        void getMobileOperator_shouldReturnCorrectOperator(

                String indicatif, String codeOperateur, Operator operateurCorrespondant

        ) throws BadPhoneException {
            //Arrange
            phone = template.formatted(indicatif, codeOperateur, groupe7Chifffres);
            //Act
            Operator operateur = Validator.getSnMobileOperator(phone);
            //Assert
            assertThat(operateur).isEqualTo(operateurCorrespondant);
        }


        @ParameterizedTest
        @MethodSource("argumentsInvalideMobilePhone")
        void getMobileOperator_shouldThrowException(
                String indicatif, String codeOperateur, String dernierGroupe
        ) {
            //Arrange
            phone = template.formatted(indicatif, codeOperateur, dernierGroupe);
            //Act and Assert
            assertThrows(
                    BadPhoneException.class,
                    () -> Validator.getSnMobileOperator(phone)
            );
        }


        //sad paths
        static Stream<Arguments> argumentsInvalideMobilePhone() {
            return Stream.of(
                    Arguments.of("+222", "70", "9876543"),
                    Arguments.of("+221", "71", "9876543"),
                    Arguments.of("+221", "70", "987"),
                    Arguments.of("+221", "70", "98765432"),
                    Arguments.of("+221", "70", "987654n"),
                    Arguments.of("+221", "", "9876543")
            );
        }


    }
}