package by.geranium.core;

import lombok.RequiredArgsConstructor;

import java.lang.reflect.Parameter;

/**
 * @author Maxim Tereshchenko
 */
@RequiredArgsConstructor
public class MethodArgument {

    private final Parameter parameter;
    private final Object value;

    public String name() {
        return parameter.getName();
    }

    public boolean isSupported(ValueSerializingStrategy valueSerializingStrategy) {
        return valueSerializingStrategy.isSupported(parameter.getType());
    }

    public String toString(ValueSerializingStrategy valueSerializingStrategy) {
        return String.format("%s = %s", name(), valueSerializingStrategy.serialize(value));
    }
}
