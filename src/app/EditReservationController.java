package app;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

import java.util.ArrayList;

public class EditReservationController {
    @FXML
    private DatePicker startPicker;
    @FXML
    private DatePicker endPicker;
    @FXML
    private ComboBox<Customer> customerSelector;
    @FXML
    private ComboBox<Room> roomNoSelector;
    @FXML
    private ComboBox roomTypeSelector;
    @FXML
    private ComboBox stateSelector;

    private DbManager manager;
    private Reservation reservation;

    @FXML
    private void initialize() {}


    public void setManager(DbManager manager) throws Exception{
        this.manager = manager;
        ArrayList<Customer> customers = manager.getCustomers(null, null, null);
        customerSelector.setItems(FXCollections.observableArrayList(customers));
        customerSelector.getSelectionModel().select(0);

    }

    public void setReservation(Reservation reservation) throws Exception {
        this.reservation = reservation;
        roomTypeSelector.getSelectionModel().select(reservation.getRoom().getType().ordinal() + 1);
        startPicker.setValue(reservation.getStart());
        endPicker.setValue(reservation.getEnd());
        stateSelector.getSelectionModel().select(reservation.getState().ordinal());
        refreshRooms();
    }

    @FXML
    private void refreshRooms() throws Exception {
        ArrayList<Room> rooms;
        int index = roomTypeSelector.getSelectionModel().getSelectedIndex();
        RoomType type;
        if(index == 0)
            type = null;
        else type = RoomType.values()[index - 1];
        rooms = manager.findFreeRooms(startPicker.getValue(), endPicker.getValue(), type, reservation);
        roomNoSelector.setItems(FXCollections.observableArrayList(rooms));
        roomNoSelector.getSelectionModel().select(0);
    }

    @FXML
    private void onDateChanged() throws Exception{
        if(startPicker.getValue().isAfter(endPicker.getValue())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Wrong date");
            alert.setHeaderText(null);
            alert.setContentText("End of reservation must take place after its beginning");
            alert.showAndWait();
            endPicker.setValue(startPicker.getValue());
            refreshRooms();
        }
    }

    @FXML
    private void onOkButtonClicked() {
        Room room = roomNoSelector.getValue();
        Customer customer = customerSelector.getValue();
        if(room == null || customer == null) return;
        reservation.setStart(startPicker.getValue());
        reservation.setEnd(endPicker.getValue());
        reservation.setRoom(room);
        reservation.setCustomer(customer);
        reservation.setState(State.values()[stateSelector.getSelectionModel().getSelectedIndex()]);
        manager.editReservation(reservation);
        ((Stage)startPicker.getScene().getWindow()).close();
    }

    @FXML
    private void onCloseButtonClicked() {
        ((Stage)startPicker.getScene().getWindow()).close();
    }

}
