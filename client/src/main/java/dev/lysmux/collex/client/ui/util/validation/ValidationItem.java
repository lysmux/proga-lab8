package dev.lysmux.collex.client.ui.util.validation;

import dev.lysmux.collex.client.ui.util.validation.validator.Validator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class ValidationItem {
    private static final String ERROR_STYLE_CLASS = "error-field";

    private final BooleanProperty isValid = new SimpleBooleanProperty(false);
    private final List<Validator> validators = new ArrayList<>();

    private final TextField field;

    public ValidationItem(TextField field) {
        this.field = field;
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            validate();
        });
    }

    public void addValidator(Validator validator) {
        validators.add(validator);
    }

    public void validate() {
        boolean valid = validators.stream().allMatch(v -> v.isValid(field.getText()));
        isValid.set(valid);

        if (valid) {
            field.getStyleClass().remove(ERROR_STYLE_CLASS);
        } else {
            if (!field.getStyleClass().contains(ERROR_STYLE_CLASS)) {
                field.getStyleClass().add(ERROR_STYLE_CLASS);
            }
        }
    }

    public BooleanProperty isValidProperty() {
        return isValid;
    }
}