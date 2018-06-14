package app;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class MainWindowController {

    @FXML
    private TextField idField;
    @FXML
    private TextField customerIdField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField surnameField;
    @FXML
    private CheckBox startEnabler;
    @FXML
    private CheckBox endEnabler;
    @FXML
    private DatePicker startPicker;
    @FXML
    private DatePicker endPicker;
    @FXML
    private TableView<Reservation> reservationsTable;
    @FXML
    private ComboBox statusSelector;
    @FXML
    private TextField roomNoField;

    private DbManager manager;
    private ArrayList<Reservation> reservations;

    @FXML
    private void initialize() {

        try {
            manager = new DbManager();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error occured while opening database");
            alert.show();
        }

        idField.textProperty().addListener((observable, o, n) -> {
            if(!n.matches("[\\d]"))
                idField.setText(n.replaceAll("[^\\d]", ""));
        });

        roomNoField.textProperty().addListener((observable, o, n) -> {
            if(!n.matches("[\\d]"))
                roomNoField.setText(n.replaceAll("[^\\d]", ""));
        });

        startPicker.setValue(LocalDate.now());
        endPicker.setValue(LocalDate.now());

        statusSelector.getSelectionModel().select(0);

        TableColumn idColumn = new TableColumn("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<Reservation, Integer>("id"));

        TableColumn nameColumn = new TableColumn("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<Reservation, String>("customerName"));
        TableColumn surnameColumn = new TableColumn("Surname");
        surnameColumn.setCellValueFactory(new PropertyValueFactory<Reservation, String>("customerSurname"));
        TableColumn roomColumn = new TableColumn("Room");
        roomColumn.setCellValueFactory(new PropertyValueFactory<Reservation, Integer>("roomNo"));
        TableColumn startColumn = new TableColumn("Start");
        startColumn.setCellValueFactory(new PropertyValueFactory<Reservation, LocalDate>("start"));
        TableColumn endColumn = new TableColumn("End");
        endColumn.setCellValueFactory(new PropertyValueFactory<Reservation, LocalDate>("end"));
        TableColumn stateColumn = new TableColumn("State");
        stateColumn.setCellValueFactory(new PropertyValueFactory<Reservation, State>("state"));

        reservationsTable.getColumns().addAll(idColumn, nameColumn, surnameColumn,
                roomColumn, startColumn, endColumn, stateColumn);
        try {
            reservations = manager.getReservations(-1, null, null, null, null, null,-1, null);
            reservationsTable.setItems(FXCollections.observableArrayList(reservations));
        } catch (Exception ex) {ex.printStackTrace();}


    }
    @FXML
    private void onStartEnablerClicked() {
        startPicker.setDisable(!startEnabler.isSelected());
        dateChanged();
    }
    @FXML
    private void onEndEnablerClicked() {
        endPicker.setDisable(!endEnabler.isSelected());
        dateChanged();
    }

    //also used to refresh reservations after adding or modifying existing ones
    @FXML
    private void onSearchClicked() {

        LocalDate start =  null, end = null;
        int id = -1, roomNo = -1;
        int selectedIndex = statusSelector.getSelectionModel().getSelectedIndex();
        State state = null;
        if(selectedIndex > 0)
            state = State.values()[selectedIndex - 1];
        if(!startPicker.isDisable()) start = startPicker.getValue();
        if(!endPicker.isDisable()) end = endPicker.getValue();

        try {
            if(idField.getText().length() > 0)
                id = Integer.parseInt(idField.getText());
            if(roomNoField.getText().length() > 0)
                roomNo = Integer.parseInt(roomNoField.getText());
        } catch (NumberFormatException ex) { return;}

        try {
            reservations = manager.getReservations(id, start, end, customerIdField.getText(),
                    nameField.getText(), surnameField.getText(), roomNo, state );
            System.out.println(reservations.size());
            reservationsTable.setItems(FXCollections.observableArrayList(reservations));
        } catch (SQLException ex) {
            ex.printStackTrace();

        }
    }
    public void onNewClicked() throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("newreservationform.fxml"));
        Parent root = loader.load();
        NewReservationController controller = loader.getController();
        controller.setManager(manager);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.sizeToScene();
        stage.initOwner(roomNoField.getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.showAndWait();
        onSearchClicked();
    }

    public void onEditClicked() throws Exception {
        Reservation reservation = reservationsTable.getSelectionModel().getSelectedItem();
        if(reservation == null) return;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("editreservationform.fxml"));
        Parent root = loader.load();
        EditReservationController controller = loader.getController();
        controller.setManager(manager);
        controller.setReservation(reservation);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.sizeToScene();
        stage.initOwner(roomNoField.getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.showAndWait();
        onSearchClicked();
    }

    public void onDeleteClicked() {
        Reservation reservation = reservationsTable.getSelectionModel().getSelectedItem();
        if(reservation == null) return;
        manager.deleteReservation(reservation);
        onSearchClicked();
    }
    @FXML
    private void onCloseClicked() {
        ((Stage)idField.getScene().getWindow()).close();
    }
    @FXML
    private void onNewCustomerClicked() throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("newcustomerform.fxml"));
        Parent root = loader.load();
        NewCustomerFormController controller = loader.getController();
        controller.setDbManager(manager);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initOwner(roomNoField.getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.showAndWait();
    }

    @FXML
    private void onNewRoomClicked() throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("newroomform.fxml"));
        Parent root = loader.load();
        NewRoomFormController controller = loader.getController();
        controller.setManager(manager);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("New room");
        stage.initOwner(roomNoField.getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.showAndWait();
    }

    @FXML
    private void onFindCustomerClicked() throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FindCustomer.fxml"));
        Parent root = loader.load();
        FindCustomerController controller = loader.getController();
        controller.setManager(manager);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Find customer");
        stage.initOwner(roomNoField.getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.showAndWait();
    }

    @FXML
    private void onFindRoomClicked() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("findroomdialog.fxml"));
        Parent root = loader.load();
        FindRoomController controller = loader.getController();
        controller.setManager(manager);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Find rooms");
        stage.initOwner(roomNoField.getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.showAndWait();
    }

    @FXML
    private void dateChanged() {
        if(startPicker.isDisable() || endPicker.isDisable()) return;
        else if(startPicker.getValue().isAfter(endPicker.getValue())){
            endPicker.setValue(startPicker.getValue());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Wrong date");
            alert.setHeaderText(null);
            alert.setContentText("End of reservation must take place after its beginning");
            alert.showAndWait();
        }
    }
}
