package Business.Class;

import java.io.IOException;

import Business.SwitchScene;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class BusinessHomePageController {

    @FXML
    private Button addaproductbtn;

    @FXML
    private Button homepageaccountbtn;

    @FXML
    private Button homepagehomebtn;

    @FXML
    private Label restaunamelabel;

    private Stage stage;
    private Scene scene; 
    private Parent root;

    @FXML
    public void toAddAProductHandler(ActionEvent event) throws IOException{
        SwitchScene.switchScene(event, "/Business/FXML/BusinessAddProduct.fxml");
    }
    
}
