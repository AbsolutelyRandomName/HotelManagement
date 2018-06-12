package app;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditCustomerFormController {
    private DbManager manager = null;
    private Customer customer = null;

    @FXML
    private TextField nameField;
    @FXML
    private TextField surnameField;

    private String previousName, previousSurname;

    @FXML
    private void initialize() {

    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        previousName = customer.getName();
        previousSurname = customer.getSurname();
        nameField.setText(customer.getName());
        surnameField.setText(customer.getSurname());
    }

    public void setManager(DbManager manager) {
        this.manager = manager;
    }

    @FXML
    private void onOkButtonClicked() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("An error occured while accessing database");

        customer.setName(nameField.getText());
        customer.setSurname(surnameField.getText());

        if(!manager.editCustomer(customer)){
            alert.showAndWait();
            return;
        }
        ((Stage)nameField.getScene().getWindow()).close();
    }

    @FXML
    private void onCancelButtonClicked() {
        //revert any changes so it won't make effect on TableView
        //if database wasn't changed
        customer.setName(previousName);
        customer.setSurname(previousSurname);
        ((Stage)nameField.getScene().getWindow()).close();
    }
}
