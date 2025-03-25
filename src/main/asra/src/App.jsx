import React, {useContext} from "react";
import './App.css'
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import Signup from "./Components/User/Signup.jsx";
import Login from "./Components/User/Login.jsx";
import MasterSignup from "./Components/Master/MasterSignup.jsx";
import MasterLogin from "./Components/Master/MasterLogin.jsx";
import Main from "./Components/User/Main.jsx";
import MasterMain from "./Components/Master/MasterMain.jsx";
import DetailProduct from "./Components/User/DetailProduct.jsx";
import ProtectedRoute from "./Components/User/ProtectedRoute.jsx"; // 로그인 필요 페이지 보호
import MyPage from "./Components/User/MyPage.jsx";
import Cart from "./Components/User/Cart.jsx";
import Favorite from "./Components/User/Favorite.jsx";
import MyCoupons from "./Components/User/MyCoupons.jsx"; // 쿠폰 페이지 추가
import Checkout from "./Components/User/Checkout.jsx";
import ChangeAddress from "./Components/User/ChangeAddress.jsx";
import AddAddress from "./Components/User/AddAddress.jsx";
import Order from "./Components/User/Order.jsx"
import ProductQuestion from "./Components/User/ProductQuestion.jsx";
import MyInquiry from "./Components/User/MyInquiry.jsx";
import AdminByQuestion from "./Components/User/AdminByQuestion.jsx";
import CreateProduct from "./Components/Master/CreateProduct.jsx";
import {UserContext} from "./context/UserContext"; // 유저 정보 가져오기
import {MasterContext} from "./context/MasterContext.jsx";


function App() {
    const {user} = useContext(UserContext);
    const {master} = useContext(MasterContext);
    return (

        <Router>
            <Routes>
                {/* 로그인 없이 접근 가능 */}
                <Route path="/signup" element={<Signup/>}/>
                <Route path="/MasterSignup" element={<MasterSignup/>}/>
                <Route path="/Login" element={<Login/>}/>
                <Route path="/MasterLogin" element={<MasterLogin/>}/>
                <Route path="/Main" element={<Main/>}/>
                <Route path="/product/:productId" element={<DetailProduct/>}/>

                {/*  유저가 로그인 후에만 접근 가능 */}
                <Route element={<ProtectedRoute user={user}/>}>
                    <Route path="/MyPage" element={<MyPage/>}/>
                    <Route path="/Cart" element={<Cart/>}/>
                    <Route path="/Favorite" element={<Favorite/>}/>
                    <Route path="/MyCoupons" element={<MyCoupons/>}/>
                    <Route path="/Checkout/:productId" element={<Checkout/>}/>
                    <Route path="/ChangeAddress" element={<ChangeAddress/>}/>
                    <Route path="/AddAddress" element={<AddAddress/>}/>
                    <Route path="/Orders" element={<Order/>}/>
                    <Route path="/question/:productId" element={<ProductQuestion/>}/>
                    <Route path="/MyInquiry" element={<MyInquiry/>}/>
                    <Route path="/AdminByQuestion" element={<AdminByQuestion/>}/>
                </Route>
                {/*쇼핑몰 판매자가 로그인후 접근 가능 */}
                <Route>
                    <Route path="/MasterMain" element={<MasterMain/>}/>
                    <Route path="/CreateProduct" element={<CreateProduct/>}/>
                </Route>

            </Routes>
        </Router>

    );
}

export default App
