package BookstoreInterface;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Date;
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
                    // return to home page
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
        String sqlPath = "./order_upadte1.sql";
        String sqlScript = readSqlScript(sqlPath);

        // Replace the placeholder with path
        sqlScript = sqlScript.replace("$ORDER_ID", orderID);

        // Execute the SQL script
        try (Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlScript);) {
            String status = "X";
            int n = -1;
            while (resultSet.next()) {
                // Retrieve data from the result set
                status = resultSet.getString("status");
                n = resultSet.getInt("n");
                // Output the retrieved data
                System.out.println(
                        "the Shipping status of " + orderID + " is " + status + " and " + n + " books ordered");
            }

            if (status == "N" && n < 1) {
                System.out.println(
                        "status is N and order does not contain at least one book with quantity greater than or equal to 1");
            } else if (status == "N" && n >= 1) {
                System.out.println("Are you sure to update the shipping status? (Yes=Y) ");
                Scanner scannerY = new Scanner(System.in);
                scannerY.nextLine(); // Consume newline character
                String Y = scannerY.nextLine();
                scannerY.close();

                if (Y == "Y") {
                    // run the sql for update status
                    sqlPath = "./order_update2.sql";
                    sqlScript = readSqlScript(sqlPath);

                    // Replace the placeholder with path
                    sqlScript = sqlScript.replace("$ORDER_ID", orderID);

                    // Execute the SQL script
                    try (Statement statement1 = connection.createStatement()) {

                        statement1.execute(sqlScript);

                        // ouput the query result

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    // return to bookstore interface
                    return;
                }
            } else if (status == "Y") {
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
        String sqlPath = "./book_store_order_query1.sql";
        String sqlScript = readSqlScript(sqlPath);

        // Replace the placeholder with path
        sqlScript = sqlScript.replace("$MONTH", month);

        // Execute the SQL script
        try (Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlScript);) {
            int totalCharge = 0;
            while (resultSet.next()) {
                // Retrieve data from the result set
                int cnt = 0;
                String orderID = resultSet.getString("orderID");
                String customerID = resultSet.getString("customerID");
                Date orderDate = resultSet.getDate("end_date");
                int charge = resultSet.getInt("charge");

                // Output the retrieved data
                System.out.println("Record : " + cnt + 1);
                System.out.println("orderID : " + orderID);
                System.out.println("customerID : " + customerID);
                System.out.println("date : " + orderDate);
                System.out.println("charge : " + charge);
                System.out.println();
                cnt++;
                totalCharge += charge;
            }
            System.out.println("Total charges of  the month is " + totalCharge);

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

        // Read the SQL script file
        String sqlPath = "./most_popular.sql";
        String sqlScript = readSqlScript(sqlPath);

        // Replace the placeholder with path
        sqlScript = sqlScript.replace("$N", n);

        // Execute the SQL script
        try (Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlScript);) {
            System.out.println("ISBN        Title       copies");
            while (resultSet.next()) {
                // Retrieve data from the result set
                String isbn = resultSet.getString("ISBN");
                String title = resultSet.getString("Title");
                int copies = resultSet.getInt("copies");

                // Output the retrieved data
                System.out.println(isbn + "  " + title + "  " + copies);
            }
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