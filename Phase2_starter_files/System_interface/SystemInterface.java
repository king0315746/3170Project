import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class SystemInterface {

    public static void main(String[] args) {
        System.out.println("<This is the system interface.>");
        System.out.println("-----------------------------------");
        System.out.println("1. Create Table.");
        System.out.println("2. Delete Table.");
        System.out.println("3. Insert Data.");
        System.out.println("4. Set System Date.");
        System.out.println("5. Back to main menu.");
        System.out.print("\nPlease enter your choice:??..");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        try {
            // Establish database connection
            Connection connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@//db18.cse.cuhk.edu.hk:1521/oradb.cse.cuhk.edu.hk",
                    "h007",
                    "Poflobra");
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
                    //return to home page
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid number.");
            }

            // Close the database connection
            connection.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        scanner.close();
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

        // Replace the placeholder with path
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
        try (Statement statement = connection.createStatement()) {

            statement.execute(sqlScript);

            System.out.print("Today is date\n");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
