import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import "./css/loading.css";

function YourClubs() {
  // STATE
  const [clubs, setClubs] = useState([]);
  const [users, setUsers] = useState({});
  const [bookDetails, setBookDetails] = useState({});
  const [isLoading, setIsLoading] = useState(true);
  const url = "http://localhost:8080/api/bookClub/user";
  const navigate = useNavigate();

  // Fetch Clubs and Users
  useEffect(() => {
    const fetchClubs = async () => {
      try {
        const response = await fetch(`${url}/1`);
        if (response.status === 200) {
          const data = await response.json();
          console.log("Fetched Clubs:", data); // Debug log
          setClubs(data);

          // Fetch usernames for all createdByIds
          const userIds = [...new Set(data.map((club) => club.createdById))];
          const userPromises = userIds.map((userId) =>
            fetch(`http://localhost:8080/api/user/${userId}`).then((res) =>
              res.json()
            )
          );
          const userResults = await Promise.all(userPromises);
          const userMap = userResults.reduce((acc, user) => {
            acc[user.id] = user.username;
            return acc;
          }, {});
          setUsers(userMap);

          // Fetch book details for all ISBNs
          const isbns = [...new Set(data.map((club) => club.isbn))].filter(
            (isbn) => isbn
          );
          console.log("ISBNs to Fetch:", isbns); // Debug log
          const bookPromises = isbns.map((isbn) =>
            fetch(`https://openlibrary.org/search.json?isbn=${isbn}`).then(
              (res) => res.json()
            )
          );
          const bookResults = await Promise.all(bookPromises);

          // Map ISBN to book details
          const bookMap = bookResults.reduce((acc, result, index) => {
            const isbn = isbns[index];
            if (result.docs && result.docs.length > 0) {
              const book = result.docs[0]; // Get the first result
              acc[isbn] = {
                title: book.title || "No title",
                author: book.author_name
                  ? book.author_name.join(", ")
                  : "Unknown author",
              };
            }
            return acc;
          }, {});
          console.log("Book Details:", bookMap); // Debug log
          setBookDetails(bookMap);
        } else {
          throw new Error(`Unexpected Status Code: ${response.status}`);
        }
      } catch (error) {
        console.error(error);
      } finally {
        setIsLoading(false);
      }
    };
    fetchClubs();
  }, []);

  const handleDeleteClub = (clubId) => {
    if (window.confirm(`Delete Book Club?`)) {
      fetch(`http://localhost:8080/api/bookClub/${clubId}`, {
        method: "DELETE",
      })
        .then((response) => {
          if (response.status === 204) {
            setClubs(clubs.filter((club) => club.id !== clubId));
            navigate("/yourClubs");
          } else {
            throw new Error(`Unexpected status code: ${response.status}`);
          }
        })
        .catch(console.log);
    }
  };

  const handleDeleteClubMember = (clubId) => {
    if (window.confirm(`Leave Book Club?`)) {
      fetch(`http://localhost:8080/api/clubMember/1/${clubId}`, {
        method: "DELETE",
      })
        .then((response) => {
          if (response.status === 202) {
            setClubs(clubs.filter((club) => club.id !== clubId));
            navigate("/yourClubs");
            window.location.reload();
          } else {
            throw new Error(`Unexpected status code: ${response.status}`);
          }
        })
        .catch(console.log);
    }
  };

  return (
    <>
      <button
        className="btn btn-success"
        onClick={() => navigate("/createAClub")}
      >
        Create a Club
      </button>
      {isLoading && (
        <div className="lds-ring">
          <div></div>
          <div></div>
          <div></div>
          <div></div>
        </div>
      )}
      {!isLoading &&
        (clubs.length > 0 ? (
          <table className="table table-striped table-hover">
            <thead>
              <tr>
                <th>Cover</th>
                <th>Name</th>
                <th>Description</th>
                <th>Genre</th>
                <th>Location</th>
                <th>Online</th>
                <th>Featured Book</th>
                <th>Creation Date</th>
                <th>Created By</th>
                <th>Interact</th>
              </tr>
            </thead>
            <tbody>
              {clubs.map((club) => (
                <tr key={club.id}>
                  <td>
                    <img
                      className="card-img-top"
                      src={
                        club.isbn
                          ? `https://covers.openlibrary.org/b/ISBN/${club.isbn}-L.jpg`
                          : ""
                      }
                      alt="Book Cover"
                    />
                  </td>
                  <td>{club.name}</td>
                  <td>{club.description}</td>
                  <td>{club.genre}</td>
                  <td>{club.location}</td>
                  <td>{club.online ? "Yes" : "No"}</td>
                  <td>
                  {club.isbn ? <Link to={`/book/${club.isbn}`}>
                                                <div>
                                                    <strong>{bookDetails[club.isbn].title}</strong><br />
                                                    {bookDetails[club.isbn].author}<br />
                                                    {club.isbn}
                                                </div>
                                            </Link>
                    : (
                      "No featured book"
                    )}
                  </td>
                  <td>{club.date}</td>
                  <td>{users[club.createdById] || "Unknown"}</td>
                  <td>
                    <div className="float-right mr-2">
                      <Link
                        className="btn btn-primary mb-1"
                        to={`/club/discussion/${club.id}`}
                      >
                        Discussions
                      </Link>
                      <Link
                        className="btn btn-primary mb-1"
                        to={`/club/edit/${club.id}`}
                      >
                        Edit Club
                      </Link>
                      <button
                        className="btn btn-danger btn-sm mb-1"
                        onClick={() => handleDeleteClub(club.id)}
                      >
                        <i className="bi bi-trash"></i> Delete Club
                      </button>
                      <button
                        className="btn btn-danger btn-sm"
                        onClick={() => handleDeleteClubMember(club.id)}
                      >
                        <i className="bi bi-trash"></i> Leave Club
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        ) : (
          <p>No clubs found</p>
        ))}
    </>
  );
}

export default YourClubs;
