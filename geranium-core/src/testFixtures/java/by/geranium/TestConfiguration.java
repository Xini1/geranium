package by.geranium;

import by.geranium.adapter.InvocationHandlerAdapter;
import by.geranium.core.LoggingStrategyFactory;
import by.geranium.core.ToStringSerializingStrategy;
import by.geranium.core.ValueSerializingStrategy;
import by.geranium.core.VoidReturnTypeSerializingStrategy;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Maxim Tereshchenko
 */
public class TestConfiguration<I, T extends I> {

    private final Class<I> interfaceClass;
    private final T object;
    private final List<ValueSerializingStrategy> valueSerializerList;
    private LoggingStrategyFactory loggingStrategyFactory;
    private String inLoggingPattern = "${methodName} > ${arguments}";
    private String outLoggingPattern = "${methodName} < ${returnValue}";
    private String throwableLoggingPattern = "${methodName} ! ${throwableClass} ${throwableMessage}";
    private GeraniumConfiguration geraniumConfiguration;

    private TestConfiguration(Class<I> interfaceClass, T object) {
        this.interfaceClass = interfaceClass;
        this.object = object;
        valueSerializerList = new ArrayList<>();
    }

    public static <U> Builder<U> forInterface(Class<U> interfaceClass) {
        return new Builder<>(interfaceClass);
    }

    public TestConfiguration<I, T> withLoggingStrategy(LoggingStrategyStub loggingStrategy) {
        this.loggingStrategyFactory = new LoggingStrategyFactoryStub(loggingStrategy);
        return this;
    }

    public TestConfiguration<I, T> withValueSerializer(ValueSerializingStrategy valueSerializer) {
        valueSerializerList.add(valueSerializer);
        return this;
    }

    public TestConfiguration<I, T> withInLoggingPattern(String pattern) {
        inLoggingPattern = pattern;
        return this;
    }

    public TestConfiguration<I, T> withOutLoggingPattern(String pattern) {
        outLoggingPattern = pattern;
        return this;
    }

    public TestConfiguration<I, T> withThrowableLoggingPattern(String pattern) {
        throwableLoggingPattern = pattern;
        return this;
    }

    public TestConfiguration<I, T> withGeraniumConfiguration(GeraniumConfiguration geraniumConfiguration) {
        this.geraniumConfiguration = geraniumConfiguration;
        return this;
    }

    public I build() {
        return interfaceClass.cast(
                Proxy.newProxyInstance(
                        interfaceClass.getClassLoader(),
                        object.getClass().getInterfaces(),
                        new InvocationHandlerAdapter(
                                Optional.ofNullable(geraniumConfiguration)
                                        .orElseGet(this::configureGeranium)
                                        .build(),
                                object
                        )
                )
        );
    }

    private GeraniumConfiguration configureGeranium() {
        var newGeraniumConfiguration = new GeraniumConfiguration()
                .withLoggingStrategyFactory(loggingStrategyFactory)
                .withInLoggingPattern(inLoggingPattern)
                .withOutLoggingPattern(outLoggingPattern)
                .withThrowableLoggingPattern(throwableLoggingPattern);

        valueSerializerList.forEach(newGeraniumConfiguration::withValueSerializingStrategy);
        newGeraniumConfiguration.withValueSerializingStrategy(new VoidReturnTypeSerializingStrategy())
                .withValueSerializingStrategy(new ToStringSerializingStrategy());

        return newGeraniumConfiguration;
    }

    public static class Builder<I> {

        private final Class<I> interfaceClass;

        private Builder(Class<I> interfaceClass) {
            this.interfaceClass = interfaceClass;
        }

        public <T extends I> TestConfiguration<I, T> forObject(T object) {
            return new TestConfiguration<>(interfaceClass, object);
        }
    }
}
