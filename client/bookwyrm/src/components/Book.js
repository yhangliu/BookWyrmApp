import React, { useEffect, useState } from 'react';
import "./css/loading.css";
import "./css/Book.css";
import { useParams } from 'react-router-dom';

function Book() {
    const { isbn } = useParams();
    const [book, setBook] = useState(null);
    const [reviews, setReviews] = useState([]);
    const [averageRating, setAverageRating] = useState(0);
    const [loading, setLoading] = useState(true);
    const [users, setUsers] = useState({});

    useEffect(() => {
        const fetchBookData = async () => {
            setLoading(true);
            try {
                const bookResponse = await fetch(`https://openlibrary.org/search.json?isbn=${isbn}`);
                const bookData = await bookResponse.json();
                const bookDetails = bookData.docs[0];
                setBook(bookDetails);
                const reviewsResponse = await fetch(`http://localhost:8080/api/review/book/${isbn}`);
                const reviewsData = await reviewsResponse.json();
                setReviews(reviewsData);

                // Fetch usernames for all createdByIds
                const userIds = [...new Set(reviewsData.map(review => review.createdById))];
                const userPromises = userIds.map(userId =>
                    fetch(`http://localhost:8080/api/user/${userId}`).then(res => res.json())
                );
                const userResults = await Promise.all(userPromises);
                const userMap = userResults.reduce((acc, user) => {
                    acc[user.id] = user.username;
                    return acc;
                }, {});
                setUsers(userMap);

                if (reviewsData.length > 0) {
                    const totalRating = reviewsData.reduce((acc, review) => acc + review.rating, 0);
                    setAverageRating(totalRating / reviewsData.length);
                }
            } catch (error) {
                console.error('Error fetching book data:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchBookData();
    }, [isbn]);

    const empty = " "

    // Render stars for rating
    const renderStars = (rating) => {
        const filledStars = Math.round(rating); // Number of filled stars
        const emptyStars = 5 - filledStars; // Number of empty stars
        return (
            <> {'★'.repeat(filledStars)}{'☆'.repeat(emptyStars)}
            </>
        );
    };

    if (loading) return <div className="lds-ring"><div></div><div></div><div></div><div></div></div>;

    if (!book) return <p>Book not found.</p>;

    return (
        <div className="book-container text-white">
            <div className="book-details">
                <img src={`https://covers.openlibrary.org/b/ISBN/${isbn}-L.jpg`} alt={book.title} className="book-cover" />
                <div className="book-info">
                    <h2><strong>{book.title}</strong></h2>
                    <p><strong>Author:</strong> {book.author_name ? book.author_name.join(', ') : "Unknown author"}</p>
                    <p><strong>ISBN:</strong> {isbn}</p>
                    <p><strong>Average Rating:</strong> {renderStars(averageRating)} ({averageRating})</p>
                    </div>
            </div>
            <div className="book-reviews">
                <h2 className="text-white">Reviews</h2>
                {reviews.length > 0 ? (
                    <div className="review-list">
                        {reviews.map((review, index) => (
                            <div key={index} className="review-item">
                                <p><strong>{users[review.createdById] || 'Unknown User'}:</strong> {review.reviewText}</p>
                                <p>{review.date} | Rating: {renderStars(review.rating)}</p>
                            </div>
                        ))}
                    </div>
                ) : (
                    <p className="text-white">No reviews available.</p>
                )}
            </div>
        </div>
    );
}

export default Book;