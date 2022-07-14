package src.Bookstore;
import java.util.Scanner;

import src.User.User;
import src.User.UserManager;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.LinkedList;

public class Bookstore {
    private LinkedList<Book> books;
    private UserManager userMng;

    public Bookstore(){
        // Instantiate User Manager
        userMng = new UserManager();
        
        // Read book data from file and parses it. Then puts them into LinkedList as Book objects
        books = new LinkedList<Book>();

        try {
            File myObj = new File("src/data/books.dat");
            Scanner fileReader = new Scanner(myObj);
            while(fileReader.hasNextLine())
            {
                String bookInfo = fileReader.nextLine();
                String[] bookInfoParse = bookInfo.split("  ", 0);
                books.add(new Book(Integer.parseInt(bookInfoParse[0]), bookInfoParse[1], Double.parseDouble(bookInfoParse[2]), Integer.parseInt(bookInfoParse[3]), Integer.parseInt(bookInfoParse[4])));
            }
            fileReader.close();
        }
        catch(FileNotFoundException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    
    public void DisplayAvailableBooks(){
        // Display a Chart of All Available Books to the user
        System.out.println("-------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-12s%-70s%-12s%-16s%-12s", "ID", "Book Title", "Price", "Total Pages", "Quantity");
        System.out.println();
        System.out.println("-------------------------------------------------------------------------------------------------------------------------");
        
        for(Book book : books)
        {
            System.out.printf("\u001B[33m"+"%-12s%-70s%s%-12.2f%-16s%-12s" + "\u001B[0m", book.getID(), book.getTitle(), "$", book.getPrice(), book.getTotalPages(), book.getQuantity()); //Prints out the bookid, title, price, total pages and quantities in yellow text
            System.out.println();
        }

        System.out.println("-------------------------------------------------------------------------------------------------------------------------");
    }

    // Verify username user entered
    public boolean verifyUsername(String enteredUsername) { return userMng.verifyUsername(enteredUsername); }

    // Verify password if the password user entered is correct
    public boolean verifyPassword(String Username, String enteredPassword) { return userMng.verifyPassword(Username, enteredPassword); }

    public String buyBook(String Username){
        // To buy a book, we need to
        // 1. Have user give id of book they want to buy
        // 2. Verify user has enough money to buy it and that they don't actually already own it
        // 3. Subtract from balance of User Object
        // 4. Subtract from quantity of Book Object
        // 5. Update data file for books
        // 6. Update data file for users

        Scanner kb = new Scanner(System.in);

        Book desiredBook = null; // Desired Book Object
        Integer buyingBookID = 0;

validate_book:
        while(desiredBook == null) // validates the bookid
        {
            DisplayAvailableBooks(); // Display list of books to user
             // Have user give id of book they want to buy
            System.out.print("Enter a book id you want to buy: ");
            String userinput = kb.nextLine();

            if(isNumeric(userinput)) //  Checks if user enters a valid data type (Integer for id)
            {
                buyingBookID = Integer.parseInt(userinput); // Asks what book user wants to buy     

                // Check if the user does not already own the book
                if(userMng.getUser(Username).getPositionofBookID(buyingBookID) != -1) // if the user already owns the book
                {
                    System.out.println("\u001B[31m" + "You already own this book! " + "\u001B[0m"); 
                    buyingBookID = 0; // reset the bookid
                    continue validate_book; // restart the loop
                }
            }
            else
            {
                System.out.println("\u001B[31m" + "Invalid input. Please enter in the form of an integer. " + "\u001B[0m"); // Red color
                continue validate_book; // Restart the loop
            }

            // Searches for desired book and saves it to desiredBook variable
            for(Book currBook : books)
            {
                if(currBook.getID() == buyingBookID)
                {
                    desiredBook = currBook;
                    break;
                }
            }
            if(desiredBook == null)
            {
                System.out.println("\u001B[31m" + "Invalid book id. Please try again." + "\u001B[0m"); // Red color
            }
        }

        if(desiredBook.getQuantity() == 0) // checks if the book is still in stock
        {
            return "\u001B[31m" + "Error: The desired book is out of stock!\n" + "\u001B[0m"; // Red Color
        }

        // Verify user has enough money to buy it and that they don't actually already own it
        if(!userMng.checkEnoughCredits(Username, desiredBook.getPrice())) // if user does not have enough credits
        {
            double amountNeeded = desiredBook.getPrice() - userMng.getUserCreditBalance(Username); // calculates credit needed to buy the book
            return  "\u001B[31m" + "Error: " + Username + " does not have enough credits\n" + Username + " is missing " + amountNeeded + " credits.\n" + "\u001B[0m";  
        }

        while(true){
            // Confirm with user that they want to buy this book;
            System.out.print("Do you want to buy " + desiredBook.getTitle() + "? (Y/N) "); 

            String userinput = kb.nextLine();

            // If Yes, process the purchase
            if(userinput.toLowerCase().equals("y"))
            {
                // Subtract from balance of User Object
                userMng.payment(Username, desiredBook.getPrice());

                // Subtract 1 from quantity of Book object
                int newQuantity = desiredBook.getQuantity() - 1;
                desiredBook.setQuantity(newQuantity);

                // Add new book to user account/object
                userMng.addPurchasedBookID(Username, buyingBookID);

                // Update book data file

                // Update user data file

                
                return "\u001B[32m" + "Book Successfully Purchased\n" + "\u001B[0m"; // Green text color
            }
            else if(userinput.toLowerCase().equals("n"))
            {
                return "\u001B[35m" + "Purchase cancelled.\n" + "\u001B[0m";
            }
            else{
                System.out.println("Please enter Y or N. ");
            }
        }
        
    }

    // Show credit balance of a specific user
    public double viewCreditBalance(String username){ return userMng.getCreditBalance(username); }

    // Displays list of books user owns
    public void displayOwnedBooks(String username) { 
        User currUser = userMng.getUser(username);
        System.out.println("-------------------------------------------------------------------------------------------------");
        System.out.println("                                         [Books Owned]                                           ");
        System.out.println("-------------------------------------------------------------------------------------------------");
        System.out.printf("%-12s%-70s%-16s", "ID", "Book Title", "Total Pages");
        System.out.println();
        System.out.println("-------------------------------------------------------------------------------------------------");

        int totalMatched = 0; // tracks number of books owned by user found

        // For each book we are going to check if any of the bookids the user owns matches if so print it. Then once we found all the bookids that match the user owned books we are done
        for(Book b : books)
        {
            for(Integer i : currUser.getBooksOwnedIDs())
            {
                if(b.getID() == i) // if bookids match
                { 
                    System.out.printf("\u001B[33m"+"%-12s%-70s%-16s" + "\u001B[0m", b.getID(), b.getTitle(), b.getTotalPages()); 
                    System.out.println();
                    totalMatched++; // increment the counter for the books owned by user found
                    break; // No need to search 
                }
            }
            if(totalMatched == currUser.getBooksOwnedIDs().size()) // if we matched all the books the user owns then we are done
            {
                break; 
            }
        }

        System.out.println("-------------------------------------------------------------------------------------------------");
        
    }



    // Return book
    public void returnBook(String Username){

        if(userMng.getUser(Username).getTotalBooksOwned() == 0)
        {
            System.out.println("\u001B[36m" + "You currently don't own any books.\n " + "\u001B[0m"); // Cyan color
            return;
        }

        // Display list of books user owns
        displayOwnedBooks(Username);
        System.out.println();

        // Prompt user for bookid to return
        Scanner kb = new Scanner(System.in);
        Integer bookidReturn = -1;

        // Validate book id 
        while(true) {
            System.out.print("Enter the book id that you want to return (type x to cancel): ");
            String userinput = kb.nextLine();

            if(userinput.toLowerCase().equals("x")) // If the user types x, then we are done
            {
                return; 
            }
            
            
            // Checks if userinput is an Integer and that the user owns the book 
            if(isNumeric(userinput) && userMng.verifyBookOwned(Username, Integer.parseInt(userinput))) 
            {
                bookidReturn = Integer.parseInt(userinput);
                break;
            }

            System.out.println("\u001B[31m" + "Invalid book id. Please try again. \n" + "\u001B[0m"); // Red color
        }

        Double refundCredits = 0.0; // refund total
        // Increment book quantity by 1 from the bookstore books
        for(Book currBook : books) // Finds the book that is returned
        {
            if(currBook.getID() == bookidReturn)
            {
                currBook.setQuantity(currBook.getQuantity()+1); // increment quantity by 1
                refundCredits = currBook.getPrice();
                break; 
            }
        }

        // Remove bookid from bookid list 
        userMng.removeBookFromUser(Username, bookidReturn);

        // Refund credits to user account
        userMng.refundUser(Username, refundCredits);
    
        // Write to book and user data files

    }


    private static boolean isNumeric(String strNum)
    {
        if(strNum == null)
            return false;
        try { 
            Integer.parseInt(strNum);
        }
        catch(NumberFormatException e)
        {
            return false;
        }
        return true;
    }
    
}