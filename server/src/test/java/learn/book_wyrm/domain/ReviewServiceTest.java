package learn.book_wyrm.domain;

import learn.book_wyrm.data.ReviewRepository;
import learn.book_wyrm.models.Review;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@SpringBootTest
class ReviewServiceTest {

    @Autowired
    ReviewService service;

    @MockBean
    ReviewRepository repository;



    @Test
    void shouldAddWhenValid() {
        Review arg = makeReview();

        when(repository.add(arg)).thenReturn(true);
        Result<Review> result = service.add(arg);
        List<Review> all = service.findAll();
        assertEquals(ResultType.SUCCESS, result.getType());
    }

    @Test
    void shouldNotAddWhenInvalidRating() {
        Review arg = makeReview();
        arg.setRating(0);

        when(repository.add(arg)).thenReturn(true);
        Result<Review> result = service.add(arg);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotAddWhenInvalidISBN() {
        Review arg = makeReview();
        arg.setIsbn("");

        when(repository.add(arg)).thenReturn(true);
        Result<Review> result = service.add(arg);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotAddWhenInvalidReviewText() {
        Review arg = makeReview();
        arg.setReviewText("");

        when(repository.add(arg)).thenReturn(true);
        Result<Review> result = service.add(arg);
        assertEquals(ResultType.INVALID, result.getType());
    }

    Review makeReview(){
        Review review = new Review();
        review.setReviewText("Really cool book");
        review.setCreatedById(2);
        review.setIsbn("9780141040349");
        review.setRating(5);
        return review;
    }
}