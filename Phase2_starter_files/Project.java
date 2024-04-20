import java.util.Scanner;

import BookstoreInterface.BookstoreInterface;
//import SystemInterface.SystemInterface;
import CustomerInterface.CustomerInterface;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Statement;
import java.util.Date;
import java.sql.ResultSet;

public class Project {

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
        Connection con = connectToMySQL();

        // Process user's choice
        switch (choice) {
            case 1:
                // Call the system interface method
                // SystemInterface.main(args);
                systemInterface(con);
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

    private static void displaySystemDate() {
        // Replace with actual system date retrieval logic
        System.out.println("System Date: 2024-04-17");
        return;
    }

    public static void systemInterface(Connection connection) {
        Boolean loop = true;
        try {
            // Establish database connection
            // Class.forName("oracle.jdbc.OracleDriver");
            Scanner scanner = new Scanner(System.in);
            while (loop) {
                System.out.println("<This is the system interface.>");
                System.out.println("-----------------------------------");
                System.out.println("1. Create Table.");
                System.out.println("2. Delete Table.");
                System.out.println("3. Insert Data.");
                System.out.println("4. Set System Date.");
                System.out.println("5. Back to main menu.");
                System.out.print("\nPlease enter your choice:??..");

                int choice = scanner.nextInt();

                // Process user's choice
                switch (choice) {
                    case 1:
                        // Call the system interface method
                        createTable(connection);
                        break;
                    case 2:
                        // Call the customer interface method
                        deleteTable(connection);
                        break;
                    case 3:
                        // Call the bookstore interface method
                        insertData(connection);
                        break;
                    case 4:
                        // Display system date
                        setSystemDate(connection);
                        break;
                    case 5:
                        // return to home page
                        return;
                    default:
                        System.out.println("Invalid choice. Please select a valid number.");
                }
            }
            scanner.close();
            // Close the database connection
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }

    private static void createTable(Connection connection) throws SQLException, IOException {
        // Read the SQL script file
        String sqlPath = "./create_table.sql";
        String sqlScript = readSqlScript(sqlPath);
        // Execute the SQL script
        try (Statement statement = connection.createStatement()) {
            statement.execute(sqlScript);
            System.out.println("Tables are created!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteTable(Connection connection) throws SQLException, IOException {
        // Read the SQL script file
        String sqlPath = "./delete_table.sql";
        String sqlScript = readSqlScript(sqlPath);
        // Execute the SQL script
        try (Statement statement = connection.createStatement()) {
            statement.execute(sqlScript);
            System.out.println("All tables are deleted!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertData(Connection connection) throws SQLException, IOException {
        System.out.println("Please enter the folder path");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine(); // Consume newline character
        String path = scanner.nextLine();
        scanner.close();
        System.out.print("Processing...");

        // Read the SQL script file
        String sqlPath = "./insert.sql";
        String sqlScript = readSqlScript(sqlPath);

        // Replace the $PATH in sql with content of path
        sqlScript = sqlScript.replace("$PATH", path);

        // Execute the SQL script
        try (Statement statement = connection.createStatement()) {

            statement.execute(sqlScript);

            System.out.print("Data is loaded!\n");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void setSystemDate(Connection connection) throws SQLException, IOException {
        System.out.println("Please Input the date (YYYYMMDD): ");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine(); // Consume newline character
        String date = scanner.nextLine();
        scanner.close();
        System.out.println("Latest date in order: ");

        // Read the SQL script file
        String sqlPath = "./system_date.sql";
        String sqlScript = readSqlScript(sqlPath);

        // Replace the placeholder with path
        sqlScript = sqlScript.replace("$DATE", date);

        // Execute the SQL script
        try (Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlScript);) {

            // Process the result set
            while (resultSet.next()) {
                // Retrieve data from the result set
                Date latestOrderDate = resultSet.getDate(sqlScript);
                Date systemDate = resultSet.getDate(sqlScript);
                // Output the retrieved data
                System.out.println("Latest date in orders: " + latestOrderDate);
                System.out.println("Today is " + systemDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // read sql file
    private static String readSqlScript(String sqlPath) {
        StringBuilder script = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(sqlPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                script.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return script.toString();
    }
}