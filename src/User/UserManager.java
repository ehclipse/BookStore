package src.User;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Scanner;

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors


public class UserManager {
    private Hashtable<String, User> users;
    
    public UserManager(){

        users = new Hashtable<String, User>();

        try {
            File myObj = new File("src/data/users.dat");
            Scanner fileReader = new Scanner(myObj);
            while(fileReader.hasNextLine())
            {
                String userData = fileReader.nextLine();
                String[] userDataParse = userData.split("  ", 0);

                String[] userBooksParse = userDataParse[4].split(" ", 0); // Parse the list of book ids owned by the user
                LinkedList<Integer> userBooks = new LinkedList<Integer>(); 

                for(String userBook : userBooksParse) // Convert that list into a Integer LinkedList
                {
                    userBooks.add(Integer.parseInt(userBook));
                }

                // Add new user to the userlist
                users.put(userDataParse[1], new User(Integer.parseInt(userDataParse[0]), userDataParse[1], userDataParse[2], Double.parseDouble(userDataParse[3]), userBooks));
            }
            fileReader.close();
        }
        catch(FileNotFoundException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    // Verify Username Exists
    public boolean verifyUsername(String enteredUsername)
    {
        if(users.containsKey(enteredUsername))
        {
            return true;
        }
        return false;
    }

    // Verify Password Matches
    public boolean verifyPassword(String Username, String enteredPassword)
    {
        // If the password in the hashtable matches with the entered password, then return true
        if(users.get(Username).getPassword().equals(enteredPassword))
            return true;
        return false;
    }

    // Check if user has enough credits to buy book
    public boolean checkEnoughCredits(String Username, double bookPrice)
    {
        if(users.get(Username).getTotalCredit() >= bookPrice)
        {
            return true;
        }
        return false;
    }

    // Add new purchased book
    public void addPurchasedBookID(String Username, Integer bookID) { users.get(Username).addPurchasedBookID(bookID); }

    // Payment for book
    public void payment(String Username, Double price) { users.get(Username).payment(price); }

    // Get a user balance
    public double getUserCreditBalance(String Username) { return users.get(Username).getTotalCredit(); }

    // Add new user

    // Delete a new user
    

    // Get credit balance
    public double getCreditBalance(String Username){ return users.get(Username).getTotalCredit(); }


    // Verify Book is owned by User
    public boolean verifyBookOwned(String Username, Integer bookid) { return users.get(Username).verifyBookOwned(bookid); }

    // Get a specific user
    public User getUser(String Username) { return users.get(Username); }

    // Gives user a refund
    public void refundUser(String Username, Double refundCredits) { 
        User currUser = users.get(Username); //get the user object

        currUser.setTotalCredit(currUser.getTotalCredit() + refundCredits); // refunds the user
    }

    // Remove bookid from user's owned list
    public void removeBookFromUser(String Username, Integer bookid) { 
        User currUser = users.get(Username); // get the User object

        LinkedList<Integer> booksOwned = currUser.getBooksOwnedIDs(); // get the book list of the user
        booksOwned.remove(currUser.getPositionofBookID(bookid)); // remove the book from the list

        currUser.setBooksOwnedIDs(booksOwned); // replace the old book list with the new book list for the user
    }

}
