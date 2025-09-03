package learn.book_wyrm.models;

import java.time.LocalDate;
import java.util.List;

public class DiscussionThread {
    private int id;
    private String title;
    private LocalDate createdAt;
    private int createdBy;
    private int bookclubId;

    public DiscussionThread() {
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public int getBookclubId() {
        return bookclubId;
    }

    public void setBookclubId(int bookclubId) {
        this.bookclubId = bookclubId;
    }

    List<Message> messages;
    public DiscussionThread(int id, String title, LocalDate date) {
        this.id = id;
        this.title = title;
        this.createdAt = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "DiscussionThread{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", createdAt=" + createdAt +
                ", createdBy=" + createdBy +
                ", bookclubId=" + bookclubId +
                ", messages=" + messages +
                '}';
    }
}