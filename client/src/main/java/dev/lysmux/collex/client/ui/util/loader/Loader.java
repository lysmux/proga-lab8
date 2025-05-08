package dev.lysmux.collex.client.ui.util.loader;

import dev.lysmux.collex.client.ui.util.DialogUtils;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.function.Consumer;


@Slf4j
public class Loader {
    public static <T> Task<T> runTask(
            StackPane root,
            Callable<T> taskBody
    ) {
        return runTask(root, taskBody, null);
    }

    public static <T> Task<T> runTask(
            StackPane root,
            Callable<T> taskBody,
            Consumer<Task<T>> onSuccess
    ) {
        Pane overlayPane = new Pane();
        overlayPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3);");

        ProgressIndicator indicator = new ProgressIndicator();
        indicator.setMaxSize(80, 80);
        indicator.setStyle("-fx-progress-color: white;");

        StackPane.setAlignment(indicator, Pos.CENTER);
        root.getChildren().addAll(overlayPane, indicator);

        Task<T> task = new Task<>() {
            @Override
            protected T call() throws Exception {
                return taskBody.call();
            }

            @Override
            protected void succeeded() {
                root.getChildren().removeAll(overlayPane, indicator);
                if (onSuccess != null) onSuccess.accept(this);
            }

            @Override
            protected void failed() {
                root.getChildren().removeAll(overlayPane, indicator);
                showError(getException().getMessage());

                log.atError()
                        .setMessage("Error occurred while executing task")
                        .setCause(getException())
                        .log();
            }
        };

        new Thread(task).start();
        return task;
    }

    private static void showError(String message) {
        DialogUtils.showAlert(
                "Error",
                message,
                Alert.AlertType.ERROR
        );
    }
}