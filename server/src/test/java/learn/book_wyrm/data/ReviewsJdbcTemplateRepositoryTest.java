package learn.book_wyrm.data;

import learn.book_wyrm.models.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReviewsJdbcTemplateRepositoryTest {

    @Autowired
    learn.book_wyrm.data.ReviewsJdbcTemplateRepository repository;


    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void findAll() {
        List<Review> reviews = repository.findAll();
        assertNotNull(reviews);

        assertTrue(reviews.size()>2);
    }

    @Test
    void findByISBN() {
        List<Review> reviews = repository.findByISBN("9780345339683");
        assertTrue(reviews.size()>0);
    }

    @Test
    void add() {
        Review review = new Review();
        review.setCreatedById(3);
        review.setIsbn("9780316769488");
        review.setRating(5);
        review.setReviewText("Really cool book");

        assertTrue(repository.add(review));
    }

    @Test
    void deleteById() {
        assertTrue(repository.deleteById(1));
    }
}