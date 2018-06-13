package app;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;
import java.time.chrono.Chronology;
import java.util.ArrayList;

public class NewReservationController {
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

    @FXML
    private void initialize() throws Exception{
        roomTypeSelector.getSelectionModel().select(0);
        startPicker.setValue(LocalDate.now());
        endPicker.setValue(LocalDate.now());
        stateSelector.getSelectionModel().select(0);
    }



    public void setManager(DbManager manager) throws Exception{
        this.manager = manager;
        ArrayList<Customer> customers = manager.getCustomers(null, null, null);
        customerSelector.setItems(FXCollections.observableArrayList(customers));
        customerSelector.getSelectionModel().select(0);
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
        rooms = manager.findFreeRooms(startPicker.getValue(), endPicker.getValue(), type);
        roomNoSelector.setItems(FXCollections.observableArrayList(rooms));
        roomNoSelector.getSelectionModel().select(0);
    }

    @FXML
    private void onDateChanged() {
        if(startPicker.getValue().isAfter(endPicker.getValue())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Wrong date");
            alert.setHeaderText(null);
            alert.setContentText("End of reservation must take place after its beginning");
            alert.showAndWait();
            endPicker.setValue(startPicker.getValue());
        }
    }

    @FXML
    private void onOkButtonClicked() {
        Reservation reservation = new Reservation();
        Room room = roomNoSelector.getValue();
        Customer customer = customerSelector.getValue();
        if(room == null || customer == null) return;
        reservation.setId(manager.lastReservation() + 1);
        reservation.setStart(startPicker.getValue());
        reservation.setEnd(endPicker.getValue());
        reservation.setRoom(room);
        reservation.setCustomer(customer);
        reservation.setState(State.values()[stateSelector.getSelectionModel().getSelectedIndex()]);
        manager.addReservation(reservation);
    }

}
