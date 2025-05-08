package dev.lysmux.collex.client.ui.controller.component;

import com.google.inject.Inject;
import dev.lysmux.collex.client.ui.StateManager;
import dev.lysmux.collex.client.ui.controller.MainController;
import dev.lysmux.collex.domain.LabWork;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class VisualViewController {
    private final StateManager stateManager;

    @Setter
    private MainController mainController;

    @FXML
    private StackPane rootPane;




    private final HashMap<Integer, Color> colorMap = new HashMap<>();
    private final HashMap<LabWork, Node> circlesMap = new HashMap<>();

    public void setItems(ObservableList<LabWork> items) {
        items.addListener((ListChangeListener<LabWork>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    List<? extends LabWork> addedItems = change.getAddedSubList();
                    for (int i = 0; i < change.getAddedSize(); i++) {
                        LabWork item = addedItems.get(i);
                        addCircle(item);
                    }
                }

                if (change.wasRemoved()) {
                    List<? extends LabWork> removedItems = change.getRemoved();
                    for (int i = 0; i < change.getRemovedSize(); i++) {
                        LabWork item = removedItems.get(i);
                        removeCircle(item);
                    }
                }
            }
        });
    }

    private void addCircle(LabWork item) {
        if (!colorMap.containsKey(item.getOwnerId())) {
            colorMap.put(item.getOwnerId(), getRandomColor());
        }

        double size = Math.log10(item.getMinimalPoint() + 1) * 10 + 10;
        Circle circle = new Circle(size, colorMap.get(item.getOwnerId()));

        double x = Math.abs(item.getCoordinates().getX()) * 50;
        double y = Math.abs(item.getCoordinates().getY()) * 50;

        StackPane stackPane = new StackPane();
        stackPane.setLayoutX(rootPane.getWidth() / 2);
        stackPane.setLayoutY(rootPane.getHeight() / 2);
        Label label = new Label(item.getId().toString());
        stackPane.getChildren().addAll(circle, label);

        stackPane.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                mainController.editBtnHandler(item);
            }
        });

        rootPane.getChildren().add(stackPane);
        circlesMap.put(item, stackPane);

        TranslateTransition translate = new TranslateTransition(Duration.millis(500), stackPane);
        translate.setToY(y);
        translate.setToX(x);
        translate.play();
    }

    private void removeCircle(LabWork item) {
        Node node = circlesMap.get(item);
        FadeTransition fade = new FadeTransition(Duration.millis(300), node);
        fade.setToValue(0);
        fade.setOnFinished(e -> {
            rootPane.getChildren().remove(node);
            circlesMap.remove(item);
        });
        fade.play();
    }

    private Color getRandomColor() {
        var random = new Random();
        var r = random.nextDouble();
        var g = random.nextDouble();
        var b = random.nextDouble();
        if (Math.abs(r - g) + Math.abs(r - b) + Math.abs(b - g) < 0.6) {
            r += (1 - r) / 1.4;
            g += (1 - g) / 1.4;
            b += (1 - b) / 1.4;
        }
        return Color.color(r, g, b);
    }
}
