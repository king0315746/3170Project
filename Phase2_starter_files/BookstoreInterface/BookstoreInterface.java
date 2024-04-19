import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class BookstoreInterface {

    public static void main(String[] args) {
        System.out.println("<This is the bookstore interface.>");
        System.out.println("-----------------------------------");
        System.out.println("1. Order Update.");
        System.out.println("2. Order Query.");
        System.out.println("3. N most Popular Book Query.");
        System.out.println("4. Back to main menu.");
        System.out.print("\nWhat is your choice??..");

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
                    orderUpdate(connection);
                    break;
                case 2:
                    // Call the customer interface method
                    orderQuery(connection);
                    break;
                case 3:
                    // Call the bookstore interface method
                    nMostPopularBookQuery(connection);
                    break;
                case 4:
                    System.out.println("Exiting the system. Goodbye!");
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

    private static void orderUpdate(Connection connection) throws SQLException, IOException {
        System.out.println("Please enter the order ID: ");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine(); // Consume newline character
        String orderID = scanner.nextLine();
        scanner.close();

        // Read the SQL script file
        String sqlPath = "./orderUpadte.sql";
        String sqlScript = readSqlScript(sqlPath);

        // Replace the placeholder with path
        sqlScript = sqlScript.replace("$ORDER_ID", orderID);

        // Execute the SQL script
        try (Statement statement = connection.createStatement()) {

            statement.execute(sqlScript);

            System.out.print("the Shipping status of orderID is Status and n books ordered\n");
            status = 'N';
            n = 1;
            if(status == 'N' && n < 1){
                System.out.println("status is N and order does not contain at least one book with quantity greater than or equal to 1");
            }
            else if(status == 'N' && n >= 1){
                System.out.println("Are you sure to update the shipping status? (Yes=Y) ");
                Scanner scannerY = new Scanner(System.in);
                scannerY.nextLine(); // Consume newline character
                String Y = scannerY.nextLine();
                scannerY.close();

                if(Y='Y'){
                    //run the sql for update status
                }
                else{
                    //return to bookstore interface
                    break;
                }
            }
            else if(status='Y'){
                System.out.println("Status is Y so no update is allowed.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void orderQuery(Connection connection) throws SQLException, IOException {
        System.out.println("Please input the Month for Order Query (e.g.2005-09): ");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine(); // Consume newline character
        String month = scanner.nextLine();
        scanner.close();

        // Read the SQL script file
        String sqlPath = "./orderQuery.sql";
        String sqlScript = readSqlScript(sqlPath);

        // Replace the placeholder with path
        sqlScript = sqlScript.replace("$MONTH", month);

        // Execute the SQL script
        try (Statement statement = connection.createStatement()) {

            statement.execute(sqlScript);

            //ouput the query result

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void nMostPopularBookQuery(Connection connection) throws SQLException, IOException {
        System.out.println("Please enter the N popular books number: ");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine(); // Consume newline character
        String n = scanner.nextLine();
        scanner.close();
        System.out.print("Processing...");

        // Read the SQL script file
        String sqlPath = "./nMost.sql";
        String sqlScript = readSqlScript(sqlPath);

        // Replace the placeholder with path
        sqlScript = sqlScript.replace("$N", n);

        // Execute the SQL script
        try (Statement statement = connection.createStatement()) {

            statement.execute(sqlScript);

            //output the n most popular books

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}