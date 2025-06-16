import { useState, useEffect } from "react";

function TodayViewPanel() {
    const [view, setView] = useState(null);

    useEffect(() => {
        fetch("http://localhost:8080/api/view/today")
            .then(res => res.json())
            .then(data => setView(data))
    }, []);

    if (!view) return <p>Loading...</p>;

    const times = ["morning", "noon", "evening"];

    return (
        <div>
            <h3>Reservations for today – {view.date}</h3>
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
                {times.map(time => (
                    <tr key={time}>
                        <td>{time}</td>
                        <td>{view[`${time}_stats`].max_slots}</td>
                        <td>{view[`${time}_stats`].reserved_slots}</td>
                        <td>{view[`${time}_stats`].available_slots}</td>
                        <td>{view[`${time}_stats`].active_reservations_count}</td>
                        <td>{view[`${time}_reservations`].length}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

export default TodayViewPanel;
