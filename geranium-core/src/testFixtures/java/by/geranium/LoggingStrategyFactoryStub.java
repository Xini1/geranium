package by.geranium;

import by.geranium.core.LoggingLevel;
import by.geranium.core.LoggingStrategy;
import by.geranium.core.LoggingStrategyFactory;

/**
 * @author Maxim Tereshchenko
 */
public class LoggingStrategyFactoryStub implements LoggingStrategyFactory {

    private final LoggingStrategyStub loggingStrategyStub;

    public LoggingStrategyFactoryStub(LoggingStrategyStub loggingStrategyStub) {
        this.loggingStrategyStub = loggingStrategyStub;
    }

    @Override
    public LoggingStrategy getStrategy(LoggingLevel loggingLevel, Class<?> type) {
        loggingStrategyStub.setLoggingLevel(loggingLevel);

        return loggingStrategyStub;
    }
}
