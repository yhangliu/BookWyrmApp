package learn.book_wyrm.domain;


import learn.book_wyrm.data.BookClubRepository;
import learn.book_wyrm.models.BookClub;
import learn.book_wyrm.models.BookClub;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookClubService {

    private final BookClubRepository repository;

    public BookClubService(BookClubRepository repository) {
        this.repository = repository;
    }

    public List<BookClub> findAll(){
        return repository.findAll();
    }

    public List<BookClub> findByLocation(String location){
        return repository.findByLocation(location);
    }

    public List<BookClub> findByGenre(String genre){
        return repository.findByGenre(genre);
    }

    public List<BookClub> findByMemberId(int id){
        return  repository.findByMemberId(id);
    }

    public List<BookClub> findByClubId(int id) {
        return  repository.findByClubId(id);
    }

    public boolean deleteById(int id){
        return repository.deleteById(id);
    }

    public Result<BookClub> add(BookClub bookClub){
        Result<BookClub> result = validate(bookClub);

        if(!result.isSuccess()) {
            return result;
        }

        if (bookClub.getId() != 0) {
            result.addMessage("Id cannot be set for `add` operation", ResultType.INVALID);
            return result;
        }

        if(repository.add(bookClub)){
            result.setPayload(bookClub);
        }

        return result;
    }

    public Result<BookClub> update(BookClub bookClub){
        Result<BookClub> result = validate(bookClub);

        if(!result.isSuccess()) {
            return result;
        }

        if (bookClub.getId() <= 0) {
            result.addMessage("Id must be set for `update` operation", ResultType.INVALID);
            return result;
        }

        if(!repository.update(bookClub)){
            String msg = String.format("BookClub: %s, not found", bookClub.getName());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    private Result<BookClub> validate(BookClub bookClub){
        Result<BookClub> result = new Result<>();
        if (bookClub == null) {
            result.addMessage("BookClub cannot be null", ResultType.INVALID);
            return result;
        }

        if (Validations.isNullOrBlank(bookClub.getIsbn())) {
            result.addMessage("ISBN is required", ResultType.INVALID);
        }


        if (Validations.isNullOrBlank(bookClub.getName())) {
            result.addMessage("BookClub name is required", ResultType.INVALID);
        }

        return result;
    }



}
