package by.geranium.core;

import static org.assertj.core.api.Assertions.assertThat;

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
class LogExcludeAnnotationTest {

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
    void givenMethodWithLogExclude_thenDoNotLogArgument() {
        testObject.methodWithLogExclude("str");

        assertThat(testStrategy.getMessages())
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