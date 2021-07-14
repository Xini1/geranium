package by.geranium.adapter;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import by.geranium.annotation.Log;
import by.geranium.core.Advice;
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
    private Advice advice;
    @Autowired
    private TestClass testClass;

    @Test
    void givenAnnotatedMethod_thenCallMethod_thenAdviceInvoked() throws Throwable {
        testClass.annotatedMethod();

        verify(advice).logMethodCall(argThat(argument -> argument.getDeclaringClass() == TestClass.class &&
                argument.getMethodArguments().isEmpty() &&
                !argument.hasReturnValue() &&
                argument.getMethodName().equals("annotatedMethod") &&
                argument.getExceptionLoggingLevel() == LoggingLevel.OFF &&
                argument.getLoggingLevel() == LoggingLevel.DEBUG));
    }

    @Test
    void givenMethodWithoutAnnotation_thenCallMethod_thenAdviceNotInvoked() {
        testClass.methodWithoutAnnotation();

        verifyNoInteractions(advice);
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
        public SpringAspectAdapter springAspectAdapter(Advice advice) {
            return new SpringAspectAdapter(advice);
        }

        @Bean
        public TestClass testClass() {
            return new TestClass();
        }
    }
}
