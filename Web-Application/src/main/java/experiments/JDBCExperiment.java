package experiments;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCExperiment {

	public static void main(String[] args) {
		String username = "user27";
		String password = "user27";
		String db = "user27";
		
		try {
			// load driver
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		}
		catch (Exception e) {
			System.err.println("Can't find driver");
			System.exit(1);
		}
		
		String urlString = "jdbc:mysql://localhost:3306/" + db;
		String timeZoneSettings = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

		try (Connection connection = DriverManager.getConnection(urlString + timeZoneSettings,
				                                                 username, password))
		    {
					                                            
			
			String selectStr = "SELECT * FROM user";
			PreparedStatement selsectStmt = connection.prepareStatement(selectStr);
			
			ResultSet resultSet = selsectStmt.executeQuery();
			
			while (resultSet.next()) {
				String str = resultSet.getString("name");
				int id = resultSet.getInt("id");
				System.out.printf("id: %d name: %s\n", id, str);
			}
			
			String nameInsert = "pochi";
			String passwordInsert = "pochi123";
			PreparedStatement insertStmt = connection.prepareStatement("INSERT INTO user (name, password) VALUES (?, ?)");
			insertStmt.setString(1, nameInsert);
			insertStmt.setString(2, passwordInsert);
			insertStmt.execute();
			
            System.out.println();
			
			resultSet = selsectStmt.executeQuery();
			
			while (resultSet.next()) {
				String str = resultSet.getString("name");
				int id = resultSet.getInt("id");
				System.out.printf("id: %d name: %s\n", id, str);
			}
			
			
			
			
			
			
			
		} catch (SQLException sqle) {
			// Ref: https://dev.mysql.com/doc/connector-j/5.1/en/connector-j-usagenotes-connect-drivermanager.html
			System.out.println("SQLException: " + sqle.getMessage());
		    System.out.println("SQLState: " + sqle.getSQLState());
		    System.out.println("VendorError: " + sqle.getErrorCode());
		} 
		
		
		
		
		
	}

}
