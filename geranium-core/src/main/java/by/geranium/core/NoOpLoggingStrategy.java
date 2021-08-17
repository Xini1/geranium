package by.geranium.core;

import java.util.function.Supplier;

/**
 * @author Maxim Tereshchenko
 */
public class NoOpLoggingStrategy implements LoggingStrategy {

    @Override
    public void log(Supplier<String> messageSupplier) {
        //empty
    }

    @Override
    public void log(Supplier<String> messageSupplier, Throwable throwable) {
        //empty
    }
}
