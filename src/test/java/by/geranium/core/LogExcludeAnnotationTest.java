package by.geranium.core;

import static org.assertj.core.api.Assertions.assertThat;

import by.geranium.LoggingStrategyFactoryStub;
import by.geranium.LoggingStrategyStub;
import by.geranium.TestConfiguration;
import by.geranium.annotation.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Maxim Tereshchenko
 */
class LogExcludeAnnotationTest {

    private LoggingStrategyStub loggingStrategyStub;
    private TestInterface testObject;

    @BeforeEach
    void setUp() {
        loggingStrategyStub = new LoggingStrategyStub();
        testObject = TestConfiguration.forInterface(TestInterface.class)
                .forObject(new TestClass())
                .withLoggingStrategyFactory(new LoggingStrategyFactoryStub(loggingStrategyStub))
                .build();
    }

    @Test
    void givenMethodWithLogExclude_thenDoNotLogArgument() {
        testObject.methodWithLogExclude("str");

        assertThat(loggingStrategyStub.getMessages())
                .containsExactly(
                        "DEBUG methodWithLogExclude > ",
                        "DEBUG methodWithLogExclude < "
                );
    }

    private interface TestInterface {

        void methodWithLogExclude(String arg);
    }

    @Log
    public static class TestClass implements TestInterface {

        @Override
        public void methodWithLogExclude(@Log.Exclude String arg) {
        }
    }
}