package by.geranium;

import by.geranium.core.LoggingLevel;
import by.geranium.core.LoggingStrategy;
import by.geranium.core.LoggingStrategyFactory;

/**
 * @author Maxim Tereshchenko
 */
public class LoggingStrategyFactoryStub implements LoggingStrategyFactory {

    private final LoggingStrategyStub testLoggingStrategy;

    public LoggingStrategyFactoryStub(LoggingStrategyStub testLoggingStrategy) {
        this.testLoggingStrategy = testLoggingStrategy;
    }

    @Override
    public LoggingStrategy getStrategy(LoggingLevel loggingLevel, Class<?> type) {
        testLoggingStrategy.setLoggingLevel(loggingLevel);

        return testLoggingStrategy;
    }
}
