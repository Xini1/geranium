package by.geranium.core;

import java.util.Objects;

/**
 * @author Maxim Tereshchenko
 */
public class ToStringSerializingStrategy implements ValueSerializingStrategy {

    @Override
    public boolean isSupported(Class<?> type) {
        return true;
    }

    @Override
    public String serialize(Object object) {
        return Objects.toString(object);
    }
}
