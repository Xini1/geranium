package by.geranium.core;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * @author Maxim Tereshchenko
 */
@RequiredArgsConstructor
public enum SupportedPlaceholders {

    METHOD_NAME,
    ARGUMENTS,
    RETURN_VALUE,
    EXCEPTION_CLASS;

    private static final String PLACEHOLDER_PREFIX = "${";
    private static final String PLACEHOLDER_SUFFIX = "}";

    public String placeholder() {
        return PLACEHOLDER_PREFIX + placeholderName() + PLACEHOLDER_SUFFIX;
    }

    private String placeholderName() {
        return Arrays.stream(name().split("_"))
                .map(String::toLowerCase)
                .map(this::toUpperCaseFirstLetter)
                .collect(Collectors.collectingAndThen(Collectors.joining(), this::toLowerCaseFirstLetter));
    }

    private String toUpperCaseFirstLetter(String str) {
        return changeFirstLetter(str, Character::toUpperCase);
    }

    private String toLowerCaseFirstLetter(String str) {
        return changeFirstLetter(str, Character::toLowerCase);
    }

    private String changeFirstLetter(String str, UnaryOperator<Character> firstLetterFunction) {
        return firstLetterFunction.apply(str.charAt(0)) + str.substring(1);
    }
}
