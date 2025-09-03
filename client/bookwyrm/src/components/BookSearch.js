import React, { useState } from "react";
import { Container, Form, Button, Card } from "react-bootstrap";
import { Link } from "react-router-dom"; // Import Link from react-router-dom
import "./css/BookSearch.css";
import "./css/loading.css"; 

function BookSearch() {
  const [query, setQuery] = useState("");
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setQuery(e.target.value);
  };

  const handleSearch = (e) => {
    e.preventDefault();
    setLoading(true);

    let url = `https://openlibrary.org/search.json?`;
    if (/^\d+$/.test(query)) {
      url += `isbn=${query}`;
    } else {
      url += `title=${query}`;
    }

    fetch(url)
      .then((response) => response.json())
      .then((data) => {
        setResults(data.docs.slice(0, 5) || []); // Show only the first five results
        setLoading(false);
      })
      .catch((error) => {
        console.error("Error fetching data:", error);
        setLoading(false);
      });
  };

  return (
    <Container className="book-search-container">
      <h2 className="text-white">Book Search</h2>
      <Form onSubmit={handleSearch} className="d-flex align-items-center">
        <Form.Group controlId="formSearch" className="mb-0 flex-grow-1 d-flex">
          <Form.Label className="text-white mb-0">Search by ISBN, Title, or Author</Form.Label>
          <Form.Control
            type="text"
            placeholder="Enter search query"
            value={query}
            onChange={handleChange}
            className="search-input ml-2"
          />
        </Form.Group>
        <Button variant="primary" type="submit" disabled={loading} className="ml-2">
          {loading ? "Searching..." : "Search"}
        </Button>
      </Form>

      {loading && (
        <div className="lds-ring">
          <div></div>
          <div></div>
          <div></div>
          <div></div>
        </div>
      )}

      <div className="results-container">
        {results.map((book) => (
            <Card className="mb-3 card-custom">
              <Card.Body className="d-flex align-items-center">
                {book.isbn && book.isbn[0] && (
                  <img
                    className="card-img"
                    src={`https://covers.openlibrary.org/b/ISBN/${book.isbn[0]}-L.jpg`}
                    alt="Book Cover"
                  />
                )}
                <div className="ml-3">
                  <Link key={book.key} to={`/book/${book.isbn[0]}`}><Card.Title className="text-white link">{book.title}</Card.Title></Link>
                  <Card.Subtitle className="mb-2 text-white">
                    {book.author_name ? book.author_name.join(", ") : "Unknown Author"}
                  </Card.Subtitle>
                  <Card.Text className="text-white">
                    {book.first_publish_year ? `First published in ${book.first_publish_year}` : ""}
                  </Card.Text>
                  <Card.Text className="text-white">
                    {book.isbn ? `ISBN: ${book.isbn.slice(0, 5).join(", ")}` : ""}
                  </Card.Text>
                </div>
              </Card.Body>
            </Card>
                  ))}
      </div>
    </Container>
  );
}

export default BookSearch;