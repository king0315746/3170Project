import java.util.Scanner;
import SystemInterface.SystemInterface;
import CustomerInterface.CustomerInterface;
//import BookstoreInterface.BookstoreInterface;

public class Project {
    public static void main(String[] args) {

        System.out.println("The System Date is now: YYYY-MM-DD");
        System.out.println("<This is the Book Ordering System.>");
        System.out.println("-----------------------------------");
        System.out.println("1. System interface.");
        System.out.println("2. Customer interface.");
        System.out.println("3. Bookstore interface.");
        System.out.println("4. Show System Date.");
        System.out.println("5. Quit the system......");
        System.out.print("\nPlease enter your choice:??..");
        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        if (scanner.hasNext()) {
            choice = scanner.nextInt();
        }

        // Process user's choice
        switch (choice) {
            case 1:
                // Call the system interface method
                SystemInterface.main(args);
                break;
            case 2:
                // Call the customer interface method
                CustomerInterface.main(args);
                break;
            case 3:
                // Call the bookstore interface method
                // BookstoreInterface.main(args);
                break;
            case 4:
                // Display system date
                displaySystemDate();
                main(args);
                break;
            case 5:
                System.out.println("Exiting the system. Goodbye!");
                break;
            default:
                System.out.println("Invalid choice. Please select a valid number.");
        }
        scanner.close();
    }

    private static void displaySystemDate() {
        // Replace with actual system date retrieval logic
        System.out.println("System Date: 2024-04-17");
        return;
    }
}
