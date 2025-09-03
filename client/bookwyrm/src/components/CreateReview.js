import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";


const REVIEW_DEFAULT = {
    rating: 0,
    reviewText: '',
    isbn: '',
    createdById: 1

};

function CreateAReview(){

    const url = "http://localhost:8080/api/review";

    const [review, setReview] = useState(REVIEW_DEFAULT);

    const navigate = useNavigate();
    const [errors, setErrors] = useState([]);

    const handleSubmit = (event) => {
        event.preventDefault();
        addReview();
    };

    const handleChange = (event) => {

        const newReview = { ...review };

        newReview[event.target.name] = event.target.value;

        setReview(newReview);


    };

    
    const addReview = () => {


        const init = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(review)
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
                if (data.rating) {
                    navigate("/reviews");
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
                    <label htmlFor="name">ISBN:</label>
                    <input id="isbn" name="isbn" type="text" className="form-control"
                        value={review.isbn} onChange={handleChange} />
                </fieldset>
                <fieldset className="form-group">
                    <label htmlFor="rating">Rating:</label>
                    <input id="rating" name="rating" type="text" className="form-control"
                        value={review.rating} onChange={handleChange} />
                </fieldset>
                <fieldset className="form-group">
                    <label htmlFor="reviewText">Review:</label>
                    <input id="genreviewText" name="reviewText" type="text" className="form-control"
                        value={review.reviewText} onChange={handleChange} />
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
                        to={"/reviews"}
                    >
                        Cancel
                    </Link>
                </fieldset>
            </form>
            </section>
        </>
    );

}

export default CreateAReview;