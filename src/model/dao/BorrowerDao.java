package model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import util.DbManager;

public class BorrowerDao {
	private DbManager dbManager = new DbManager();

	public boolean insertBorrower(String ssn, String name, String address, String phone) {
		dbManager.setPreparedSql("INSERT INTO borrower (Ssn,Bname,Address,Phone) VALUES (?,?,?,?);", ssn, name, address, phone);
		dbManager.executeUpdate();
		return true;
	}

	public boolean borrowerExists(String cardId) {
		dbManager.setPreparedSql("SELECT Card_id FROM borrower WHERE Card_id=?;", cardId);
		ResultSet resultSet = dbManager.executeQuery();
		boolean existed = false;
		try {
			existed = resultSet.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return existed;
	}

	public boolean borrowerSsnExists(String ssn) {
		dbManager.setPreparedSql("SELECT Ssn FROM borrower WHERE Ssn=?;", ssn);
		ResultSet resultSet = dbManager.executeQuery();
		boolean existed = false;
		try {
			existed = resultSet.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return existed;
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
