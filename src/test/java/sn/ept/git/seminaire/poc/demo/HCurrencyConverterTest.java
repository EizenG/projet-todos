package sn.ept.git.seminaire.poc.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import sn.ept.git.seminaire.poc.demo.Currency;
import sn.ept.git.seminaire.poc.demo.CurrencyConverter;
import sn.ept.git.seminaire.poc.demo.CurrencyService;

import java.io.IOException;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;


@Slf4j
 class HCurrencyConverterTest {

    private static final double coef = 1.25;
    private static final double   value =12;
    private static CurrencyConverter mockConverter;
    private  static CurrencyService mockCurrencyService;
    private  static CurrencyService  currencyService;
    private static CurrencyConverter converter;

    @BeforeAll
    public static void beforeAll() throws Exception {
        mockCurrencyService = Mockito.mock(CurrencyService.class);
        mockConverter = new CurrencyConverter(mockCurrencyService);

        currencyService = new CurrencyService();
        converter = new CurrencyConverter(currencyService);
    }

    static Stream<Arguments> sameCurrenciesTestData() {
        return Stream.of(
                of(Currency.USD, Currency.USD, value, value),
                of(Currency.EURO, Currency.EURO, value, value),
                of(Currency.XAF, Currency.XAF, value, value),
                of(Currency.XOF, Currency.XOF, value, value)
        );
    }


    static Stream<Arguments> differentCurrenciesTestData() {
        return Stream.of(
                of(Currency.USD, Currency.EURO, value, value*coef),
                of(Currency.USD, Currency.XAF, value, value*coef),
                of(Currency.USD, Currency.XOF, value, value*coef),
                of(Currency.EURO, Currency.USD, value, value*coef),
                of(Currency.EURO, Currency.XAF, value, value*coef),
                of(Currency.EURO, Currency.XOF, value, value*coef),
                of(Currency.XAF, Currency.USD, value, value*coef),
                of(Currency.XAF, Currency.EURO, value, value*coef),
                of(Currency.XAF, Currency.XOF, value, value*coef),
                of(Currency.XOF, Currency.USD, value, value*coef),
                of(Currency.XOF, Currency.EURO, value, value*coef),
                of(Currency.XOF, Currency.XAF, value, value*coef)
        );
    }

    @BeforeEach
    void beforeEach() throws IOException {
        Mockito
                .when(mockCurrencyService.convert(
                        ArgumentMatchers.any(Currency.class),
                        ArgumentMatchers.any(Currency.class),
                        ArgumentMatchers.anyDouble()
                ))
                .thenAnswer((Answer<Double>) invocation -> {
                    Object[] args = invocation.getArguments();
                   log.info(" >>> {} " , args[0]);
                   log.info(" >>> {} " , args[1]);
                   log.info(" >>> {} " , args[2]);
                    return Double.parseDouble(args[2].toString()) * (args[0].equals(args[1]) ? 1 : coef);
                });

    }

    @ParameterizedTest
    @MethodSource("sameCurrenciesTestData")
     void sameCurrentShouldReturnSameValue(Currency from, Currency to, double input, double expected) {
        double result = mockConverter.convert(from, to, input);
        assertThat(result).isEqualTo(expected);
    }


    @ParameterizedTest
    @MethodSource("differentCurrenciesTestData")
     void differentCurrentShouldReturnDifferentValue(Currency from, Currency to, double input, double expected) {
        double result = mockConverter.convert(from, to, input);
        assertThat(result).isEqualTo(expected);
    }


    //this test will fail if CurrencyService does not work
    @ParameterizedTest
    @MethodSource("sameCurrenciesTestData")
    void _sameCurrentShouldReturnSameValue(Currency from, Currency to, double input, double expected) {
        double result = converter.convert(from, to, input);
        assertThat(result). isEqualTo(expected);
    }



    //this test will fail if CurrencyService does not work
    @ParameterizedTest
    @MethodSource("differentCurrenciesTestData")
    void _differentCurrentShouldReturnDifferentValue(Currency from, Currency to, double input, double expected) {
        double result = converter.convert(from, to, input);
        assertThat(result).isNotEqualTo(expected);
    }
} 
