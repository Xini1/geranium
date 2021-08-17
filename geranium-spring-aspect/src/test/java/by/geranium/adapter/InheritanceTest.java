package by.geranium.adapter;

import by.geranium.GeraniumConfiguration;
import by.geranium.LoggingStrategyFactoryStub;
import by.geranium.LoggingStrategyStub;
import by.geranium.annotation.Log;
import by.geranium.core.LoggingLevel;
import by.geranium.core.ToStringSerializingStrategy;
import by.geranium.core.VoidReturnTypeSerializingStrategy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author Maxim Tereshchenko
 */
@ExtendWith(SpringExtension.class)
class InheritanceTest {

    @Autowired
    private LoggingStrategyStub loggingStrategyStub;
    @Autowired
    private TestClass testClass;

    @BeforeEach
    void setUp() {
        loggingStrategyStub.reset();
    }

    @Test
    void givenParentMethodWithAnnotation_thenLogLevelTrace() {
        testClass.parentMethodWithAnnotation();

        Assertions.assertThat(loggingStrategyStub.getMessages())
                .containsExactly(
                        "TRACE parentMethodWithAnnotation > ",
                        "TRACE parentMethodWithAnnotation < "
                );
    }

    @Test
    void givenParentMethodWithoutAnnotation_thenLogLevelDebug() {
        testClass.parentMethodWithoutAnnotation();

        Assertions.assertThat(loggingStrategyStub.getMessages())
                .containsExactly(
                        "DEBUG parentMethodWithoutAnnotation > ",
                        "DEBUG parentMethodWithoutAnnotation < "
                );
    }

    @Test
    void givenClassMethodWithAnnotation_thenLogLevelInfo() {
        testClass.classMethodWithAnnotation();

        Assertions.assertThat(loggingStrategyStub.getMessages())
                .containsExactly(
                        "INFO classMethodWithAnnotation > ",
                        "INFO classMethodWithAnnotation < "
                );
    }

    @Log(LoggingLevel.DEBUG)
    private static class ParentClass {

        @Log(LoggingLevel.TRACE)
        void parentMethodWithAnnotation() {
        }

        void parentMethodWithoutAnnotation() {
        }
    }

    static class TestClass extends ParentClass {

        @Log(LoggingLevel.INFO)
        void classMethodWithAnnotation() {
        }
    }

    @TestConfiguration
    @EnableAspectJAutoProxy(proxyTargetClass = true)
    static class TestConfig {

        @Bean
        public SpringAspectAdapter springAspectAdapter() {
            return new SpringAspectAdapter(
                    new GeraniumConfiguration()
                            .withLoggingStrategyFactory(new LoggingStrategyFactoryStub(loggingStrategyStub()))
                            .withValueSerializingStrategy(new VoidReturnTypeSerializingStrategy())
                            .withValueSerializingStrategy(new ToStringSerializingStrategy())
                            .withInLoggingPattern("${methodName} > ${arguments}")
                            .withOutLoggingPattern("${methodName} < ${returnValue}")
                            .withThrowableLoggingPattern("")
                            .build()
            );
        }

        @Bean
        public LoggingStrategyStub loggingStrategyStub() {
            return new LoggingStrategyStub();
        }

        @Bean
        public TestClass testClass() {
            return new TestClass();
        }
    }
}
