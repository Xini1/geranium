package by.geranium.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import by.geranium.LoggingStrategyStub;
import by.geranium.TestConfiguration;
import by.geranium.annotation.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Maxim Tereshchenko
 */
class LoggingPatternConfigurationTest {

    private LoggingStrategyStub loggingStrategyStub;
    private TestInterface testObject;

    @BeforeEach
    void setUp() {
        loggingStrategyStub = new LoggingStrategyStub();
        testObject = TestConfiguration.forInterface(TestInterface.class)
                .forObject(new TestClass())
                .withLoggingStrategy(loggingStrategyStub)
                .withInLoggingPattern("${methodName} - start: ${arguments}")
                .withOutLoggingPattern("${methodName} - end: ${returnValue}")
                .withThrowableLoggingPattern("${methodName} - error: ${throwableClass} ${throwableMessage}")
                .build();
    }

    @Test
    void givenMethodWithLogAnnotation_thenLogInSpecifiedPatterns() {
        testObject.methodWithLogAnnotation(1, 2);

        assertThat(loggingStrategyStub.getMessages())
                .containsExactly(
                        "INFO methodWithLogAnnotation - start: first = 1, second = 2",
                        "INFO methodWithLogAnnotation - end: 42"
                );
    }

    @Test
    void givenMethodThrowingException_thenLogInSpecifiedPatterns() {
        assertThatThrownBy(() -> testObject.methodThrowingException(1, 2)).isInstanceOf(IllegalArgumentException.class);

        assertThat(loggingStrategyStub.getMessages())
                .containsExactly(
                        "INFO methodThrowingException - start: first = 1, second = 2",
                        "WARN methodThrowingException - error: java.lang.IllegalArgumentException message"
                );
    }

    private interface TestInterface {

        int methodWithLogAnnotation(int first, int second);

        int methodThrowingException(int first, int second);
    }

    public static class TestClass implements TestInterface {

        @Log(LoggingLevel.INFO)
        @Override
        public int methodWithLogAnnotation(int first, int second) {
            return 42;
        }

        @Log(LoggingLevel.INFO)
        @Log.Error(LoggingLevel.WARN)
        @Override
        public int methodThrowingException(int first, int second) {
            throw new IllegalArgumentException("message");
        }
    }
}
