import React from "react";
import './App.css'
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Signup from "./Components/Signup.jsx";
import Login from "./Components/Login.jsx";
import AdminSignup from "./Components/AdminSignup.jsx";


function App() {
  return (
      <Router>
        <Routes>
          <Route path="/signup" element={<Signup />} />
          <Route path="/AdminSignup" element={<AdminSignup/>} />
          <Route path="/Login" element={<Login/>} />
        </Routes>
      </Router>
  );
}

export default App
