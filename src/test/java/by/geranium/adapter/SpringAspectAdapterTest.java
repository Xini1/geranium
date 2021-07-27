package by.geranium.adapter;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import by.geranium.annotation.Log;
import by.geranium.core.Geranium;
import by.geranium.core.LoggingLevel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author Maxim Tereshchenko
 */
@ExtendWith(SpringExtension.class)
class SpringAspectAdapterTest {

    @MockBean
    private Geranium geranium;
    @Autowired
    private TestClass testClass;

    @Test
    void givenAnnotatedMethod_thenCallMethod_thenAdviceInvoked() throws Throwable {
        testClass.annotatedMethod();

        verify(geranium).logMethodCall(argThat(argument -> argument.targetClass() == TestClass.class &&
                argument.methodArguments().isEmpty() &&
                argument.returnType() == void.class &&
                argument.methodName().equals("annotatedMethod") &&
                argument.exceptionLoggingLevel() == LoggingLevel.OFF &&
                argument.inLoggingLevel() == LoggingLevel.DEBUG));
    }

    @Test
    void givenMethodWithoutAnnotation_thenCallMethod_thenAdviceNotInvoked() {
        testClass.methodWithoutAnnotation();

        verifyNoInteractions(geranium);
    }

    static class TestClass {

        @Log
        void annotatedMethod() {
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
