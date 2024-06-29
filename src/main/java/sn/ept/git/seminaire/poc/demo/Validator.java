package sn.ept.git.seminaire.poc.demo;

import sn.ept.git.seminaire.poc.demo.exception.BadPhoneException;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Validator {

    public static final String MOTIF = "^(\\+221|00221)?(70|75|76|77|78)\\d{7}$";

    private Validator() {
        super();
    }


    public static Operator getSnMobileOperator(String phone) throws BadPhoneException {
        Pattern r = Pattern.compile(MOTIF, Pattern.CASE_INSENSITIVE);
        Matcher matcher = r.matcher(phone);

        if (!matcher.matches()) {
            throw new BadPhoneException("Bad phone " + phone);
        }

        String operator = matcher.group(2);

        return switch (operator) {
            case "77", "78" -> Operator.ORANGE;
            case "70" -> Operator.EXPRESSO;
            case "76" -> Operator.FREE;
            default -> Operator.PROMOBILE;
        };
    }

    public static Integer sumOfSquaresOfEvenNumbers(List<Integer> liste) {
        return liste
                .stream()
                .filter(value -> value % 2 == 0)
                .map(value -> value * value)
                .reduce(0, Integer::sum);
    }

}
