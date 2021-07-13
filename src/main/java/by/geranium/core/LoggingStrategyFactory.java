package by.geranium.core;

/**
 * @author Maxim Tereshchenko
 */
public interface LoggingStrategyFactory {

    LoggingStrategy getStrategy(LoggingLevel loggingLevel, Class<?> type);
}
