package model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.bean.FineToDisplay;
import util.DbManager;

public class FineDao {
	private DbManager dbManager = new DbManager();

	public boolean updateFines() {
		dbManager.setPreparedSql("INSERT INTO fines (Loan_id) "
				+ "SELECT Loan_id "
				+ "FROM (SELECT Loan_id FROM book_loans WHERE curdate() > Due_date) AS Fine_ids "
				+ "WHERE Loan_id NOT IN (SELECT Loan_id FROM fines);");
		dbManager.executeUpdate();
		dbManager.setPreparedSql("UPDATE fines "
				+ "JOIN book_loans ON fines.Loan_id=book_loans.Loan_id "
				+ "SET fines.Fine_amt=(book_loans.Date_in-book_loans.Due_date)*0.25 "
				+ "WHERE book_loans.Date_in IS NOT NULL AND Paid=0;");
		dbManager.executeUpdate();
		dbManager.setPreparedSql("UPDATE fines "
				+ "JOIN book_loans ON fines.Loan_id=book_loans.Loan_id "
				+ "SET fines.Fine_amt=(curdate()-book_loans.Due_date)*0.25 "
				+ "WHERE book_loans.Date_in IS NULL AND Paid=0;");
		dbManager.executeUpdate();
		return true;
	}

	public ArrayList<FineToDisplay> getAllFines() {
		ArrayList<FineToDisplay> fineList = new ArrayList<FineToDisplay>();
		dbManager.setPreparedSql("SELECT Card_id,SUM(Fine_amt) AS Total_amount "
				+ "FROM book_loans,fines "
				+ "WHERE book_loans.Loan_id=fines.Loan_id AND fines.Paid=0 "
				+ "GROUP BY Card_id "
				+ "ORDER BY Card_id;");
		ResultSet resultSet = dbManager.executeQuery();
		try {
			while (resultSet.next()) {
				FineToDisplay fineToDisplay = new FineToDisplay();
				fineToDisplay.setCardId(resultSet.getInt("Card_id"));
				fineToDisplay.setTotalAmount(resultSet.getDouble("Total_amount"));
				fineList.add(fineToDisplay);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fineList;
	}

	public boolean payFine(int cardId) {
		dbManager.setPreparedSql("UPDATE fines "
				+ "JOIN book_loans ON fines.Loan_id=book_loans.Loan_id "
				+ "SET Paid=1 "
				+ "WHERE BOOK_LOANS.CARD_ID=? AND book_loans.Date_in IS NOT NULL;", cardId);
		dbManager.executeUpdate();
		return true;
	}
}
