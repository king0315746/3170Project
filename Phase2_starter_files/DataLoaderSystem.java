
import java.sql.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.io.InputStreamReader;

public class DataLoaderSystem {

    public static void main(String[] args) {
        try {
            // Establish database connection
            Connection connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@//db18.cse.cuhk.edu.hk:1521/oradb.cse.cuhk.edu.hk", // Replace with your database
                                                                                           // URL
                    "h007", // Replace with your database username
                    "Poflobra" // Replace with your database password
            );

            // Prompt user for data folder path
            System.out.print("Enter the path of the folder containing data files: ");
            // String folderPath = new BufferedReader(new
            // InputStreamReader(System.in)).readLine();

            // Read each data file and load into the Book table
            // loadBooks(connection, folderPath);

            // Close the database connection
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /*
     * private static void loadBooks(Connection connection, String folderPath)
     * throws SQLException, IOException {
     * // Prepare SQL statement for inserting data
     * String insertQuery =
     * "INSERT INTO Book (ISBN, Title, unitprice, No_of_copies) VALUES (?, ?, ?, ?)"
     * ;
     * PreparedStatement preparedStatement =
     * connection.prepareStatement(insertQuery);
     * 
     * // Read data files and load into the table
     * try (BufferedReader reader = new BufferedReader(new FileReader(folderPath +
     * "/book.txt"))) {
     * String line;
     * while ((line = reader.readLine()) != null) {
     * String[] parts = line.split("\\|");
     * if (parts.length == 4) {
     * preparedStatement.setString(1, parts[0]);
     * preparedStatement.setString(2, parts[1]);
     * preparedStatement.setInt(3, Integer.parseInt(parts[2]));
     * preparedStatement.setInt(4, Integer.parseInt(parts[3]));
     * preparedStatement.executeUpdate();
     * }
     * }
     * }
     * System.out.println("Data loaded successfully!");
     * }
     */
}
