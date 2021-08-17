package by.geranium.adapter;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

/**
 * @author Maxim Tereshchenko
 */
class ReflectiveMethodCallNegativeTest {

    @Test
    void givenOverriddenInterfaceMethodCouldNotBeFound_whenReflectiveMethodCallFrom_theIllegalArgumentException() {
        Method interfaceMethod = TestInterface.class.getMethods()[0];
        Object[] arguments = new Object[0];
        TestClass testClass = new TestClass();

        assertThatThrownBy(() -> ReflectiveMethodCall.from(testClass, interfaceMethod, arguments))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Could not find overridden method public abstract void " +
                        "by.geranium.adapter.ReflectiveMethodCallNegativeTest$TestInterface.interfaceMethod()");
    }

    private interface TestInterface {

        void interfaceMethod();
    }

    private static class TestClass {
    }
}
