package model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

import model.bean.Book;
import util.DbManager;

public class BookDao {
	private DbManager dbManager = new DbManager();

	public boolean insertBook(String isbn, String title) {
        dbManager.setPreparedSql("INSERT INTO book (Isbn, Title) VALUES (?,?);", isbn, title);
		dbManager.executeUpdate();
		return true;
	}

	public boolean insertBookAndAuthor(int Author_id, String isbn) {
		dbManager.setPreparedSql("SELECT Author_id,Isbn FROM book_authors WHERE Author_id=? AND Isbn=?;", Author_id, isbn);
		ResultSet resultSet = dbManager.executeQuery();
		boolean existed = false;
		try {
			existed = resultSet.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(existed) {
			return false;
		} else {
			dbManager.setPreparedSql("INSERT INTO book_authors (Author_id, Isbn) VALUES (?,?);", Author_id, isbn);
			dbManager.executeUpdate();
			return true;
		}
	}

	public ArrayList<Book> searchBook(String keyword) {
		ArrayList<Book> resultBooks = new ArrayList<Book>();
		HashSet<String> resultBooksIsbnSet = new HashSet<String>();
		String[] keywords = keyword.split("\\s+");
        for(String eachKeyword : keywords){
        	eachKeyword = eachKeyword.trim();
        	if(!"".equals(eachKeyword) && eachKeyword != null){
        		dbManager.setPreparedSql(
        				"SELECT DISTINCT Isbn FROM book WHERE Isbn=?"
        				+ " UNION "
        				+ "SELECT DISTINCT Isbn FROM book WHERE Title LIKE ?"
        				+ " UNION "
        				+ "SELECT DISTINCT Isbn FROM book_authors WHERE Author_id IN (SELECT Author_id FROM authors WHERE Name LIKE ?);",
        				eachKeyword, "%" + eachKeyword + "%", "%" + eachKeyword + "%");
        		ResultSet resultSet = dbManager.executeQuery();
    			try {
					while (resultSet.next()) {
						String Isbn = resultSet.getString("Isbn");
						resultBooksIsbnSet.add(Isbn);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        }
        for(String Isbn: resultBooksIsbnSet) {
        	resultBooks.add(getBookFromIsbn(Isbn));
        }
		return resultBooks;
	}

	public Book getBookFromIsbn(String Isbn) {
		Book book = new Book();
		dbManager.setPreparedSql("SELECT Isbn,Title FROM book WHERE Isbn=?;", Isbn);
		ResultSet resultSet = dbManager.executeQuery();
		try {
			while (resultSet.next()) {
				book.setIsbn(resultSet.getString("Isbn"));
				book.setTitle(resultSet.getString("Title"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String authors = "";
		dbManager.setPreparedSql("SELECT Name FROM book_authors,authors WHERE book_authors.Author_id=authors.Author_id AND book_authors.Isbn=?;", Isbn);
		ResultSet resultSetAuthors = dbManager.executeQuery();
		try {
			if(resultSetAuthors.next()) {
				authors+=resultSetAuthors.getString("Name");
				while (resultSetAuthors.next()) {
					authors+=", ";
					authors+=resultSetAuthors.getString("Name");
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		book.setAuthors(authors);
		dbManager.setPreparedSql("SELECT Date_in FROM book_loans WHERE Isbn=?;", book.getIsbn());
		ResultSet resultSetBookLoaned = dbManager.executeQuery();
		try {
			book.setAvailability(!(resultSetBookLoaned.next() && (resultSetBookLoaned.getDate("Date_in") == null)));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return book;
	}
	/**
	 * Before releasing resources, execute mySQLJDBC.close() method to ensure
	 * this.preparedStatement and this.connection has been closed 
	 */
	protected void finalize() throws Throwable {
		dbManager.close();
		super.finalize();
	}
}
