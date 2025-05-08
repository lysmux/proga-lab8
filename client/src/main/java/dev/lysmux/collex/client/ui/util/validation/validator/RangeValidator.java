package dev.lysmux.collex.client.ui.util.validation.validator;

import lombok.Builder;

@Builder
public class RangeValidator implements Validator {
    private final Double min;
    private final Double max;

    @Override
    public boolean isValid(String value) {
        if (value == null) return false;

        try {
            double doubleValue = Double.parseDouble(value);

            if (min != null && doubleValue < min) return false;
            if (max != null && doubleValue > max) return false;
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }
}
