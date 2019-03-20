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
import javafx.scene.control.TextField;
import model.bean.Book;
import model.bean.FineToDisplay;
import model.bean.Loan;
import model.dao.BookDao;
import model.dao.FineDao;
import model.dao.LoanDao;

public class SearchController implements Initializable {

    @FXML
    private TextField searchTextField;

    /**
     * Initializes the controller class.
     */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		searchTextField.setText("");
	}
    

    @FXML
    private void search(ActionEvent event){
		if("".equals(searchTextField.getText())) {
    		Alert alert = new Alert(Alert.AlertType.INFORMATION);
    		alert.setTitle("Information Dialog");
    		alert.setHeaderText(null);
    		alert.setContentText("Please insert keywords!");
    		
    		alert.showAndWait();    		
		} else {
			try {
				BookDao bookDao = new BookDao();
				ArrayList<Book> bookList = bookDao.searchBook(searchTextField.getText());
				Main.getBookList().clear();
				for(int i = 0; i < bookList.size(); i++) {
					Main.getBookList().add(bookList.get(i));
				}
				FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/view/BookCheckOut.fxml"));
				Parent bookCheckOutScene = (Parent) myLoader.load();
				Main.primaryStage.setScene(new Scene(bookCheckOutScene));
				Main.primaryStage.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }

    @FXML
    private void checkIn(ActionEvent event){
		if("".equals(searchTextField.getText())) {
    		Alert alert = new Alert(Alert.AlertType.INFORMATION);
    		alert.setTitle("Information Dialog");
    		alert.setHeaderText(null);
    		alert.setContentText("Please insert keywords!");
    		
    		alert.showAndWait();    		
		} else {
			try {
				LoanDao loanDao = new LoanDao();
				ArrayList<Loan> loanList = loanDao.searchLoan(searchTextField.getText());
				Main.getLoanList().clear();
				for(int i = 0; i < loanList.size(); i++) {
					Main.getLoanList().add(loanList.get(i));
				}
				FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/view/BookCheckIn.fxml"));
				Parent bookCheckOutScene = (Parent) myLoader.load();
				Main.primaryStage.setScene(new Scene(bookCheckOutScene));
				Main.primaryStage.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
    
    @FXML
    private void register(ActionEvent event){
    	try {
    		FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/view/BorrowerManage.fxml"));
    		Parent borrowerManageScene = (Parent) myLoader.load();
    		Main.primaryStage.setScene(new Scene(borrowerManageScene));
    		Main.primaryStage.show();
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    }

    @FXML
    private void manageFines(ActionEvent event){
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
}
