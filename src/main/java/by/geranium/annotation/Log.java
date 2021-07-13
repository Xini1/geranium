package by.geranium.annotation;

import by.geranium.core.LoggingLevel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Maxim Tereshchenko
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Log {

    LoggingLevel value() default LoggingLevel.DEBUG;

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    @interface Exclude {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface Error {
        LoggingLevel value() default LoggingLevel.ERROR;
    }
}
