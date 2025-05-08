package dev.lysmux.collex.client.ui.controller;

import com.google.inject.Inject;
import dev.lysmux.collex.client.service.UserService;
import dev.lysmux.collex.client.ui.SceneManager;
import dev.lysmux.collex.client.ui.util.SceneUtils;
import dev.lysmux.collex.client.ui.util.loader.Loader;
import dev.lysmux.collex.dto.UserInfo;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import lombok.RequiredArgsConstructor;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class ProfileController implements Initializable {
    private final UserService userService;
    private final SceneManager sceneManager;

    private ResourceBundle bundle;

    @FXML
    private StackPane rootPane;
    @FXML
    private Label idLabel;
    @FXML
    private Label registrationDateLabel;

    private TextField passwordField;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bundle = resourceBundle;
        passwordField = (TextField) rootPane.lookup("#passwordHiddenField");

        SceneUtils.onShowBind(rootPane, this::updateStat);
    }

    public void changePassword() {
        Loader.runTask(rootPane, () -> {
            userService.changePassword(passwordField.getText());
            return null;
        }, _ -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(bundle.getString("profile.changePwd.title"));
            alert.setHeaderText(null);
            alert.setContentText(bundle.getString("profile.changePwd.success"));
            alert.show();
        });
    }

    public void updateStat() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

        Loader.runTask(rootPane, userService::getUserInfo, task -> {
            UserInfo userInfo = task.getValue();
            idLabel.setText(Integer.toString(userInfo.id()));
            registrationDateLabel.setText(userInfo.registrationTime().format(formatter));
        });
    }

    public void goToMain() {
        sceneManager.switchTo("main");
    }
}
