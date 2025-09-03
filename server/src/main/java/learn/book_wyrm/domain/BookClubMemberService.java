package learn.book_wyrm.domain;

import learn.book_wyrm.data.BookClubMemberRepository;
import learn.book_wyrm.models.BookClubMember;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class BookClubMemberService {

    private final BookClubMemberRepository repository;

    public BookClubMemberService(BookClubMemberRepository repository) {
        this.repository = repository;
    }

    public List<BookClubMember> findAll() {
        return repository.findAll();
    }

    public List<BookClubMember> findByClub(int id) {
        return repository.findByBookClubId(id);
    }

    public Result<BookClubMember> add(BookClubMember member) {
        Result<BookClubMember> result = validate(member);
        if (!result.isSuccess()) {
            return result;
        }

        BookClubMember addedMember = repository.add(member);
        if (addedMember == null) {
            result.addMessage("Member already exists or could not be added.", ResultType.INVALID);
        } else {
            result.setPayload(addedMember);
        }

        return result;
    }

    public Result<Void> deleteMember(int userId, int bookClubId) {
        Result<Void> result = validateDelete(userId, bookClubId);
        if (!result.isSuccess()) {
            return result;
        }

        boolean success = repository.deleteMember(userId, bookClubId);
        if (!success) {
            result.addMessage("Member could not be deleted.", ResultType.INVALID);
        }

        return result;
    }

    private Result<BookClubMember> validate(BookClubMember member) {
        Result<BookClubMember> result = new Result<>();

        // Validate userId
        if (member.getUserId() <= 0) {
            result.addMessage("User ID must be valid.", ResultType.INVALID);
        }

        // Validate bookClubId
        if (member.getBookClubId() <= 0) {
            result.addMessage("Book Club ID must be valid.", ResultType.INVALID);
        }

        if (member.getDate().isAfter(LocalDate.now())) {
            result.addMessage("Date cannot be added in the future", ResultType.INVALID);
        }

        return result;
    }

    private Result<Void> validateDelete(int userId, int bookClubId) {
        Result<Void> result = new Result<>();

        // Validate userId
        if (userId <= 0) {
            result.addMessage("User ID must be valid.", ResultType.NOT_FOUND);
        }

        // Validate bookClubId
        if (bookClubId <= 0) {
            result.addMessage("Book Club ID must be valid.", ResultType.INVALID);
        }

        // Check if the member exists
        List<BookClubMember> members = repository.findAll();
        boolean memberExists = members.stream()
                .anyMatch(m -> m.getUserId() == userId && m.getBookClubId() == bookClubId);

        if (!memberExists) {
            result.addMessage("Member does not exist.", ResultType.NOT_FOUND);
        }

        return result;
    }
}
