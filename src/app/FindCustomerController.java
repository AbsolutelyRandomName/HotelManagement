package app;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

public class FindCustomerController {
    private DbManager manager;
    private ArrayList<Customer> customers;

    @FXML
    private TextField idField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField surnameField;
    @FXML
    private TableView customerTable;

    @FXML
    private void initialize() {
        TableColumn idColumn = new TableColumn("ID");
        TableColumn nameColumn = new TableColumn("Name");
        TableColumn surnameColumn = new TableColumn("Surname");

        idColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("surname"));

        customerTable.getColumns().addAll(idColumn, nameColumn, surnameColumn);
    }

    public void setManager(DbManager manager) {
        this.manager = manager;
    }

    @FXML
    private void onSearchButtonClicked() {
        updateCustomers();
    }

    @FXML
    private void onEditButtonClicked() throws Exception{
        Customer selectedCustomer = (Customer)customerTable.getSelectionModel().getSelectedItem();
        System.out.println(selectedCustomer.getName());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("editcustomerform.fxml"));
        Parent root = loader.load();
        EditCustomerFormController controller = loader.getController();
        controller.setManager(manager);
        controller.setCustomer(selectedCustomer);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Edit customer");
        stage.initOwner(idField.getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.showAndWait();
        customerTable.refresh();
    }

    @FXML
    private void onDeleteButtonClicked() {
        Customer selectedCustomer = (Customer)customerTable.getSelectionModel().getSelectedItem();
        if(!manager.deleteCustomer(selectedCustomer)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occured while deleting item");
            alert.showAndWait();
            return;
        }
        updateCustomers();
    }

    @FXML
    private void onCancelButtonClicked() {
        ((Stage)idField.getScene().getWindow()).close();
    }

    private void updateCustomers() {
        try {
            customers = manager.getCustomers(idField.getText(), nameField.getText(),
                    surnameField.getText());
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occured while accessing database");
            alert.showAndWait();
            return;
        }
        customerTable.setItems(FXCollections.observableArrayList(customers));
    }
}
