package dev.lysmux.collex.client.ui.controller;

import com.google.inject.Inject;
import dev.lysmux.collex.client.ui.StateManager;
import dev.lysmux.collex.client.ui.util.loader.Loader;
import dev.lysmux.collex.client.ui.util.validation.FormValidator;
import dev.lysmux.collex.client.ui.util.validation.validator.LengthValidator;
import dev.lysmux.collex.client.ui.util.validation.validator.RangeValidator;
import dev.lysmux.collex.domain.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class ItemViewController implements Initializable {
    private final StateManager stateManager;

    private final ObjectProperty<LabWork> itemProperty = new SimpleObjectProperty<>();
    private final BooleanProperty readOnlyProperty = new SimpleBooleanProperty();

    @Setter private Consumer<LabWork> onSave;

    @FXML
    private StackPane rootPane;
    @FXML
    private TextField id;
    @FXML
    private TextField ownerId;
    @FXML
    private TextField creationDate;
    @FXML
    private TextField nameField;
    @FXML
    private TextField minimalPointField;
    @FXML
    private ChoiceBox<Difficulty> difficultyChoice;
    @FXML
    private TextField coordinateXField;
    @FXML
    private TextField coordinateYField;
    @FXML
    private TextField authorNameField;
    @FXML
    private DatePicker authorBirthdayField;
    @FXML
    private TextField authorWeightField;
    @FXML
    private TextField authorLocationX;
    @FXML
    private TextField authorLocationY;
    @FXML
    private TextField authorLocationName;

    @FXML
    private Button saveBtn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        difficultyChoice.getItems().addAll(Difficulty.values());

        itemProperty.addListener((_, _, newValue) -> {
            readOnlyProperty.set(newValue == null || stateManager.getUserId() != newValue.getOwnerId());
            updateFields();
        });
        saveBtn.textProperty().bind(Bindings.createStringBinding(() -> itemProperty.get() == null
                ? resourceBundle.getString("collection.button.add")
                : resourceBundle.getString("collection.button.save"), itemProperty));

        nameField.disableProperty().bind(readOnlyProperty);
        minimalPointField.disableProperty().bind(readOnlyProperty);
        difficultyChoice.disableProperty().bind(readOnlyProperty);
        coordinateXField.disableProperty().bind(readOnlyProperty);
        coordinateYField.disableProperty().bind(readOnlyProperty);
        authorNameField.disableProperty().bind(readOnlyProperty);
        authorBirthdayField.disableProperty().bind(readOnlyProperty);
        authorWeightField.disableProperty().bind(readOnlyProperty);
        authorLocationX.disableProperty().bind(readOnlyProperty);
        authorLocationY.disableProperty().bind(readOnlyProperty);
        authorLocationName.disableProperty().bind(readOnlyProperty);

        saveBtn.visibleProperty().bind(readOnlyProperty.not());
        saveBtn.managedProperty().bind(saveBtn.visibleProperty());
        saveBtn.setOnAction(e -> applyChanges());

        FormValidator formValidator = new FormValidator();
        formValidator.addField(nameField)
                        .addValidator(LengthValidator.builder().build());
        formValidator.addField(minimalPointField)
                .addValidator(RangeValidator.builder().min(0d).build());
        formValidator.addField(coordinateXField)
                .addValidator(RangeValidator.builder().max(592d).build());
        formValidator.addField(coordinateYField)
                .addValidator(RangeValidator.builder().max(592d).build());
        formValidator.addField(authorNameField)
                .addValidator(LengthValidator.builder().build());
        formValidator.addField(authorWeightField)
                .addValidator(RangeValidator.builder().min(0d).build());
        formValidator.addField(authorLocationX)
                .addValidator(RangeValidator.builder().build());
        formValidator.addField(authorLocationY)
                .addValidator(RangeValidator.builder().build());
        formValidator.addField(authorLocationName)
                .addValidator(LengthValidator.builder().max(783).build());

        saveBtn.disableProperty().bind(formValidator.isValidProperty().not());
    }

    public void setItem(LabWork item) {
        itemProperty.set(item);
    }

    private void applyChanges() {
        Location location = Location.builder()
                .x(Integer.parseInt(authorLocationX.getText()))
                .y(Float.parseFloat(authorLocationY.getText()))
                .name(authorLocationName.getText())
                .build();
        Person author = Person.builder()
                .name(authorNameField.getText())
                .birthday(authorBirthdayField.getValue())
                .weight(Long.parseLong(authorWeightField.getText()))
                .location(location)
                .build();

        Coordinates coordinates = Coordinates.builder()
                .x(Integer.parseInt(coordinateXField.getText()))
                .y(Double.parseDouble(coordinateYField.getText()))
                .build();

        LabWork.LabWorkBuilder builder = LabWork.builder();
        if (itemProperty.get() != null) {
            builder.id(itemProperty.get().getId())
                    .ownerId(itemProperty.get().getOwnerId())
                    .creationDate(itemProperty.get().getCreationDate());
        }

        LabWork item = builder.name(nameField.getText())
                .minimalPoint(Long.parseLong(minimalPointField.getText()))
                .difficulty(difficultyChoice.getValue())
                .coordinates(coordinates)
                .author(author)
                .build();

        Stage stage = (Stage) saveBtn.getScene().getWindow();
        if (onSave != null) {
            Loader.runTask(rootPane, () -> {
                        onSave.accept(item);
                        return null;
                    }, _ -> stage.close()
            );
        } else stage.close();
    }

    private void updateFields() {
        LabWork item = itemProperty.get();

        id.setText(item.getId().toString());
        ownerId.setText(item.getOwnerId().toString());
        creationDate.setText(item.getCreationDate().toString());
        nameField.setText(item.getName());
        minimalPointField.setText(Long.toString(item.getMinimalPoint()));
        difficultyChoice.getSelectionModel().select(item.getDifficulty());
        coordinateXField.setText(item.getCoordinates().getX().toString());
        coordinateYField.setText(item.getCoordinates().getY().toString());
        authorNameField.setText(item.getAuthor().getName());
        authorBirthdayField.setValue(item.getAuthor().getBirthday());
        authorWeightField.setText(item.getAuthor().getWeight().toString());
        authorLocationX.setText(item.getAuthor().getLocation().getX().toString());
        authorLocationY.setText(item.getAuthor().getLocation().getY().toString());
        authorLocationName.setText(item.getAuthor().getLocation().getName());
    }
}
