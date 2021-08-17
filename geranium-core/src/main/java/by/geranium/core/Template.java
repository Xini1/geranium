package by.geranium.core;

import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * @author Maxim Tereshchenko
 */
@RequiredArgsConstructor
public class Template {

    private final String pattern;

    public static Template from(String pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("Logging pattern should not be null");
        }

        return new Template(pattern);
    }

    public String replace(Map<SupportedPlaceholders, String> placeholderMap) {
        return placeholderMap.entrySet()
                .stream()
                .reduce(
                        pattern,
                        (accumulated, entry) -> accumulated.replace(entry.getKey().placeholder(), entry.getValue()),
                        (first, second) -> first //parallel streams are not supported
                );
    }
}
