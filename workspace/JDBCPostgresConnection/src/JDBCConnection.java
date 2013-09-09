import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


public class JDBCConnection {

	public static String URL = "jdbc:postgresql://localhost:5432/register";
	public static String USER = "postgres";
	public static String PASSWORD = "godIsAProgrammer";

	private static final String DEVICE_IMEI = "000";
	private static final String USERNAME = "tintim";

	public static void main(String[] args) {
		loadDatabaseDriver("org.postgresql.Driver");

		Connection connection = establishConnection(URL, USER, PASSWORD, false);

		String query;

		query = "SELECT queue_name FROM queue WHERE device_imei='"
				+ DEVICE_IMEI + "' AND username='" + USERNAME + "'";

		ResultSet rs = executeQuery(connection, query);

		try {
			if (rs.next()) {

				System.out.println(rs.getString("queue_name"));

			} else {
				System.out.println("No results to show");
			}
		} catch (SQLException e) {
			System.out.println("Error retrieving data from ResultSet! - "
					+ e.getMessage());
		}
	}

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

		String ssl_requisition;
		if (ssl_connection) {
			ssl_requisition = "True";
		} else {
			ssl_requisition = "False";
		}

		// props.setProperty("ssl", ssl_requisition);

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
