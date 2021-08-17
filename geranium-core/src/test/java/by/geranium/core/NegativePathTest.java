package by.geranium.core;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import by.geranium.LoggingStrategyFactoryStub;
import by.geranium.LoggingStrategyStub;
import by.geranium.adapter.ReflectiveMethodCall;
import org.junit.jupiter.api.Test;

import java.util.Collections;

/**
 * @author Maxim Tereshchenko
 */
class NegativePathTest {

    @Test
    void givenValueSerializingStrategyListIsEmpty_whenLogIn_thenIllegalArgumentException()
            throws NoSuchMethodException {

        Geranium geranium = new Geranium(
                new Logger(
                        new LoggingStrategyFactoryStub(new LoggingStrategyStub()),
                        Collections.emptyList(),
                        Template.from(""),
                        Template.from(""),
                        null
                )
        );

        MethodCall methodCall = ReflectiveMethodCall.from(
                new TestClass(),
                TestInterface.class.getDeclaredMethod("methodWithArgument", String.class),
                new Object[]{"input"}
        );

        assertThatThrownBy(() -> geranium.logMethodCall(methodCall)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Could not find any suitable value serializing strategy for argument str");
    }

    @Test
    void givenValueSerializingStrategyListIsNotContainSuitableSerializer_whenLogIn_thenIllegalArgumentException()
            throws NoSuchMethodException {

        Geranium geranium = new Geranium(
                new Logger(
                        new LoggingStrategyFactoryStub(new LoggingStrategyStub()),
                        Collections.singletonList(new NothingSupportedValueSerializingStrategy()),
                        Template.from(""),
                        Template.from(""),
                        null
                )
        );

        MethodCall methodCall = ReflectiveMethodCall.from(
                new TestClass(),
                TestInterface.class.getDeclaredMethod("methodWithArgument", String.class),
                new Object[]{"input"}
        );

        assertThatThrownBy(() -> geranium.logMethodCall(methodCall)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Could not find any suitable value serializing strategy for argument str");
    }

    @Test
    void givenValueSerializingStrategyListIsEmpty_whenLogOut_thenIllegalArgumentException()
            throws NoSuchMethodException {

        Geranium geranium = new Geranium(
                new Logger(
                        new LoggingStrategyFactoryStub(new LoggingStrategyStub()),
                        Collections.emptyList(),
                        Template.from(""),
                        Template.from(""),
                        null
                )
        );

        MethodCall methodCall = ReflectiveMethodCall.from(
                new TestClass(),
                TestInterface.class.getDeclaredMethod("methodWithReturnValue"),
                new Object[0]
        );

        assertThatThrownBy(() -> geranium.logMethodCall(methodCall)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Could not find any suitable value serializing strategy for return value of " +
                        "by.geranium.core.NegativePathTest$TestClass.methodWithReturnValue");
    }

    @Test
    void givenValueSerializingStrategyListIsNotContainSuitableSerializer_whenLogOut_thenIllegalArgumentException()
            throws NoSuchMethodException {

        Geranium geranium = new Geranium(
                new Logger(
                        new LoggingStrategyFactoryStub(new LoggingStrategyStub()),
                        Collections.singletonList(new NothingSupportedValueSerializingStrategy()),
                        Template.from(""),
                        Template.from(""),
                        null
                )
        );

        MethodCall methodCall = ReflectiveMethodCall.from(
                new TestClass(),
                TestInterface.class.getDeclaredMethod("methodWithReturnValue"),
                new Object[0]
        );

        assertThatThrownBy(() -> geranium.logMethodCall(methodCall)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Could not find any suitable value serializing strategy for return value of " +
                        "by.geranium.core.NegativePathTest$TestClass.methodWithReturnValue");
    }

    private interface TestInterface {

        void methodWithArgument(String str);

        String methodWithReturnValue();
    }

    public static class TestClass implements TestInterface {

        @Override
        public void methodWithArgument(String str) {
        }

        @Override
        public String methodWithReturnValue() {
            return "";
        }
    }

    private static class NothingSupportedValueSerializingStrategy implements ValueSerializingStrategy {

        @Override
        public boolean isSupported(Class<?> type) {
            return false;
        }

        @Override
        public String serialize(Object object) {
            return null;
        }
    }
}
