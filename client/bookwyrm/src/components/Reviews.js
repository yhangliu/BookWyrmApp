import { useEffect, useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import "./css/loading.css"; 

function Reviews() {
    // STATE
    const [reviews, setReviews] = useState([]);
    const [users, setUsers] = useState({});
    const [bookDetails, setBookDetails] = useState({});
    const [isLoading, setIsLoading] = useState(true);
    const [searchInput, setSearchInput] = useState('');
    const url = "http://localhost:8080/api/review";
    const navigate = useNavigate();

    // Fetch Reviews, Users, and Book Details
    useEffect(() => {
        const fetchData = async () => {
            try {
                const reviewResponse = await fetch(url);
                if (reviewResponse.status === 200) {
                    const reviewsData = await reviewResponse.json();
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

                    // Fetch book details for all ISBNs
                    const isbns = [...new Set(reviewsData.map(review => review.isbn))].filter(isbn => isbn);
                    const bookPromises = isbns.map(isbn =>
                        fetch(`https://openlibrary.org/search.json?isbn=${isbn}`).then(res => res.json())
                    );
                    const bookResults = await Promise.all(bookPromises);

                    // Map ISBN to book details
                    const bookMap = bookResults.reduce((acc, result, index) => {
                        const isbn = isbns[index];
                        if (result.docs && result.docs.length > 0) {
                            const book = result.docs[0]; // Get the first result
                            acc[isbn] = {
                                title: book.title || "No title",
                                author: book.author_name ? book.author_name.join(', ') : "Unknown author"
                            };
                        }
                        return acc;
                    }, {});
                    setBookDetails(bookMap);
                } else {
                    throw new Error(`Unexpected Status Code: ${reviewResponse.status}`);
                }
            } catch (error) {
                console.error(error);
            } finally {
                setIsLoading(false);
            }
        };

        fetchData();
    }, []);

    // Handle search input change
    const handleSearchChange = (e) => {
        setSearchInput(e.target.value.toLowerCase());
    };

    // Filter reviews based on search input
    const filteredReviews = searchInput
        ? reviews.filter(review => {
            const book = bookDetails[review.isbn] || {};
            const titleMatch = book.title.toLowerCase().includes(searchInput);
            const authorMatch = book.author.toLowerCase().includes(searchInput);
            return titleMatch || authorMatch;
        })
        : reviews; // Display all reviews if searchInput is empty

    // Render stars for rating
    const renderStars = (rating) => {
        const stars = Array(5).fill('☆');
        for (let i = 0; i < Math.round(rating); i++) {
            stars[i] = '★';
        }
        return stars.join('');
    };

    return (
        <>
            <div style={{ marginBottom: '20px' }}>
                <button className="btn btn-success" onClick={() => navigate("/createAReview")}>Create a Review</button>
                <input
                    type="text"
                    placeholder="Search Title or Author"
                    onChange={handleSearchChange}
                    value={searchInput}
                    style={{ marginLeft: '10px', width: '300px' }}
                />
            </div>
            {isLoading ? (
                <div className="lds-ring"><div></div><div></div><div></div><div></div></div>
            ) : (
                <table className="table table-striped table-hover">
                    <thead>
                        <tr>
                            <th>Cover</th>
                            <th>ISBN</th>
                            <th>Title</th>
                            <th>Author</th>
                            <th>Rating</th>
                            <th>Review</th>
                            <th>Creation Date</th>
                            <th>Created By</th>
                        </tr>
                    </thead>
                    <tbody>
                        {filteredReviews.map(review => (
                            <tr key={review.id}>
                                <td>
                                    <img
                                        className="card-img-top"
                                        src={review.isbn ? `https://covers.openlibrary.org/b/ISBN/${review.isbn}-L.jpg` : ''}
                                        alt="Book Cover"
                                        style={{ width: '50px', height: '75px' }}
                                    />
                                </td>
                                <td>
                                    <Link to={`/book/${review.isbn}`}>{review.isbn}</Link>
                                </td>
                                <td>{bookDetails[review.isbn]?.title || 'No title'}</td>
                                <td>{bookDetails[review.isbn]?.author || 'Unknown author'}</td>
                                <td>{renderStars(review.rating)}</td>
                                <td>{review.reviewText}</td>
                                <td>{review.date}</td>
                                <td>{users[review.createdById] || 'Unknown'}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            )}
        </>
    );
}

export default Reviews;