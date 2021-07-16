package by.geranium;

import by.geranium.adapter.InvocationHandlerAdapter;
import by.geranium.core.LoggingStrategyFactory;
import by.geranium.core.ToStringSerializingStrategy;
import by.geranium.core.ValueSerializingStrategy;
import by.geranium.core.VoidReturnTypeSerializingStrategy;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Maxim Tereshchenko
 */
public class TestConfiguration<I, T extends I> {

    private final Class<I> interfaceClass;
    private final T object;
    private final List<ValueSerializingStrategy> valueSerializerList;
    private LoggingStrategyFactory loggingStrategyFactory;

    private TestConfiguration(Class<I> interfaceClass, T object) {
        this.interfaceClass = interfaceClass;
        this.object = object;
        valueSerializerList = new ArrayList<>();
    }

    public static <U> TestConfiguration.Builder<U> forInterface(Class<U> interfaceClass) {
        return new Builder<>(interfaceClass);
    }

    public TestConfiguration<I, T> withLoggingStrategyFactory(LoggingStrategyFactory loggingStrategyFactory) {
        this.loggingStrategyFactory = loggingStrategyFactory;
        return this;
    }

    public TestConfiguration<I, T> withValueSerializer(ValueSerializingStrategy valueSerializer) {
        valueSerializerList.add(valueSerializer);
        return this;
    }

    public I build() {
        var geraniumConfiguration = new GeraniumConfiguration()
                .withLoggingStrategyFactory(loggingStrategyFactory);
        valueSerializerList.forEach(geraniumConfiguration::withValueSerializingStrategy);
        geraniumConfiguration.withValueSerializingStrategy(new VoidReturnTypeSerializingStrategy())
                .withValueSerializingStrategy(new ToStringSerializingStrategy());

        return interfaceClass.cast(
                Proxy.newProxyInstance(
                        interfaceClass.getClassLoader(),
                        object.getClass().getInterfaces(),
                        new InvocationHandlerAdapter(
                                geraniumConfiguration.build(),
                                object
                        )
                )
        );
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
