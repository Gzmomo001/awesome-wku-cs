package DatabaseManagement;

import java.util.Scanner;

public class DatabaseMain {
    // main for console test
    public static void main(String... args) {
        int selection;
        int indexPrompt;
        Scanner input = new Scanner(System.in);

        DatabaseManager DBoperator = new DatabaseManager();
        System.out.println("\n=== Database Management Tool Activated ===");

        // Functions(3): Display whole table, Display personal info, Delete personal
        // info, Update personal info
        // Display menu
        showmenu();

        while (true) {
            // Read input
            selection = input.nextInt();

            if (selection == 1) {
                System.out.println(DBoperator.getAllBookings());

                // Request: Continue or exit
                continueRequest();
            }

            if (selection == 2) {
                System.out.println("Please enter the phone of the person you intend to search;");

                String phone = input.next();
                System.out.println(DBoperator.getPersonalBooking(phone));

                continueRequest();
            }

            if (selection == 3) {
                System.out.println("Please enter the database_index of the personal data you intend to delete");

                indexPrompt = input.nextInt();
                DBoperator.deleteValueBooking(indexPrompt);

                continueRequest();
            }

            if (selection == 4) {
                System.out.println("Please enter the index, key and change to the value");
                System.out.println("Eg: 10, price, 1000");

                indexPrompt = input.nextInt();
                String keyPrompt = input.next();
                String changePrompt = input.next();

                DBoperator.updateValueBookings(indexPrompt, keyPrompt, changePrompt);

                continueRequest();
            }

            // Exit if enter 0;
            if (Integer.valueOf(input.next()).equals(0)) {
                System.exit(114514);
            } else {
                showmenu();
            }
        }
    }

    private static void showmenu() {
        System.out.println("1. Display whole table");
        System.out.println("2. Display info of a specific individual sorted by phone number");
        System.out.println("3. Delete info of a specific individual sorted by phone number");
        System.out.println("4. Update a specific value of a specific individual in the table");
        System.out.println("\nChoose the operations by entering the corresponding number: ");
    }

    private static void continueRequest() {
        System.out.println("Input any number for more database operations.");
        System.out.println("Enter 0 to exit.");
    }
}
