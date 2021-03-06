package com.aptoide.androidclient;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AptoideClientDatabaseConnection {

	private static final String DRIVER_NAME = "org.postgresql.Driver";

	private static final String URL = "jdbc:postgresql://localhost:5432/register";
	private static final String USER = "postgres";
	private static final String PASSWORD = "godIsAProgrammer";

	private static final String COLUMN_NAME = "token";

	private Connection connection;

	public AptoideClientDatabaseConnection() {
		JDBCConnection.loadDatabaseDriver(DRIVER_NAME);

		connection = JDBCConnection.establishConnection(URL, USER, PASSWORD,
				true);

	}

	public String LoginUser(String email, String password) throws SQLException {
		String query = "SELECT usertoken FROM aptoide_user WHERE email='"
				+ email + "' AND password='" + password + "'";

		ResultSet rs = JDBCConnection.executeQuery(connection, query);
		
		String token = null;

		if (rs.next()) {
			token = rs.getString(COLUMN_NAME);
		}
		
		return token;
	}

	public void CloseConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			System.err.println("Erro ao fechar a conexão! - " + e.getMessage());
		}
	}

}
