import { useState, useEffect } from "react";

function MonthViewPanel() {
    const [view, setView] = useState([]);

    useEffect(() => {
        fetch("http://localhost:8080/api/view/month")
            .then(res => res.json())
            .then(data => setView(data))
    }, []);

    if (!view.length) return <p>Loading...</p>;

    return (
        <div>
            <h3>Reservations for the next 30 days</h3>
            {view.map(day => (
                <div key={day.date} style={{ marginBottom: '40px' }}>
                    <h4>{day.date}</h4>
                    <table border="1" cellPadding="8">
                        <thead>
                        <tr>
                            <th>Time slot</th>
                            <th>Maks time slot</th>
                            <th>Reserved</th>
                            <th>Avaiable</th>
                            <th>Active reservations</th>
                            <th>Number of reservations</th>
                        </tr>
                        </thead>
                        <tbody>
                        {["morning", "noon", "evening"].map(time => (
                            <tr key={time}>
                                <td>{time}</td>
                                <td>{day[`${time}_stats`].max_slots}</td>
                                <td>{day[`${time}_stats`].reserved_slots}</td>
                                <td>{day[`${time}_stats`].available_slots}</td>
                                <td>{day[`${time}_stats`].active_reservations_count}</td>
                                <td>{day[`${time}_reservations`].length}</td>
                                <td>
                                    <ul>
                                        {day[`${time}_reservations`].map(res => (
                                            <li key={res.reservation_id}>
                                                {res.user_name} ({res.people_amount} osób), status: {res.status}
                                            </li>
                                        ))}
                                    </ul>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            ))}
        </div>
    );
}

export default MonthViewPanel;
