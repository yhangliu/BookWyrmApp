package learn.book_wyrm.data;

import learn.book_wyrm.models.Review;

import java.util.List;

public interface ReviewRepository {

    List<Review> findAll();

    List<Review> findByISBN(String isbn);

    boolean add(Review review);

    boolean deleteById(int id);
}