package by.geranium.adapter;

import by.geranium.core.LoggingLevel;
import by.geranium.core.LoggingStrategy;
import by.geranium.core.LoggingStrategyFactory;
import by.geranium.core.NoOpLoggingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.Function;

/**
 * @author Maxim Tereshchenko
 */
public class Slf4jLoggingStrategyFactory implements LoggingStrategyFactory {

    private static final Map<LoggingLevel, Function<Logger, Boolean>> IS_LOGGING_LEVEL_ENABLED_FUNCTION_MAP = Map.of(
            LoggingLevel.OFF, logger -> false,
            LoggingLevel.TRACE, Logger::isTraceEnabled,
            LoggingLevel.DEBUG, Logger::isDebugEnabled,
            LoggingLevel.INFO, Logger::isInfoEnabled,
            LoggingLevel.WARN, Logger::isWarnEnabled,
            LoggingLevel.ERROR, Logger::isErrorEnabled
    );
    private static final Map<LoggingLevel, Function<Logger, LoggingStrategy>> LOGGING_STRATEGY_FUNCTION_MAP = Map.of(
            LoggingLevel.TRACE, Slf4jLoggingStrategy::trace,
            LoggingLevel.DEBUG, Slf4jLoggingStrategy::debug,
            LoggingLevel.INFO, Slf4jLoggingStrategy::info,
            LoggingLevel.WARN, Slf4jLoggingStrategy::warn,
            LoggingLevel.ERROR, Slf4jLoggingStrategy::error
    );

    @Override
    public LoggingStrategy getStrategy(LoggingLevel loggingLevel, Class<?> type) {
        var logger = LoggerFactory.getLogger(type);

        if (isLoggingLevelDisabled(loggingLevel, logger)) {
            return new NoOpLoggingStrategy();
        }

        return LOGGING_STRATEGY_FUNCTION_MAP.get(loggingLevel)
                .apply(logger);
    }

    private boolean isLoggingLevelDisabled(LoggingLevel loggingLevel, Logger logger) {
        return Boolean.FALSE.equals(IS_LOGGING_LEVEL_ENABLED_FUNCTION_MAP.get(loggingLevel).apply(logger));
    }
}
