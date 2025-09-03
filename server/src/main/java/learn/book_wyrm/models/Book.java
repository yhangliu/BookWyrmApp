package learn.book_wyrm.models;

public class Book {

    private int id;
    private String title;
    private String author;
    private String isbn;
    private String description;

    public Book() {}

    public Book(int book_id, String title, String author, String isbn, String text) {
        this.id = book_id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.description = text;
    }
    public int getBookId() {
        return id;
    }

    public void setBookId(int book_id) {
        this.id = book_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String text) {
        this.description = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}