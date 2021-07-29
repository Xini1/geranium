package by.geranium;

import by.geranium.core.Geranium;
import by.geranium.core.Logger;
import by.geranium.core.LoggingStrategyFactory;
import by.geranium.core.Template;
import by.geranium.core.ValueSerializingStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Maxim Tereshchenko
 */
public class GeraniumConfiguration {

    private final List<ValueSerializingStrategy> valueSerializingStrategyList = new ArrayList<>();
    private LoggingStrategyFactory loggingStrategyFactory;
    private Template inLoggingTemplate;
    private Template outLoggingTemplate;
    private Template throwableLoggingTemplate;

    public GeraniumConfiguration withLoggingStrategyFactory(LoggingStrategyFactory loggingStrategyFactory) {
        this.loggingStrategyFactory = loggingStrategyFactory;
        return this;
    }

    public GeraniumConfiguration withValueSerializingStrategy(ValueSerializingStrategy valueSerializingStrategy) {
        valueSerializingStrategyList.add(valueSerializingStrategy);
        return this;
    }

    public GeraniumConfiguration withInLoggingPattern(String pattern) {
        inLoggingTemplate = new Template(pattern);
        return this;
    }

    public GeraniumConfiguration withOutLoggingPattern(String pattern) {
        outLoggingTemplate = new Template(pattern);
        return this;
    }

    public GeraniumConfiguration withThrowableLoggingPattern(String pattern) {
        throwableLoggingTemplate = new Template(pattern);
        return this;
    }

    public Geranium build() {
        return new Geranium(
                new Logger(
                        loggingStrategyFactory,
                        valueSerializingStrategyList,
                        inLoggingTemplate, outLoggingTemplate, throwableLoggingTemplate
                )
        );
    }
}
