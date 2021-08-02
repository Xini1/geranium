package by.geranium;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import by.geranium.core.ToStringSerializingStrategy;
import org.junit.jupiter.api.Test;

/**
 * @author Maxim Tereshchenko
 */
class GeraniumConfigurationNegativeTest {

    @Test
    void givenLoggingTemplateWithNullString_whenGeraniumBuild_thenIllegalArgumentException() {
        var geraniumConfiguration = new GeraniumConfiguration()
                .withValueSerializingStrategy(new ToStringSerializingStrategy())
                .withLoggingStrategyFactory(new LoggingStrategyFactoryStub(new LoggingStrategyStub()));

        assertThatThrownBy(geraniumConfiguration::build).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Logging pattern should not be null");
    }

    @Test
    void givenLoggingStrategyFactoryIsNull_whenGeraniumBuild_thenIllegalArgumentException() {
        var geraniumConfiguration = new GeraniumConfiguration()
                .withValueSerializingStrategy(new ToStringSerializingStrategy());

        assertThatThrownBy(geraniumConfiguration::build).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Logging strategy factory should not be null");
    }

    @Test
    void givenValueSerializingStrategyListIsEmpty_whenGeraniumBuild_thenIllegalArgumentException() {
        var geraniumConfiguration = new GeraniumConfiguration()
                .withLoggingStrategyFactory(new LoggingStrategyFactoryStub(new LoggingStrategyStub()));

        assertThatThrownBy(geraniumConfiguration::build).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("At least one value serializing strategy should be specified");
    }
}
