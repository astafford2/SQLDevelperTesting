import java.sql.*;
import java.io.*;
import java.util.Scanner;

class CS418Project3 {
	public static void main(String[] args) throws SQLException, IOException {
		DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
		
		String serverName = "csor12c.dhcp.bsu.edu";
		String portNumber = "1521";
		String sid = "or12cdb";
		String url = "jdbc:oracle:thin:@" + serverName + ":" + portNumber + ":" + sid;
		
		Connection conn = DriverManager.getConnection(url, "BSU901196571", "901196571");
		
		Statement stmt = conn.createStatement();
		
		userChoice(stmt);
		
		stmt.close();

		conn.close();
	}
	
	public static void userChoice(Statement stmt) throws SQLException {
		Scanner k = new Scanner(System.in);
		System.out.println("What would you like to do?");
		System.out.println("1 - View Volunteers by VID Range");
		System.out.println("2 - View Volunteers by Birth Year");
		System.out.println("3 - View Volunteers by Bank");
		System.out.println("4 - Set Volunteer Weekly Hours");
		System.out.println("5 - View Food Items by Product ID");
		
		int choice = k.nextInt();
		
		switch (choice) {
			case 1:
				viewVolunteersByIdRange(stmt);
				break;
			case 2:
				viewVolunteersByBirthYear(stmt);
				break;
			case 3:
				viewVolunteersByBank(stmt);
				break;
			case 4:
				setVolunteerHours(stmt);
				break;
			case 5:
				viewFoodItemsByIdRange(stmt);
				break;
			default:
				System.out.println("Invalid choice");
		}
		
		k.close();
	}
	
	//Function 1
	public static void viewVolunteersByIdRange(Statement stmt) throws SQLException {
		Scanner k = new Scanner(System.in);
		
		System.out.println("Enter the VID range you would like to select: ");
		System.out.println("Low VID: ");
		int lowVID = k.nextInt();
		System.out.println("High VID: ");
		int highVID = k.nextInt();
		
		ResultSet rset = stmt.executeQuery("SELECT * FROM VOLUNTEER WHERE VID BETWEEN " + lowVID + " AND "
		+ highVID + " ORDER BY VID");
		
		System.out.println("\nVolunteer Table:");
		System.out.println("----------------------------------------------------");
		System.out.println("Fname \tLname \tVID \tHours \tBank \tVTimes");
		System.out.println("----------------------------------------------------");
		
		while(rset.next()) {
			System.out.println(rset.getString(1)
					+ "\t" + rset.getString(3)
					+ "\t" + rset.getString(4)
					+ "\t" + rset.getString(6)
					+ "\t" + rset.getString(9)
					+ "\t" + rset.getString(10));
		}
		
		rset.close();
		
		k.close();
	}
	
	//Function 2
	public static void viewVolunteersByBirthYear(Statement stmt) throws SQLException {
		Scanner k = new Scanner(System.in);
		
		System.out.println("Enter the birth year you would like to select (spelled out, capitalized): ");
		String year = k.nextLine();
		
		ResultSet rset = stmt.executeQuery("SELECT Fname, Lname, to_char(BDATE, 'Year') AS BIRTH_YEAR "
				+ "FROM VOLUNTEER WHERE to_char(bdate, 'Year') LIKE " + "\'" + year + "\'" + "ORDER BY Lname");
		
		System.out.println("\nVolunteer Table:");
		System.out.println("----------------------------------------------------");
		System.out.println("Fname \tLname \tBirth Year");
		System.out.println("----------------------------------------------------");
		
		while(rset.next()) {
			System.out.println(rset.getString(1)
					+ "\t" + rset.getString(2)
					+ "\t" + rset.getString(3));
		}
		
		rset.close();
		
		k.close();
	}
	
	//Function 3
	public static void viewVolunteersByBank(Statement stmt) throws SQLException {
		Scanner k = new Scanner(System.in);
		
		System.out.println("Enter the bank number you would like to select: ");
		int bNum = k.nextInt();
		
		ResultSet rset = stmt.executeQuery("SELECT Fname, Lname, VID, Bnum, Numhours, Times_volunteering FROM VOLUNTEER WHERE Bnum = " + bNum
				+ " ORDER BY Bnum");
		
		System.out.println("\nVolunteer Table:");
		System.out.println("----------------------------------------------------");
		System.out.println("Fname \tLname \tVID \tBank \tHours \tVtimes");
		System.out.println("----------------------------------------------------");
		
		while(rset.next()) {
			System.out.println(rset.getString(1)
					+ "\t" + rset.getString(2)
					+ "\t" + rset.getString(3)
					+ "\t" + rset.getString(4)
					+ "\t" + rset.getString(5)
					+ "\t" + rset.getString(6));
		}
		
		rset.close();
		
		k.close();
	}
	
	//Function 4
	public static void setVolunteerHours(Statement stmt) throws SQLException {
		Scanner k = new Scanner(System.in);
		
		System.out.println("Enter the VID of the volunteer you are selecting: ");
		int VID = k.nextInt();
		System.out.println("Enter the new number of hours for this employee: ");
		int hours = k.nextInt();
		
		ResultSet rset = stmt.executeQuery("UPDATE VOLUNTEER SET Numhours = " + hours + " WHERE VID = " + VID);
		
		rset.close();
		
		k.close();
	}
	
	//Function 5
	public static void viewFoodItemsByIdRange(Statement stmt) throws SQLException {
		Scanner k = new Scanner(System.in);
		
		System.out.println("Enter the Product ID range you would like to select: ");
		System.out.println("Low Product ID: ");
		int lowPID = k.nextInt();
		System.out.println("High Product ID: ");
		int highPID = k.nextInt();
		
		ResultSet rset = stmt.executeQuery("SELECT * FROM FOOD_ITEM WHERE ProductID BETWEEN " + lowPID + " AND "
		+ highPID + " ORDER BY ProductID");
		
		System.out.println("\nFood Item Table:");
		System.out.println("----------------------------------------------------");
		System.out.println("Item Name \tPID \tExpDate \t\tBrand");
		System.out.println("----------------------------------------------------");
		
		while(rset.next()) {
			System.out.println(rset.getString(1)
					+ "\t" + rset.getString(2)
					+ "\t" + rset.getString(4)
					+ "\t" + rset.getString(3));
		}
		
		rset.close();
		
		k.close();
	}

}
