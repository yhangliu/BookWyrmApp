package learn.book_wyrm.data;

import learn.book_wyrm.models.Book;

import java.util.List;

public interface BookRepository {
    List<Book> findAll();
    List<Book> findByTitle(String title);
    List<Book> findByAuthor(String author);

    Book findById(int id);

    Book findByIsbn(String isbn);
    Book add(Book book);
}