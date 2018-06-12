package app;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class NewRoomFormController {
    private DbManager manager;

    @FXML
    private TextField roomNoField;
    @FXML
    private ComboBox typeSelector;

    @FXML
    public void initialize() {
        roomNoField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.matches("[\\d]")) {
                    roomNoField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        typeSelector.getSelectionModel().select(0);
    }

    public void setManager(DbManager manager) {
        this.manager = manager;
    }

    @FXML
    private void onOkButtonClicked() {
        Room room = new Room();
        int number;
        try {
            number = Integer.parseInt(roomNoField.getText());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occured");
            alert.showAndWait();
            return;
        }
        room.setNumber(number);
        room.setType(RoomType.values()[typeSelector.getSelectionModel().getSelectedIndex()]);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("An error occured while accessing database");
        if(!manager.addRoom(room)) {
            alert.showAndWait();
            return;
        }
        ((Stage)roomNoField.getScene().getWindow()).close();
    }

    @FXML
    private void onCancelButtonClicked() {
        ((Stage)roomNoField.getScene().getWindow()).close();
    }
}
