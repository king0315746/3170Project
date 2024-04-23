import java.sql.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.File;
import java.sql.CallableStatement;

public class system_interface {
    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        // Establish database connection
        // Class.forName("oracle.jdbc.OracleDriver");
        Boolean loop = true;
        Class.forName("com.mysql.cj.jdbc.Driver");
        String URL = "jdbc:mysql://127.0.0.1:3306/my_database";// "mysql --host=projgw --port=2633";//
        String username = "root"; // "csci3170"
        String password = "Cx20030225!";// "testfor3170"
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
        if (!path.endsWith(File.separator)) {
            path += File.separator;
        }

        System.out.print("Processing...");

        // Read the SQL script file
        String sqlPath = "./src/insert.sql";
        String sqlScript = readSqlScript(sqlPath);
        // Replace the $PATH in sql with the content of path
        String dataFolderPath = System.getProperty("user.dir").replace('\\', '/') + '/' + "data";
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
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String inputDate = reader.readLine();

        // Convert input date to SQL format (YYYY-MM-DD)
        String formattedDate = inputDate.substring(0, 4) + "-" + inputDate.substring(4, 6) + "-"
                + inputDate.substring(6, 8);

        // Prepare the call to the stored procedure
        String procedureCall = "{call set_systemDate(?)}"; // Assuming the stored procedure is named 'setSystemDate' and
                                                           // accepts one date parameter

        try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
            callableStatement.setString(1, formattedDate); // Set the date parameter
            callableStatement.execute(); // Execute the stored procedure

            System.out.println("System date set to: " + formattedDate);
        } catch (SQLException e) {
            System.err.println("SQL error occurred:");
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