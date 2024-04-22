import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Date;

public class BookstoreInterface {

    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        // Establish database connection
        Boolean loop = true;
        Class.forName("com.mysql.cj.jdbc.Driver");
        String URL = "jdbc:mysql://localhost:3306/project";
        String username = "root";
        String password = "king416";
        try {
            Connection conn = DriverManager.getConnection(URL, username, password);
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (loop) {
                System.out.println("<This is the bookstore interface.>");
                System.out.println("-----------------------------------");
                System.out.println("1. Order Update.");
                System.out.println("2. Order Query.");
                System.out.println("3. N most Popular Book Query.");
                System.out.println("4. Back to main menu.");
                System.out.print("\nWhat is your choice??..");

                String input = reader.readLine(); // Read user input as a string
                int choice = Integer.parseInt(input); // Convert input to an integer

                // Process user's choice
                switch (choice) {
                    case 1:
                        // Call the system interface method
                        orderUpdate(conn, reader);
                        break;
                    case 2:
                        // Call the customer interface method
                        orderQuery(conn, reader);
                        break;
                    case 3:
                        // Call the bookstore interface method
                        nMostPopularBookQuery(conn, reader);
                        break;
                    case 4:
                        System.out.println("Exiting the system. Goodbye!");
                        // return to home page
                        break;
                    default:
                        System.out.println("Invalid choice. Please select a valid number.");
                }
            }
            reader.close();
            // Close the database connection
            conn.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void orderUpdate(Connection connection, BufferedReader reader) throws SQLException, IOException {
        System.out.println("Please enter the order ID: ");
        String orderID = reader.readLine(); // Read user input as a string
        // int orderID = Integer.parseInt(input); // Convert input to an integer

        // Read the SQL script file
        String sqlPath = "./src/order_update1.sql";
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
                status = resultSet.getString("shipping_status");
                n = resultSet.getInt("quantity");
                // Output the retrieved data
                System.out.println(
                        "the Shipping status of " + orderID + " is " + status + " and " + n + " books ordered");
            }

            if (status.equals("N") && n < 1) {
                System.out.println(
                        "status is N and order does not contain at least one book with quantity greater than or equal to 1");
            } else if (status.equals("N") && n >= 1) {
                System.out.println("Are you sure to update the shipping status? (Yes=Y) ");
                String Y = reader.readLine();

                if (Y.equals("Y")) {
                    // run the sql for update status
                    sqlPath = "./src/order_update2.sql";
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
            } else if (status.equals("Y")) {
                System.out.println("Status is Y so no update is allowed.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void orderQuery(Connection connection, BufferedReader reader) throws SQLException, IOException {
        System.out.println("Please input the Month for Order Query (e.g.2005-09): ");
        String month = reader.readLine(); // Read user input as a string

        // Read the SQL script file
        String sqlPath = "./src/book_store_order_query.sql";
        String sqlScript = readSqlScript(sqlPath);

        // Replace the placeholder with path
        sqlScript = sqlScript.replaceAll("\\$MONTH", month);

        // Execute the SQL script
        try (Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlScript);) {
            int totalCharge = 0;
            while (resultSet.next()) {
                // Retrieve data from the result set
                int cnt = 0;
                String order_id = resultSet.getString("order_id");
                String customer_id = resultSet.getString("customer_id");
                Date order_date = resultSet.getDate("order_date");
                int charge = resultSet.getInt("charge");

                // Output the retrieved data
                System.out.println("Record : " + cnt + 1);
                System.out.println("orderID : " + order_id);
                System.out.println("customerID : " + customer_id);
                System.out.println("date : " + order_date);
                System.out.println("charge : " + charge);
                System.out.println();
                cnt++;
                totalCharge += charge;
            }
            System.out.println("Total charges of  the month is " + totalCharge);
            System.out.println();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void nMostPopularBookQuery(Connection connection, BufferedReader reader)
            throws SQLException, IOException {
        System.out.println("Please enter the N popular books number: ");
        // Scanner scanner = new Scanner(System.in);
        // int n = scanner.nextInt();
        String input = reader.readLine(); // Read user input as a string
        int n = Integer.parseInt(input);

        // Read the SQL script file
        String sqlPath = "./src/most_popular.sql";
        String sqlScript = readSqlScript(sqlPath);

        // Replace the placeholder with path
        sqlScript = sqlScript.replace("$N", String.valueOf(n));

        // Execute the SQL script
        try (Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlScript);) {
            System.out.println("ISBN        Title       copies");
            while (resultSet.next()) {
                // Retrieve data from the result set
                String isbn = resultSet.getString("ISBN");
                String title = resultSet.getString("Book_Title");
                int copies = resultSet.getInt("Total_Ordered_Copies");

                // Output the retrieved data
                System.out.println(isbn + "  " + title + "  " + copies);

            }
            System.out.println();
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