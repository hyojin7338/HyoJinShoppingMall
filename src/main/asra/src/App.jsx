import React from "react";
import './App.css'
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Signup from "./Components/Signup.jsx";
import Login from "./Components/Login.jsx";
import MasterSignup from "./Components/MasterSignup.jsx";
import MasterLogin from "./Components/MasterLogin.jsx";
import Main from "./Components/Main.jsx";
import MasterMain from "./Components/MasterMain.jsx"
import DetailProduct from "./Components/DetailProduct.jsx"


function App() {
  return (
      <Router>
        <Routes>
            <Route path="/signup" element={<Signup />} />
            <Route path="/MasterSignup" element={<MasterSignup/>} />
            <Route path="/Login" element={<Login/>} />
            <Route path="/MasterLogin" element={<MasterLogin/>} />
            <Route path="/Main" element={<Main/>} />
            <Route path="/MasterMain" element={<MasterMain/>} />
            <Route Path="/DetailProduct" element={<DetailProduct/> } />
        </Routes>
      </Router>
  );
}

export default App
