package learn.book_wyrm.models;

import java.time.LocalDate;

public class Message {
    private int id;
    private String message;

    private int threadId;

    private int userId;

    private LocalDate createdAt;

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public Message() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", threadId=" + threadId +
                ", userId=" + userId +
                ", createdAt=" + createdAt +
                '}';
    }
}