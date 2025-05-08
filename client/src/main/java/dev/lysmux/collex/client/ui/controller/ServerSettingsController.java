package dev.lysmux.collex.client.ui.controller;

import com.google.inject.Inject;
import dev.lysmux.collex.client.service.ConnectionService;
import dev.lysmux.collex.client.ui.SceneManager;
import dev.lysmux.collex.client.ui.UserPreferences;
import dev.lysmux.collex.client.ui.util.loader.Loader;
import dev.lysmux.collex.client.ui.util.validation.FormValidator;
import dev.lysmux.collex.client.ui.util.validation.validator.RangeValidator;
import dev.lysmux.collex.client.ui.util.validation.validator.RegexValidator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import lombok.RequiredArgsConstructor;

import java.net.URL;
import java.util.ResourceBundle;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class ServerSettingsController implements Initializable {
    private final UserPreferences userPreferences;
    private final SceneManager sceneManager;
    private final ConnectionService connectionService;

    @FXML
    private StackPane rootPane;
    @FXML
    private TextField addressField;
    @FXML
    private TextField portField;
    @FXML
    private Button connectBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FormValidator formValidator = new FormValidator();
        formValidator.addField(addressField)
                .addValidator(RegexValidator.builder().regex("^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$").build());
        formValidator.addField(portField)
                .addValidator(RangeValidator.builder().min(1d).max(65535d).build());

        connectBtn.disableProperty().bind(formValidator.isValidProperty().not());

        if (userPreferences.getServerHost() != null)
            addressField.setText(userPreferences.getServerHost());
        if (userPreferences.getServerPort() != null)
            portField.setText(userPreferences.getServerPort().toString());
    }

    public void connect() {
        String address = addressField.getText();
        int port = Integer.parseInt(portField.getText());

        userPreferences.setServerHost(address);
        userPreferences.setServerPort(port);

        Loader.runTask(rootPane, () -> {
            connectionService.connect(address, port);
            return null;
        }, _ -> {
            sceneManager.switchTo("auth");
        });
    }
}
