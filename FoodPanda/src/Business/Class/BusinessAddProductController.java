package Business.Class;

import java.io.File;
import java.io.IOException;

import Business.BusinessDatabaseHandler;
import Business.SwitchScene;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class BusinessAddProductController {

    @FXML
    private TextField productdesctextf;

    @FXML
    private ImageView productimageview;

    @FXML
    private TextField productnametextf;

    @FXML
    private TextField productpricetextf;

    @FXML
    private TextField productquantitytextf;

    @FXML
    private Button addreturnbtn;

    @FXML
    private Button saveuploadbtn;

    @FXML
    private Button uploadbtn;

    private Stage stage;
    private Scene scene; 
    private Parent root;

    @FXML
    public void toReturntoHomePageAddHandler(ActionEvent event) throws IOException{
        SwitchScene.switchScene(event, "/Business/FXML/BusinessHomePage.fxml");
    }

    private File selectedImageFile;

    @FXML
    private void initialize() {
        uploadbtn.setOnAction(e -> uploadImage());
        saveuploadbtn.setOnAction(e -> saveProduct());
    }

    private void uploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Product Image");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            selectedImageFile = file;
            Image image = new Image(file.toURI().toString());
            productimageview.setImage(image);
        }
    }

    private void saveProduct() {
    String name = productnametextf.getText();
    String desc = productdesctextf.getText();
    String priceText = productpricetextf.getText();
    String qtyText = productquantitytextf.getText();
    String imagePath = (selectedImageFile != null) ? selectedImageFile.getAbsolutePath() : null;

    if (name.isEmpty() || priceText.isEmpty() || qtyText.isEmpty() || imagePath == null) {
        System.out.println("All fields and image must be filled.");
        return;
    }

    try {
        double price = Double.parseDouble(priceText);
        int quantity = Integer.parseInt(qtyText);
        String productID = BusinessDatabaseHandler.generateProductID();
        String restaurantID = "R001";  // <-- replace with actual logged-in restaurant ID
        String priceRangeID = "PR1";   // <-- you may want to select this dynamically in your UI

        boolean success = BusinessDatabaseHandler.insertProduct(
            productID,
            restaurantID,
            priceRangeID,
            name,
            quantity,
            price,
            desc,
            imagePath
        );

        if (success) {
            System.out.println("Product saved successfully!");
        } else {
            System.out.println("Failed to save product.");
        }

    } catch (NumberFormatException e) {
        System.out.println("Price and Quantity must be valid numbers.");
    }
}

    
}
