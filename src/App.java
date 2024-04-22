import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class App {

    public static Date getSystemDate() throws SQLException, IOException, ClassNotFoundException {
        Date system_date = Date.valueOf(LocalDate.of(2019, 01, 01));
        Class.forName("com.mysql.cj.jdbc.Driver");
        String URL = "jdbc:mysql://localhost:3306/project";
        String username = "root";
        String password = "king416";
        String sql = "SELECT (SELECT MAX(order_date) FROM orders) AS max_order_date, s_date FROM s_date;";
        try (
                // Establish connection
                Connection conn = DriverManager.getConnection(URL, username, password);
                // Create statement
                Statement statement = conn.createStatement();
                // Execute query
                ResultSet resultSet = statement.executeQuery(sql);) {
            if (resultSet.next()) {
                // Retrieve values from each row
                system_date = resultSet.getDate("max_order_date");
                system_date = resultSet.getDate("s_date");
            }

            // Close the database connection
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return system_date;
    }

    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {

        Date system_date = getSystemDate();

        System.out.println("The System Date is now: " + system_date);
        System.out.println("<This is the Book Ordering System.>");
        System.out.println("-----------------------------------");
        System.out.println("1. System interface.");
        System.out.println("2. Customer interface.");
        System.out.println("3. Bookstore interface.");
        System.out.println("4. Show System Date.");
        System.out.println("5. Quit the system......");
        System.out.print("\nPlease enter your choice:??..");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input = reader.readLine(); // Read user input as a string
        int choice = Integer.parseInt(input); // Convert input to an integer

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
        reader.close();
    }

    private static void displaySystemDate() throws SQLException, IOException, ClassNotFoundException {
        Date system_date = getSystemDate();
        // Replace with actual system date retrieval logic
        System.out.println("The System Date is now: " + system_date);
        return;
    }
}
