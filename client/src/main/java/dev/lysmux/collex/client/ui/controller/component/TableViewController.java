package dev.lysmux.collex.client.ui.controller.component;

import com.google.inject.Inject;
import dev.lysmux.collex.client.ui.StateManager;
import dev.lysmux.collex.client.ui.controller.MainController;
import dev.lysmux.collex.client.ui.util.table.NestedPropertyValueFactory;
import dev.lysmux.collex.domain.LabWork;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.net.URL;
import java.util.ResourceBundle;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class TableViewController implements Initializable {
    private final StateManager stateManager;

    @Setter
    private MainController mainController;

    @FXML
    private TableView<LabWork> table;
    @FXML
    private TableColumn<LabWork, Integer> idCol;
    @FXML
    private TableColumn<LabWork, Integer> ownerIdCol;
    @FXML
    private TableColumn<LabWork, String> nameCol;
    @FXML
    private TableColumn<LabWork, Long> minimalPointCol;
    @FXML
    private TableColumn<LabWork, String> difficultyCol;
    @FXML
    private TableColumn<LabWork, String> creationDateCol;
    @FXML
    private TableColumn<LabWork, Integer> coordinatesXCol;
    @FXML
    private TableColumn<LabWork, Double> coordinatesYCol;
    @FXML
    private TableColumn<LabWork, String> authorNameCol;
    @FXML
    private TableColumn<LabWork, String> authorBirthdayCol;
    @FXML
    private TableColumn<LabWork, Long> authorWeightCol;
    @FXML
    private TableColumn<LabWork, Float> authorLocationYCol;
    @FXML
    private TableColumn<LabWork, Integer> authorLocationXCol;
    @FXML
    private TableColumn<LabWork, String> authorLocationNameCol;

    @FXML
    private ContextMenu contextMenu;
    @FXML
    private MenuItem editItem;
    @FXML
    private MenuItem removeItem;
    @FXML
    private MenuItem removeGreater;
    @FXML
    private MenuItem removeLower;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        ownerIdCol.setCellValueFactory(new PropertyValueFactory<>("ownerId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        minimalPointCol.setCellValueFactory(new PropertyValueFactory<>("minimalPoint"));
        difficultyCol.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        creationDateCol.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        coordinatesXCol.setCellValueFactory(new NestedPropertyValueFactory<>("coordinates.x"));
        coordinatesYCol.setCellValueFactory(new NestedPropertyValueFactory<>("coordinates.y"));
        authorNameCol.setCellValueFactory(new NestedPropertyValueFactory<>("author.name"));
        authorBirthdayCol.setCellValueFactory(new NestedPropertyValueFactory<>("author.birthday"));
        authorWeightCol.setCellValueFactory(new NestedPropertyValueFactory<>("author.weight"));
        authorLocationYCol.setCellValueFactory(new NestedPropertyValueFactory<>("author.location.y"));
        authorLocationXCol.setCellValueFactory(new NestedPropertyValueFactory<>("author.location.x"));
        authorLocationNameCol.setCellValueFactory(new NestedPropertyValueFactory<>("author.location.name"));

        table.setRowFactory(tv -> {
            TableRow<LabWork> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 && !row.isEmpty()) {
                    LabWork selectedItem = row.getItem();
                    mainController.editBtnHandler(selectedItem);
                }

                if (event.getButton() == MouseButton.SECONDARY && !row.isEmpty()) {
                    if (row.getItem().getOwnerId() != stateManager.getUserId()) return;
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                }
            });

            return row;
        });

        editItem.setOnAction(event -> {
            LabWork selectedItem = table.getSelectionModel().getSelectedItem();
            mainController.editBtnHandler(selectedItem);
        });
        removeItem.setOnAction(event -> {
            LabWork selectedItem = table.getSelectionModel().getSelectedItem();
            mainController.removeItem(selectedItem);
        });

        removeLower.setOnAction(event -> {
            LabWork selectedItem = table.getSelectionModel().getSelectedItem();
            mainController.removeLower(selectedItem);
        });
        removeGreater.setOnAction(event -> {
            LabWork selectedItem = table.getSelectionModel().getSelectedItem();
            mainController.removeGreater(selectedItem);
        });
    }
}
