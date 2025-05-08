package dev.lysmux.collex.client.ui.controller;

import com.google.inject.Inject;
import dev.lysmux.collex.client.service.UserService;
import dev.lysmux.collex.client.ui.SceneManager;
import dev.lysmux.collex.client.ui.StateManager;
import dev.lysmux.collex.client.ui.util.loader.Loader;
import dev.lysmux.collex.client.ui.util.validation.FormValidator;
import dev.lysmux.collex.client.ui.util.validation.validator.LengthValidator;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import lombok.RequiredArgsConstructor;

import java.net.URL;
import java.util.ResourceBundle;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class AuthController implements Initializable {
    private final SceneManager sceneManager;
    private final StateManager stateManager;
    private final UserService userService;


    @FXML private Label loginTabLabel;
    @FXML private Label registerTabLabel;
    @FXML private Line modeUnderline;
    @FXML private Button authBtn;
    @FXML private StackPane rootPane;
    @FXML private Label invalidAuthDataLabel;

    private TextField loginField;
    private TextField passwordField;

    private ResourceBundle bundle;

    private final BooleanProperty loginMode = new SimpleBooleanProperty(true);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bundle = resourceBundle;

        loginField = (TextField) rootPane.lookup("#loginField");
        passwordField = (TextField) rootPane.lookup("#passwordHiddenField");

        loginTabLabel.setOnMouseClicked(this::switchTabHandler);
        registerTabLabel.setOnMouseClicked(this::switchTabHandler);

        authBtn.textProperty().bind(Bindings.createStringBinding(
                () -> loginMode.get()
                        ? resourceBundle.getString("auth.button.login")
                        : resourceBundle.getString("auth.button.register"),
                loginMode
        ));
        Platform.runLater(() -> updateLinePosition(loginTabLabel));

        FormValidator formValidator = new FormValidator();
        formValidator.addField(loginField)
                .addValidator(LengthValidator.builder().min(3).build());
        formValidator.addField(passwordField)
                .addValidator(LengthValidator.builder().min(3).build());

        authBtn.disableProperty().bind(formValidator.isValidProperty().not());
    }

    private void switchTabHandler(MouseEvent event) {
        boolean isLogin = event.getSource() == loginTabLabel;
        loginMode.set(isLogin);
        invalidAuthDataLabel.setVisible(false);
        animateLineTo((Label) event.getSource());
    }

    private void animateLineTo(Label label) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(100), modeUnderline);
        transition.setToX(label.localToParent(label.getBoundsInLocal()).getMinX());

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(modeUnderline.endXProperty(), modeUnderline.getEndX())),
                new KeyFrame(Duration.millis(100), new KeyValue(modeUnderline.endXProperty(), label.getWidth()))
        );
        new ParallelTransition(transition, timeline).play();
    }

    private void updateLinePosition(Label label) {
        modeUnderline.setStartX(0);
        modeUnderline.setEndX(label.getWidth());
        modeUnderline.setLayoutX(label.localToParent(label.getBoundsInLocal()).getMinX());
    }

    public void changeServer() {
        sceneManager.switchTo("serverSettings");
    }

    public void auth() {
        String login = loginField.getText();
        String password = passwordField.getText();

        Loader.runTask(rootPane, () -> {
            return loginMode.get()
                    ? userService.login(login, password)
                    : userService.register(login, password);
        }, task -> {
            int userId = task.getValue();
            String errorMessage = loginMode.get()
                    ? bundle.getString("auth.label.invalidAuth")
                    : bundle.getString("auth.label.accountExists");

            invalidAuthDataLabel.setText(errorMessage);
            invalidAuthDataLabel.setVisible(userId == -1);

            stateManager.setUserId(userId);
            if (userId != -1) sceneManager.switchTo("main");
        });
    }
}
