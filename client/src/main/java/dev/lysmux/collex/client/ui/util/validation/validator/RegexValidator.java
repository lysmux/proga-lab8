package dev.lysmux.collex.client.ui.util.validation.validator;

import lombok.Builder;

@Builder
public class RegexValidator implements Validator {
    private final String regex;

    @Override
    public boolean isValid(String value) {
        return value != null && value.matches(regex);
    }
}
