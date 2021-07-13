package by.geranium.core;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Parameter;

/**
 * @author Maxim Tereshchenko
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MethodArgument {

    private final String name;
    private final Object value;

    public static MethodArgument from(Parameter parameter, Object value) {
        return new MethodArgument(parameter.getName(), value);
    }

    @Override
    public String toString() {
        return String.format("%s = %s", name, value);
    }
}
