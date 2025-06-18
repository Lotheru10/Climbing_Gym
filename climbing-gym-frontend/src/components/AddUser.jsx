import {useState} from "react";


function AddUser({ onUserAdded}) {
    const [form, setform] = useState({
        firstname: "",
        lastname: "",
        registerDate: "",
    });
    const handleChange = (e) =>
        setform({ ...form, [e.target.name]: e.target.value});

    const handleSubmit = (e) => {
        e.preventDefault();
        fetch("http://localhost:8080/api/users", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({ ...form, entries: [], reservations: [] }),
        }).then((res) => {
            if (res.ok){
                alert("User added");
                onUserAdded();
            }
            else{
                alert("Error adding user");
            }

        });
    };

    return (
        <form onSubmit={handleSubmit}>
            <label>Firstname:</label>

            <input name="firstname" onChange={handleChange} required/>
            <br></br>
            <label>Lastname:</label>
            <input name="lastname" onChange={handleChange} required/>
            <br></br>
            <label>Register Date:</label>
            <input name="registerDate" type="date" onChange={handleChange} />
            <br></br>
            <button type="submit">Save</button>
        </form>
    );
}

export default AddUser;
