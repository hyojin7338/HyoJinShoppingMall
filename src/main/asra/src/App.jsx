import React, { useContext }  from "react";
import './App.css'
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Signup from "./Components/Signup.jsx";
import Login from "./Components/Login.jsx";
import MasterSignup from "./Components/MasterSignup.jsx";
import MasterLogin from "./Components/MasterLogin.jsx";
import Main from "./Components/Main.jsx";
import MasterMain from "./Components/MasterMain.jsx";
import DetailProduct from "./Components/DetailProduct.jsx";
import ProtectedRoute from "./Components/ProtectedRoute"; // 로그인 필요 페이지 보호
import MyPage from "./Components/MyPage.jsx";
import Cart from "./Components/Cart.jsx";
import Favorite from "./Components/Favorite.jsx";
import MyCoupons from "./Components/MyCoupons.jsx"; // 쿠폰 페이지 추가
import Checkout from "./Components/Checkout.jsx";
import ChangeAddress from "./Components/ChangeAddress.jsx";
import AddAddress from "./Components/AddAddress.jsx";
import Order from "./Components/Order.jsx"
import { UserContext  } from "./context/UserContext"; // 유저 정보 가져오기


function App() {
  const { user } = useContext(UserContext);
  return (

      <Router>
          <Routes>
              {/* 로그인 없이 접근 가능 */}
              <Route path="/signup" element={<Signup />} />
              <Route path="/MasterSignup" element={<MasterSignup />} />
              <Route path="/Login" element={<Login />} />
              <Route path="/MasterLogin" element={<MasterLogin />} />
              <Route path="/Main" element={<Main />} />
              <Route path="/product/:productId" element={<DetailProduct />} />

              {/*  로그인 후에만 접근 가능 */}
              <Route element={<ProtectedRoute user={user} />}>
                  <Route path="/MasterMain" element={<MasterMain />} />
                  <Route path="/MyPage" element={<MyPage />} />
                  <Route path="/Cart" element={<Cart />} />
                  <Route path="/Favorite" element={<Favorite />} />
                  <Route path="/MyCoupons" element={<MyCoupons />} />
                  <Route path="/Checkout/:productId" element={<Checkout />} />
                  <Route path="/ChangeAddress" element={<ChangeAddress />} />
                  <Route path="/AddAddress" element={<AddAddress />} />
                  <Route path="/Orders" element={<Order />} />
              </Route>
          </Routes>
      </Router>

  );
}

export default App
