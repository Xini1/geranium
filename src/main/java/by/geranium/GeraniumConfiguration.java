package by.geranium;

import by.geranium.core.Geranium;
import by.geranium.core.Logger;
import by.geranium.core.LoggingStrategyFactory;
import by.geranium.core.ValueSerializingStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Maxim Tereshchenko
 */
public class GeraniumConfiguration {

    private final List<ValueSerializingStrategy> valueSerializingStrategyList = new ArrayList<>();
    private LoggingStrategyFactory loggingStrategyFactory;

    public GeraniumConfiguration withLoggingStrategyFactory(LoggingStrategyFactory loggingStrategyFactory) {
        this.loggingStrategyFactory = loggingStrategyFactory;
        return this;
    }

    public GeraniumConfiguration withValueSerializingStrategy(ValueSerializingStrategy valueSerializingStrategy) {
        valueSerializingStrategyList.add(valueSerializingStrategy);
        return this;
    }

    public Geranium build() {
        return new Geranium(
                new Logger(
                        loggingStrategyFactory,
                        valueSerializingStrategyList
                )
        );
    }
}
