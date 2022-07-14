package src.User;

import java.util.LinkedList;

public class User {
    // User file format: ID, username, password, credit, list of books owned by id
    private int userID;
    private String username, password;
    private double credit;
    private LinkedList<Integer> booksOwnedIDs;

    public User(){
        userID = -1;
        username = "";
        password = "";
        credit = 0.0;
        booksOwnedIDs = new LinkedList<Integer>();
    }

    public User(int id, String user, String pass, double balance, LinkedList<Integer> ownedBooks){
        userID = id;
        username = user;
        password = pass;
        credit = balance;
        booksOwnedIDs = ownedBooks;
    }


    // Accessors

    public int getUserID() { return userID; }

    public String getUsername() { return username; }
    
    public String getPassword() { return password; }
    
    public double getTotalCredit() { return credit; }

    public LinkedList<Integer> getBooksOwnedIDs() { return booksOwnedIDs; }

    public int getTotalBooksOwned() { return booksOwnedIDs.size(); }


    // Mutators

    public void setUserID(int newUserID) { userID = newUserID; }

    public void setUsername(String newUsername) { username = newUsername; }

    public void setPassword(String newPassword) { password = newPassword; }

    public void setTotalCredit(Double newTotalCredit) { credit = newTotalCredit; }

    public void setBooksOwnedIDs(LinkedList<Integer> newBooksOwnedIDs) { booksOwnedIDs = newBooksOwnedIDs; }


    // Add new purchased book
    public void addPurchasedBookID(Integer bookID) { booksOwnedIDs.add(bookID); }

    // Make payment
    public void payment(Double price) { credit -= price; }

    // Verify Book is owned by User
    public boolean verifyBookOwned(Integer bookid){
        for(Integer currBookID : booksOwnedIDs)
        {
            if(bookid.equals(currBookID))
            {
                return true;
            }
        }
        return false;

    }

    // Get position of book id from books owned ID list
    public int getPositionofBookID(Integer bookid)
    {
        for(int i = 0; i < booksOwnedIDs.size(); i++)
        {
            if(bookid.equals(booksOwnedIDs.get(i)))
            {
                return i;
            }
        }
        return -1;
    }
    

}
