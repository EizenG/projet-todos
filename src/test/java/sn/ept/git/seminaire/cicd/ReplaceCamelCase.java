package sn.ept.git.seminaire.cicd;

import org.junit.jupiter.api.DisplayNameGenerator;

import java.lang.reflect.Method;
import java.util.Optional;

public class ReplaceCamelCase extends DisplayNameGenerator.Standard {
    @Override
    public String generateDisplayNameForClass(Class<?> testClass) {
        return replaceCamelCase(super.generateDisplayNameForClass(testClass));
    }

    @Override
    public String generateDisplayNameForNestedClass(Class<?> nestedClass) {
        return replaceCamelCase(super.generateDisplayNameForNestedClass(nestedClass));
    }

    @Override
    public String generateDisplayNameForMethod(Class<?> testClass, Method testMethod) {
        return this.replaceCamelCase(testMethod.getName()) +
                DisplayNameGenerator.parameterTypesAsString(testMethod);
    }

    String replaceCamelCase(String camelCaseMethodeName) {
        StringBuilder displayedName = new StringBuilder();
        Optional.ofNullable(camelCaseMethodeName)
                .orElse("testSansNomCaNeDevraitJamaisArriver")
                .chars()
                .forEach(c -> {
                    char currentChar =(char)c;
                    if (Character.isUpperCase(currentChar)) {
                        displayedName
                                .append(' ')
                                .append(Character.toLowerCase(currentChar));
                    }else if (currentChar=='_') {
                        displayedName.append(' ');
                    } else {
                        displayedName.append(currentChar);
                    }
                });
        return displayedName.toString();
    }
}