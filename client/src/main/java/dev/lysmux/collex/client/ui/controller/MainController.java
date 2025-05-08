package dev.lysmux.collex.client.ui.controller;

import com.google.inject.Inject;
import dev.lysmux.collex.client.service.CollectionService;
import dev.lysmux.collex.client.ui.SceneManager;
import dev.lysmux.collex.client.ui.StateManager;
import dev.lysmux.collex.client.ui.controller.component.TableViewController;
import dev.lysmux.collex.client.ui.controller.component.VisualViewController;
import dev.lysmux.collex.client.ui.util.DialogUtils;
import dev.lysmux.collex.client.ui.util.SceneUtils;
import dev.lysmux.collex.domain.LabWork;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import lombok.RequiredArgsConstructor;

import java.net.URL;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class MainController implements Initializable {
    private final StateManager stateManager;
    private final SceneManager sceneManager;
    private final CollectionService collectionService;

    private final ObservableList<LabWork> data = FXCollections.observableArrayList();
    private final FilteredList<LabWork> filteredData = new FilteredList<>(data);

    @FXML
    private StackPane rootPane;
    @FXML
    private CheckBox displayOnlyMy;
    @FXML
    private Button clearBtn;
    @FXML
    private Button addBtn;
    @FXML
    private TableView<LabWork> table;

    @FXML
    private TableViewController tableController;
    @FXML
    private VisualViewController visualController;

    private ResourceBundle resourceBundle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        tableController.setMainController(this);
        visualController.setMainController(this);
        visualController.setItems(filteredData);

        SceneUtils.onShowBind(rootPane, this::refreshCollection);
        table.setItems(filteredData);

        displayOnlyMy.selectedProperty().addListener((_, _, newValue) -> displayOnlyMyHandler(newValue));
        clearBtn.setOnAction(_ -> clearBtnHandler());
        addBtn.setOnAction(_ -> addBtnHandler());

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            if (stateManager.isUserLoggedIn()) refreshCollection();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void refreshCollection() {
        List<LabWork> newItems = collectionService.getAll();
        data.removeIf(i -> !newItems.contains(i));
        for (LabWork item : newItems) {
            if (!data.contains(item)) data.add(item);
        }

        table.getColumns().forEach(column -> {
            Button filterBtn = new Button("*");
            filterBtn.getStyleClass().add("filter-button");

            ContextMenu menu = createFilterMenu(column);
            filterBtn.setOnMouseClicked(e -> menu.show(filterBtn, e.getScreenX(), e.getScreenY()));
            column.setGraphic(filterBtn);
        });
    }

    public void openEditor(LabWork item, Consumer<LabWork> onSave) {
        ItemViewController controller = sceneManager.openPopup("itemView");
        controller.setItem(item);
        controller.setOnSave(onSave);
    }

    public void removeItem(LabWork item) {
        collectionService.remove(item.getId());
        refreshCollection();
    }

    public void removeGreater(LabWork item) {
        if (!collectionService.removeGreater(item)) {
            DialogUtils.showAlert(
                    resourceBundle.getString("entities.notFound"),
                    resourceBundle.getString("entities.notFound"),
                    Alert.AlertType.INFORMATION
            );
        } else refreshCollection();
    }

    public void removeLower(LabWork item) {
        if (!collectionService.removeLower(item)) {
            DialogUtils.showAlert(
                    resourceBundle.getString("entities.notFound"),
                    resourceBundle.getString("entities.notFound"),
                    Alert.AlertType.INFORMATION
            );
        } else refreshCollection();
    }

    private void displayOnlyMyHandler(boolean value) {
        filteredData.setPredicate(item -> {
            if (!value) return true;
            return item.getOwnerId() == stateManager.getUserId();
        });
    }

    private void addBtnHandler() {
        openEditor(null, item -> {
            collectionService.add(item);
            refreshCollection();
        });
    }

    public void editBtnHandler(LabWork item) {
        openEditor(item, updatedItem -> {
            collectionService.update(updatedItem);
            refreshCollection();
        });
    }

    private void clearBtnHandler() {
        ButtonType result = DialogUtils.showAlert(
                resourceBundle.getString("main.clearCollection.title"),
                resourceBundle.getString("main.clearCollection.question"),
                Alert.AlertType.CONFIRMATION,
                ButtonType.YES,
                ButtonType.NO
        );

        if (result == ButtonType.YES) {
            collectionService.clear();
            refreshCollection();
        }
    }

    public void addRandomBtnHandler() {
        Optional<Integer> value = DialogUtils.requestValue(
                resourceBundle.getString("otherCommands.addRandom.title"),
                resourceBundle.getString("otherCommands.addRandom.content"),
                v -> {
                    int parsed = Integer.parseInt(v);
                    if (parsed <= 0 || parsed >= 1000) throw new IllegalArgumentException("Value out of range");

                    return parsed;
                }
        );

        if (value.isPresent()) {
            collectionService.addRandom(value.get());
            refreshCollection();
        }
    }

    public void addIfMaxBtnHandler() {
        openEditor(null, item -> {
            if (!collectionService.addIfMax(item)) {
                DialogUtils.showAlert(
                        resourceBundle.getString("entities.notAdded"),
                        resourceBundle.getString("entities.notAdded"),
                        Alert.AlertType.INFORMATION
                );
            } else refreshCollection();
        });
    }

    public void countGreaterThanMinimalPointBtnHandler() {
        Optional<Long> value = DialogUtils.requestValue(
                resourceBundle.getString("otherCommands.countGreaterThanMinimalPoint.title"),
                resourceBundle.getString("otherCommands.countGreaterThanMinimalPoint.content"),
                v -> {
                    long parsed = Long.parseLong(v);
                    if (parsed <= 0) throw new IllegalArgumentException("Value out of range");

                    return parsed;
                }
        );
        if (value.isPresent()) {
            long count = collectionService.countGreaterThanMinimalPoint(value.get());
            DialogUtils.showAlert(
                    resourceBundle.getString("entities.count"),
                    String.valueOf(count),
                    Alert.AlertType.INFORMATION
            );
        }
    }

    private ContextMenu createFilterMenu(TableColumn<LabWork, ?> column) {
        ContextMenu menu = new ContextMenu();

        TextField searchField = createSearchField(column, menu);
        CustomMenuItem searchItem = new CustomMenuItem(searchField);
        searchItem.setHideOnClick(false);
        menu.getItems().add(searchItem);

        addSortOptions(menu, column);
        addFilterOptions(menu, column);
        addSelectionButtons(menu, column);

        return menu;
    }

    private TextField createSearchField(TableColumn<LabWork, ?> column, ContextMenu menu) {
        TextField searchField = new TextField();

        searchField.setPromptText(resourceBundle.getString("main.sorting.search"));

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String lowerVal = newVal.toLowerCase();

            getCheckBoxItems(menu).forEach(cb -> {
                boolean visible = cb.getText().toLowerCase().startsWith(lowerVal);
                cb.setVisible(newVal.isEmpty() || visible);
            });

            filteredData.setPredicate(person -> {
                Object cellData = column.getCellData(person);
                if (cellData == null) return false;
                return newVal.isEmpty() || cellData.toString().toLowerCase().startsWith(lowerVal);
            });
        });

        return searchField;
    }

    private void addSortOptions(ContextMenu menu, TableColumn<LabWork, ?> column) {
        MenuItem sortAsc = new MenuItem(resourceBundle.getString("main.sorting.asc"));
        sortAsc.setOnAction(e -> {
            column.setSortType(TableColumn.SortType.ASCENDING);
            table.getSortOrder().setAll(column);
        });

        MenuItem sortDesc = new MenuItem(resourceBundle.getString("main.sorting.desc"));
        sortDesc.setOnAction(e -> {
            column.setSortType(TableColumn.SortType.DESCENDING);
            table.getSortOrder().setAll(column);
        });

        menu.getItems().addAll(sortAsc, sortDesc, new SeparatorMenuItem());
    }

    private void addFilterOptions(ContextMenu menu, TableColumn<LabWork, ?> column) {
        Set<String> uniqueValues = column.getTableView().getItems().stream()
                .map(column::getCellData)
                .filter(Objects::nonNull)
                .map(Object::toString)
                .collect(Collectors.toCollection(TreeSet::new)); // Сортировка

        for (String value : uniqueValues) {
            CheckBox checkBox = new CheckBox(value);
            checkBox.setSelected(true);
            checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> applyMultiFilter(column, menu));

            CustomMenuItem item = new CustomMenuItem(checkBox);
            item.setHideOnClick(false);
            menu.getItems().add(item);
        }
    }

    private void addSelectionButtons(ContextMenu menu, TableColumn<LabWork, ?> column) {
        Button selectAll = new Button(resourceBundle.getString("main.sorting.all"));
        selectAll.setOnAction(e -> {
            getCheckBoxItems(menu).forEach(cb -> cb.setSelected(true));
            applyMultiFilter(column, menu);
        });

        Button clearAll = new Button(resourceBundle.getString("main.sorting.clear"));
        clearAll.setOnAction(e -> {
            getCheckBoxItems(menu).forEach(cb -> cb.setSelected(false));
            getTextField(menu).clear();
            applyMultiFilter(column, menu);
        });

        HBox buttonBox = new HBox(5, selectAll, clearAll);
        buttonBox.setPadding(new Insets(5));
        CustomMenuItem buttonsItem = new CustomMenuItem(buttonBox);
        buttonsItem.setHideOnClick(false);
        menu.getItems().add(buttonsItem);
    }

    private List<CheckBox> getCheckBoxItems(ContextMenu menu) {
        return menu.getItems().stream()
                .filter(item -> item instanceof CustomMenuItem)
                .map(item -> (CustomMenuItem) item)
                .map(CustomMenuItem::getContent)
                .filter(node -> node instanceof CheckBox)
                .map(node -> (CheckBox) node)
                .collect(Collectors.toList());
    }

    private TextField getTextField(ContextMenu menu) {
        return menu.getItems().stream()
                .filter(item -> item instanceof CustomMenuItem)
                .map(item -> (CustomMenuItem) item)
                .map(CustomMenuItem::getContent)
                .filter(node -> node instanceof TextField)
                .map(node -> (TextField) node)
                .findFirst().orElse(null);
    }

    private void applyMultiFilter(TableColumn<LabWork, ?> column, ContextMenu menu) {
        Set<String> selectedValues = getCheckBoxItems(menu).stream()
                .filter(CheckBox::isSelected)
                .map(CheckBox::getText)
                .collect(Collectors.toSet());

        if (selectedValues.isEmpty()) {
            filteredData.setPredicate(person -> false);
        } else {
            filteredData.setPredicate(person -> {
                Object value = column.getCellData(person);
                return value != null && selectedValues.contains(value.toString());
            });
        }
    }
}
