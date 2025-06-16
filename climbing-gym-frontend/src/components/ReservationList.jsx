import {useEffect, useState} from "react";


function UserList() {
    const [users, setUsers] = useState([]);
    const [editingId, setEditingId] = useState(null);
    const [editedData, setEditedData] = useState({});
    const [searchTerm, setSearchTerm] = useState("");

    const loadUsers = () => {
        fetch("http://localhost:8080/api/users")
            .then(res => res.json())
            .then(data => setUsers(data));
    };

    useEffect(() => {
        loadUsers();
    }, []);

    const handleDelete = (id) => {
        fetch(`http://localhost:8080/api/users/${id}`, {
            method: "DELETE",
        })
            .then((res) => {
                if (res.ok) {
                    setUsers(users.filter((user) => user.id !== id));
                }
                else {
                    alert("Error deleting");
                }
            });
    };

    const handleEditClick = (user) => {
        setEditingId(user.id);
        setEditedData({
            firstname: user.firstname,
            lastname: user.lastname,
            registerDate: user.registerDate,
        });
    };
    const handleEditChange = (e) => {
        setEditedData({ ...editedData, [e.target.name]: e.target.value });
    };
    const handleEditSave = (user) => {
        fetch(`http://localhost:8080/api/users/${user.id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ ...user, ...editedData }),
        }).then((res) => {
            if (res.ok) {
                setEditingId(null);
                loadUsers();
            } else {
                alert("Update Error");
            }
        });
    };
    const handleEditCancel = () => {
        setEditingId(null);
        setEditedData({});
    };
    const filteredUsers = users.filter((user) =>
        user.lastname.toLowerCase().includes(searchTerm.toLowerCase())
    );


    return (
        <div>
            <h3>List of users:</h3>
            <input
                type="text"
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
            />
            <ul>
                {filteredUsers.map(user => (
                    <li key={user.id}>
                        {editingId === user.id ? (
                            <>
                                <input
                                    name="firstname"
                                    value={editedData.firstname}
                                    onChange={handleEditChange}
                                />
                                <input
                                    name="lastname"
                                    value={editedData.lastname}
                                    onChange={handleEditChange}
                                />
                                <input
                                    name="registerDate"
                                    type="date"
                                    value={editedData.registerDate}
                                    onChange={handleEditChange}
                                />
                                <button onClick={() => handleEditSave(user)}>Save</button>
                                <button onClick={handleEditCancel}>Cancel</button>
                            </>
                        ) : (
                            <>
                                {user.firstname} {user.lastname} {" "}
                                <button onClick={() => handleDelete(user.id)}>Delete</button>
                                <button onClick={() => handleEditClick(user)}>Edit</button>
                            </>
                        )}
                    </li>
                ))}
            </ul>
        </div>
    )
}

export default UserList;
