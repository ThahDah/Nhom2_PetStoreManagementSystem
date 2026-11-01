package connectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
	private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=PetShopLand;"
			+ "encrypt=true;trustServerCertificate=true";
	private static final String USER = "sa";
	private static final String PASSWORD = "sapassword";

	private ConnectDB() {
	}
	
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}
}
