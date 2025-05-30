package Business.Class;

import java.io.IOException;

import Business.BusinessDatabaseHandler;

import Business.SwitchScene;
import Database.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class BusinessSignUpController {

    @FXML
    private TextField addresstextf;

    @FXML
    private TextField citytextf;

    @FXML
    private TextField companynametextf;

    @FXML
    private TextField postalcodetextf;

    @FXML
    private TextField signupemailtextf;

    @FXML
    private TextField signupfnametextf;

    @FXML
    private TextField signuplnametextf;

    @FXML
    private PasswordField signuppasswordf;

    @FXML
    private CheckBox termscb;

    private Stage stage;
    private Scene scene; 
    private Parent root;

    @FXML
    public void toReturnToLogintHandler(ActionEvent event) throws IOException{
        SwitchScene.switchScene(event, "/Business/FXML/BusinessLogin.fxml");
    }

    @FXML
    public void submitBusinessSignupHandler(ActionEvent event) throws IOException {
        String address = addresstextf.getText().trim();
        String city = citytextf.getText().trim();
        String companyName = companynametextf.getText().trim(); // Assuming this maps to restaurant
        String postalCode = postalcodetextf.getText().trim();
        String email = signupemailtextf.getText().trim();
        String firstName = signupfnametextf.getText().trim();
        String lastName = signuplnametextf.getText().trim();
        String password = signuppasswordf.getText().trim();

        // Validate required fields
        if (address.isEmpty() || city.isEmpty() || companyName.isEmpty() ||
            postalCode.isEmpty() || email.isEmpty() || firstName.isEmpty() ||
            lastName.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "All fields must be filled.");
            return;
        }

        if (!postalCode.matches("^\\d{4}$")) {
            showAlert(Alert.AlertType.ERROR, "Postal code must be 4 digits.");
            return;
        }

        if (!termscb.isSelected()) {
            showAlert(Alert.AlertType.ERROR, "You must agree to the terms and conditions.");
            return;
        }

        // Check if email already exists
        if (BusinessDatabaseHandler.businessEmailExists(email)) {
            showAlert(Alert.AlertType.ERROR, "Email is already registered.");
            return;
        }

        // Save restaurant location
        String locationID = BusinessDatabaseHandler.generateLocationID();
        boolean locationSaved = BusinessDatabaseHandler.insertRestaurantLocationWithID(locationID, city, address, postalCode);

        if (!locationSaved) {
            showAlert(Alert.AlertType.ERROR, "Failed to save location.");
            return;
        }

        // Save restaurant and get restaurant ID (simulate or implement this)
        String restaurantID = BusinessDatabaseHandler.insertRestaurant(companyName, locationID);

        if (restaurantID == null) {
            showAlert(Alert.AlertType.ERROR, "Failed to save restaurant.");
            return;
        }

        // Save business owner
        boolean ownerSaved = BusinessDatabaseHandler.insertBusinessOwner(restaurantID, firstName, lastName, email, password);

        if (!ownerSaved) {
            showAlert(Alert.AlertType.ERROR, "Failed to create business account.");
            return;
        }

        showAlert(Alert.AlertType.INFORMATION, "Account successfully created!");
        SwitchScene.switchScene(event, "/Business/FXML/BusinessLogin.fxml");
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Signup");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
