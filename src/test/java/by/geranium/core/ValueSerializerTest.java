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
class ValueSerializerTest {

    private LoggingStrategyStub loggingStrategyStub;
    private TestInterface testObject;

    @BeforeEach
    void setUp() {
        loggingStrategyStub = new LoggingStrategyStub();
        testObject = TestConfiguration.forInterface(TestInterface.class)
                .forObject(new TestClass())
                .withLoggingStrategyFactory(new LoggingStrategyFactoryStub(loggingStrategyStub))
                .withValueSerializer(new TestIntegerSerializingStrategy())
                .build();
    }

    @Test
    void givenMethodWithIntegerArgument_thenModifyIntegerValue() {
        testObject.methodWithIntegerArgument(1);

        assertThat(loggingStrategyStub.getMessages())
                .containsExactly(
                        "DEBUG methodWithIntegerArgument > intArg = number",
                        "DEBUG methodWithIntegerArgument < "
                );
    }

    @Test
    void givenMethodWithIntegerAndStringArguments_thenDoNotModifyStringValue() {
        testObject.methodWithIntegerAndStringArguments(1, "string");

        assertThat(loggingStrategyStub.getMessages())
                .containsExactly(
                        "DEBUG methodWithIntegerAndStringArguments > intArg = number, strArg = string",
                        "DEBUG methodWithIntegerAndStringArguments < "
                );
    }

    @Test
    void givenMethodWithIntegerReturnValue_thenModifyReturnValue() {
        testObject.methodWithIntegerReturnValue();

        assertThat(loggingStrategyStub.getMessages())
                .containsExactly(
                        "DEBUG methodWithIntegerReturnValue > ",
                        "DEBUG methodWithIntegerReturnValue < number"
                );
    }

    private interface TestInterface {

        void methodWithIntegerArgument(int intArg);

        void methodWithIntegerAndStringArguments(int intArg, String strArg);

        int methodWithIntegerReturnValue();
    }

    @Log
    public static class TestClass implements TestInterface {

        @Override
        public void methodWithIntegerArgument(int intArg) {
        }

        @Override
        public void methodWithIntegerAndStringArguments(int intArg, String strArg) {
        }

        @Override
        public int methodWithIntegerReturnValue() {
            return 0;
        }
    }

    private static class TestIntegerSerializingStrategy implements ValueSerializingStrategy {

        @Override
        public boolean isSupported(Class<?> type) {
            return type == int.class;
        }

        @Override
        public String serialize(Object object) {
            return "number";
        }
    }
}
