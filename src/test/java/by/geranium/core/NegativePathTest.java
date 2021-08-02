package by.geranium.core;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import by.geranium.GeraniumConfiguration;
import by.geranium.LoggingStrategyFactoryStub;
import by.geranium.LoggingStrategyStub;
import by.geranium.adapter.ReflectiveMethodCall;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Maxim Tereshchenko
 */
class NegativePathTest {

    private Geranium geranium;

    @BeforeEach
    void setUp() {
        geranium = new GeraniumConfiguration()
                .withLoggingStrategyFactory(new LoggingStrategyFactoryStub(new LoggingStrategyStub()))
                .withInLoggingPattern("")
                .build();
    }

    @Test
    void givenValueSerializingStrategyListIsEmpty_whenLogIn_thenIllegalArgumentException() {
        MethodCall methodCall = ReflectiveMethodCall.from(
                new TestClass(),
                TestInterface.class.getDeclaredMethods()[0],
                new Object[]{"input"}
        );

        assertThatThrownBy(() -> geranium.logMethodCall(methodCall)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Could not find any suitable value serializer for argument: str");
    }

    private interface TestInterface {

        void methodToLog(String str);
    }

    public static class TestClass implements TestInterface {

        @Override
        public void methodToLog(String str) {
        }
    }
}
