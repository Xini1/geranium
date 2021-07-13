package by.geranium.adapter;

import by.geranium.core.LoggingStrategy;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Maxim Tereshchenko
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Slf4jLoggingStrategy implements LoggingStrategy {

    private final Consumer<String> loggingConsumer;
    private final BiConsumer<String, Throwable> exceptionLoggingConsumer;

    public static LoggingStrategy trace(Logger logger) {
        return new Slf4jLoggingStrategy(logger::trace, logger::trace);
    }

    public static LoggingStrategy debug(Logger logger) {
        return new Slf4jLoggingStrategy(logger::debug, logger::debug);
    }

    public static LoggingStrategy info(Logger logger) {
        return new Slf4jLoggingStrategy(logger::info, logger::info);
    }

    public static LoggingStrategy warn(Logger logger) {
        return new Slf4jLoggingStrategy(logger::warn, logger::warn);
    }

    public static LoggingStrategy error(Logger logger) {
        return new Slf4jLoggingStrategy(logger::error, logger::error);
    }

    @Override
    public void log(Supplier<String> messageSupplier) {
        loggingConsumer.accept(messageSupplier.get());
    }

    @Override
    public void log(Supplier<String> messageSupplier, Throwable throwable) {
        exceptionLoggingConsumer.accept(messageSupplier.get(), throwable);
    }
}
