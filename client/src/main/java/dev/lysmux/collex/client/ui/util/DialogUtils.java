package dev.lysmux.collex.client.ui.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;
import java.util.function.Function;

public class DialogUtils {
    public static ButtonType showAlert(
            String title,
            String content,
            Alert.AlertType type,
            ButtonType... buttons
    ) {
        Alert alert = new Alert(type, content, buttons);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();

        return alert.getResult();
    }

    public static <R> Optional<R> requestValue(
            String title,
            String content,
            Function<String, R> parser
    ) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText(content);

        while (true) {
            Optional<String> result = dialog.showAndWait();
            if (result.isEmpty()) return Optional.empty();

            try {
                return Optional.of(parser.apply(result.get()));
            } catch (Exception e) {
                showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }
}
