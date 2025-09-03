import { useEffect, useState } from "react";
import Dropdown from 'react-dropdown';
import 'react-dropdown/style.css';
import SelectUSState from 'react-select-us-states';
import Select from 'react-select';
import { useNavigate, useParams, Link } from "react-router-dom";
import './css/CreateClub.css';

const CLUB_DEFAULT = {
    name: '',
    description: '',
    genre: '',
    location: '',
    isOnline: false,
    isbn: '',
    date: '',
    createdById: 1

};

const options = [
    { value: "", label: "N/A" },
    { value: "AL", label: "AL - Alabama" },
    { value: "AK", label: "AK - Alaska" },
    { value: "AZ", label: "AZ - Arizona" },
    { value: "AR", label: "AR - Arkansas" },
    { value: "CA", label: "CA - California" },
    { value: "CO", label: "CO - Colorado" },
    { value: "CT", label: "CT - Connecticut" },
    { value: "DE", label: "DE - Delaware" },
    { value: "DC", label: "DC - District Of Columbia" },
    { value: "FL", label: "FL - Florida" },
    { value: "GA", label: "GA - Georgia" },
    { value: "HI", label: "HI - Hawaii" },
    { value: "ID", label: "ID - Idaho" },
    { value: "IL", label: "IL - Illinois" },
    { value: "IN", label: "IN - Indiana" },
    { value: "IA", label: "IA - Iowa" },
    { value: "KS", label: "KS - Kansas" },
    { value: "KY", label: "KY - Kentucky" },
    { value: "LA", label: "LA - Louisiana" },
    { value: "ME", label: "ME - Maine" },
    { value: "MD", label: "MD - Maryland" },
    { value: "MA", label: "MA - Massachusetts" },
    { value: "MI", label: "MI - Michigan" },
    { value: "MN", label: "MN - Minnesota" },
    { value: "MS", label: "MS - Mississippi" },
    { value: "MO", label: "MO - Missouri" },
    { value: "MT", label: "MT - Montana" },
    { value: "NE", label: "NE - Nebraska" },
    { value: "NV", label: "NV - Nevada" },
    { value: "NH", label: "NH - New Hampshire" },
    { value: "NJ", label: "NJ - New Jersey" },
    { value: "NM", label: "NM - New Mexico" },
    { value: "NY", label: "NY - New York" },
    { value: "NC", label: "NC - North Carolina" },
    { value: "ND", label: "ND - North Dakota" },
    { value: "OH", label: "OH - Ohio" },
    { value: "OK", label: "OK - Oklahoma" },
    { value: "OR", label: "OR - Oregon" },
    { value: "PA", label: "PA - Pennsylvania" },
    { value: "PR", label: "PR - Puerto Rico" },
    { value: "RI", label: "RI - Rhode Island" },
    { value: "SC", label: "SC - South Carolina" },
    { value: "SD", label: "SD - South Dakota" },
    { value: "TN", label: "TN - Tennessee" },
    { value: "TX", label: "TX - Texas" },
    { value: "UT", label: "UT - Utah" },
    { value: "VT", label: "VT - Vermont" },
    { value: "VI", label: "VI - Virgin Islands" },
    { value: "VA", label: "VA - Virginia" },
    { value: "WA", label: "WA - Washington" },
    { value: "WV", label: "WV - West Virginia" },
    { value: "WI", label: "WI - Wisconsin" },
    { value: "WY", label: "WY - Wyoming" }
  ];

function EditClub() {

    const url = "http://localhost:8080/api/bookClub";
    const [selectedOption, setSelectedOption] = useState(options[0].value);

    const [club, setClub] = useState(CLUB_DEFAULT);

    const navigate = useNavigate();
    const [errors, setErrors] = useState([]);
    const { id } = useParams();

    useEffect(() => {
        fetch(`${url}/${id}`)
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                } else {
                    return Promise.reject(`Unexpected status code: ${response.status}`)
                }
            })
            .then(data => setClub(data[0]))
            .catch(console.log);
    }, [id]);

    



    const handleChange = (event) => {

        const newClub = { ...club };

        newClub[event.target.name] = event.target.value;
        if (event.target.type === 'checkbox') {
            newClub[event.target.name] = event.target.checked
        } else {
            newClub[event.target.name] = event.target.value;
        }

        setClub(newClub);


    };

    const handleSubmit = (event) => {
        event.preventDefault();
        editClub();
    };

    const editClub = () => {

        
        club.id = id;
        club.location = selectedOption;

        const init = {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(club)
        };

        fetch(`${url}/${id}`, init)
            .then(response => {
                if (response.status === 200) {
                    return null;
                } else if (response.status === 400) {
                    return response.json();
                } else {
                    return Promise.reject(`Unexpected status code: ${response.status}`);
                }
            })
            .then(data => {
                if (!data) {
                    navigate("/yourClubs");
                } else {
                    setErrors(data);
                }
            })
            .catch(console.log);


    };

    const Dropdown = ({
      }) => {
        return (
            <select
              value={selectedOption}
              onChange={e => setSelectedOption(e.target.value)
              }>
              {options.map(o => (
                <option key={o.value} value={o.value}>{o.label}</option>
              ))}
            </select>
        );
      };


    




    return (
        <>
        <section className="container form-container" style={{ marginTop: 200 }}>
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
                    <label htmlFor="name">Name:</label>
                    <input id="name" name="name" type="text" className="form-control"
                        value={club.name} onChange={handleChange} />
                </fieldset>
                <fieldset className="form-group">
                    <label htmlFor="description">Club Description:</label>
                    <input id="description" name="description" type="text" className="form-control"
                        value={club.description} onChange={handleChange} />
                </fieldset>
                <fieldset className="form-group">
                    <label htmlFor="genre">Genre:</label>
                    <input id="genre" name="genre" type="text" className="form-control"
                        value={club.genre} onChange={handleChange} />
                </fieldset>
                <fieldset className="checkbox-group">
                    <label htmlFor="isOnline">Online</label>
                    <input id="isOnline" name="isOnline" type="checkbox" 
                        checked={club.isOnline} onChange={handleChange} />
                </fieldset>
                <fieldset className="form-group">

                <label>Select a State:</label>
                <Dropdown />
                </fieldset>
                <fieldset className="form-group">
                    <label htmlFor="genre">ISBN for Featured book:</label>
                    <input id="isbn" name="isbn" type="text" className="form-control"
                        value={club.isbn} onChange={handleChange} />
                </fieldset>

                <fieldset className="mt-4">
                    <button
                        className="btn btn-success mr-2"
                        type="submit"
                    >
                        Edit
                    </button>
                    <Link
                        button className="btn btn-warning" type="button"
                        to={"/yourClubs"}
                    >
                        Cancel
                    </Link>
                </fieldset>
            </form>
            </section>
        </>
    );
}



export default EditClub;