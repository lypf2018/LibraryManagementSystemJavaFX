package model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import model.bean.Author;
import util.DbManager;

public class AuthorDao {
	private DbManager dbManager = new DbManager();

	public boolean insertAuthor(String authorName) {
		dbManager.setPreparedSql("SELECT Author_id from authors where Name=?;", authorName);
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
			dbManager.setPreparedSql("INSERT INTO authors (Name) VALUES (?);", authorName);
			dbManager.executeUpdate();
			return true;
		}
	}

	public Author getAuthorFromName(String authorName) {
		dbManager.setPreparedSql("SELECT Author_id,Name from authors where Name=?;", authorName);
		ResultSet resultSet = dbManager.executeQuery();
		Author author = new Author();
		try {
			while (resultSet.next()) {
				author.setId(resultSet.getString("Author_id"));;
				author.setName(resultSet.getString("Name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return author;
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
