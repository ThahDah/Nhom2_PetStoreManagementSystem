package connectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.security.Security;

public class ConnectDB {
	public static Connection con = null;
	private static ConnectDB instance = new ConnectDB();

	public static ConnectDB getInstance() {
		return instance;
	}

	public void connect() throws SQLException {
		Security.setProperty("jdk.tls.disabledAlgorithms", "");
		String url = "jdbc:sqlserver://localhost:1433;databaseName=PetShopDB;encrypt=false;trustServerCertificate=true";
		String user = "sa";
		String pwd = "sapassword";
		con = DriverManager.getConnection(url, user, pwd);
	}

	public static Connection getConnection() {
		return con;
	}
}
