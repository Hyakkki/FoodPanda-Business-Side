package Business.Class;

import java.io.IOException;

import Business.BusinessDatabaseHandler;
import Business.SwitchScene;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class BusinessSignInController {

    @FXML
    private Button signinbtn;

    @FXML
    private TextField signinemailadd;

    @FXML
    private PasswordField signinpassword;

    @FXML
    private Button signreturnbtn;

    private Stage stage;
    private Scene scene; 
    private Parent root;

    @FXML
    public void toReturnToSignIntHandler(ActionEvent event) throws IOException{
        SwitchScene.switchScene(event, "/Business/FXML/BusinessLogin.fxml");
    }

    @FXML
    public void toValidateOwnerLogin(ActionEvent event) throws IOException {
        String oemail = signinemailadd.getText();
        String opassword = signinpassword.getText();

        if (BusinessDatabaseHandler.validateBusinessOwnerLogin(oemail, opassword)) {
            System.out.println("Login successful!");

            SwitchScene.switchScene(event, "/Business/FXML/BusinessHomePage.fxml");
        } else {
            System.out.println("Login failed! Invalid email or password.");
        }
    }
}
