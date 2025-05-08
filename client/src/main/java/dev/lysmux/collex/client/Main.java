package dev.lysmux.collex.client;

import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.lysmux.collex.client.di.ServiceModule;
import dev.lysmux.collex.client.di.UIModule;
import dev.lysmux.collex.client.ui.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    private static final String START_SCENE = "serverSettings";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Injector injector = Guice.createInjector(new ServiceModule(), new UIModule());

        stage.setMinHeight(600);
        stage.setMinWidth(800);

        SceneManager router = injector.getInstance(SceneManager.class);
        router.setPrimaryStage(stage);
        router.setControllerFactory(injector::getInstance);
        router.switchTo(START_SCENE);
    }
}
