package by.geranium;

import static org.assertj.core.api.Assertions.assertThat;

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
    void givenDefaultLogInPattern_thenLogWithExpectedFormat() {
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
    void givenDefaultValueSerializingStrategies_thenLogWithExpectedFormat() {
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

    private interface TestInterface {

        String methodWithLogAnnotation(String str);
    }

    public static class TestClass implements TestInterface {

        @Log
        @Override
        public String methodWithLogAnnotation(String str) {
            return "return value";
        }
    }
}
