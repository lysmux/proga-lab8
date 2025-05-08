package dev.lysmux.collex.client.ui.util.validation;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class FormValidator {
    private final List<ValidationItem> fields = new ArrayList<>();
    private final BooleanProperty isValid = new SimpleBooleanProperty(false);

    public ValidationItem addField(TextField field) {
        ValidationItem item = new ValidationItem(field);
        fields.add(item);
        reBindIsValid();

        return item;
    }

    private void reBindIsValid() {
        isValid.unbind();
        isValid.bind(isValidBindingFactory());
    }

    private BooleanBinding isValidBindingFactory() {
        return Bindings.createBooleanBinding(
                () -> fields.stream().allMatch(f -> f.isValidProperty().get()),
                fields.stream().map(ValidationItem::isValidProperty).toArray(BooleanProperty[]::new)
        );
    }

    public BooleanProperty isValidProperty() {
        return isValid;
    }
}
