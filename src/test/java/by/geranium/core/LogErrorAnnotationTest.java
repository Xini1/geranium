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
class LogErrorAnnotationTest {

    private LoggingStrategyStub loggingStrategyStub;
    private TestInterface testObject;

    @BeforeEach
    void setUp() {
        loggingStrategyStub = new LoggingStrategyStub();
        testObject = TestConfiguration.forInterface(TestInterface.class)
                .forObject(new TestClass())
                .withLoggingStrategy(loggingStrategyStub)
                .build();
    }

    @Test
    void givenMethodThrowingRuntimeExceptionWithoutLogErrorAnnotation_thenLogExceptionWithOffLevel() {
        assertThatThrownBy(() -> testObject.methodThrowingRuntimeException()).isInstanceOf(RuntimeException.class);

        assertThat(loggingStrategyStub.getMessages())
                .containsExactly(
                        "OFF methodThrowingRuntimeException > ",
                        "OFF methodThrowingRuntimeException ! java.lang.RuntimeException message"
                );
    }

    @Test
    void givenMethodThrowingRuntimeExceptionWithLogErrorAnnotation_thenLogException() {
        assertThatThrownBy(() -> testObject.methodThrowingRuntimeExceptionWithLogErrorAnnotation())
                .isInstanceOf(RuntimeException.class);

        assertThat(loggingStrategyStub.getMessages())
                .containsExactly(
                        "OFF methodThrowingRuntimeExceptionWithLogErrorAnnotation > ",
                        "ERROR methodThrowingRuntimeExceptionWithLogErrorAnnotation ! " +
                                "java.lang.RuntimeException message"
                );
    }

    @Test
    void givenMethodThrowingIllegalArgumentExceptionWithLogErrorExceptionIncluded_thenLogExceptionAndStacktrace() {
        assertThatThrownBy(() -> testObject.methodThrowingIllegalArgumentExceptionWithLogErrorThrowableIncluded())
                .isInstanceOf(IllegalArgumentException.class);

        assertThat(loggingStrategyStub.getMessages())
                .containsExactly(
                        "OFF methodThrowingIllegalArgumentExceptionWithLogErrorExceptionIncluded > ",
                        "WARN methodThrowingIllegalArgumentExceptionWithLogErrorExceptionIncluded ! " +
                                "java.lang.IllegalArgumentException message stacktrace"
                );
    }

    private interface TestInterface {

        void methodThrowingRuntimeException();

        void methodThrowingRuntimeExceptionWithLogErrorAnnotation();

        void methodThrowingIllegalArgumentExceptionWithLogErrorThrowableIncluded();
    }

    public static class TestClass implements TestInterface {

        @Override
        public void methodThrowingRuntimeException() {
            throw new RuntimeException("message");
        }

        @Log.Error(LoggingLevel.ERROR)
        @Override
        public void methodThrowingRuntimeExceptionWithLogErrorAnnotation() {
            throw new RuntimeException("message");
        }

        @Log.Error(value = LoggingLevel.WARN, isThrowableIncluded = true)
        @Override
        public void methodThrowingIllegalArgumentExceptionWithLogErrorThrowableIncluded() {
            throw new IllegalArgumentException("message");
        }
    }
}