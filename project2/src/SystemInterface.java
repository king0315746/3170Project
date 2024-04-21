import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Scanner;
import java.sql.SQLException;
import java.io.IOException;
import java.util.Date;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;

public class SystemInterface {
    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        // Establish database connection
        // Class.forName("oracle.jdbc.OracleDriver");
        Boolean loop = true;
        Class.forName("com.mysql.cj.jdbc.Driver");
        String URL = "jdbc:mysql://localhost:3306/project";
        String username = "root";
        String password = "king416";
        Connection conn = DriverManager.getConnection(URL, username, password);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (loop) {
            System.out.println("<This is the system interface.>");
            System.out.println("-----------------------------------");
            System.out.println("1. Create Table.");
            System.out.println("2. Delete Table.");
            System.out.println("3. Insert Data.");
            System.out.println("4. Set System Date.");
            System.out.println("5. Back to main menu.");
            System.out.print("\nPlease enter your choice:??..");

            String input = reader.readLine(); // Read user input as a string
            int choice = Integer.parseInt(input); // Convert input to an integer

            // Process user's choice
            switch (choice) {
                case 1:
                    // Call the system interface method
                    createTable(conn);
                    break;
                case 2:
                    // Call the customer interface method
                    deleteTable(conn);
                    break;
                case 3:
                    // Call the bookstore interface method
                    insertData(conn);
                    break;
                case 4:
                    // Display system date
                    setSystemDate(conn);
                    break;
                case 5:
                    // return to home page
                    return;
                default:
                    System.out.println("Invalid choice. Please select a valid number.");
            }
        }
        reader.close();
        // Close the database connection
        conn.close();
    }

    private static void createTable(Connection connection) throws SQLException, IOException {
        // Read the SQL script file
        String sqlPath = "./src/create_table.sql";
        String sqlScript = readSqlScript(sqlPath);
        String[] statements = sqlScript.split(";");
        // Execute the SQL script
        try (Statement statement = connection.createStatement()) {
            for (String sql : statements) {
                if (sql.trim().length() > 0) {
                    statement.addBatch(sql.trim() + ";"); // Add back the semicolon
                }
            }
            statement.executeBatch();
            System.out.println("Tables are created!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteTable(Connection connection) throws SQLException, IOException {
        // Read the SQL script file
        String sqlPath = "./src/delete_table.sql";
        String sqlScript = readSqlScript(sqlPath);
        String[] statements = sqlScript.split(";");
        // Execute the SQL script
        try (Statement statement = connection.createStatement()) {
            for (String sql : statements) {
                if (sql.trim().length() > 0) {
                    statement.addBatch(sql.trim() + ";"); // Add back the semicolon
                }
            }
            statement.executeBatch();
            System.out.println("All tables are deleted!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertData(Connection connection) throws SQLException, IOException {
        System.out.println("Please enter the folder path:");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String path = reader.readLine().trim(); // ��ȡ��ȥ�����˿ո�

        // Ensure the path ends with a single directory separator that matches the OS
        // standard.
        // if (!path.endsWith(File.separator)) {
        // path += File.separator;
        // }

        System.out.print("Processing...");

        // Read the SQL script file
        String sqlPath = "./src/insert.sql";
        String sqlScript = readSqlScript(sqlPath);
        // Replace the $PATH in sql with the content of path
        // String dataFolderPath = System.getProperty("user.dir").replace('\\', '/') +
        // '/' + "data";
        String dataFolderPath = path;
        System.out.println("PATH is " + dataFolderPath);
        sqlScript = sqlScript.replaceAll("\\$PATH", dataFolderPath);

        System.out.println("Final SQL script to execute: " + sqlScript); // Debugging line

        String[] statements = sqlScript.split(";");
        // Execute the SQL script
        try (Statement statement = connection.createStatement()) {
            for (String sql : statements) {
                if (sql.trim().length() > 0) {
                    statement.addBatch(sql.trim() + ";"); // Add back the semicolon
                }
            }
            statement.executeBatch();

            System.out.print("Data is loaded!\n");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
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
        String sqlPath = "./src/system_date.sql";
        String sqlScript = readSqlScript(sqlPath);

        // Replace the placeholder with path
        sqlScript = sqlScript.replace("2021-01-01", date);
        String[] statements = sqlScript.split(";");
        // Execute the SQL script
        try (Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlScript);) {
            for (String sql : statements) {
                if (sql.trim().length() > 0) {
                    statement.addBatch(sql.trim() + ";"); // Add back the semicolon
                }
            }
            statement.executeBatch();
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
