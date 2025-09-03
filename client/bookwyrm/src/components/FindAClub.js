import { useEffect, useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import "./css/FindAClub.css";
import "./css/loading.css";

const CLUB_MEMBER_DEFAULT = {
    date: '',
    bookClubId: 0,
    genre: '',
    userId: 1
};

function FindAClub() {
    const [clubs, setClubs] = useState([]);
    const [usernames, setUsernames] = useState({});
    const [clubMember, setClubMember] = useState(CLUB_MEMBER_DEFAULT);
    const [isLoading, setIsLoading] = useState(true);
    const [errors, setErrors] = useState([]);
    const [searchInput, setSearchInput] = useState("");
    const [searchInputLocation, setSearchInputLocation] = useState("");
    const [bookDetails, setBookDetails] = useState({});
    
    const url = "http://localhost:8080/api/bookClub";
    const urlMember = "http://localhost:8080/api/clubMember";
    const urlUser = "http://localhost:8080/api/user";
    const navigate = useNavigate();

    const handleChangeSearch = (e) => {
        if (e.target.value.length < searchInput.length) {
            resetFilter();
        }
        e.preventDefault();
        setSearchInput(e.target.value.toLowerCase());

        setClubs(clubs.filter((club) => {
            return club.genre.toLowerCase().includes(searchInput);
        }));
    };

    const handleChangeSearchState = (e) => {
        if (e.target.value.length < searchInputLocation.length) {
            resetFilter();
        }
        e.preventDefault();
        setSearchInputLocation(e.target.value.toUpperCase());
        setClubs(clubs.filter((club) => {
            return club.location.toUpperCase().includes(searchInputLocation);
        }));
    };

    const fetchBookDetails = async (isbns) => {
        try {
            const bookPromises = isbns.map(isbn =>
                fetch(`https://openlibrary.org/search.json?isbn=${isbn}`).then(res => res.json())
            );
            const bookResults = await Promise.all(bookPromises);

            const bookMap = bookResults.reduce((acc, result, index) => {
                const isbn = isbns[index];
                if (result.docs && result.docs.length > 0) {
                    const book = result.docs[0];
                    acc[isbn] = {
                        title: book.title || "No title",
                        author: book.author_name ? book.author_name.join(', ') : "Unknown author"
                    };
                }
                return acc;
            }, {});

            setBookDetails(bookMap);
        } catch (error) {
            console.error("Error fetching book details:", error);
        }
    };

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await fetch(url);
                if (response.status === 200) {
                    const data = await response.json();
                    setClubs(data);

                    const userIds = [...new Set(data.map(club => club.createdById))];
                    const userPromises = userIds.map(userId =>
                        fetch(`${urlUser}/${userId}`).then(res => res.json())
                    );
                    const userResults = await Promise.all(userPromises);
                    const userMap = userResults.reduce((acc, user) => {
                        acc[user.id] = user.username;
                        return acc;
                    }, {});
                    setUsernames(userMap);

                    const isbns = [...new Set(data.map(club => club.isbn))].filter(isbn => isbn);
                    await fetchBookDetails(isbns);
                } else {
                    throw new Error(`Unexpected Status Code: ${response.status}`);
                }
            } catch (error) {
                console.error(error);
            } finally {
                setIsLoading(false);
            }
        };

        fetchData();
    }, []);

    const resetFilter = () => {
        setIsLoading(true);
        fetch(url)
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                } else {
                    return Promise.reject(`Unexpected Status Code: ${response.status}`);
                }
            })
            .then(data => setClubs(data))
            .catch(console.log)
            .finally(() => setIsLoading(false));
    };

    const joinClub = (clubId) => {
        clubMember.bookClubId = clubId;
        clubMember.date = new Date();
        const init = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(clubMember)
        };

        fetch(urlMember, init)
            .then(response => {
                if (response.status === 201 || response.status === 400) {
                    return response.json();
                } else {
                    return Promise.reject(`Unexpected status code: ${response.status}`);
                }
            })
            .then(data => {
                if (data.name) {
                } else {
                    setErrors(data);
                }
            })
            .catch(console.log);
    };

    return (
        <section className="container">
            {errors.length > 0 && (
                <div className="alert alert-danger">
                    <p>The Following Errors were found: </p>
                    <ul>
                        {errors.map(error => (
                            <li key={error}>{error}</li>
                        ))}
                    </ul>
                </div>
            )}
            <div className="mb-3">
                <button className="btn btn-success mb-3 mr-3" onClick={() => navigate("/createAClub")}>Create a Club</button>
                <input
                    type="text"
                    placeholder="Search Genre"
                    onChange={handleChangeSearch}
                    value={searchInput}
                    className="mr-3" />
                <input
                    type="text"
                    placeholder="Search State"
                    onChange={handleChangeSearchState}
                    value={searchInputLocation}
                    className="mr-2" />
                <button className="btn btn-danger mb-3" onClick={resetFilter}>Reset filter</button>
            </div>

            {isLoading ? (
                <div className="lds-ring"><div></div><div></div><div></div><div></div></div>
            ) : (
                clubs.length > 0 ? (
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
                                <th>Join</th>
                            </tr>
                        </thead>
                        <tbody>
                            {clubs.map(club => (
                                <tr key={club.id}>
                                    <td>
                                        <img
                                            className="card-img-top"
                                            src={club.isbn ? `https://covers.openlibrary.org/b/ISBN/${club.isbn}-L.jpg` : ''}
                                            alt="Book Cover"
                                        />
                                    </td>
                                    <td>{club.name}</td>
                                    <td>{club.description}</td>
                                    <td>{club.genre}</td>
                                    <td>{club.location}</td>
                                    <td>{club.online ? 'Yes' : 'No'}</td>
                                    <td>
                                        {club.isbn && bookDetails[club.isbn] ? (
                                            <Link to={`/book/${club.isbn}`}>
                                                <div>
                                                    <strong>{bookDetails[club.isbn].title}</strong><br />
                                                    {bookDetails[club.isbn].author}<br/>
                                                    {club.isbn}
                                                </div>
                                            </Link>
                                        ) : 'No featured book'}
                                    </td>
                                    <td>{club.date}</td>
                                    <td>{usernames[club.createdById] || "Unknown"}</td>
                                    <td>
                                        <div className="float-right mr-2">
                                            <button className="btn btn-primary" onClick={() => joinClub(club.id)}>Join Club</button>
                                        </div>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                ) : (
                    <p>No clubs found</p>
                )
            )}
        </section>
    );
}

export default FindAClub;