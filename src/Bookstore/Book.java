package src.Bookstore;

public class Book {
    private int id, totalPages, quantity;
    private double price;
    private String title;

    public Book()
    {
        id = -1;
        totalPages = -1;
        price = 0.00;
        title = "";
        quantity = 0;
    }

    public Book(int idNum, String bookTitle, double bookPrice, int totalBookPages, int quant)
    {
        id = idNum;
        title = bookTitle;
        price = bookPrice;
        totalPages = totalBookPages;
        quantity = quant;
    }

    // Accessors and Mutators
    public int getID() { return id; }

    public int getTotalPages() { return totalPages; }

    public double getPrice() { return price; }

    public String getTitle() { return title; }

    public int getQuantity() { return quantity; }

    public void setID(int newID) { id = newID; }

    public void setTotalPages(int newTotalPages) { totalPages = newTotalPages; }

    public void setPrice(double newPrice) { price = newPrice; }

    public void setTitle(String newTitle) { title = newTitle; }

    public void setQuantity(int newQuantity) { quantity = newQuantity; }


}
