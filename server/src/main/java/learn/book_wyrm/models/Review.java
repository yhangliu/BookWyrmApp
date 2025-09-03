    package learn.book_wyrm.models;

    import java.sql.Date;
    import java.time.LocalDate;

    public class Review {
        private int id;
        private int rating;
        private String reviewText;
        private Date date;
        private int createdById;
        private String isbn;


        public int getCreatedById() {
            return createdById;
        }

        public void setCreatedById(int createdById) {
            this.createdById = createdById;
        }

        public String getIsbn() {
            return isbn;
        }

        public void setIsbn(String isbn) {
            this.isbn = isbn;
        }


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }

        public String getReviewText() {
            return reviewText;
        }

        public void setReviewText(String reviewText) {
            this.reviewText = reviewText;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        @Override
        public String toString() {
            return "Review{" +
                    "id=" + id +
                    ", rating=" + rating +
                    ", reviewText='" + reviewText + '\'' +
                    ", date=" + date +
                    ", createdById=" + createdById +
                    ", isbn='" + isbn + '\'' +
                    '}';
        }
    }