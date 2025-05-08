package dev.lysmux.collex.client.ui.util;

import javafx.scene.layout.Pane;

public class SceneUtils {
    public static void onShowBind(Pane pane, Runnable action) {
        pane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene == null) return;
            newScene.windowProperty().addListener((obs, oldWindow, newWindow) -> {
                if (newWindow == null) return;
                action.run();
            });
        });
    }
}
