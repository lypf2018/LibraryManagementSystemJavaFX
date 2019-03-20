package application;
	
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.bean.Book;
import model.bean.FineToDisplay;
import model.bean.Loan;
import model.dao.AuthorDao;
import model.dao.BookDao;
import model.dao.BorrowerDao;



public class Main extends Application {

	public static Stage primaryStage;

    private static ObservableList<Book> bookList = FXCollections.observableArrayList();
    private static ObservableList<Loan> loanList = FXCollections.observableArrayList();
    private static ObservableList<FineToDisplay> fineList = FXCollections.observableArrayList();

	@Override
	public void start(Stage primaryStage) {
//		importDataIntoDb();
		Main.primaryStage = primaryStage;
        Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("/view/Search.fxml"));
			Main.primaryStage.setTitle("Library Management System");
			Scene scene = new Scene(root);
			Main.primaryStage.setScene(scene);
			Main.primaryStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void importDataIntoDb(){
		String booksCsvFile = System.getProperty("user.dir") +"/initialdata/books.csv";
		String borrowersCsvFile = System.getProperty("user.dir") + "/initialdata/borrowers.csv";
        BufferedReader br = null;
        String line = "";
        String booksCvsSplitBy = "	";
        String borrowersCvsSplitBy = ",";
        BookDao bookDao = new BookDao();
        AuthorDao authorDao = new AuthorDao();
        BorrowerDao borrowerDao = new BorrowerDao();
        
        try {
            br = new BufferedReader(new FileReader(booksCsvFile));
            line = br.readLine();//skip the first line
//            int count = 0;
            while ((line = br.readLine()) != null) {
                String[] book = line.split(booksCvsSplitBy);
                bookDao.insertBook(book[0], book[2]);
                String[] authors = book[3].split(",");
                for(String authorName : authors){//insert into table Author
                	authorName = authorName.trim();
                	if(!"".equals(authorName) && authorName != null){
                		authorDao.insertAuthor(authorName);
                		bookDao.insertBookAndAuthor(Integer.parseInt(authorDao.getAuthorFromName(authorName).getId()), book[0]);
                	}
                }
//                System.out.println(count + "\n");
//                count++;
            }
            br.close();
            br = new BufferedReader(new FileReader(borrowersCsvFile));
            line = br.readLine();//skip the first line
            while ((line = br.readLine()) != null ) {
                String[] borrower = line.split(borrowersCvsSplitBy);
                borrowerDao.insertBorrower(borrower[1], borrower[2] + " " + borrower[3], borrower[5] + ", " + borrower[6] + ", " + borrower[7], borrower[8]);
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	public static ObservableList<Book> getBookList() {
		return bookList;
	}
	
	public static ObservableList<Loan> getLoanList() {
		return loanList;
	}

	public static ObservableList<FineToDisplay> getFineList() {
		return fineList;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
