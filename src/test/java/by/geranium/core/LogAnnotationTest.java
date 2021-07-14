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
class LogAnnotationTest {

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
    void givenMethodWithArgumentsAndReturnValue_thenLogArgumentAndReturnValue() {
        testObject.methodWithArguments("first", "second");

        assertThat(testStrategy.getMessages())
                .containsExactly(
                        "DEBUG methodWithArguments > str1 = first, str2 = second",
                        "DEBUG methodWithArguments < return string"
                );
    }

    @Test
    void givenMethodWithoutArgumentsAndWithReturnValue_thenLogReturnValue() {
        testObject.methodWithoutArguments();

        assertThat(testStrategy.getMessages())
                .containsExactly(
                        "DEBUG methodWithoutArguments > ",
                        "DEBUG methodWithoutArguments < return string"
                );
    }

    @Test
    void givenMethodWithoutArgumentsAndReturnValue_thenLogMethodName() {
        testObject.methodWithoutArgumentsAndReturnValue();

        assertThat(testStrategy.getMessages())
                .containsExactly(
                        "DEBUG methodWithoutArgumentsAndReturnValue > ",
                        "DEBUG methodWithoutArgumentsAndReturnValue < "
                );
    }

    @Test
    void givenMethodWithNumericPrimitiveArguments_thenLogArguments() {
        testObject.methodWithNumericPrimitiveArguments((byte) 1, (short) 1, 1, 1L);

        assertThat(testStrategy.getMessages())
                .containsExactly(
                        "DEBUG methodWithNumericPrimitiveArguments > byteArg = 1, shortArg = 1, intArg = 1, " +
                                "longArg = 1",
                        "DEBUG methodWithNumericPrimitiveArguments < "
                );
    }

    @Test
    void givenMethodWithFloatPointPrimitiveArguments_thenLogArguments() {
        testObject.methodWithFloatPointPrimitiveArguments(1.1f, 1.1);

        assertThat(testStrategy.getMessages())
                .containsExactly(
                        "DEBUG methodWithFloatPointPrimitiveArguments > floatArg = 1.1, doubleArg = 1.1",
                        "DEBUG methodWithFloatPointPrimitiveArguments < "
                );
    }

    @Test
    void givenMethodWithOtherPrimitiveArguments_thenLogArguments() {
        testObject.methodWithOtherPrimitiveArguments(false, 'a');

        assertThat(testStrategy.getMessages())
                .containsExactly(
                        "DEBUG methodWithOtherPrimitiveArguments > booleanArg = false, charArg = a",
                        "DEBUG methodWithOtherPrimitiveArguments < "
                );
    }

    @Test
    void givenMethodWithLogInfoAnnotation_thenLogInfoLevel() {
        testObject.methodWithLogInfo();

        assertThat(testStrategy.getMessages())
                .containsExactly(
                        "INFO methodWithLogInfo > ",
                        "INFO methodWithLogInfo < "
                );
    }

    @Test
    void givenOverloadedMethod_thenLogCorrectMethods() {
        testObject.overloadedMethod("str11");
        testObject.overloadedMethod(1, "str22");

        assertThat(testStrategy.getMessages())
                .containsExactly(
                        "DEBUG overloadedMethod > str1 = str11",
                        "DEBUG overloadedMethod < ",
                        "DEBUG overloadedMethod > int1 = 1, str2 = str22",
                        "DEBUG overloadedMethod < "
                );
    }

    private interface TestInterface {

        String methodWithArguments(String str1, String str2);

        String methodWithoutArguments();

        void methodWithoutArgumentsAndReturnValue();

        void methodWithNumericPrimitiveArguments(byte byteArg, short shortArg, int intArg, long longArg);

        void methodWithFloatPointPrimitiveArguments(float floatArg, double doubleArg);

        void methodWithOtherPrimitiveArguments(boolean booleanArg, char charArg);

        void methodWithLogInfo();

        void overloadedMethod(String str1);

        void overloadedMethod(int int1, String str2);
    }

    @Log
    public static class TestClass implements TestInterface {

        @Override
        public String methodWithArguments(String str1, String str2) {
            return "return string";
        }

        @Override
        public String methodWithoutArguments() {
            return "return string";
        }

        @Override
        public void methodWithoutArgumentsAndReturnValue() {
        }

        @Override
        public void methodWithNumericPrimitiveArguments(byte byteArg, short shortArg, int intArg, long longArg) {
        }

        @Override
        public void methodWithFloatPointPrimitiveArguments(float floatArg, double doubleArg) {
        }

        @Override
        public void methodWithOtherPrimitiveArguments(boolean booleanArg, char charArg) {
        }

        @Log(LoggingLevel.INFO)
        @Override
        public void methodWithLogInfo() {
        }

        @Override
        public void overloadedMethod(String str1) {
        }

        @Override
        public void overloadedMethod(int int1, String str2) {
        }
    }
}