import { useEffect, useState } from "react";
import { useNavigate, useParams, Link } from "react-router-dom";
import "./css/Discussions.css";
import "./css/loading.css";

function Discussions() {
    const url = "http://localhost:8080/api/threads/club";
    const clubUrl = "http://localhost:8080/api/bookClub";
    const { id } = useParams();
    const [discussions, setDiscussions] = useState([]);
    const [users, setUsers] = useState({});
    const [clubName, setClubName] = useState("");
    const [loading, setLoading] = useState(true);

    // Fetch discussions, usernames, and club name
    useEffect(() => {
        const fetchDiscussions = async () => {
            setLoading(true); // Set loading to true when starting to fetch

            try {
                const [discussionResponse, clubResponse] = await Promise.all([
                    fetch(`${url}/${id}`),
                    fetch(`${clubUrl}/${id}`)
                ]);

                if (discussionResponse.status === 200 && clubResponse.status === 200) {
                    const discussionData = await discussionResponse.json();
                    const clubData = await clubResponse.json();

                    setDiscussions(discussionData);
                    setClubName(clubData[0].name);

                    // Extract unique user IDs
                    const userIds = [...new Set(discussionData.map(discussion => discussion.createdBy))];
                    const userPromises = userIds.map(userId =>
                        fetch(`http://localhost:8080/api/user/${userId}`).then(res => res.json())
                    );
                    const userResults = await Promise.all(userPromises);
                    const userMap = userResults.reduce((acc, user) => {
                        acc[user.id] = user.username;
                        return acc;
                    }, {});
                    setUsers(userMap);
                } else {
                    throw new Error(`Unexpected Status Code: ${discussionResponse.status} or ${clubResponse.status}`);
                }
            } catch (error) {
                console.error(error);
            } finally {
                setLoading(false); // Set loading to false once data has been fetched or if an error occurs
            }
        };

        fetchDiscussions();
    }, [id]); // Fetch data whenever the `id` changes

    return (
        <>
            {loading ? (
                <div className="lds-ring"><div></div><div></div><div></div><div></div></div>
            ) : (
                <>
                    <h2 className="club-name">{clubName}</h2>
                    <Link className="btn btn-success" to={`/createDiscussion/${id}`} style={{ marginBottom: "1rem" }}>Create a Discussion</Link>
                    <table className="table table-striped table-hover">
                        <thead>
                            <tr>
                                <th>Title</th>
                                <th>Creation Date</th>
                                <th>Created By</th>
                                <th>Messages</th>
                            </tr>
                        </thead>
                        <tbody>
                            {discussions.map(discussion =>
                                <tr key={discussion.id}>
                                    <td>{discussion.title}</td>
                                    <td>{discussion.createdAt}</td>
                                    <td>{users[discussion.createdBy] || 'Unknown User'}</td>
                                    <td>
                                        <Link className="btn btn-primary" to={`/discussion/messages/${discussion.id}`}>Messages</Link>
                                    </td>
                                </tr>
                            )}
                        </tbody>
                    </table>
                </>
            )}
        </>
    );
}

export default Discussions;
