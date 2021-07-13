package by.geranium.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import by.geranium.LoggingStrategyFactoryStub;
import by.geranium.LoggingStrategyStub;
import by.geranium.adapter.InvocationHandlerAdapter;
import by.geranium.annotation.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

/**
 * @author Maxim Tereshchenko
 */
class LogErrorAnnotationTest {

    private LoggingStrategyStub testStrategy;
    private TestInterface testObject;

    @BeforeEach
    void setUp() {
        testStrategy = new LoggingStrategyStub();
        testObject = (TestInterface) Proxy.newProxyInstance(
                TestClass.class.getClassLoader(),
                TestClass.class.getInterfaces(),
                new InvocationHandlerAdapter(
                        new Advice(
                                new Logger(
                                        new LoggingStrategyFactoryStub(testStrategy)
                                )
                        ),
                        new TestClass()
                )
        );
    }

    @Test
    void givenMethodThrowingRuntimeExceptionWithoutLogErrorAnnotation_thenLogExceptionWithOffLevel() {
        assertThatThrownBy(() -> testObject.methodThrowingRuntimeException()).isInstanceOf(RuntimeException.class);

        assertThat(testStrategy.getMessages())
                .containsExactly(
                        "DEBUG methodThrowingRuntimeException > ",
                        "OFF methodThrowingRuntimeException ! java.lang.RuntimeException"
                );
    }

    @Test
    void givenMethodThrowingRuntimeExceptionWithLogErrorAnnotation_thenLogException() {
        assertThatThrownBy(() -> testObject.methodThrowingRuntimeExceptionWithLogErrorAnnotation())
                .isInstanceOf(RuntimeException.class);

        assertThat(testStrategy.getMessages())
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

        @Log.Error
        @Override
        public void methodThrowingRuntimeExceptionWithLogErrorAnnotation() {
            throw new RuntimeException();
        }
    }
}