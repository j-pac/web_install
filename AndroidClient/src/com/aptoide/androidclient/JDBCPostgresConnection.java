package com.aptoide.androidclient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.postgresql.PGStatement;

/**
 *  This class was created for testing purposes only
 *  
 * @author brutus
 *
 */

public class JDBCPostgresConnection {

	private static final String CONNECTION_URL = "jdbc:postgresql://localhost:5432/web_install";
	private static final String CONNECTION_USERNAME = "postgres";
	private static final String CONNECTION_PASSWORD = "blfa5661";

	private static final String PREPARED_STATEMENT = "SELECT queue_name FROM queue WHERE username=? AND password=?";
	
	private Properties connection_props;
	private Connection connection = null;
	private org.postgresql.PGStatement pstmt;

	public JDBCPostgresConnection() {
		try {
			Class.forName("org.postgresql.Driver");

			defineConnectionProperties();

			establishConnection();

		} catch (SQLException e) {
			System.err.println("Connection failed!");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.err.println("Where is your PostgreSQL JDBC Driver? "
					+ "Include in your library path!");
			e.printStackTrace();
		}
	}

	private void defineConnectionProperties() {
		Properties props = new Properties();
		props.setProperty("user", CONNECTION_USERNAME);
		props.setProperty("password", CONNECTION_PASSWORD);
		props.setProperty("ssl", "true");
	}

	private void establishConnection() throws SQLException {

		connection = DriverManager.getConnection(CONNECTION_URL,
				connection_props);

		if (connection != null) {
			System.out.println("The connection was successfully established!");
		} else {
			System.out.println("The connection failed!");
		}
	}
	
	public void closeConnection() {
		try {
			connection.close();
			
		} catch (SQLException e) {
			System.err.println("Failed to close the connection to database!");
			e.printStackTrace();
		}
	}
	
	public void createPreparedStatement() {
		try {
			pstmt = (org.postgresql.PGStatement) connection.prepareStatement(PREPARED_STATEMENT);
		} catch (SQLException e) {
			System.out.println("Failed to create prepared statement: " + PREPARED_STATEMENT);
			e.printStackTrace();
		}
	}
	
//	public ResultSet executeQuery() {
//		ResultSet rs = ((JDBCPostgresConnection) pstmt).executeQuery();
//	}

}
