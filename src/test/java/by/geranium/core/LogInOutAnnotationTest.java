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
class LogInOutAnnotationTest {

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
    void givenMethodWithLogInAnnotation_thenLogOnlyArguments() {
        testObject.methodWithLogInAnnotation("input string");

        assertThat(loggingStrategyStub.getMessages())
                .containsExactly(
                        "INFO methodWithLogInAnnotation > str = input string",
                        "OFF methodWithLogInAnnotation < "
                );
    }

    @Test
    void givenMethodWithLogOutAnnotation_thenLogOnlyReturnValue() {
        testObject.methodWithLogOutAnnotation();

        assertThat(loggingStrategyStub.getMessages())
                .containsExactly(
                        "OFF methodWithLogOutAnnotation > ",
                        "INFO methodWithLogOutAnnotation < return string"
                );
    }

    @Test
    void givenMethodWithBothLogInOutAnnotations_thenLogBothArgumentsAndReturnValue() {
        testObject.methodWithBothLogInOutAnnotations("input string");

        assertThat(loggingStrategyStub.getMessages())
                .containsExactly(
                        "WARN methodWithBothLogInOutAnnotations > str = input string",
                        "ERROR methodWithBothLogInOutAnnotations < return string"
                );
    }

    @Test
    void givenMethodWithBothLogInOutAndPlainLogAnnotations_thenPlainLogPrioritized() {
        testObject.methodWithBothLogInOutAnnotations("input string");

        assertThat(loggingStrategyStub.getMessages())
                .containsExactly(
                        "WARN methodWithBothLogInOutAnnotations > str = input string",
                        "ERROR methodWithBothLogInOutAnnotations < return string"
                );
    }

    private interface TestInterface {

        void methodWithLogInAnnotation(String str);

        String methodWithLogOutAnnotation();

        String methodWithBothLogInOutAnnotations(String str);

        String methodWithBothLogInOutAndPlainLogAnnotations(String str);
    }

    public static class TestClass implements TestInterface {

        @Log.In(LoggingLevel.INFO)
        @Override
        public void methodWithLogInAnnotation(String str) {
        }

        @Log.Out(LoggingLevel.INFO)
        @Override
        public String methodWithLogOutAnnotation() {
            return "return string";
        }

        @Log.In(LoggingLevel.WARN)
        @Log.Out(LoggingLevel.ERROR)
        @Override
        public String methodWithBothLogInOutAnnotations(String str) {
            return "return string";
        }

        @Log(LoggingLevel.INFO)
        @Log.In(LoggingLevel.WARN)
        @Log.Out(LoggingLevel.ERROR)
        @Override
        public String methodWithBothLogInOutAndPlainLogAnnotations(String str) {
            return "return string";
        }
    }
}
