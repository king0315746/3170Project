import java.util.Scanner;

public class BookStore {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Display initial menu
        System.out.println("The System Date is now: YYYY-MM-DD");
        System.out.println("<This is the Book Ordering System.>");
        System.out.println("------------------------------------------------------------");
        System.out.println("1. System interface.");
        System.out.println("2. Customer interface.");
        System.out.println("3. Bookstore interface.");
        System.out.println("4. Show System Date.");
        System.out.println("5. Quit the system......");
        System.out.print("\nPlease enter your choice:??..");

        int choice = scanner.nextInt();

        // Process user's choice
        switch (choice) {
            case 1:
                // Call the system interface method
                systemInterface();
                break;
            case 2:
                // Call the customer interface method
                customerInterface();
                break;
            case 3:
                // Call the bookstore interface method
                bookstoreInterface();
                break;
            case 4:
                // Display system date
                displaySystemDate();
                break;
            case 5:
                System.out.println("Exiting the system. Goodbye!");
                break;
            default:
                System.out.println("Invalid choice. Please select a valid number.");
        }

        scanner.close();
    }

    // Placeholder methods for different interfaces
    private static void systemInterface() {
        System.out.println("Welcome to the System Interface!");
        // Add your system-specific logic here
    }

    private static void customerInterface() {
        System.out.println("Welcome to the Customer Interface!");
        // Add your customer-specific logic here
    }

    private static void bookstoreInterface() {
        System.out.println("Welcome to the Bookstore Interface!");
        // Add your bookstore-specific logic here
    }

    private static void displaySystemDate() {
        // Replace with actual system date retrieval logic
        System.out.println("System Date: 2024-04-17");
    }
}

