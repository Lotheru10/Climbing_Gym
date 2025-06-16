import { useEffect, useState } from "react";

function UserList() {
    const [users, setUsers] = useState([]);
    const [entryTypes, setEntryTypes] = useState([]);
    const [editingId, setEditingId] = useState(null);
    const [editedData, setEditedData] = useState({});
    const [searchTerm, setSearchTerm] = useState("");
    const [visibleReservations, setVisibleReservations] = useState(null);
    const [visibleEntries, setVisibleEntries] = useState(null);
    const [newReservation, setNewReservation] = useState({ date: "", day_time: "morning", people_amount: "1" });
    const [activeUserForReservation, setActiveUserForReservation] = useState(null);
    const [newEntry, setNewEntry] = useState({ entryTypeId: "", type: "Regular", amount: 1 });
    const [activeUserForEntry, setActiveUserForEntry] = useState(null);

    useEffect(() => {
        fetch("http://localhost:8080/api/users").then(res => res.json()).then(data => setUsers(data));
        fetch("http://localhost:8080/api/entry").then(res => res.json()).then(data => setEntryTypes(data));
    }, []);

    const loadUsers = () => {
        fetch("http://localhost:8080/api/users").then(res => res.json()).then(data => setUsers(data));
    };

    const handleDelete = (id) => {
        fetch(`http://localhost:8080/api/users/${id}`, { method: "DELETE" })
            .then((res) => res.ok && loadUsers());
    };

    const handleEditClick = (user) => {
        setEditingId(user.id);
        setEditedData({ firstname: user.firstname, lastname: user.lastname, registerDate: user.registerDate });
    };

    const handleEditChange = (e) => setEditedData({ ...editedData, [e.target.name]: e.target.value });

    const handleEditSave = (user) => {
        fetch(`http://localhost:8080/api/users/${user.id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ ...user, ...editedData })
        }).then((res) => res.ok && (setEditingId(null), loadUsers()));
    };

    const handleEditCancel = () => {
        setEditingId(null);
        setEditedData({});
    };

    const handleReservationChange = (e) => setNewReservation({ ...newReservation, [e.target.name]: e.target.value });

    const handleAddReservationSubmit = (e) => {
        e.preventDefault();
        const { date, day_time, people_amount } = newReservation;
        const validDayTimes = ["morning", "noon", "evening"];
        if (!validDayTimes.includes(day_time) || parseInt(people_amount) < 1) return alert("Invalid reservation");
        fetch(`http://localhost:8080/api/users/${activeUserForReservation}/reservations`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ ...newReservation, reservationId: "r-temp", status: "A" })
        }).then(() => {
            setNewReservation({ date: "", day_time: "morning", people_amount: "1" });
            setActiveUserForReservation(null);
            loadUsers();
        });
    };

    const handleCancelReservation = (userId, reservationId) => {
        fetch(`http://localhost:8080/api/users/${userId}/reservations/${reservationId}/cancel`, { method: "PUT" })
            .then(() => loadUsers());
    };

    const handleEntryChange = (e) => setNewEntry({ ...newEntry, [e.target.name]: e.target.value });

    const handleAddEntrySubmit = (userId) => {
        const isReduced = newEntry.type.toLowerCase() === "reduced";
        fetch(`http://localhost:8080/api/entry/purchase?userId=${userId}&entryTypeId=${newEntry.entryTypeId}&isReduced=${isReduced}`, {
            method: "POST"
        }).then(res => {
            if (!res.ok) throw new Error();
            setNewEntry({ entryTypeId: "", type: "Regular", amount: 1 });
            setActiveUserForEntry(null);
            loadUsers();
        }).catch(() => alert("Nie udało się dodać wejściówki"));
    };

    const toggleReservations = (id) => setVisibleReservations(prev => prev === id ? null : id);
    const toggleEntries = (id) => setVisibleEntries(prev => prev === id ? null : id);

    const filteredUsers = users.filter(user => user.lastname.toLowerCase().includes(searchTerm.toLowerCase()));

    return (
        <div>
            <h3>List of users:</h3>
            <input value={searchTerm} onChange={(e) => setSearchTerm(e.target.value)} />
            <ul>
                {filteredUsers.map(user => (
                    <li key={user.id}>
                        {editingId === user.id ? (
                            <>
                                <input name="firstname" value={editedData.firstname} onChange={handleEditChange} />
                                <input name="lastname" value={editedData.lastname} onChange={handleEditChange} />
                                <input name="registerDate" type="date" value={editedData.registerDate} onChange={handleEditChange} />
                                <button onClick={() => handleEditSave(user)}>Save</button>
                                <button onClick={handleEditCancel}>Cancel</button>
                            </>
                        ) : (
                            <>
                                {user.firstname} {user.lastname} { }
                                <button onClick={() => handleDelete(user.id)}>Delete</button>
                                <button onClick={() => handleEditClick(user)}>Edit</button>
                                <button onClick={() => toggleReservations(user.id)}>
                                    {visibleReservations === user.id ? "Hide reservations" : "Show reservations"}
                                </button>
                                <button onClick={() => toggleEntries(user.id)}>
                                    {visibleEntries === user.id ? "Hide entries" : "Show entries"}
                                </button>
                            </>
                        )}

                        {visibleReservations === user.id && (
                            <>
                                <ul>
                                    {user.reservations?.map(res => (
                                        <li key={res.reservationId}>
                                            {res.date}, {res.day_time}, {res.people_amount} people, status: {res.status}
                                            <button onClick={() => handleCancelReservation(user.id, res.reservationId)}>Cancel</button>
                                        </li>
                                    ))}
                                </ul>
                                {activeUserForReservation === user.id ? (
                                    <form onSubmit={handleAddReservationSubmit}>
                                        <input name="date" type="date" value={newReservation.date} onChange={handleReservationChange} required />
                                        <select name="day_time" value={newReservation.day_time} onChange={handleReservationChange}>
                                            <option value="morning">morning</option>
                                            <option value="noon">noon</option>
                                            <option value="evening">evening</option>
                                        </select>
                                        <input name="people_amount" type="number" min="1" value={newReservation.people_amount} onChange={handleReservationChange} required />
                                        <button type="submit">Add Reservation</button>
                                    </form>
                                ) : (
                                    <button onClick={() => setActiveUserForReservation(user.id)}>Add Reservation</button>
                                )}
                            </>
                        )}

                        {visibleEntries === user.id && (
                            <>
                                <ul>
                                    {user.entries?.map(ent => (
                                        <li key={ent.entryId}>
                                            {ent.type}, amount: {ent.amount}
                                        </li>
                                    ))}
                                </ul>
                                {activeUserForEntry === user.id ? (
                                    <>
                                        <select name="entryTypeId" value={newEntry.entryTypeId} onChange={handleEntryChange}>
                                            <option value="">-- Select Entry Type --</option>
                                            {entryTypes.map(type => (
                                                <option key={type.id} value={type.id}>{type.name}</option>
                                            ))}
                                        </select>
                                        <select name="type" value={newEntry.type} onChange={handleEntryChange}>
                                            <option value="Regular">Regular</option>
                                            <option value="Reduced">Reduced</option>
                                        </select>
                                        <input name="amount" type="number" min="1" value={newEntry.amount} onChange={handleEntryChange} />
                                        <button onClick={() => handleAddEntrySubmit(user.id)}>Add Entry</button>
                                    </>
                                ) : (
                                    <button onClick={() => setActiveUserForEntry(user.id)}>Add Entry</button>
                                )}
                            </>
                        )}
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default UserList;
