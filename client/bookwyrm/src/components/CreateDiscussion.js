import { useEffect, useState } from "react";
import { useNavigate, useParams, Link } from "react-router-dom";

const DISCUSSION_DEFAULT = {
    title: '',
    createdAt: new Date,
    bookclubId: 0,
    createdBy: 1

};

function CreateDiscussion() {

    const url = "http://localhost:8080/api/threads";

    const { id } = useParams();

    const [discussion, setDiscussion] = useState(DISCUSSION_DEFAULT);

    const navigate = useNavigate();
    const [errors, setErrors] = useState([]);

    const handleChange = (event) => {

        const newDiscussion = { ...discussion };

        newDiscussion[event.target.name] = event.target.value;

        setDiscussion(newDiscussion);


    };

    const handleSubmit = (event) => {
        event.preventDefault();
        addDiscussion();
    };

    
    const addDiscussion = () => {

        discussion.bookclubId = id;

        const init = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(discussion)
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
                if (data.title) {
                    navigate("/club/discussion/"+id);
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
                        <label htmlFor="name">Title:</label>
                        <input id="title" name="title" type="text" className="form-control"
                            value={discussion.isbn} onChange={handleChange} />
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
                            to={"/club/discussion/"+id}
                        >
                            Cancel
                        </Link>
                    </fieldset>
                </form>
            </section>
        </>
    );

}
export default CreateDiscussion;