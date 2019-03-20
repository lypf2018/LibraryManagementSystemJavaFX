package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
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
import model.bean.Loan;
import model.dao.LoanDao;

public class BookCheckInController implements Initializable {

	@FXML
	private TableView<Loan> loanTable;

	@FXML
	private TableColumn<Loan, String> isbnColumn;

	@FXML
	private TableColumn<Loan, String> titleColumn;

	@FXML
	private TableColumn<Loan, String> cardIdColumn;

	@FXML
	private TableColumn<Loan, String> borrowerNameColumn;

	@FXML
	private TableColumn<Loan, String> dateOutColumn;
	
	@FXML
	private TableColumn<Loan, String> dueDateColumn;
	
	@FXML
	private TableColumn<Loan, String> dateInColumn;

    /**
     * Initializes the controller class.
     */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		isbnColumn.setCellValueFactory(new PropertyValueFactory<Loan, String>("isbn"));
		titleColumn.setCellValueFactory(new PropertyValueFactory<Loan, String>("title"));
		cardIdColumn.setCellValueFactory(new PropertyValueFactory<Loan, String>("cardId"));
		borrowerNameColumn.setCellValueFactory(new PropertyValueFactory<Loan, String>("borrowerName"));
		dateOutColumn.setCellValueFactory(new PropertyValueFactory<Loan, String>("dateOut"));
		dueDateColumn.setCellValueFactory(new PropertyValueFactory<Loan, String>("dueDate"));
		dateInColumn.setCellValueFactory(new PropertyValueFactory<Loan, String>("dateIn"));
		loanTable.setItems(Main.getLoanList());
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
    private void checkIn(ActionEvent event) {
		LoanDao loanDao = new LoanDao();
    	if(loanTable.getSelectionModel().getSelectedCells().isEmpty()) {
    		Alert alert = new Alert(Alert.AlertType.INFORMATION);
    		alert.setTitle("Information Dialog");
    		alert.setHeaderText(null);
    		alert.setContentText("Please select a loan!");
    		
    		alert.showAndWait();    		
    	} else if(Main.getLoanList().get(loanTable.getSelectionModel().getSelectedCells().get(0).getRow()).getDateIn() != null) {
    		Alert alert = new Alert(Alert.AlertType.INFORMATION);
    		alert.setTitle("Information Dialog");
    		alert.setHeaderText(null);
    		alert.setContentText("Book has been checked in! Choose another loan!");
    		
    		alert.showAndWait();    		
    	} else {
    		loanDao.updateLoan(Main.getLoanList().get(loanTable.getSelectionModel().getSelectedCells().get(0).getRow()).getId());
    		Main.getLoanList().get(loanTable.getSelectionModel().getSelectedCells().get(0).getRow()).setDateIn(new Date(System.currentTimeMillis()));;
    		loanTable.setItems(Main.getLoanList());
    		try {
    			FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/view/BookCheckIn.fxml"));
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
