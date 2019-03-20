package model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

import model.bean.Loan;
import util.DbManager;

public class LoanDao {
	private DbManager dbManager = new DbManager();

	public boolean insertLoan(String isbn, String cardId) {
		dbManager.setPreparedSql("INSERT INTO book_loans (Isbn,Card_id,Date_out,Due_date,Date_in) VALUES (?,?,curdate(),date_add(curdate(), interval 14 day),NULL);", isbn, cardId);
		dbManager.executeUpdate();
		return true;
	}

	public boolean updateLoan(int loanId) {
		dbManager.setPreparedSql("UPDATE book_loans SET Date_in=curdate() WHERE Loan_id=?;", loanId);
		dbManager.executeUpdate();
		return true;
	}

	public int getLoanNumFromCardId(String cardId) {
		dbManager.setPreparedSql("SELECT Isbn FROM book_loans WHERE Card_id=?;", cardId);
		ResultSet resultSet = dbManager.executeQuery();
		int LoanNum = 0;
		try {
			while (resultSet.next()) {
				LoanNum++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return LoanNum;
	}

	public ArrayList<Loan> searchLoan(String keyword) {
		ArrayList<Loan> resultLoans = new ArrayList<Loan>();
		HashSet<Integer> resultLoansIdSet = new HashSet<Integer>();
		String[] keywords = keyword.split("\\s+");
        for(String eachKeyword : keywords){
        	eachKeyword = eachKeyword.trim();
        	if(!"".equals(eachKeyword) && eachKeyword != null){
        		dbManager.setPreparedSql(
        				"SELECT DISTINCT Loan_id FROM book_loans WHERE Isbn=?"
        				+ " UNION "
        				+ "SELECT DISTINCT Loan_id FROM book_loans WHERE Card_id=?"
        				+ " UNION "
        				+ "SELECT DISTINCT Loan_id FROM book_loans WHERE Card_id IN (SELECT Card_id FROM borrower WHERE Bname LIKE ?);",
        				eachKeyword, eachKeyword, "%" + eachKeyword + "%");
        		ResultSet resultSet = dbManager.executeQuery();
    			try {
					while (resultSet.next()) {
						int loanId = resultSet.getInt("Loan_id");
						resultLoansIdSet.add(loanId);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        }
        for(int loanId: resultLoansIdSet) {
        	resultLoans.add(getLoanFromId(loanId));
        }
		return resultLoans;
	}

	private Loan getLoanFromId(int loanId) {
		Loan loan = new Loan();
		dbManager.setPreparedSql("SELECT book_loans.Loan_id AS Loan_id,book_loans.Isbn AS Isbn,book.Title AS Title,book_loans.Card_id AS Card_id,borrower.Bname AS Bname,book_loans.Date_out AS Date_out,book_loans.Due_date AS Due_date,book_loans.Date_in AS Date_in "
				+ "FROM book_loans,book,borrower "
				+ "WHERE book_loans.Loan_id=? "
				+ "AND book_loans.Isbn=book.Isbn "
				+ "AND book_loans.Card_id=borrower.Card_id;", loanId);
		ResultSet resultSet = dbManager.executeQuery();
		try {
			while (resultSet.next()) {
				loan.setId(resultSet.getInt("Loan_id"));
				loan.setIsbn(resultSet.getString("Isbn"));
				loan.setTitle(resultSet.getString("Title"));
				loan.setCardId(resultSet.getInt("Card_id"));
				loan.setBorrowerName(resultSet.getString("Bname"));
				loan.setDateOut(resultSet.getDate("Date_out"));
				loan.setDueDate(resultSet.getDate("Due_date"));
				loan.setDateIn(resultSet.getDate("Date_in"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return loan;
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
