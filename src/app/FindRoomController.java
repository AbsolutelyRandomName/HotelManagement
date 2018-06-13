package app;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

public class FindRoomController {
    @FXML
    private TextField numberField;
    @FXML
    private ComboBox typeSelector;
    @FXML
    private TableView<Room> roomsTable;

    private DbManager manager = null;
    private ArrayList<Room> rooms;

    @FXML
    private void initialize() {
        typeSelector.getSelectionModel().select(0);
        numberField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.matches("\\d"))
                    numberField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        TableColumn idColumn = new TableColumn("Number");
        idColumn.setCellValueFactory(new PropertyValueFactory<Room, Integer>("number"));
        TableColumn typeColumn = new TableColumn("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<Room, RoomType>("type"));
        roomsTable.getColumns().addAll(idColumn, typeColumn);
    }

    @FXML
    private void onSearchClicked() {
        refreshRooms();
    }

    @FXML
    private void onEditClicked() throws Exception{
        Room room = roomsTable.getSelectionModel().getSelectedItem();
        if(room == null) return;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("editroomform.fxml"));
        Parent root = loader.load();
        EditRoomController controller = loader.getController();
        controller.setManager(manager);
        controller.setRoom(room);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Edit room");
        stage.initOwner(numberField.getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.showAndWait();
        roomsTable.refresh();
    }

    @FXML
    private void onDeleteClicked() {
        Room room = roomsTable.getSelectionModel().getSelectedItem();
        if(room != null)
            manager.deleteRoom(room);
        refreshRooms();
    }

    @FXML
    private void onCloseClicked() {
        ((Stage)numberField.getScene().getWindow()).close();

    }

    public void setManager(DbManager manager) {
        this.manager = manager;
        refreshRooms();
    }

    private void refreshRooms() {
        int number;
        RoomType type;
        if(typeSelector.getSelectionModel().getSelectedIndex() == 0)
            type = null;
        else type = RoomType.values()[typeSelector.getSelectionModel().getSelectedIndex() - 1];
        if(numberField.getText().length() > 0)
            number = Integer.parseInt(numberField.getText());
        else number = -1;
        try {
            rooms = manager.getRooms(number, type);
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while accessing database");
            alert.showAndWait();
            return;
        }
        roomsTable.setItems(FXCollections.observableArrayList(rooms));
    }
}
