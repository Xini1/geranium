package by.geranium.core;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import by.geranium.GeraniumConfiguration;
import by.geranium.LoggingStrategyFactoryStub;
import by.geranium.LoggingStrategyStub;
import by.geranium.adapter.ReflectiveMethodCall;
import org.junit.jupiter.api.Test;

/**
 * @author Maxim Tereshchenko
 */
class NegativePathTest {

    @Test
    void givenValueSerializingStrategyListIsEmpty_whenLogIn_thenIllegalArgumentException()
            throws NoSuchMethodException {

        Geranium geranium = new GeraniumConfiguration()
                .withLoggingStrategyFactory(new LoggingStrategyFactoryStub(new LoggingStrategyStub()))
                .withInLoggingPattern("")
                .build();

        MethodCall methodCall = ReflectiveMethodCall.from(
                new TestClass(),
                TestInterface.class.getDeclaredMethod("methodWithArgument", String.class),
                new Object[]{"input"}
        );

        assertThatThrownBy(() -> geranium.logMethodCall(methodCall)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Could not find any suitable value serializing strategy for argument str");
    }

    @Test
    void givenValueSerializingStrategyListIsNotContainSuitableSerializer_whenLogIn_thenIllegalArgumentException()
            throws NoSuchMethodException {

        Geranium geranium = new GeraniumConfiguration()
                .withLoggingStrategyFactory(new LoggingStrategyFactoryStub(new LoggingStrategyStub()))
                .withInLoggingPattern("")
                .withValueSerializingStrategy(new NothingSupportedValueSerializingStrategy())
                .build();

        MethodCall methodCall = ReflectiveMethodCall.from(
                new TestClass(),
                TestInterface.class.getDeclaredMethod("methodWithArgument", String.class),
                new Object[]{"input"}
        );

        assertThatThrownBy(() -> geranium.logMethodCall(methodCall)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Could not find any suitable value serializing strategy for argument str");
    }

    private interface TestInterface {

        void methodWithArgument(String str);
    }

    public static class TestClass implements TestInterface {

        @Override
        public void methodWithArgument(String str) {
        }
    }

    private static class NothingSupportedValueSerializingStrategy implements ValueSerializingStrategy {

        @Override
        public boolean isSupported(Class<?> type) {
            return false;
        }

        @Override
        public String serialize(Object object) {
            return null;
        }
    }
}
