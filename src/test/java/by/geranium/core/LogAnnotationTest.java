package by.geranium.core;

import static org.assertj.core.api.Assertions.assertThat;

import by.geranium.LoggingStrategyStub;
import by.geranium.TestConfiguration;
import by.geranium.annotation.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Maxim Tereshchenko
 */
class LogAnnotationTest {

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
    void givenMethodWithArgumentsAndReturnValue_thenLogArgumentAndReturnValue() {
        testObject.methodWithArguments("first", "second");

        assertThat(loggingStrategyStub.getMessages())
                .containsExactly(
                        "DEBUG methodWithArguments > str1 = first, str2 = second",
                        "DEBUG methodWithArguments < return string"
                );
    }

    @Test
    void givenMethodWithoutArgumentsAndWithReturnValue_thenLogReturnValue() {
        testObject.methodWithoutArguments();

        assertThat(loggingStrategyStub.getMessages())
                .containsExactly(
                        "DEBUG methodWithoutArguments > ",
                        "DEBUG methodWithoutArguments < return string"
                );
    }

    @Test
    void givenMethodWithoutArgumentsAndReturnValue_thenLogMethodName() {
        testObject.methodWithoutArgumentsAndReturnValue();

        assertThat(loggingStrategyStub.getMessages())
                .containsExactly(
                        "DEBUG methodWithoutArgumentsAndReturnValue > ",
                        "DEBUG methodWithoutArgumentsAndReturnValue < "
                );
    }

    @Test
    void givenMethodWithNumericPrimitiveArguments_thenLogArguments() {
        testObject.methodWithNumericPrimitiveArguments((byte) 1, (short) 1, 1, 1L);

        assertThat(loggingStrategyStub.getMessages())
                .containsExactly(
                        "DEBUG methodWithNumericPrimitiveArguments > byteArg = 1, shortArg = 1, intArg = 1, " +
                                "longArg = 1",
                        "DEBUG methodWithNumericPrimitiveArguments < "
                );
    }

    @Test
    void givenMethodWithFloatPointPrimitiveArguments_thenLogArguments() {
        testObject.methodWithFloatPointPrimitiveArguments(1.1f, 1.1);

        assertThat(loggingStrategyStub.getMessages())
                .containsExactly(
                        "DEBUG methodWithFloatPointPrimitiveArguments > floatArg = 1.1, doubleArg = 1.1",
                        "DEBUG methodWithFloatPointPrimitiveArguments < "
                );
    }

    @Test
    void givenMethodWithOtherPrimitiveArguments_thenLogArguments() {
        testObject.methodWithOtherPrimitiveArguments(false, 'a');

        assertThat(loggingStrategyStub.getMessages())
                .containsExactly(
                        "DEBUG methodWithOtherPrimitiveArguments > booleanArg = false, charArg = a",
                        "DEBUG methodWithOtherPrimitiveArguments < "
                );
    }

    @Test
    void givenMethodWithLogInfoAnnotation_thenLogInfoLevel() {
        testObject.methodWithLogInfo();

        assertThat(loggingStrategyStub.getMessages())
                .containsExactly(
                        "INFO methodWithLogInfo > ",
                        "INFO methodWithLogInfo < "
                );
    }

    @Test
    void givenMethodWithLogOffAnnotation_thenLogOffLevel() {
        testObject.methodWithLogOff();

        assertThat(loggingStrategyStub.getMessages())
                .containsExactly(
                        "OFF methodWithLogOff > ",
                        "OFF methodWithLogOff < "
                );
    }

    @Test
    void givenOverloadedMethod_thenLogCorrectMethods() {
        testObject.overloadedMethod("str11");
        testObject.overloadedMethod(1, "str22");

        assertThat(loggingStrategyStub.getMessages())
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

        void methodWithLogOff();

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

        @Log(LoggingLevel.OFF)
        @Override
        public void methodWithLogOff() {
        }

        @Override
        public void overloadedMethod(String str1) {
        }

        @Override
        public void overloadedMethod(int int1, String str2) {
        }
    }
}