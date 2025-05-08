package dev.lysmux.collex.client.ui.controller.component;

import com.google.inject.Inject;
import dev.lysmux.collex.client.service.UserService;
import dev.lysmux.collex.client.ui.SceneManager;
import dev.lysmux.collex.client.ui.StateManager;
import dev.lysmux.collex.client.ui.Theme;
import dev.lysmux.collex.client.ui.util.SceneUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import lombok.RequiredArgsConstructor;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class HeaderController implements Initializable {
    private final SceneManager sceneManager;
    private final UserService userService;

    @FXML private MenuButton profileBtn;
    @FXML private HBox rootPane;

    private final StateManager stateManager;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SceneUtils.onShowBind(rootPane, () -> {
            profileBtn.getItems().stream()
                    .filter(item -> item.getStyleClass().contains("login-visible"))
                    .forEach(item -> item.setVisible(stateManager.isUserLoggedIn()));
        });
    }

    public void logout() {
        userService.logout();
        stateManager.logoutUser();
        sceneManager.switchTo("auth");
    }

    public void switchToProfile() {
        sceneManager.switchTo("profile");
    }

    public void changeLanguage(ActionEvent event) {
        MenuItem selectedItem = (MenuItem) event.getSource();
        String languageTag = (String) selectedItem.getUserData();

        Locale locale = Locale.forLanguageTag(languageTag);
        sceneManager.switchLocale(locale);
    }

    public void changeTheme(ActionEvent event) {
        MenuItem selectedItem = (MenuItem) event.getSource();
        String themeTag = (String) selectedItem.getUserData();

        Theme theme = Theme.valueOf(themeTag);
        sceneManager.switchTheme(theme);
    }
}
