# Geranium

Declarative logging library

## Example use case

Simple class to demonstrate logging:

```java
public class Greeter {

    @Log(LoggingLevel.INFO)
    public void greet(String name) {
        System.out.println("Hello " + name);
    }
}
```

To enable logging using aspects we need to create
[`SpringAspectAdapter`](https://github.com/Xini1/geranium/blob/master/geranium-spring-aspect/src/main/java/by/geranium/adapter/SpringAspectAdapter.java)
. This
requires [`Geranium`](https://github.com/Xini1/geranium/blob/master/geranium-core/src/main/java/by/geranium/core/Geranium.java)
object, which can be configured with default values using
[`GeraniumConfiguration`](https://github.com/Xini1/geranium/blob/master/geranium-core/src/main/java/by/geranium/GeraniumConfiguration.java)
. Also logging provider must be specified
by [`LoggingStrategyFactory`](https://github.com/Xini1/geranium/blob/master/geranium-core/src/main/java/by/geranium/core/LoggingStrategyFactory.java)
implementation (in this
example [`Slf4jLoggingStrategyFactory`](https://github.com/Xini1/geranium/blob/master/geranium-slf4j/src/main/java/by/geranium/adapter/Slf4jLoggingStrategyFactory.java))
. Spring Boot Application class should look like this:

```java

@SpringBootApplication
@EnableAspectJAutoProxy
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public Greeter greeter() {
        return new Greeter();
    }

    @Bean
    public SpringAspectAdapter springAspectAdapter() {
        return new SpringAspectAdapter(
                new GeraniumConfiguration()
                        .withLoggingStrategyFactory(new Slf4jLoggingStrategyFactory())
                        .withDefaultValueSerializingStrategies()
                        .withDefaultInLoggingPattern()
                        .withDefaultOutLoggingPattern()
                        .withDefaultThrowableLoggingPattern()
                        .build()
        );
    }

    @Bean
    public CommandLineRunner commandLineRunner(Greeter greeter) {
        return args -> greeter.greet("Max");
    }
}
```

Upon executing main method we'll be able to seen in the console:

```
2021-10-01 15:35:39.837  INFO 5304 --- [main] org.example.Greeter: greet > name = Max
Hello Max
2021-10-01 15:35:39.842  INFO 5304 --- [main] org.example.Greeter: greet < 
```

Note: to see actual parameter names, sources should be compiled with `-parameters` option. This can be specified in
Gradle:

```kotlin
tasks.compileJava {
    options.compilerArgs.add("-parameters")
}
```