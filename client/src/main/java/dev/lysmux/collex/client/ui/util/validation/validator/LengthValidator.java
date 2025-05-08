package dev.lysmux.collex.client.ui.util.validation.validator;

import lombok.Builder;

@Builder
public class LengthValidator implements Validator {
    @Builder.Default
    private Integer min = 0;
    private Integer max;

    @Override
    public boolean isValid(String value) {
        if (value == null) return false;

        if (value.length() < min) return false;
        if (max != null && value.length() > max) return false;

        return true;
    }
}
