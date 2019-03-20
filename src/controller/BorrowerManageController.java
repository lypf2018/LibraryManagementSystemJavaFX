package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import model.dao.BorrowerDao;

public class BorrowerManageController implements Initializable {

	@FXML
	private TextField ssnTextField;

	@FXML
	private TextField nameTextField;

	@FXML
	private TextField addressTextField;

	@FXML
	private TextField phoneTextField;

    /**
     * Initializes the controller class.
     */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ssnTextField.setText("");
		nameTextField.setText("");
		addressTextField.setText("");
		phoneTextField.setText("");
	}
    
    @FXML
    private void search(ActionEvent event){
		try {
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/view/Search.fxml"));
            Parent searchScene = (Parent) myLoader.load();
			Main.primaryStage.setScene(new Scene(searchScene));
			Main.primaryStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @FXML
    private void register(ActionEvent event){
		BorrowerDao borrowerDao = new BorrowerDao();
    	if("".equals(ssnTextField.getText())) {
    		Alert alert = new Alert(Alert.AlertType.INFORMATION);
    		alert.setTitle("Information Dialog");
    		alert.setHeaderText(null);
    		alert.setContentText("Please insert SSN!");
    		
    		alert.showAndWait();    		
    	} else if("".equals(nameTextField.getText())) {
    		Alert alert = new Alert(Alert.AlertType.INFORMATION);
    		alert.setTitle("Information Dialog");
    		alert.setHeaderText(null);
    		alert.setContentText("Please insert Name!");
    		
    		alert.showAndWait();    		
    	} else if("".equals(addressTextField.getText())) {
    		Alert alert = new Alert(Alert.AlertType.INFORMATION);
    		alert.setTitle("Information Dialog");
    		alert.setHeaderText(null);
    		alert.setContentText("Please insert Address!");
    		
    		alert.showAndWait();    		
    	} else if("".equals(phoneTextField.getText())) {
    		Alert alert = new Alert(Alert.AlertType.INFORMATION);
    		alert.setTitle("Information Dialog");
    		alert.setHeaderText(null);
    		alert.setContentText("Please insert Phone!");
    		
    		alert.showAndWait();    		
    	} else {
    		if(borrowerDao.borrowerSsnExists(ssnTextField.getText())) {
        		Alert alert = new Alert(Alert.AlertType.INFORMATION);
        		alert.setTitle("Information Dialog");
        		alert.setHeaderText(null);
        		alert.setContentText("SSN exists. Please choose another SSN or use your library card directly!");
        		
        		alert.showAndWait();    		
    		} else {
    			borrowerDao.insertBorrower(ssnTextField.getText(), nameTextField.getText(), addressTextField.getText(), phoneTextField.getText());

    			Alert alert = new Alert(Alert.AlertType.INFORMATION);
        		alert.setTitle("Information Dialog");
        		alert.setHeaderText(null);
        		alert.setContentText("Create a new borrower successfully!");
        		
        		alert.showAndWait();    		

        		ssnTextField.setText("");
    			nameTextField.setText("");
    			addressTextField.setText("");
    			phoneTextField.setText("");
    		}
    	}
    }
}
