import java.sql.*;
import java.io.*;
import oracle.jdbc.OracleDriver;

class sqlTest {

	public static void main(String[] args) throws SQLException, IOException {
		//Load Oracle JDBC Driver
		DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
		
		String serverName = "csor12c.dhcp.bsu.edu";
		String portNumber = "1521";
		String sid = "or12cdb";
		String url = "jdbc:oracle:thin:@" + serverName + ":" + portNumber + ":" + sid;
		
		Connection conn = DriverManager.getConnection(url, "BSU901196571", "901196571");
		
		//Create a statement
		Statement stmt = conn.createStatement();
		
		//Do the SQL
		ResultSet rset = stmt.executeQuery("SELECT * FROM VOLUNTEER ORDER BY Fname");
		
		System.out.println("Employee Table:");
		System.out.println("FName		LName		SSN			Address			Salary");
		System.out.println("------------------------------------------------------------------");
		
		while(rset.next()) {
			System.out.println(rset.getString(1) + " "
					+ "\t" + rset.getString(3)
					+ "\t" + rset.getString(4)
					+ "\t" + rset.getString(6)
					+ "\t" + rset.getString(8));
		}
		
		System.out.println("-------------------------------------------------------------------");
		
		//Close the result set
		rset.close();
		
		//Close the statement
		stmt.close();
		
		//Close the connection
		conn.close();

	}

}
