package learn.book_wyrm.models;

import java.time.LocalDate;

public class BookClubMember {
    private int id;
    private int userId;       // Foreign key to User
    private int bookClubId;   // Foreign key to BookClub
    private LocalDate date;   // Represents when the user joined the book club

    public BookClubMember() {}

    public BookClubMember(int id, int userId, int bookClubId, LocalDate date) {
        this.id = id;
        this.userId = userId;
        this.bookClubId = bookClubId;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBookClubId() {
        return bookClubId;
    }

    public void setBookClubId(int bookClubId) {
        this.bookClubId = bookClubId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "BookClubMember{" +
                "id=" + id +
                ", userId=" + userId +
                ", bookClubId=" + bookClubId +
                ", date=" + date +
                '}';
    }
}
