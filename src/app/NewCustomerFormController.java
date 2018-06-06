package app;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;


public class NewCustomerFormController {
    @FXML
    private TextField idField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField surnameField;
    @FXML
    private RadioButton peselRadio;
    @FXML
    private RadioButton otherRadio;

    private DbManager manager;

    @FXML
    private void initialize() {
        ToggleGroup group = new ToggleGroup();
        peselRadio.setToggleGroup(group);
        otherRadio.setToggleGroup(group);
    }

    public void setDbManager(DbManager manager) {
        this.manager = manager;
    }

    @FXML
    private void onOkButtonClicked() {
        String id = idField.getText();
        Alert dbAlert = new Alert(Alert.AlertType.ERROR);
        dbAlert.setTitle("Error");
        dbAlert.setHeaderText(null);
        dbAlert.setContentText("Error occured while accessing database");
        if(peselRadio.isSelected()) {
            Alert peselAlert = new Alert(Alert.AlertType.ERROR);
            peselAlert.setTitle("Error");
            peselAlert.setHeaderText(null);
            peselAlert.setContentText("Wrong PESEL");
            if(id.length() == 11) {
                int sum = 0;
                sum += Character.getNumericValue(id.charAt(0)) * 9;
                sum += Character.getNumericValue(id.charAt(1)) * 7;
                sum += Character.getNumericValue(id.charAt(2)) * 3;
                sum += Character.getNumericValue(id.charAt(3));
                sum += Character.getNumericValue(id.charAt(4)) * 9;
                sum += Character.getNumericValue(id.charAt(5)) * 7;
                sum += Character.getNumericValue(id.charAt(6)) * 3;
                sum += Character.getNumericValue(id.charAt(7));
                sum += Character.getNumericValue(id.charAt(8)) * 9;
                sum += Character.getNumericValue(id.charAt(9)) * 7;
                if(Character.getNumericValue(id.charAt(9)) != sum % 10) {
                    peselAlert.showAndWait();
                    return;
                }
            }
            else {
                peselAlert.showAndWait();
                return;
            }
        }
        Customer customer = new Customer();
        customer.setId(id);
        customer.setName(nameField.getText());
        customer.setSurname(surnameField.getText());
        if(!manager.addCustomer(customer)){
            dbAlert.showAndWait();
            return;
        }
        ((Stage)peselRadio.getScene().getWindow()).close();
    }

    @FXML
    private void onCancelButtonClicked() {
        ((Stage)peselRadio.getScene().getWindow()).close();
    }
}
