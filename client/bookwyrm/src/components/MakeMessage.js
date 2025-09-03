import { useEffect, useState } from "react";
import { useNavigate, useParams, Link } from "react-router-dom";

const MESSAGE_DEFAULT = {
    message: '',
    createdAt: new Date,
    userId: 1

};

function MakeMessage() {
    const url = "http://localhost:8080/api/messages";

    const { id } = useParams();

    const [message, setMessage] = useState(MESSAGE_DEFAULT);

    const navigate = useNavigate();
    const [errors, setErrors] = useState([]);

    const handleChange = (event) => {

        const newMessage = { ...message };

        newMessage[event.target.name] = event.target.value;

        setMessage(newMessage);


    };

    const handleSubmit = (event) => {
        event.preventDefault();
        addMessage();
    };


    const addMessage = () => {

        message.threadId = id;

        const init = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(message)
        };

        fetch(url, init)
            .then(response => {
                if (response.status === 201 || response.status === 400) {
                    return response.json();
                } else {
                    return Promise.reject(`Unexpected status code: ${response.status}`);
                }
            })
            .then(data => {
                if (data.message) {
                    navigate("/discussion/messages/" + id);
                } else { //unhappy
                    setErrors(data);
                }
            })
            .catch(console.log);
    };

    return (
        <>
            <section className="container" style={{ marginTop: 200 }}>
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
                <form onSubmit={handleSubmit}>
                    <fieldset className="form-group">
                        <label htmlFor="name">Message:</label>
                        <input id="message" name="message" type="text" className="form-control"
                            value={message.isbn} onChange={handleChange} />
                    </fieldset>

                    <fieldset className="mt-4">
                        <button
                            className="btn btn-success mr-2"
                            type="submit"
                        >
                            Create
                        </button>
                        <Link
                            button className="btn btn-warning" type="button"
                            to={"/discussion/messages/" + id}
                        >
                            Cancel
                        </Link>
                    </fieldset>
                </form>
            </section>
        </>
    );

}

export default MakeMessage;