package by.geranium;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import by.geranium.annotation.Log;
import by.geranium.core.ToStringSerializingStrategy;
import by.geranium.core.VoidReturnTypeSerializingStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Maxim Tereshchenko
 */
class DefaultGeraniumConfigurationTest {

    private LoggingStrategyStub loggingStrategyStub;

    @BeforeEach
    void setUp() {
        loggingStrategyStub = new LoggingStrategyStub();
    }

    @Test
    void givenDefaultInLoggingPattern_thenLogWithExpectedFormat() {
        TestConfiguration.forInterface(TestInterface.class)
                .forObject(new TestClass())
                .withGeraniumConfiguration(
                        new GeraniumConfiguration()
                                .withLoggingStrategyFactory(new LoggingStrategyFactoryStub(loggingStrategyStub))
                                .withValueSerializingStrategy(new VoidReturnTypeSerializingStrategy())
                                .withValueSerializingStrategy(new ToStringSerializingStrategy())
                                .withDefaultInLoggingPattern()
                                .withOutLoggingPattern("")
                                .withThrowableLoggingPattern("")
                )
                .build()
                .methodWithLogAnnotation("input");

        assertThat(loggingStrategyStub.getMessages())
                .contains("DEBUG methodWithLogAnnotation > str = input");
    }

    @Test
    void givenDefaultValueSerializingStrategiesAndNonVoidMethod_thenLogWithExpectedFormat() {
        TestConfiguration.forInterface(TestInterface.class)
                .forObject(new TestClass())
                .withGeraniumConfiguration(
                        new GeraniumConfiguration()
                                .withLoggingStrategyFactory(new LoggingStrategyFactoryStub(loggingStrategyStub))
                                .withDefaultValueSerializingStrategies()
                                .withInLoggingPattern("")
                                .withOutLoggingPattern("${returnValue}")
                                .withThrowableLoggingPattern("")
                )
                .build()
                .methodWithLogAnnotation("");

        assertThat(loggingStrategyStub.getMessages())
                .contains("DEBUG return value");
    }

    @Test
    void givenDefaultValueSerializingStrategiesAndVoidMethod_thenLogWithExpectedFormat() {
        TestConfiguration.forInterface(TestInterface.class)
                .forObject(new TestClass())
                .withGeraniumConfiguration(
                        new GeraniumConfiguration()
                                .withLoggingStrategyFactory(new LoggingStrategyFactoryStub(loggingStrategyStub))
                                .withDefaultValueSerializingStrategies()
                                .withInLoggingPattern("")
                                .withOutLoggingPattern("${returnValue}")
                                .withThrowableLoggingPattern("")
                )
                .build()
                .voidMethod();

        assertThat(loggingStrategyStub.getMessages())
                .contains("DEBUG ");
    }

    @Test
    void givenDefaultOutLoggingPattern_thenLogWithExpectedFormat() {
        TestConfiguration.forInterface(TestInterface.class)
                .forObject(new TestClass())
                .withGeraniumConfiguration(
                        new GeraniumConfiguration()
                                .withLoggingStrategyFactory(new LoggingStrategyFactoryStub(loggingStrategyStub))
                                .withValueSerializingStrategy(new VoidReturnTypeSerializingStrategy())
                                .withValueSerializingStrategy(new ToStringSerializingStrategy())
                                .withInLoggingPattern("")
                                .withDefaultOutLoggingPattern()
                                .withThrowableLoggingPattern("")
                )
                .build()
                .methodWithLogAnnotation("input");

        assertThat(loggingStrategyStub.getMessages())
                .contains("DEBUG methodWithLogAnnotation < return value");
    }

    @Test
    void givenDefaultThrowableLoggingPattern_thenLogWithExpectedFormat() {
        TestInterface testObject = TestConfiguration.forInterface(TestInterface.class)
                .forObject(new TestClass())
                .withGeraniumConfiguration(
                        new GeraniumConfiguration()
                                .withLoggingStrategyFactory(new LoggingStrategyFactoryStub(loggingStrategyStub))
                                .withValueSerializingStrategy(new VoidReturnTypeSerializingStrategy())
                                .withValueSerializingStrategy(new ToStringSerializingStrategy())
                                .withInLoggingPattern("")
                                .withOutLoggingPattern("")
                                .withDefaultThrowableLoggingPattern()
                )
                .build();

        assertThatThrownBy(testObject::methodThrowingException).isInstanceOf(RuntimeException.class);

        assertThat(loggingStrategyStub.getMessages())
                .contains("ERROR methodThrowingException ! java.lang.RuntimeException message");
    }

    @Test
    void givenFullDefaultConfigurationAndMethodWithLogAnnotation_thenLogWithExpectedFormat() {
        TestConfiguration.forInterface(TestInterface.class)
                .forObject(new TestClass())
                .withGeraniumConfiguration(
                        GeraniumConfiguration.byDefault(new LoggingStrategyFactoryStub(loggingStrategyStub))
                )
                .build()
                .methodWithLogAnnotation("input");

        assertThat(loggingStrategyStub.getMessages())
                .containsExactly(
                        "DEBUG methodWithLogAnnotation > str = input",
                        "DEBUG methodWithLogAnnotation < return value"
                );
    }

    @Test
    void givenFullDefaultConfigurationAndMethodThrowingException_thenLogWithExpectedFormat() {
        TestInterface testObject = TestConfiguration.forInterface(TestInterface.class)
                .forObject(new TestClass())
                .withGeraniumConfiguration(
                        GeraniumConfiguration.byDefault(new LoggingStrategyFactoryStub(loggingStrategyStub))
                )
                .build();

        assertThatThrownBy(testObject::methodThrowingException).isInstanceOf(RuntimeException.class);

        assertThat(loggingStrategyStub.getMessages())
                .containsExactly(
                        "OFF methodThrowingException > ",
                        "ERROR methodThrowingException ! java.lang.RuntimeException message"
                );
    }

    private interface TestInterface {

        String methodWithLogAnnotation(String str);

        void voidMethod();

        void methodThrowingException();
    }

    public static class TestClass implements TestInterface {

        @Log
        @Override
        public String methodWithLogAnnotation(String str) {
            return "return value";
        }

        @Log
        @Override
        public void voidMethod() {
        }

        @Log.Error
        @Override
        public void methodThrowingException() {
            throw new RuntimeException("message");
        }
    }
}
