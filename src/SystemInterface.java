import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.IOException;
import java.sql.Date;
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
                    insertData(conn, reader);
                    break;
                case 4:
                    // Display system date
                    setSystemDate(conn, reader);
                    break;
                case 5:
                    App.main(args);
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

    private static void insertData(Connection connection, BufferedReader reader) throws SQLException, IOException {
        System.out.println("Please enter the folder path:");
        String path = reader.readLine().trim();

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
        // String dataFolderPath = path;
        System.out.println("PATH is " + path);
        sqlScript = sqlScript.replaceAll("\\$PATH", path);

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

    private static void setSystemDate(Connection connection, BufferedReader reader) throws SQLException, IOException {
        System.out.println("Please Input the date (YYYY-MM-DD): ");
        String input = reader.readLine(); // Read user input as a string
        Date inputDate = Date.valueOf(input);

        // get max_order_date and system_date from database
        String sql = "SELECT (SELECT MAX(order_date) FROM orders) AS max_order_date, s_date FROM s_date;";
        Date max_order_date = null;
        Date system_date = null;
        try (
                // Create statement
                Statement statement = connection.createStatement();
                // Execute query
                ResultSet resultSet = statement.executeQuery(sql);) {
            while (resultSet.next()) {
                // Retrieve values from each row
                max_order_date = resultSet.getDate("max_order_date");
                system_date = resultSet.getDate("s_date");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // compare with input date
        if (inputDate.compareTo(max_order_date) > 0) { // inputDate is after max_order_date
            system_date = inputDate;
        } else if (system_date.compareTo(max_order_date) < 0) { // system_date is before max_order_date
            system_date = max_order_date;
        }

        // update system date in datebase
        sql = "UPDATE s_date SET s_date = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, system_date);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // print result
        System.out.println("Latest date in orders: " + max_order_date);
        System.out.println("Today is: " + system_date);

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
