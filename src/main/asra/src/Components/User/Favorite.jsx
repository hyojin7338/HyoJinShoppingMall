import React, { useEffect, useState, useContext } from "react";
import axios from "axios";
import { UserContext } from "../../context/UserContext.jsx";
import { useNavigate } from "react-router-dom";
import "../../styles/Favorite.css";
import FooterNav from "./FooterNav.jsx";

const Favorite = () => {
    const { user } = useContext(UserContext);
    const navigate = useNavigate();
    const [favorites, setFavorites] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        if (!user) {
            alert("로그인이 필요합니다.");
            navigate("/login");
            return;
        }

        const fetchFavorites = async () => {
            try {
                const response = await axios.get(`http://15.164.216.15:8080/favorite/find/${user.userId}`);
                console.log("찜 목록 데이터:", response.data);
                setFavorites(response.data);
            } catch (error) {
                console.error("찜 목록 조회 실패:", error);
            } finally {
                setLoading(false);
            }
        };

        fetchFavorites();
    }, [user, navigate]);

    const handleRemoveFavorite = async (productId) => {
        try {
            await axios.delete(`http://15.164.216.15:8080/favorite/remove/${user.userId}/${productId}`);
            alert("찜 목록에서 삭제되었습니다.");
            setFavorites(favorites.filter(item => item.favoriteId !== productId));
        } catch (error) {
            console.error("찜 삭제 실패:", error);
            alert("삭제 실패!");
        }
    };

    const handleAddToCart = async (productId) => {
        try {
            const response = await axios.post(`http://15.164.216.15:8080/favorite/add-to-cart/${user.userId}/${productId}`);
            console.log("장바구니 추가 성공:", response.data);
            alert("장바구니에 추가되었습니다.");
        } catch (error) {
            console.error("장바구니 추가 실패:", error);
            alert(error.response?.data.message || "장바구니 추가 실패!");
        }
    };

    if (loading) return <p>로딩 중...</p>;
    if (!favorites.length) return <p>찜한 상품이 없습니다.</p>;

    return (
        <div className="favorite-container">
            <h2>찜 목록</h2>
            <div className="favorite-list">
                {favorites.map(favorite => (
                    <div key={favorite.favoriteId} className="favorite-item">
                        <img src={favorite.mainImgUrl || "/default.jpg"} alt={favorite.productName} className="favorite-image" />
                        <div className="favorite-info">
                            <h3>{favorite.productName}</h3>
                            <p>찜한 날짜: {new Date(favorite.favoriteAt).toLocaleDateString()}</p>
                        </div>
                        <div className="favorite-actions">
                            <button className="cart-button" onClick={() => handleAddToCart(favorite.favoriteId)}>장바구니</button>
                            <button className="remove-button" onClick={() => handleRemoveFavorite(favorite.favoriteId)}>삭제</button>
                        </div>
                    </div>
                ))}
            </div>
            <FooterNav />
        </div>
    );
};

export default Favorite;
