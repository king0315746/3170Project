package CustomerInterface;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Scanner;

public class CustomerInterface {

    public static void main(String[] args) {
        System.out.println("<This is the customer interface.>");
        System.out.println("-------------------------------------");
        System.out.println("1. Book Search.");
        System.out.println("2. Order Creation.");
        System.out.println("3. Order Altering.");
        System.out.println("4. Order Query.");
        System.out.println("5. Back to main menu.");
        System.out.print("\nPlease enter your choice:??..");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.close();
        try {
            // Establish database connection
            Connection connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@//db18.cse.cuhk.edu.hk:1521/oradb.cse.cuhk.edu.hk",
                    "h007",
                    "Poflobra");
            // Process user's choice
            switch (choice) {
                case 1:
                    // Call the bookSearch
                    bookSearch(connection);
                    break;
                case 2:
                    // Call the order Creation
                    orderCreation(connection);
                    break;
                case 3:
                    // Call the order Altering
                    orderAltering(connection);
                    break;
                case 4:
                    // Call the order Query
                    orderQuery(connection);
                    break;
                case 5:
                    System.out.println("Exiting the system. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid number.");
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }

    private static void bookSearch(Connection connection) throws SQLException, IOException {
        System.out.println("What do u want to search??");
        System.out.println("1 ISBN");
        System.out.println("2 Book Title");
        System.out.println("3 Author Name");
        System.out.println("4 Exit");
        System.out.print("Your choice?...");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.nextLine();
        // Process user's choice
        switch (choice) {
            case 1:
                System.out.println("Input the ISBN:");
                String isbn = scanner.nextLine();
                scanner.close();

                // Read the SQL script file
                String sqlPath = "./query_by_ISBN.sql";
                String sqlScript = readSqlScript(sqlPath);

                // Replace the placeholder with path
                sqlScript = sqlScript.replace("1-1234-1234-1", isbn);

                break;
            case 2:
                // Call the customer interface method
                System.out.println("Input the Book Title:");
                String title = scanner.nextLine();
                scanner.close();

                // Read the SQL script file
                sqlPath = "./query_by_title.sql";
                sqlScript = readSqlScript(sqlPath);

                // Replace the placeholder with path
                sqlScript = sqlScript.replace("Operating%", title);

                break;
            case 3:
                // Call the bookstore interface method
                System.out.println("Input the Author Name:");
                String author = scanner.nextLine();
                scanner.close();

                // Read the SQL script file
                sqlPath = "./query_by_author_name.sql";
                sqlScript = readSqlScript(sqlPath);

                // Replace the placeholder with path
                sqlScript = sqlScript.replace("Ada%", author);

                break;
            case 4:
                System.out.println("Exiting the system. Goodbye!");
                break;
            default:
                System.out.println("Invalid choice. Please select a valid number.");
        }
    }

    private static void orderCreation(Connection connection) throws SQLException, IOException {

        System.out.println("Please enter your customerID??");

        Scanner scanner = new Scanner(System.in);
        String ID = scanner.nextLine();
        scanner.close();

        System.out.println(">> What books do you want to order??");
        System.out.println(">> Input ISBN and then the quantity.");
        System.out.println(">> You can press \"L\" to see ordered list, or \"F\" to finish ordering");

        // input: store the input of the customer
        String input = "0";

        // Enter the loop, until the customer enter F
        while (!input.equals("F")) {

            // Allow the customer to input
            System.out.println("Please enter the book's ISBN: ");
            input = scanner.nextLine();

            if (input.equals("L")) {
                // show the list of the book, ISBN and Number

            } else { // the customer input a ISBN, then allow customer enter book order
                System.out.println("Please enter the quantity of the order: ");
                String quantity = scanner.nextLine();
                scanner.close();
            }
        }
        // until the customer enter F
        System.out.println("Finish Ordering");
    }

    private static void orderAltering(Connection connection) throws SQLException, IOException {

        System.out.println("Please enter the OrderID that you want to change: ");
        // customer enter the id
        Scanner scanner = new Scanner(System.in);
        String orderID = scanner.nextLine();
        // Read the SQL script file
        String sqlPath1 = "./add_to_order.sql";
        String sqlScript1 = readSqlScript(sqlPath1);
        String sqlPath2 = "./delete_from_order.sql";
        String sqlScript2 = readSqlScript(sqlPath2);

        try (Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlScript1);) {

            // Replace the placeholder with path
            sqlScript1 = sqlScript1.replace("00000002", orderID);
            sqlScript1 = sqlScript1.replace("1", "0");
            // Execute the query
            resultSet1 = statement.executeQuery(sqlScript1);
            int orderId = resultSet1.getInt("orderID");
            String shippingStatus = resultSet1.getString("shippingStatus");
            double charge = resultSet1.getDouble("charge");

            // output
            System.out.println("order_id:" + orderID + "  shipping:" + shippingStatus + "  charge=" + charge + " customerID="+ customerID);
            // output list of book
            isbn_list<String> stringList = new ArrayList<>(); //store the isbn of book list
            int cnt_book=1;
            while (resultSet1.next()) {
                String isbn = resultSet1.getString("ISBN");
                int quantity = resultSet1.getInt("quantity");
                isbn_list.add(isbn);
                System.out.println("book no: "+cnt_book+" ISBN:"+isbn+" quantity="+quantity );
                cnt_book+1;
            }

            System.out.println("Which book you want to alter (input book no.):");
            int bookNo = scanner.nextInt(); //decide whcih book
            System.out.println("input add or remove");
            string add_or_remove=scanner.nextLine(); //decide add or remove
            System.out.println("Input the number: ");
            int quantity_change =scanner.nextInt(); //decide the quantity change
            
            If(add_or_remove.equals("add")){
                int no_of_copies=resultSet1.getString("No of Copies Available");
                If(shippingStatus.equals("N") && quantity_change<=no_of_copies){
                    //add success, update the dateset
                    String isbn_chosen=isbn_list.get(bookNo-1); //find out the ISBN of the book chosen
                    sqlScript1 = sqlScript1.replace("00000002", orderID);
                    sqlScript1 = sqlScript1.replace("1-1234-1234-1", isbn_chosen);
                    sqlScript1 = sqlScript1.replace("1", quantity_change);
                    resultSet2 = statement.executeQuery(sqlScript1);
                    //output
                    System.out.println("Update is ok!");
                    System.out.println("update done!!");
                    System.out.println("updated charge");
                }
                else{
                    if (shippingStatusequals("N")){
                        System.out.println("The books in the order are shipped");    
                    }
                    else{
                        System.out.println("There are not enough copies in the book store");
                    }
                }
            }
            else{//remove case
                If (shippingStatus.equals("N")){
                    //remove success
                    String isbn_chosen=isbn_list.get(bookNo-1); //find out the ISBN of the book chosen
                // Set the input parameters
                statement.setInt(1, quantity_change);
                statement.setString(2, orderId);
                statement.setString(3, isbn_chosen);
                resultSet2 = statement.executeQuery(sqlScript2);
                //output
                System.out.println("Update is ok!");
                System.out.println("update done!!");
                System.out.println("updated charge");
                }
                else{
                    System.out.println("The books in the order are shipped");
                }
            }
            //final output
            int orderId = resultSet2.getInt("orderID");
            String shippingStatus = resultSet2.getString("shippingStatus");
            double charge = resultSet2.getDouble("charge");
            // output
            System.out.println("order_id:" + orderID + "  shipping:" + shippingStatus + "  charge=" + charge + " customerID="+ customerID);
            int cnt_book=1;
            while (resultSet1.next()) {
                String isbn = resultSet2.getString("ISBN");
                int quantity = resultSet2.getInt("quantity");
                System.out.println("book no: "+cnt_book+" ISBN:"+isbn+" quantity="+quantity );
                cnt_book+1;
            
            
        }
    }

    private static void orderQuery(Connection connection) throws SQLException, IOException {
        System.out.println("Please Input Customer ID: ");
        Scanner scanner = new Scanner(System.in);
        String customerID = scanner.nextLine();
        System.out.println("Please Input the Year: ");
        int year = scanner.nextInt();
        // Read the SQL script
        String sqlPath = "./order_query.sql";
        String sqlScript = readSqlScript(sqlPath);

        // Replace the placeholder with path
        sqlScript = sqlScript.replace("adafu", customerID);
        sqlScript = sqlScript.replace("2024", year);

        try (Statement statement = connection.createStatement()) {

            // Execute the query
            ResultSet resultSet = statement.executeQuery(sqlScript);

            // count the number of record
            int cnt = 0;

            // Process the query results
            while (resultSet.next()) {
                // Extract the order information from the result set
                int orderId = resultSet.getInt("orderID");
                Date orderDate = resultSet.getDate("orderDate");
                String shippingStatus = resultSet.getString("shippingStatus");
                double charge = resultSet.getDouble("charge");

                // Print the order information
                System.out.println("Record : " + cnt);
                System.out.println("Order ID : " + orderId);
                System.out.println("Order Date: " + orderDate);
                System.out.println("Charge : " + charge);
                System.out.println("Shipping Status: " + shippingStatus);
                System.out.println();
                cnt++;
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
