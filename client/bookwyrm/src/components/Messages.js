import { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import "./css/Discussions.css";

function Messages() {
    const url = "http://localhost:8080/api/messages/thread";
    const { id } = useParams();
    const [messages, setMessages] = useState([]);
    const [users, setUsers] = useState({});

    // Fetch messages and usernames
    useEffect(() => {
        const fetchMessages = async () => {
            try {
                const response = await fetch(`${url}/${id}`);
                if (response.status === 200) {
                    const data = await response.json();
                    setMessages(data);

                    // Extract unique user IDs
                    const userIds = [...new Set(data.map(message => message.userId))];
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
                    throw new Error(`Unexpected Status Code: ${response.status}`);
                }
            } catch (error) {
                console.error(error);
            }
        };

        fetchMessages();
    }, [id]); // Fetch data whenever the `id` changes

    return (
        <>
            <Link className="btn btn-success" to={`/createMessage/${id}`} style={{ marginBottom: "1rem" }}>Create a Message</Link>
            <table className="table table-striped table-hover">
                <thead>
                    <tr>
                        <th>Message</th>
                        <th>Creation Date</th>
                        <th>Created By</th>
                    </tr>
                </thead>
                <tbody>
                    {messages.map(message =>
                        <tr key={message.id}>
                            <td>{message.message}</td>
                            <td>{message.createdAt}</td>
                            <td>{users[message.userId] || 'Unknown User'}</td>
                        </tr>
                    )}
                </tbody>
            </table>
        </>
    );
}

export default Messages;
