package by.geranium.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import by.geranium.annotation.Log;
import by.geranium.core.Geranium;
import by.geranium.core.LoggingLevel;
import by.geranium.core.MethodCall;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

/**
 * @author Maxim Tereshchenko
 */
@ExtendWith(SpringExtension.class)
class SpringAspectAdapterTest {

    @MockBean
    private Geranium geranium;
    @Autowired
    private TestClass testClass;
    @Captor
    private ArgumentCaptor<MethodCall> methodCallArgumentCaptor;

    @Test
    void givenMethodWithLogAnnotation_thenCallMethod_thenAdviceInvoked() throws Throwable {
        testClass.methodWithLogAnnotation();

        verify(geranium).logMethodCall(methodCallArgumentCaptor.capture());
        assertThat(methodCallArgumentCaptor.getValue())
                .extracting(
                        MethodCall::targetClass,
                        MethodCall::methodArguments,
                        MethodCall::returnType,
                        MethodCall::methodName,
                        MethodCall::inLoggingLevel,
                        MethodCall::outLoggingLevel,
                        MethodCall::exceptionLoggingLevel
                )
                .containsExactly(
                        TestClass.class,
                        Collections.emptyList(),
                        void.class,
                        "methodWithLogAnnotation",
                        LoggingLevel.INFO,
                        LoggingLevel.INFO,
                        LoggingLevel.OFF
                );
    }

    @Test
    void givenMethodWithLogInAnnotation_thenCallMethod_thenAdviceInvoked() throws Throwable {
        testClass.methodWithLogInAnnotation();

        verify(geranium).logMethodCall(methodCallArgumentCaptor.capture());
        assertThat(methodCallArgumentCaptor.getValue())
                .extracting(
                        MethodCall::targetClass,
                        MethodCall::methodArguments,
                        MethodCall::returnType,
                        MethodCall::methodName,
                        MethodCall::inLoggingLevel,
                        MethodCall::outLoggingLevel,
                        MethodCall::exceptionLoggingLevel
                )
                .containsExactly(
                        TestClass.class,
                        Collections.emptyList(),
                        void.class,
                        "methodWithLogInAnnotation",
                        LoggingLevel.INFO,
                        LoggingLevel.OFF,
                        LoggingLevel.OFF
                );
    }

    @Test
    void givenMethodWithLogOutAnnotation_thenCallMethod_thenAdviceInvoked() throws Throwable {
        testClass.methodWithLogOutAnnotation();

        verify(geranium).logMethodCall(methodCallArgumentCaptor.capture());
        assertThat(methodCallArgumentCaptor.getValue())
                .extracting(
                        MethodCall::targetClass,
                        MethodCall::methodArguments,
                        MethodCall::returnType,
                        MethodCall::methodName,
                        MethodCall::inLoggingLevel,
                        MethodCall::outLoggingLevel,
                        MethodCall::exceptionLoggingLevel
                )
                .containsExactly(
                        TestClass.class,
                        Collections.emptyList(),
                        void.class,
                        "methodWithLogOutAnnotation",
                        LoggingLevel.OFF,
                        LoggingLevel.INFO,
                        LoggingLevel.OFF
                );
    }

    @Test
    void givenMethodWithBothLogInOutAnnotations_thenCallMethod_thenAdviceInvoked() throws Throwable {
        testClass.methodWithBothLogInOutAnnotations();

        verify(geranium).logMethodCall(methodCallArgumentCaptor.capture());
        assertThat(methodCallArgumentCaptor.getValue())
                .extracting(
                        MethodCall::targetClass,
                        MethodCall::methodArguments,
                        MethodCall::returnType,
                        MethodCall::methodName,
                        MethodCall::inLoggingLevel,
                        MethodCall::outLoggingLevel,
                        MethodCall::exceptionLoggingLevel
                )
                .containsExactly(
                        TestClass.class,
                        Collections.emptyList(),
                        void.class,
                        "methodWithBothLogInOutAnnotations",
                        LoggingLevel.WARN,
                        LoggingLevel.ERROR,
                        LoggingLevel.OFF
                );
    }

    @Test
    void givenMethodWithBothLogInOutAndPlainLogAnnotations_thenCallMethod_thenAdviceInvoked() throws Throwable {
        testClass.methodWithBothLogInOutAndPlainLogAnnotations();

        verify(geranium).logMethodCall(methodCallArgumentCaptor.capture());
        assertThat(methodCallArgumentCaptor.getValue())
                .extracting(
                        MethodCall::targetClass,
                        MethodCall::methodArguments,
                        MethodCall::returnType,
                        MethodCall::methodName,
                        MethodCall::inLoggingLevel,
                        MethodCall::outLoggingLevel,
                        MethodCall::exceptionLoggingLevel
                )
                .containsExactly(
                        TestClass.class,
                        Collections.emptyList(),
                        void.class,
                        "methodWithBothLogInOutAndPlainLogAnnotations",
                        LoggingLevel.INFO,
                        LoggingLevel.INFO,
                        LoggingLevel.OFF
                );
    }

    @Test
    void givenMethodWithLogErrorAnnotation_thenCallMethod_thenAdviceInvoked() throws Throwable {
        assertThatThrownBy(() -> testClass.methodWithLogErrorAnnotation())
                .isInstanceOf(IllegalArgumentException.class);

        verify(geranium).logMethodCall(methodCallArgumentCaptor.capture());
        assertThat(methodCallArgumentCaptor.getValue())
                .extracting(
                        MethodCall::targetClass,
                        MethodCall::methodArguments,
                        MethodCall::returnType,
                        MethodCall::methodName,
                        MethodCall::inLoggingLevel,
                        MethodCall::outLoggingLevel,
                        MethodCall::exceptionLoggingLevel
                )
                .containsExactly(
                        TestClass.class,
                        Collections.emptyList(),
                        void.class,
                        "methodWithLogErrorAnnotation",
                        LoggingLevel.OFF,
                        LoggingLevel.OFF,
                        LoggingLevel.INFO
                );
    }

    @Test
    void givenMethodWithoutAnnotation_thenCallMethod_thenAdviceNotInvoked() {
        testClass.methodWithoutAnnotation();

        verifyNoInteractions(geranium);
    }

    static class TestClass {

        @Log(LoggingLevel.INFO)
        void methodWithLogAnnotation() {
        }

        @Log.In(LoggingLevel.INFO)
        void methodWithLogInAnnotation() {
        }

        @Log.Out(LoggingLevel.INFO)
        void methodWithLogOutAnnotation() {
        }

        @Log.In(LoggingLevel.WARN)
        @Log.Out(LoggingLevel.ERROR)
        void methodWithBothLogInOutAnnotations() {
        }

        @Log(LoggingLevel.INFO)
        @Log.In(LoggingLevel.WARN)
        @Log.Out(LoggingLevel.ERROR)
        void methodWithBothLogInOutAndPlainLogAnnotations() {
        }

        @Log.Error(LoggingLevel.INFO)
        void methodWithLogErrorAnnotation() {
            throw new IllegalArgumentException();
        }

        void methodWithoutAnnotation() {
        }
    }

    @TestConfiguration
    @EnableAspectJAutoProxy
    static class TestConfig {

        @Bean
        public SpringAspectAdapter springAspectAdapter(Geranium advice) {
            return new SpringAspectAdapter(advice);
        }

        @Bean
        public TestClass testClass() {
            return new TestClass();
        }
    }
}
