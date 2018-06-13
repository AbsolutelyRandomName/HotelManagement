package app;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class EditRoomController {
    @FXML
    private ComboBox<String> typeSelector;

    private DbManager manager;
    private Room room;
    private RoomType initType;

    @FXML
    private void initialize() {
    }

    public void setManager(DbManager manager) {
        this.manager = manager;
    }

    public void setRoom(Room room) {
        this.room = room;
        initType = room.getType();
        typeSelector.getSelectionModel().select(room.getType().ordinal());
    }

    @FXML
    private void onOkButtonClicked() {
        int index = typeSelector.getSelectionModel().getSelectedIndex();
        if(initType.ordinal() != index) {
            room.setType(RoomType.values()[index]);
            manager.editRoom(room);
        }
        ((Stage)typeSelector.getScene().getWindow()).close();
    }

    @FXML
    private void onCancelClicked() {
        ((Stage)typeSelector.getScene().getWindow()).close();
    }
}
