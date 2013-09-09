package com.aptoide.androidclient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.postgresql.PGStatement;
import org.postgresql.util.PSQLException;

/**
 * This class was created for testing purposes only
 * 
 * @author brutus
 * 
 */

public class JDBCConnection {

	public static Class loadDatabaseDriver(String driver_name) {
		try {
			return Class.forName(driver_name);

		} catch (ClassNotFoundException e) {
			System.err.println("Can't load driver - " + e.getMessage());
			return null;
		}
	}

	public static Connection establishConnection(String connection_url,
			String username, String password, boolean ssl_connection) {

		Connection connection;
		try {
			connection = DriverManager.getConnection(
					connection_url,
					defineConnectionProperties(username, password,
							ssl_connection));

			return connection;

		} catch (SQLException e) {
			System.err.println("Can't connect to database - " + e.getMessage());
			return null;
		}
	}

	// public void closeConnection() {
	// try {
	// connection.close();
	//
	// } catch (SQLException e) {
	// System.err.println("Failed to close the connection to database!");
	// e.printStackTrace();
	// }
	// }

	private static Properties defineConnectionProperties(String username,
			String password, boolean ssl_connection) {
		Properties props = new Properties();
		props.setProperty("user", username);
		props.setProperty("password", password);
//		props.setProperty("ssl", Boolean.toString(ssl_connection));

		return props;
	}

	public static ResultSet executeQuery(Connection connection, String query) {

		Statement stmt;
		try {
			stmt = connection.createStatement();

			ResultSet rs = stmt.executeQuery(query);

			return rs;
		} catch (SQLException e) {
			System.out.println("Query failed - " + e.getMessage());
			return null;
		}
	}

}
