package src;
import java.util.*;
import src.Bookstore.Bookstore;

public class Main {

    public static void main(String[] args) {
        
        // Main Menu
        Scanner kb = new Scanner(System.in);
        Bookstore bs = new Bookstore();
        
        String currentUsername; // Username of the user currently logged in
        Boolean loggedin = false;

        while(true){
            main_menu: while(true)
            {
                System.out.println("Main Menu");
                System.out.println("1. Login");
                System.out.println("2. Display Books Available");
                System.out.println("3. Quit");

                System.out.println();
                
                switch (kb.nextLine()) {
                    case "1":
                        while(true){
                            // Ask user to enter username
                            System.out.print("Username: ");
                            String enteredUser = kb.nextLine();
                            // Verify username exists
                            if(bs.verifyUsername(enteredUser))
                            {
                                currentUsername = enteredUser;
                                while(true)
                                {
                                    System.out.print("Password: ");
                                    String enteredPassword = kb.nextLine();
                                    // Validate password
                                    if(bs.verifyPassword(enteredUser, enteredPassword))
                                    {
                                        System.out.println("Login Successful!\n");
                                        loggedin = true;
                                        break main_menu;
                                    }
                                    else
                                    {
                                        System.out.println("Password is invalid. Please try again.");
                                    }
                                }
                            }
                            else
                            {
                                System.out.println("Username does not exist. Please try again.");
                            }
                        }
                    case "2":
                        bs.DisplayAvailableBooks();
                        System.out.print("\n\n");
                        break;
                    case "3":
                        kb.close();
                        System.exit(0); // End program
                }

            }

            bookstore_main_menu: while(true && loggedin == true){
                System.out.println("Bookstore Main Menu");
                System.out.println("1. Display Books Available");//
                System.out.println("2. Buy a Book");//
                System.out.println("3. Return a Book");
                System.out.println("4. View Credit Balance");//
                System.out.println("5. View My Books");
                System.out.println("6. View Receipts");
                System.out.println("7. Logout");//
                System.out.println("8. Quit");//
                System.out.print("\n");
                
                switch (kb.nextLine()) {
                    case "1":
                        bs.DisplayAvailableBooks();
                        System.out.print("\n\n");
                        break;
                    case "2":
                        System.out.println(bs.buyBook(currentUsername));
                        break;
                    case "3":
                        bs.returnBook(currentUsername);
                        break;
                    case "4":
                        System.out.println("\u001B[33m" + "\n====================" + "\u001B[0m");
                        System.out.println("\u001B[33m" + "Credit balance: " + bs.viewCreditBalance(currentUsername) + "\u001B[0m"); // Yellow Color
                        System.out.println("\u001B[33m" + "====================\n" + "\u001B[0m");
                        break;
                    case "5":
                        System.out.println("View My Books");
                        break;
                    case "6":
                        System.out.println("View Receipts");
                        break;
                    case "7":
                        System.out.println("\u001B[32m" + "Logged Out Successful!\n" + "\u001B[0m");
                        loggedin = false; // set login flag to false
                        currentUsername = null; // clear current username variable
                        break bookstore_main_menu;
                    case "8":
                        // Need to write users to files 
                        // Need to write books to files
                        System.exit(0);
                        kb.close();
                        break bookstore_main_menu;
                    default:
                        break;
                }
            }
        }
        
    }
    
}