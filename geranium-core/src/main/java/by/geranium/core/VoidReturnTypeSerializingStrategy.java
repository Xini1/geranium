package by.geranium.core;

/**
 * @author Maxim Tereshchenko
 */
public class VoidReturnTypeSerializingStrategy implements ValueSerializingStrategy {

    @Override
    public boolean isSupported(Class<?> type) {
        return type == void.class;
    }

    @Override
    public String serialize(Object object) {
        return "";
    }
}
