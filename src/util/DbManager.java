package util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author lypf2018
 *
 * MySQL database utility class.
 * Implement based on PreparedStatement
 * for convenience to use JDBC and to code with SQL without dealing with SQLException
 * 
 */
public class DbManager {

	/**
	 * @param args
	 * @throws SQLException
	 */
	private String databaseUrl = null;
	private String user = null;
	private String password = null;
	private Connection connection = null;
	private PreparedStatement preparedStatement = null;

	/**
	 * @return the connection
	 */
	public Connection getConnection() {
		return connection;
	}

	static {
		LoadDriver();// Load MySQL driver
	}

	/**
	 * Load JDBC driver
	 */
	private static void LoadDriver() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Constructor of DbManager class with no parameter 
	 * Call the constructor using parameters in database configuration
	 * file,"database.properties" , in serverconf directory
	 */
	public DbManager() {
		this("databaseconfig/database.properties");
	}

	/**
	 * Constructor of DbManager class with parameter configured in configuration file
	 * 
	 * @param configurationFile the database configuration file
	 */
	public DbManager(String configurationFile) {
		Properties databaseProperties = new Properties();
		try {
			databaseProperties.load(this.getClass().getClassLoader().getResourceAsStream(configurationFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.databaseUrl = databaseProperties.getProperty("databaseurl");
		this.user = databaseProperties.getProperty("user");
		this.password = databaseProperties.getProperty("password");
		this.connection = getConnectionToDatabase(this.databaseUrl, this.user, this.password);
		this.preparedStatement = null;
	}

	/**
	 * Get connection to database
	 * 
	 * @param databaseUrl the database url
	 * @param user the database user
	 * @param password the user's password
	 * @return
	 */
	private Connection getConnectionToDatabase(String databaseUrl, String user, String password) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(databaseUrl, user, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}

	/**
	 * Set up precompilation of SQL parametric statement
	 * 
	 * @param sql an SQL statement that may contain one or more '?' IN
     * parameter placeholders
	 */
	private void prepareSql(String sql)	{
		try {
			this.preparedStatement = this.connection.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	/**
	 * Set values into SQL parametric statement
	 * 
	 * @param values list of parameters
	 */
	private void setValuesIntoSql(Object... values) {
		for (int parameterIndex = 1; parameterIndex <= values.length; parameterIndex++) {
			if (values[parameterIndex - 1] instanceof String) {
				try {
					this.preparedStatement.setString(parameterIndex, (String) values[parameterIndex - 1]);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (values[parameterIndex - 1] instanceof Integer) {
				try {
					this.preparedStatement.setInt(parameterIndex, (Integer) values[parameterIndex - 1]);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (values[parameterIndex - 1] instanceof Double) {
				try {
					this.preparedStatement.setDouble(parameterIndex, (Double) values[parameterIndex - 1]);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Congregate PreparedStatement's methods-such as setString,
	 * setInt, setDouble-with different data types of input into
	 * one method for convenience to code with SQL.
	 * Set up precompilation of SQL parametric statement,
	 * and set values into SQL parametric statement,
	 * using prepareSql() and setValuesIntoSql().
	 * 
	 * @param sql an SQL statement that may contain one or more '?' IN
     * parameter placeholders
	 * @param values values list of parameters
	 */
	public void setPreparedSql(String sql, Object... values) {
		prepareSql(sql);
		setValuesIntoSql(values);
	}
	
	/**
	 * Executes the SQL statement in this.preparedStatement,
	 * which must be an SQL statement, such as <code>INSERT</code>, <code>UPDATE</code> or
	 * <code>DELETE</code>; or an SQL statement that returns nothing,
	 * 
	 * Rewrite PreparedStatement's method executeUpdate()
	 * to encapsulate it into DbManager for convenience to use
	 * 
	 * @return (1) the row count for SQL Data Manipulation Language (DML) statements,
     *         (2) 0 for SQL statements that return nothing
     *         or (3) -1 for Erroneous SQL execution throwing SQLException
	 */
	public int executeUpdate() {
		try {
			return this.preparedStatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * Executes the SQL query in this.preparedStatement,
	 * and returns the <code>ResultSet</code> object generated by the query.
	 * 
	 * Rewrite PreparedStatement's method executeQuery()
	 * to encapsulate it into DbManager for convenience to use
	 * 
	 * @return a <code>ResultSet</code> object that contains the data produced by the
	 *         query; <code>null</code> means Erroneous SQL execution throwing SQLException
	 */
	public ResultSet executeQuery() {
		try {
			return this.preparedStatement.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Close resources,this.preparedStatement and this.connection
	 * Using this method, no need to deal with PreparedStatement.close() and Connection.close()
	 */
	public void close() {
		if (this.preparedStatement != null) {
			try {
				this.preparedStatement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (this.connection != null) {
			try {
				this.connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Before releasing resources, execute close() method to ensure
	 * this.preparedStatement and this.connection has been closed 
	 */
	protected void finalize() throws Throwable {
		close();
		super.finalize();
	}

}
