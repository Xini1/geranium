package by.geranium;

import by.geranium.core.Geranium;
import by.geranium.core.Logger;
import by.geranium.core.LoggingStrategyFactory;
import by.geranium.core.SupportedPlaceholders;
import by.geranium.core.Template;
import by.geranium.core.ToStringSerializingStrategy;
import by.geranium.core.ValueSerializingStrategy;
import by.geranium.core.VoidReturnTypeSerializingStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Maxim Tereshchenko
 */
public class GeraniumConfiguration {

    private final List<ValueSerializingStrategy> valueSerializingStrategyList = new ArrayList<>();
    private LoggingStrategyFactory loggingStrategyFactory;
    private String inLoggingPattern;
    private String outLoggingPattern;
    private String throwableLoggingPattern;

    public static GeraniumConfiguration byDefault(LoggingStrategyFactory loggingStrategyFactory) {
        return new GeraniumConfiguration()
                .withLoggingStrategyFactory(loggingStrategyFactory)
                .withDefaultInLoggingPattern()
                .withDefaultOutLoggingPattern()
                .withDefaultThrowableLoggingPattern()
                .withDefaultValueSerializingStrategies();
    }

    public GeraniumConfiguration withLoggingStrategyFactory(LoggingStrategyFactory loggingStrategyFactory) {
        this.loggingStrategyFactory = loggingStrategyFactory;
        return this;
    }

    public GeraniumConfiguration withValueSerializingStrategy(ValueSerializingStrategy valueSerializingStrategy) {
        valueSerializingStrategyList.add(valueSerializingStrategy);
        return this;
    }

    public GeraniumConfiguration withInLoggingPattern(String pattern) {
        inLoggingPattern = pattern;
        return this;
    }

    public GeraniumConfiguration withOutLoggingPattern(String pattern) {
        outLoggingPattern = pattern;
        return this;
    }

    public GeraniumConfiguration withThrowableLoggingPattern(String pattern) {
        throwableLoggingPattern = pattern;
        return this;
    }

    public GeraniumConfiguration withDefaultValueSerializingStrategies() {
        valueSerializingStrategyList.add(new VoidReturnTypeSerializingStrategy());
        valueSerializingStrategyList.add(new ToStringSerializingStrategy());
        return this;
    }

    public GeraniumConfiguration withDefaultInLoggingPattern() {
        inLoggingPattern = String.format(
                "%s > %s",
                SupportedPlaceholders.METHOD_NAME.placeholder(),
                SupportedPlaceholders.ARGUMENTS.placeholder()
        );
        return this;
    }

    public GeraniumConfiguration withDefaultOutLoggingPattern() {
        outLoggingPattern = String.format(
                "%s < %s",
                SupportedPlaceholders.METHOD_NAME.placeholder(),
                SupportedPlaceholders.RETURN_VALUE.placeholder()
        );
        return this;
    }

    public GeraniumConfiguration withDefaultThrowableLoggingPattern() {
        throwableLoggingPattern = String.format(
                "%s ! %s %s",
                SupportedPlaceholders.METHOD_NAME.placeholder(),
                SupportedPlaceholders.THROWABLE_CLASS.placeholder(),
                SupportedPlaceholders.THROWABLE_MESSAGE.placeholder()

        );
        return this;
    }

    public Geranium build() {
        if (loggingStrategyFactory == null) {
            throw new IllegalArgumentException("Logging strategy factory should not be null");
        }

        if (valueSerializingStrategyList.isEmpty()) {
            throw new IllegalArgumentException("At least one value serializing strategy should be specified");
        }

        return new Geranium(
                new Logger(
                        loggingStrategyFactory,
                        valueSerializingStrategyList,
                        Template.from(inLoggingPattern),
                        Template.from(outLoggingPattern),
                        Template.from(throwableLoggingPattern)
                )
        );
    }
}
