package learn.book_wyrm.domain;

import learn.book_wyrm.data.ReviewRepository;
import learn.book_wyrm.models.Review;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository repository;


    public ReviewService(ReviewRepository repository) {
        this.repository = repository;
    }

    public List<Review> findAll(){
        return repository.findAll();
    }

    public List<Review> findByISBN(String isbn){
        return repository.findByISBN(isbn);
    }

    public Result<Review> add(Review review){
        Result<Review> result = validate(review);

        if(!result.isSuccess()) {
            return result;
        }

        if (review.getId() != 0) {
            result.addMessage("Id cannot be set for `add` operation", ResultType.INVALID);
            return result;
        }

        if(repository.add(review)){
            result.setPayload(review);
        }

        return result;
    }

    public boolean deleteById(int id){
        return repository.deleteById(id);
    }

    private Result<Review> validate(Review review){
        Result<Review> result = new Result<>();
        if (review == null) {
            result.addMessage("Review cannot be null", ResultType.INVALID);
            return result;
        }

        if (Validations.isNullOrBlank(review.getIsbn())) {
            result.addMessage("ISBN is required", ResultType.INVALID);
        }

        if ((review.getRating()<1) || (review.getRating()>5)) {
            result.addMessage("Rating must be between 1 and 5", ResultType.INVALID);
        }

        if (Validations.isNullOrBlank(review.getReviewText())) {
            result.addMessage("Review text is required", ResultType.INVALID);
        }

        return result;
    }
}
