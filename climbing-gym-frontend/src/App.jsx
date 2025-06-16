import { useState } from 'react'
import './App.css'
import UserList from "./components/UserList.jsx"
import AddUser from "./components/AddUser.jsx";
import TodayPanelView from "./components/TodayPanelView.jsx";
import MonthViewPanel from "./components/MonthViewPanel.jsx";


const VIEWS = {
    HOME: "home",
    USERS_PAGE: 'users_page',
    RESERVATION_PAGE: 'reservation_page',
    TODAY: 'today',
    MONTH: 'month',
    USERS: "users",
    ADD_USER: "add_user",
};

function App() {
    const [view, setView] = useState(VIEWS.HOME);

    return (
        <div className="container">
            <h1>Climbing Gym</h1>

            {view === VIEWS.HOME && (
                <>
                    <button onClick={() => setView(VIEWS.USERS_PAGE)}>Users</button>
                    <button onClick={() => setView(VIEWS.RESERVATION_PAGE)}>Reservations</button>


                </>)}

            {view === VIEWS.USERS_PAGE && (
                <>
                <button onClick={() => setView(VIEWS.USERS)}>Shows users</button> <br/>
                    <button onClick={() => setView(VIEWS.ADD_USER)}>Add User</button> <br/>
                    <button onClick={() => setView(VIEWS.HOME)}>Go back</button>


                </>)}

            {view === VIEWS.USERS && (
                <>
                    <UserList/>
                    <button onClick={() => setView(VIEWS.USERS_PAGE)}>Go back</button>
                </>
            )}
            {view === VIEWS.ADD_USER && (
                <>
                    <AddUser/>
                    <button onClick={() => setView(VIEWS.USERS_PAGE)}>Go back</button>
                </>
            )}



            {view === VIEWS.RESERVATION_PAGE && (
                <>
                    <button onClick={() => setView(VIEWS.TODAY)}>Show reservation for today</button>
                    <br/>
                    <button onClick={() => setView(VIEWS.MONTH)}>Show reservation for the past month</button>
                    <br/>

                    <button onClick={() => setView(VIEWS.HOME)}>Go back</button>


                </>)}
            {view === VIEWS.TODAY && (
                <>
                    <TodayPanelView/>
                    <button onClick={() => setView(VIEWS.RESERVATION_PAGE)}>Go back</button>
                </>
            )}
            {view === VIEWS.MONTH && (
                <>
                    <MonthViewPanel/>
                    <button onClick={() => setView(VIEWS.RESERVATION_PAGE)}>Go back</button>
                </>
            )}
        </div>
    );

}

export default App
