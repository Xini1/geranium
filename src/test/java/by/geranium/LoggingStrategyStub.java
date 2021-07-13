package by.geranium;

import by.geranium.core.LoggingLevel;
import by.geranium.core.LoggingStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author Maxim Tereshchenko
 */
public class LoggingStrategyStub implements LoggingStrategy {

    private final List<String> messages = new ArrayList<>();
    private LoggingLevel loggingLevel = null;

    public List<String> getMessages() {
        return messages;
    }

    @Override
    public void log(Supplier<String> messageSupplier) {
        messages.add(String.format("%s %s", loggingLevel, messageSupplier.get()));
    }

    @Override
    public void log(Supplier<String> messageSupplier, Throwable throwable) {
        log(messageSupplier);
    }

    public void setLoggingLevel(LoggingLevel loggingLevel) {
        this.loggingLevel = loggingLevel;
    }
}
