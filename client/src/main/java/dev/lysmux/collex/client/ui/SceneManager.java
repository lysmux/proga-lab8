package dev.lysmux.collex.client.ui;

import com.google.inject.Inject;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class SceneManager {
    private static final String FXML_PATH = "/ui/fxml/views/";

    private final UserPreferences userPreferences;

    @Setter private Stage primaryStage;
    @Setter private Callback<Class<?>, Object> controllerFactory;

    private String currentSceneName;


    public void switchTo(String sceneName) {
        Objects.requireNonNull(primaryStage, "Primary stage not set");

        SceneData<?> sceneData = loadScene(sceneName);
        if (sceneData == null) return;

        currentSceneName = sceneName;

        primaryStage.setScene(sceneData.scene());
        primaryStage.setTitle(sceneData.title());
        primaryStage.show();
    }

    public <T> T openPopup(String sceneName) {
        SceneData<T> sceneData = loadScene(sceneName);
        if (sceneData == null) return null;

        Stage popupStage = new Stage();
        popupStage.setScene(sceneData.scene());
        popupStage.setTitle(sceneData.title());

        popupStage.initModality(Modality.WINDOW_MODAL);
        popupStage.initOwner(primaryStage);
        popupStage.show();

        return sceneData.controller();
    }

    public void switchLocale(Locale locale) {
        userPreferences.setCurrentLocale(locale);
        switchTo(currentSceneName);
    }

    public void switchTheme(Theme theme) {
        userPreferences.setCurrentTheme(theme);
        applyTheme(primaryStage.getScene());
    }

    private void applyTheme(Scene scene) {
        scene.getStylesheets().clear();
        String cssPath = switch (userPreferences.getCurrentTheme()) {
            case LIGHT -> "/ui/styles/light.css";
            case DARK -> "/ui/styles/dark.css";
        };

        var cssURL = SceneManager.class.getResource(cssPath);
        if (cssURL != null) {
            scene.getStylesheets().add(cssURL.toExternalForm());
        }
    }

    private <T> SceneData<T> loadScene(String sceneName) {
        URL sceneURL = SceneManager.class.getResource(getScenePath(sceneName));
        if (sceneURL == null) {
            log.atWarn()
                    .setMessage("Scene not found")
                    .addKeyValue("sceneName", sceneName)
                    .log();
            return null;
        }

        ResourceBundle bundle = ResourceBundle.getBundle("ui.i18n.locale", userPreferences.getCurrentLocale());
        FXMLLoader fxmlLoader = new FXMLLoader(sceneURL, bundle);
        fxmlLoader.setControllerFactory(controllerFactory);
        try {
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            applyTheme(scene);

            String title = bundle.getString(sceneName + ".title");

            return new SceneData<>(scene, title, fxmlLoader.getController());
        } catch (IOException e) {
            log.atError()
                    .setMessage("Could not load scene")
                    .addKeyValue("sceneName", sceneName)
                    .setCause(e)
                    .log();
            return null;
        }
    }

    private static String getScenePath(String sceneName) {
        sceneName = sceneName.substring(0, 1).toUpperCase() + sceneName.substring(1);

        return FXML_PATH + sceneName + ".fxml";
    }

    private record SceneData<T>(Scene scene, String title, T controller) {}
}
