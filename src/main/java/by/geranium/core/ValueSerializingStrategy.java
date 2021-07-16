package by.geranium.core;

/**
 * @author Maxim Tereshchenko
 */
public interface ValueSerializingStrategy {

    boolean isSupported(Class<?> type);

    String serialize(Object object);
}
