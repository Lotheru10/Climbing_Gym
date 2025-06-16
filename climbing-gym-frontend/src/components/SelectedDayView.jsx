import { useState } from "react";

function DayViewPanel() {
    const [date, setDate] = useState("");
    const [view, setView] = useState(null);

    const fetchView = () => {
        fetch(`http://localhost:8080/api/view?date=${date}`)
            .then(res => res.json())
            .then(data => setView(data))
    };

    return (
        <div>
            <h3>Reservations for selected day </h3>
            <input type="date" value={date} onChange={e => setDate(e.target.value)} />
            <button onClick={fetchView}>Show</button>

            {view && (
                <div style={{ marginTop: "20px" }}>
                    <h4>{view.date}</h4>
                    <table border="1" cellPadding="8">
                        <thead>
                        <tr>
                            <th>Time slot</th>
                            <th>Maks time slot</th>
                            <th>Reserved</th>
                            <th>Avaiable</th>
                            <th>Active reservations</th>
                            <th>Number of reservations</th>
                            <th>Details</th>

                        </tr>
                        </thead>
                        <tbody>
                        {["morning", "noon", "evening"].map(time => (
                            <tr key={time}>
                                <td>{time}</td>
                                <td>{view[`${time}_stats`]?.max_slots}</td>
                                <td>{view[`${time}_stats`]?.reserved_slots}</td>
                                <td>{view[`${time}_stats`]?.available_slots}</td>
                                <td>{view[`${time}_stats`]?.active_reservations_count}</td>
                                <td>{view[`${time}_reservations`]?.length || 0}</td>
                                <td>
                                    <ul>
                                        {view[`${time}_reservations`]?.map(res => (
                                            <li key={res.reservation_id}>
                                                {res.user_name} ({res.people_amount}), status: {res.status}
                                            </li>
                                        ))}
                                    </ul>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            )}
        </div>
    );
}

export default DayViewPanel;
