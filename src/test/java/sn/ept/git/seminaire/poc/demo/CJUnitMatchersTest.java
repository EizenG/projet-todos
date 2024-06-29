package sn.ept.git.seminaire.poc.demo;

import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import sn.ept.git.seminaire.poc.demo.calculator.Calculator;
import sn.ept.git.seminaire.poc.demo.exception.DivisionByZeroException;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;


@Slf4j
class CJUnitMatchersTest {

    public static final String GIT_EPT = "GIT EPT";


    @Test
        //@DisplayName("Should be true")
    void shouldBeTrue() {
        boolean result = GIT_EPT.startsWith("G");
        assertTrue(result);

        //with a message displayed in case of failure
        result = GIT_EPT.endsWith("T");
        assertTrue(result, "Should ends with T");

    }


    @Test
        //@DisplayName("Should be false")
    void shouldBeFalse() {
        boolean result = GIT_EPT.startsWith("P");
        assertFalse(result, "should not start with P");
    }

    @Test
        //@DisplayName("Should be null")
    void shouldBeNull() {
        final Object o = null;
        assertNull(o);
    }

    @Test
        //@DisplayName("Should not be null")
    void shouldNotBeNull() {
        Object o = new Object();
        assertNotNull(o);
    }


    @Test
        //@DisplayName("Should be equal")
    void shouldBeEqual() {
        final Integer actual = 9;
        final Integer expected = 3 * 3;
        assertEquals(expected, actual);
    }


    //@DisplayName("Should be equal with delta")
    @Test
    void whenAssertingEqualityWithDelta_thenEqual() {
        final float square = 2 * 2; // 4
        final float rectangle = 3 * 2; // 6
        // |6-4|=2
        final float delta = 2;  // |6-4| <= delta
        assertEquals(square, rectangle, delta);
    }


    @Test
        //@DisplayName("Should not be equal")
    void shouldNotBeEqual() {
        final Integer actual = 9;
        final Integer expected = 8;
        assertNotEquals(expected, actual);
    }


    @Test
        //@DisplayName("Should refer to the same object")
    void shouldReferToSameObject() {
        //comparer des references d'objet
        final Object actual = new Object();
        final Object expected = actual;
        assertSame(expected, actual);
    }

    @Test
        //@DisplayName("Should not refer to the same object")
    void shouldNotReferToSameObject() {
        final Object actual = new Object();
        final Object expected = new Object();
        assertNotSame(expected, actual);
    }


    @Test
        //@DisplayName("Should contain the same integers")
    void shouldContainSameIntegers() {
        final int[] actual = new int[]{2, 5, 7};
        final int[] expected = new int[]{2, 5, 7};
        assertArrayEquals(expected, actual);
    }


    @Test
        //@DisplayName("Should contain the same elements")
    void shouldContainSameElements() {
        final List<Integer> FIRST = Arrays.asList(1, 2, 3);
        final List<Integer> SECOND = Arrays.asList(1, 2, 3);
        assertIterableEquals(FIRST, SECOND);
    }

    @Test
        //@DisplayName("Should throw the correct exception")
    void shouldThrowCorrectException() {
        assertThrows(
                DivisionByZeroException.class,
                () -> new Calculator().divide(0, 0)
        );
    }


    @Test
        //@DisplayName("Should not throw an exception")
    void shouldNotThrowException() {
        assertDoesNotThrow(
                () -> new Calculator().divide(1, 1)
        );
    }


    //In case we want to assert that the execution of a supplied Executable ends before a given Timeout, we can use the assertTimeout assertion:
    @Test
    //@DisplayName("Should return the correct message before timeout is exceeded")
    void shouldReturnCorrectMessageBeforeTimeoutIsExceeded() {
        final String VALUE = "Hello World!";
        // final String valeur =

        assertTimeout(
                Duration.ofMillis(1000),
                () -> {
                    //un traitement qui prend un certain temps
                    List<Integer> data = new ArrayList<>();
                    IntStream.range(0, 1000000)
                            .forEach(data::add);
                    Awaitility
                            .await()
                            .atMost(800, TimeUnit.MILLISECONDS)
                            .until(() -> data.size() > 10000);

                    return VALUE;
                }
        );

    }


    //It asserts that the expected list of Strings matches the actual list.
    // The logic to match a string with another string is :
    /*
        A?	    A occurs once or not at all => {0,1}
        A+	    A occurs once or more times => {1,}
        A*	    A occurs zero or more times =>  => {0,}
        A{n}    A occurs n times exactly
        A{n,}	A occurs n or more times
        A{n,m}	A occurs at least n times but less than m times
    */
    @Test
    void whenAssertingEqualityListOfStrings_thenEqual() {
        List<String> actual = Arrays.asList("GIT", "11", "JUnit", "+221762236160");

        List<String> expected = Arrays.asList("[a-zA-Z]+", "[0-9]+", "JUnit", "^(\\+221|00221)?(33|78|77|76|75|70)[0-9]{7}$");

        assertLinesMatch(expected, actual);
    }

}