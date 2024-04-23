import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.util.List;
import java.sql.*;

public class CustomerInterface {
    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\u001B[1m";
    public static final String PINK = "\u001B[35m"; // 实际上通常显示为品红色
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String GREEN = "\u001B[32m";// 看着头晕，加点彩色效果

    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        String URL = "jdbc:mysql://127.0.0.1:3306/my_database";// "mysql --host=projgw --port=2633";//
        String username = "root"; // "csci3170"
        String password = "Cx20030225!";// "testfor3170"
        Connection connection = DriverManager.getConnection(URL, username, password);
        if (connection == null) {
            System.out.println("Database connection failed. Please check your database settings.");
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Boolean running = true;
        while (running) {
            System.out.println(BOLD + YELLOW + "<This is the customer interface.>" + RESET);
            System.out.println(BOLD + YELLOW + "-------------------------------------" + RESET);
            System.out.println(BOLD + YELLOW + "1. Book Search." + RESET);
            System.out.println(BOLD + YELLOW + "2. Order Creation." + RESET);
            System.out.println(BOLD + YELLOW + "3. Order Altering." + RESET);
            System.out.println(BOLD + YELLOW + "4. Order Query." + RESET);
            System.out.println(BOLD + YELLOW + "5. Back to main menu." + RESET);
            System.out.print(BOLD + GREEN + "PLEASE ENTER YOUR CHOICE>> " + RESET);
            String input = reader.readLine(); // Read user input as a string
            int choice = Integer.parseInt(input); // Convert input to an integer
            switch (choice) {
                case 1:
                    // Call the bookSearch
                    bookSearch(connection, reader);
                    break;
                case 2:
                    // Call the order Creation
                    orderCreation(connection, reader);
                    break;
                case 3:
                    // Call the order Altering
                    orderAltering(connection, reader);
                    break;
                case 4:
                    // Call the order Query
                    orderQuery(connection, reader);
                    break;
                case 5:
                    system_interface.main(args);
                    return;
                default:
                    System.out.println("Invalid choice. Please select a valid number.");
            }
        }
        reader.close();
        connection.close();
    }

    private static void bookSearch(Connection connection, BufferedReader reader) throws SQLException, IOException {
        System.out.println("What do u want to search??");
        System.out.println("1 ISBN");
        System.out.println("2 Book Title");
        System.out.println("3 Author Name");
        System.out.println("4 Exit");
        System.out.print("Your choice?...");

        String input = reader.readLine(); // Read user input as a string
        int choice = Integer.parseInt(input); // Convert input to an integer
        String sqlPath, sqlScript, queryParam = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        // Process user's choice
        switch (choice) {
            case 1:
                System.out.println("Input the ISBN:");
                queryParam = reader.readLine();
                sqlPath = "./src/query_by_ISBN.sql";
                sqlScript = readSqlScript(sqlPath);
                stmt = connection.prepareStatement(sqlScript);
                stmt.setString(1, '%' + queryParam + '%'); // Set the ISBN in the query
                rs = stmt.executeQuery();
                break;
            case 2:
                // Call the customer interface method
                System.out.println("Input the Book Title:");
                queryParam = reader.readLine();
                sqlPath = "./src/query_by_title.sql";
                sqlScript = readSqlScript(sqlPath);
                stmt = connection.prepareStatement(sqlScript);
                stmt.setString(1, '%' + queryParam + "%"); // Set the title in the query
                rs = stmt.executeQuery();
                break;
            case 3:
                System.out.println("Input the Author Name:");
                queryParam = reader.readLine();
                sqlPath = "./src/query_by_author_name.sql";
                sqlScript = readSqlScript(sqlPath);
                stmt = connection.prepareStatement(sqlScript);
                stmt.setString(1, '%' + queryParam + "%"); // Set the author name in the query
                rs = stmt.executeQuery();
                break;
            case 4:
                System.out.println("Exiting the system. Goodbye!");
                break;
            default:
                System.out.println("Invalid choice. Please select a valid number.");
        }
        System.out.println("\u001B[1m\u001B[34m Here is the search result \u001B[0m");
        while (rs.next()) {
            // Assuming book details include a title and author, adjust according to your
            // actual table schema
            System.out.println("Title : " + rs.getString("Book Title") + "   ,ISBN : " + rs.getString("ISBN")
                    + ",  UnitPrice :" + rs.getInt("Unit Price") + ",  No_of_copies :"
                    + rs.getInt("No of Copies Available")
                    + ",  AuthorName :" + rs.getString("A List of Authors"));
        }
        rs.close();
        stmt.close();
    }

    private static void orderCreation(Connection connection, BufferedReader reader) throws SQLException, IOException {
        // Need to create the procudure in create_order_first

        System.out.println(BOLD + GREEN + "Please enter your customerID>>" + RESET);
        String customerID = reader.readLine();

        System.out.println(BOLD + YELLOW + ">> What books do you want to order?" + RESET);
        System.out.println(BOLD + YELLOW + ">> Input ISBN and then the quantity." + RESET);
        System.out.println(BOLD + YELLOW
                + ">> Notice: <You can press \"L\" to see ordered list, or \"F\" to finish ordering>" + RESET);
        String input = reader.readLine();

        ArrayList<String> isbn_list = new ArrayList<>();
        ArrayList<Integer> quantity_list = new ArrayList<>();

        while (!input.equals("F")) {
            if (input.equals("L")) {
                System.out.println(BLUE + BOLD + "ISBN          Number:" + RESET);
                for (int i = 0; i < isbn_list.size(); i++) {
                    System.out.println(BLUE + BOLD + isbn_list.get(i) + "   " + quantity_list.get(i) + RESET);
                }
            } else {
                String isbn = input;
                System.out.println(GREEN + BOLD + "Please enter the quantity of the order:" + RESET);
                int quantity = Integer.parseInt(reader.readLine());

                // Create order using stored procedure
                try (CallableStatement cstmt = connection.prepareCall("{CALL create_order(?, ?, ?)}")) {
                    cstmt.setString(1, customerID);
                    cstmt.setString(2, isbn);
                    cstmt.setInt(3, quantity);
                    boolean hasResults = cstmt.execute();

                    if (hasResults) {
                        System.out.println(
                                BLUE + BOLD + "Order created successfully for ISBN: " + isbn + " with quantity: "
                                        + quantity + RESET);
                        isbn_list.add(isbn);
                        quantity_list.add(quantity);
                    } else {
                        System.out.println("Failed to create order. Not enough stock or other error.");
                    }
                }
            }

            System.out.println(GREEN + BOLD + "Please enter the book's ISBN (or 'L' to list, 'F' to finish):" + RESET);
            input = reader.readLine();
        }

        System.out.println(YELLOW + BOLD + "Order Finished" + RESET);
    }

    private static void orderAltering(Connection connection, BufferedReader reader) throws SQLException, IOException {

        System.out.println("Please enter the OrderID that you want to change: ");
        // customer enter the id
        String orderID = reader.readLine();
        orderID = String.format("%08d", Integer.parseInt(orderID));
        // displayOrder(connection, orderID);
        // Read the SQL script file
        String sqlPath0 = "./src/view_order.sql";
        String sqlScript0 = readSqlScript(sqlPath0);
        String sqlPath1 = "./src/add_to_order.sql";
        String sqlScript1 = readSqlScript(sqlPath1);

        try (PreparedStatement preparestatement = connection.prepareStatement(sqlScript0,
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);)
        // 创建一个可滚动的statement
        {
            // output list of book
            preparestatement.setString(1, '%' + orderID);
            ResultSet resultSet = preparestatement.executeQuery();
            List<String> isbn_list = new ArrayList<>(); // store the isbn of book list
            int cnt_book = 1;
            while (resultSet.next()) {
                String isbn = resultSet.getString("ISBN");
                // System.out.printf("briefing ISBN %s\n", resultSet.getString("ISBN"));
                int quantity = resultSet.getInt("quantity");
                String shipping_status = resultSet.getString("Shipping Status");
                isbn_list.add(isbn);
                System.out.printf(
                        BLUE + BOLD
                                + "book no: %d , orderID: %s, Shipping Status:%s , charge:%d , ISBN: %s , quantity = %d\n"
                                + RESET,
                        cnt_book,
                        resultSet.getString("order_id"),
                        shipping_status,
                        resultSet.getInt("Charge"),
                        isbn, quantity);
                cnt_book++;
            }

            System.out.println("Which book you want to alter (input book no.):");
            int bookNo = Integer.parseInt(reader.readLine()); // decide whcih book
            String selectedIsbn = isbn_list.get(bookNo - 1);
            System.out.printf("Selected ISBN:%s\n", selectedIsbn);
            System.out.println("input add or remove");
            String add_or_remove = reader.readLine(); // decide add or remove
            System.out.println("Input the number: ");
            int quantity_change = Integer.parseInt(reader.readLine()); // decide the quantity change

            if (add_or_remove.equals("add")) {
                String shippingStatus = null;
                int no_of_copies = 0;
                resultSet.beforeFirst(); // 将指针移回第一行之前
                while (resultSet.next()) {
                    String isbn = resultSet.getString("ISBN");
                    // System.out.printf("briefing ISBN %s\n", resultSet.getString("ISBN"));
                    if (isbn.equals(selectedIsbn)) {
                        shippingStatus = resultSet.getString("Shipping Status");
                        no_of_copies = resultSet.getInt("No of Copies Available");
                        // System.out.printf("Hurray, we get it from %s :%s, %d\n", isbn,
                        // shippingStatus, no_of_copies);
                        break; // 找到匹配的ISBN后，退出循环
                    }
                }
                if (shippingStatus.equals("N") && quantity_change <= no_of_copies) {
                    // add success, update the dateset
                    // 设置用户变量
                    String setOrderId = "SET @v_order_id = ?";
                    String setInputIsbn = "SET @input_ISBN = ?";
                    String setInputCopies = "SET @input_copies = ?";
                    try (PreparedStatement ps1 = connection.prepareStatement(setOrderId);
                            PreparedStatement ps2 = connection.prepareStatement(setInputIsbn);
                            PreparedStatement ps3 = connection.prepareStatement(setInputCopies)) {
                        ps1.setString(1, orderID.toString());
                        ps1.executeUpdate();

                        ps2.setString(1, selectedIsbn.toString());
                        ps2.executeUpdate();

                        ps3.setInt(1, quantity_change);
                        ps3.executeUpdate();
                    }
                    sqlScript1 = sqlScript1.replace("00000002", orderID.toString());
                    sqlScript1 = sqlScript1.replace("1-1234-1234-1", selectedIsbn.toString());
                    sqlScript1 = sqlScript1.replace("1", String.valueOf(quantity_change));
                    preparestatement.executeUpdate(sqlScript1);

                    PreparedStatement preparestatement2 = connection.prepareStatement(sqlScript0,
                            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    preparestatement2.setString(1, '%' + orderID);
                    ResultSet resultSet2 = preparestatement2.executeQuery();

                    int cnt_book_1 = 1;
                    while (resultSet2.next()) {
                        String isbn = resultSet2.getString("ISBN");
                        // System.out.printf("briefing ISBN %s\n", resultSet.getString("ISBN"));
                        int quantity = resultSet2.getInt("quantity");
                        String shipping_status = resultSet2.getString("Shipping Status");
                        isbn_list.add(isbn);
                        System.out.printf(
                                BLUE + BOLD
                                        + "book no: %d , orderID: %s, Shipping Status:%s , charge:%d , ISBN: %s , quantity = %d\n"
                                        + RESET,
                                cnt_book_1,
                                resultSet2.getString("order_id"),
                                shipping_status,
                                resultSet2.getInt("Charge"),
                                isbn, quantity);
                        cnt_book_1++;
                    }
                    // output
                    System.out.println("Update is ok!");
                    System.out.println("update done!!");
                    System.out.println("updated charge");
                } else {
                    if (shippingStatus.equals("Y")) {
                        System.out.println("The books in the order are shipped");
                    } else {
                        System.out.println("There are not enough copies in the book store");
                    }
                }
            } else {// remove case
                String shippingStatus = null;
                resultSet.beforeFirst(); // 将指针移回第一行之前
                while (resultSet.next()) {
                    String isbn = resultSet.getString("ISBN");
                    // System.out.printf("briefing ISBN %s\n", resultSet.getString("ISBN"));
                    if (isbn.equals(selectedIsbn)) {
                        shippingStatus = resultSet.getString("Shipping Status");
                        // System.out.printf("Hurray, we get it from %s :%s, %d\n", isbn,
                        // shippingStatus, no_of_copies);
                        break; // 找到匹配的ISBN后，退出循环
                    }
                }
                if (shippingStatus.equals("N")) {
                    // remove success

                    // Prepare the SQL statement for calling the stored procedure
                    String callProcedureSQL = "{CALL remove_book(?, ?, ?)}";
                    try (CallableStatement cstmt = connection.prepareCall(callProcedureSQL)) {
                        // Set the input parameters for the stored procedure
                        cstmt.setInt(1, quantity_change);
                        cstmt.setString(2, orderID);
                        cstmt.setString(3, selectedIsbn);

                        // Execute the stored procedure
                        boolean hasResults = cstmt.execute();

                        // Process the results
                        if (hasResults) {
                            try (ResultSet rs = cstmt.getResultSet()) {
                                if (rs.next()) {
                                    String message = rs.getString(1);
                                    System.out.println(message); // Printing success or error message
                                }
                            }

                            // Fetching and displaying additional order details
                            if (cstmt.getMoreResults()) {
                                try (ResultSet rs = cstmt.getResultSet()) {
                                    while (rs.next()) {
                                        String order_id = rs.getString("order_id");
                                        String shipping_status = rs.getString("shipping_status");
                                        Date order_date = rs.getDate("order_date");
                                        int quantity_left = rs.getInt("quantity left");
                                        System.out.printf(
                                                "orderid: %s, shipping: %s, order_date: %s, Quantity_left: %d\n",
                                                order_id, shipping_status, order_date.toString(), quantity_left);
                                    }
                                }
                            }
                        }

                        System.out.println("Procedure executed successfully.");
                        PreparedStatement preparestatement2 = connection.prepareStatement(sqlScript0,
                                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        preparestatement2.setString(1, '%' + orderID);
                        ResultSet resultSet2 = preparestatement2.executeQuery();

                        int cnt_book_1 = 1;
                        while (resultSet2.next()) {
                            String isbn = resultSet2.getString("ISBN");
                            // System.out.printf("briefing ISBN %s\n", resultSet.getString("ISBN"));
                            int quantity = resultSet2.getInt("quantity");
                            String shipping_status = resultSet2.getString("Shipping Status");
                            isbn_list.add(isbn);
                            System.out.printf(
                                    BLUE + BOLD
                                            + "book no: %d , orderID: %s, Shipping Status:%s , charge:%d , ISBN: %s , quantity = %d\n"
                                            + RESET,
                                    cnt_book_1,
                                    resultSet2.getString("order_id"),
                                    shipping_status,
                                    resultSet2.getInt("Charge"),
                                    isbn, quantity);
                            cnt_book_1++;
                        }
                    } catch (SQLException e) {
                        System.out.println("SQL Error: " + e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("The books in the order are shipped");
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void orderQuery(Connection connection, BufferedReader reader) throws SQLException, IOException {

        System.out.println("Please Input Customer ID: ");
        String customerID = reader.readLine();
        System.out.println("Please Input the Year: ");
        int year = Integer.parseInt(reader.readLine());

        // Need to execute set first
        try (Statement statement = connection.createStatement()) {
            statement.execute("SET @customer_id = '" + customerID + "'");
            statement.execute("SET @year = " + year);
        }
        // Read the SQL script
        String sqlPath = "./src/order_query.sql";
        String sqlScript = readSqlScript(sqlPath);
        try (Statement statement = connection.createStatement()) {
            // Execute the query
            ResultSet resultSet = statement.executeQuery(sqlScript);
            // count the number of record
            int cnt = 0;
            // Process the query results
            while (resultSet.next()) {
                // Extract the order information from the result set
                int orderId = resultSet.getInt("Order ID");
                Date orderDate = resultSet.getDate("Order Date");
                String shippingStatus = resultSet.getString("Shipping Status");
                int charge = resultSet.getInt("Charge");

                // Print the order information
                System.out.print(BLUE + "Record: " + cnt +
                        ", Order ID: " + orderId +
                        ", Order Date: " + orderDate +
                        ", Charge: " + charge +
                        ", Shipping Status: " + shippingStatus + RESET + "\n");
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
