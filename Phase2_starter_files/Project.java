import java.util.Scanner;

import BookstoreInterface.BookstoreInterface;
import SystemInterface.SystemInterface;
import CustomerInterface.CustomerInterface;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Project {

    public static void main(String[] args) {

        System.out.println("The System Date is now: YYYY-MM-DD");
        System.out.println("<This is the Book Ordering System.>");
        System.out.println("-----------------------------------");
        System.out.println("1. System interface.");
        System.out.println("2. Customer interface.");
        System.out.println("3. Bookstore interface.");
        System.out.println("4. Show System Date.");
        System.out.println("5. Quit the system......");
        System.out.print("\nPlease enter your choice:??..");
        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        if (scanner.hasNext()) {
            choice = scanner.nextInt();
        }

        // Process user's choice
        switch (choice) {
            case 1:
                // Call the system interface method
                SystemInterface.main(args);
                break;
            case 2:
                // Call the customer interface method
                CustomerInterface.main(args);
                break;
            case 3:
                // Call the bookstore interface method
                BookstoreInterface.main(args);
                break;
            case 4:
                // Display system date
                displaySystemDate();
                main(args);
                break;
            case 5:
                System.out.println("Exiting the system. Goodbye!");
                break;
            default:
                System.out.println("Invalid choice. Please select a valid number.");
        }
        scanner.close();
    }

    public static String dbAddress = "jdbc:oracle:thin://@db18.cse.cuhk.edu.hk:1521/oradb.cse.cuhk.edu.hk";
    public static String dbUsername = "h007";
    public static String dbPassword = "Poflobra";

    public static Connection connectToMySQL() {
        Connection con = null;
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
        } catch (ClassNotFoundException e) {
            System.out.println("[Error]: Java MySQL DB Driver not found!!");
            System.exit(0);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return con;
    }

    private static void displaySystemDate() {
        String query = "SELECT SYSDATE FROM dual";
        try (Connection connection = connectToMySQL();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);) {

            if (resultSet.next()) {
                // Retrieve the date from the result set
                java.sql.Date date = resultSet.getDate(1);

                // Output the date
                System.out.println("Current date: " + date);
            }
            System.out.println("All tables are deleted!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Replace with actual system date retrieval logic
        System.out.println("System Date: 2024-04-17");
        return;
    }

}
