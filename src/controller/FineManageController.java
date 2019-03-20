package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.bean.FineToDisplay;
import model.dao.FineDao;

public class FineManageController implements Initializable {

	@FXML
	private TableView<FineToDisplay> fineTable;

	@FXML
	private TableColumn<FineToDisplay, String> cardIdColumn;

	@FXML
	private TableColumn<FineToDisplay, String> totalAmountColumn;

    /**
     * Initializes the controller class.
     */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cardIdColumn.setCellValueFactory(new PropertyValueFactory<FineToDisplay, String>("cardId"));
		totalAmountColumn.setCellValueFactory(new PropertyValueFactory<FineToDisplay, String>("totalAmount"));
		fineTable.setItems(Main.getFineList());
	}
    
    @FXML
    private void search(ActionEvent event) {
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
    private void updateFines(ActionEvent event) {
    	FineDao fineDao = new FineDao();
    	fineDao.updateFines();
		try {
			ArrayList<FineToDisplay> fineList = fineDao.getAllFines();
			Main.getFineList().clear();
			for(int i = 0; i < fineList.size(); i++) {
				Main.getFineList().add(fineList.get(i));
			}
			FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/view/FineManage.fxml"));
			Parent fineManageScene;
			fineManageScene = (Parent) myLoader.load();
			Main.primaryStage.setScene(new Scene(fineManageScene));
			Main.primaryStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @FXML
    private void payFine(ActionEvent event) {
		FineDao fineDao = new FineDao();
    	if(fineTable.getSelectionModel().getSelectedCells().isEmpty()) {
    		Alert alert = new Alert(Alert.AlertType.INFORMATION);
    		alert.setTitle("Information Dialog");
    		alert.setHeaderText(null);
    		alert.setContentText("Please select a loan!");
    		
    		alert.showAndWait();    		
    	} else {
    		fineDao.payFine(Main.getFineList().get(fineTable.getSelectionModel().getSelectedCells().get(0).getRow()).getCardId());
        	fineDao.updateFines();
        	ArrayList<FineToDisplay> fineList = fineDao.getAllFines();
        	Main.getFineList().clear();
        	for(int i = 0; i < fineList.size(); i++) {
        		Main.getFineList().add(fineList.get(i));
        	}
        	try {
        		FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/view/FineManage.fxml"));
        		Parent searchScene = (Parent) myLoader.load();
        		Main.primaryStage.setScene(new Scene(searchScene));
        		Main.primaryStage.show();
        	} catch (IOException e) {
        		// TODO Auto-generated catch block
        		e.printStackTrace();
        	}
    	}
    }
}
