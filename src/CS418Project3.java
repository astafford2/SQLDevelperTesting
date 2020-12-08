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
		System.out.println("4 - View Volunteer Count & Average Hours by Bank Range");
		System.out.println("5 - View Volunteer Stocking Assignments");
		System.out.println("6 - Set Volunteer Weekly Hours");
		System.out.println("7 - View Food Items by Product ID");
		System.out.println("8 - View Food Items Expiration Date");
		System.out.println("9 - Set Stocking Quantity");
		System.out.println("10 - Set Bank Hours");
		
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
				viewVCountAvgHoursByBankRange(stmt);
				break;
			case 5:
				viewVolunteerStockingAssignments(stmt);
				break;
			case 6:
				setVolunteerHours(stmt);
				break;
			case 7:
				viewFoodItemsByIdRange(stmt);
				break;
			case 8:
				viewFoodItemExpDate(stmt);
				break;
			case 9:
				setStocksQuantity(stmt);
				break;
			case 10:
				setBankHours(stmt);
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
		System.out.print("Low VID: ");
		int lowVID = k.nextInt();
		System.out.print("High VID: ");
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
		
		System.out.print("Enter the birth year you would like to select (spelled out, capitalized): ");
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
		
		System.out.print("Enter the bank number you would like to select: ");
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
	public static void viewVCountAvgHoursByBankRange(Statement stmt) throws SQLException {
		Scanner k = new Scanner(System.in);
		
		System.out.println("Enter the Bank Number range you would like to select: ");
		System.out.print("Low Bank Number: ");
		int lowBnum = k.nextInt();
		System.out.print("High Bank Number: ");
		int highBnum = k.nextInt();
		
		ResultSet rset = stmt.executeQuery("SELECT Bnum, COUNT(*), AVG(Numhours) FROM VOLUNTEER WHERE Bnum BETWEEN " + lowBnum
				+ " AND " + highBnum + " GROUP BY Bnum ORDER BY Bnum");
		
		System.out.println("\nVolunteer Count and Average Hours Table:");
		System.out.println("------------------------------------------");
		System.out.println("Bnum \tVcount \tAvg Hours");
		System.out.println("------------------------------------------");
		
		while(rset.next()) {
			System.out.println(rset.getString(1)
					+ "\t" + rset.getString(2)
					+ "\t" + rset.getString(3));
		}
		
		rset.close();
		
		k.close();
	}
	
	//Function 5
	public static void viewVolunteerStockingAssignments(Statement stmt) throws SQLException {
		Scanner k = new Scanner(System.in);
		
		System.out.print("Enter VID you would like to view assignments for: ");
		int VID = k.nextInt();
		
		ResultSet rset = stmt.executeQuery("SELECT V.Fname, V.Lname, V.VID, F.ProductID, F.Item_name, F.Item_brand, S.Quantity "
				+ "FROM VOLUNTEER V, FOOD_ITEM F, STOCKS S "
				+ "WHERE V.VID = " + VID + " AND S.VID = V.VID AND F.ProductID = S.FproductID");
		
		System.out.println("\nVolunteer Stocking Assignments");
		System.out.println("------------------------------------------------------------------------");
		System.out.println("Fname \tLname \tVID \tPID \tItem name \tItem brand \tQuantity");
		System.out.println("------------------------------------------------------------------------");
		
		while(rset.next()) {
			System.out.println(rset.getString(1)
					+ "\t" + rset.getString(2)
					+ "\t" + rset.getString(3)
					+ "\t" + rset.getString(4)
					+ "\t" + rset.getString(5)
					+ "\t" + rset.getString(6)
					+ "\t\t" + rset.getString(7));
		}
		
		rset.close();
		
		k.close();
	}
	
	//Function 6
	public static void setVolunteerHours(Statement stmt) throws SQLException {
		Scanner k = new Scanner(System.in);
		
		System.out.print("Enter the VID of the volunteer you are selecting: ");
		int VID = k.nextInt();
		System.out.print("Enter the new number of hours for this employee: ");
		int hours = k.nextInt();
		
		ResultSet rset = stmt.executeQuery("UPDATE VOLUNTEER SET Numhours = " + hours + " WHERE VID = " + VID);
		
		System.out.println("Volunteer hours set successfully");
		
		rset.close();
		
		k.close();
	}
	
	//Function 7
	public static void viewFoodItemsByIdRange(Statement stmt) throws SQLException {
		Scanner k = new Scanner(System.in);
		
		System.out.println("Enter the Product ID range you would like to select: ");
		System.out.print("Low Product ID: ");
		int lowPID = k.nextInt();
		System.out.print("High Product ID: ");
		int highPID = k.nextInt();
		
		ResultSet rset = stmt.executeQuery("SELECT * FROM FOOD_ITEM WHERE ProductID BETWEEN " + lowPID + " AND "
		+ highPID + " ORDER BY ProductID");
		
		System.out.println("\nFood Item Table:");
		System.out.println("----------------------------------------------------");
		System.out.println("Item Name \tPID \tBrand");
		System.out.println("----------------------------------------------------");
		
		while(rset.next()) {
			System.out.println(rset.getString(1)
					+ "\t" + rset.getString(2)
					+ "\t" + rset.getString(3));
		}
		
		rset.close();
		
		k.close();
	}
	
	//Function 8
	public static void viewFoodItemExpDate(Statement stmt) throws SQLException {
		Scanner k = new Scanner(System.in);
		
		int lowPID;
		int highPID;
		
		System.out.print("Enter the Product ID range you would like to select: ");
		System.out.print("Low Product ID (* for all): ");
		String sLowPID = k.nextLine();
		if(!sLowPID.equals("*")) { 
			lowPID = Integer.parseInt(sLowPID);
			
			System.out.print("High Product ID: ");
			highPID = k.nextInt();
		}
		else {
			lowPID = 0;
			highPID = 100000000;
		}
		
		ResultSet rset = stmt.executeQuery("SELECT Item_name, ProductID, Exp_date FROM FOOD_ITEM WHERE ProductID BETWEEN " + lowPID + " AND "
		+ highPID + " ORDER BY ProductID");
		
		System.out.println("\nFood Item Table:");
		System.out.println("----------------------------------------------------");
		System.out.println("Item Name \tPID \tExpDate");
		System.out.println("----------------------------------------------------");
		
		while(rset.next()) {
			System.out.println(rset.getString(1)
					+ "\t" + rset.getString(2)
					+ "\t" + rset.getString(3));
		}
		
		rset.close();
		
		k.close();
	}
	
	//Function 9
	public static void setStocksQuantity(Statement stmt) throws SQLException {
		Scanner k = new Scanner(System.in);
		
		System.out.print("Enter the VID of the volunteer stocking: ");
		int VID = k.nextInt();
		System.out.print("Enter the product ID of the item being stocked: ");
		int PID = k.nextInt();
		System.out.print("Enter the quantity being stocked: ");
		int qty = k.nextInt();
		
		ResultSet rset = stmt.executeQuery("UPDATE STOCKS SET Quantity = " + qty + " WHERE VID = " + VID + " AND FproductID = " + PID);
		
		System.out.println("Stocking quantity set successfully");
		
		rset.close();
		
		k.close();
	}
	
	//Function 10
		public static void setBankHours(Statement stmt) throws SQLException {
			Scanner k = new Scanner(System.in);
			
			System.out.print("Enter the Bank Number you would like to select: ");
			int Bnumber = k.nextInt();
			System.out.print("Enter the new hours for the bank: ");
			String hours = k.next();
			
			ResultSet rset = stmt.executeQuery("UPDATE BANK SET Hours_open = \'" + hours + "\' WHERE Bnumber = " + Bnumber);
			
			System.out.println("New bank hours set successfully");
			
			rset.close();
			
			k.close();
		}

}
