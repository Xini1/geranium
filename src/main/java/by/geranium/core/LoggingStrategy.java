package by.geranium.core;

import java.util.function.Supplier;

/**
 * @author Maxim Tereshchenko
 */
public interface LoggingStrategy {

    void log(Supplier<String> messageSupplier);

    void log(Supplier<String> messageSupplier, Throwable throwable);
}
