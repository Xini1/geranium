package by.geranium.core;

import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * @author Maxim Tereshchenko
 */
@RequiredArgsConstructor
public class Template {

    private final String original;

    public String replace(Map<SupportedPlaceholders, String> placeholderMap) {
        return placeholderMap.entrySet()
                .stream()
                .reduce(
                        original,
                        (accumulated, entry) -> accumulated.replace(entry.getKey().placeholder(), entry.getValue()),
                        (first, second) -> first //parallel streams are not supported
                );
    }
}
