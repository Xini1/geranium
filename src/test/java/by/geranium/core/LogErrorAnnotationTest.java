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
                        "DEBUG methodThrowingRuntimeException > ",
                        "OFF methodThrowingRuntimeException ! java.lang.RuntimeException"
                );
    }

    @Test
    void givenMethodThrowingRuntimeExceptionWithLogErrorAnnotation_thenLogException() {
        assertThatThrownBy(() -> testObject.methodThrowingRuntimeExceptionWithLogErrorAnnotation())
                .isInstanceOf(RuntimeException.class);

        assertThat(loggingStrategyStub.getMessages())
                .containsExactly(
                        "DEBUG methodThrowingRuntimeExceptionWithLogErrorAnnotation > ",
                        "ERROR methodThrowingRuntimeExceptionWithLogErrorAnnotation ! java.lang.RuntimeException"
                );
    }

    private interface TestInterface {

        void methodThrowingRuntimeException();

        void methodThrowingRuntimeExceptionWithLogErrorAnnotation();
    }

    @Log
    public static class TestClass implements TestInterface {

        @Override
        public void methodThrowingRuntimeException() {
            throw new RuntimeException();
        }

        @Log.Error(LoggingLevel.ERROR)
        @Override
        public void methodThrowingRuntimeExceptionWithLogErrorAnnotation() {
            throw new RuntimeException();
        }
    }
}