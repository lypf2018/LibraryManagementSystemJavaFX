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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.bean.Book;
import model.dao.BorrowerDao;
import model.dao.LoanDao;

public class BookCheckOutController implements Initializable {

	@FXML
	private TableView<Book> bookTable;

	@FXML
	private TableColumn<Book, String> isbnColumn;

	@FXML
	private TableColumn<Book, String> titleColumn;

	@FXML
	private TableColumn<Book, String> authorsColumn;

	@FXML
	private TableColumn<Book, String> availabilityColumn;

	@FXML
	private TextField cardNumberTextField;

	/**
     * Initializes the controller class.
     */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		isbnColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("isbn"));
		titleColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
		authorsColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("authors"));
		availabilityColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("availability"));
		bookTable.setItems(Main.getBookList());
		cardNumberTextField.setText("");
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
    private void checkOut(ActionEvent event){
		LoanDao loanDao = new LoanDao();
		BorrowerDao borrowerDao = new BorrowerDao();
    	if(bookTable.getSelectionModel().getSelectedCells().isEmpty()) {
    		Alert alert = new Alert(Alert.AlertType.INFORMATION);
    		alert.setTitle("Information Dialog");
    		alert.setHeaderText(null);
    		alert.setContentText("Please select a book!");
    		
    		alert.showAndWait();    		
    	} else {
    		if(!Main.getBookList().get(bookTable.getSelectionModel().getSelectedCells().get(0).getRow()).getAvailability()) {
        		Alert alert = new Alert(Alert.AlertType.INFORMATION);
        		alert.setTitle("Information Dialog");
        		alert.setHeaderText(null);
        		alert.setContentText("Book has been checked out! Choose another book!");
        		
        		alert.showAndWait();    		
    		} else {
    			if("".equals(cardNumberTextField.getText())) {
            		Alert alert = new Alert(Alert.AlertType.INFORMATION);
            		alert.setTitle("Information Dialog");
            		alert.setHeaderText(null);
            		alert.setContentText("Please insert Card Number!");
            		
            		alert.showAndWait();    		
    			} else {
    				if(!borrowerDao.borrowerExists(cardNumberTextField.getText())) {
    					Alert alert = new Alert(Alert.AlertType.INFORMATION);
    					alert.setTitle("Information Dialog");
    					alert.setHeaderText(null);
    					alert.setContentText("Card Number Error! Please re-enter or register!");
    					
    					alert.showAndWait();    		
    				} else {
    					int loanNum = loanDao.getLoanNumFromCardId(cardNumberTextField.getText());
    					if(loanNum >= 3) {
    						Alert alert = new Alert(Alert.AlertType.INFORMATION);
    						alert.setTitle("Information Dialog");
    						alert.setHeaderText(null);
    						alert.setContentText("Borrower reach max number 3! Please check in book first!");
    						
    						alert.showAndWait();    		
    					} else {
    						loanDao.insertLoan(Main.getBookList().get(bookTable.getSelectionModel().getSelectedCells().get(0).getRow()).getIsbn(), cardNumberTextField.getText());
    						Main.getBookList().get(bookTable.getSelectionModel().getSelectedCells().get(0).getRow()).setAvailability(false);
    						bookTable.setItems(Main.getBookList());
    						try {
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
    			}
    		}
    	}
    }
}
