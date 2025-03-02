import React, { useEffect, useState, useContext } from "react";
import axios from "axios";
import { UserContext } from "../context/UserContext";
import { useNavigate } from "react-router-dom";
import "../styles/Favorite.css"; // 스타일 파일 추가
import FooterNav from "../Components/FooterNav.jsx";

const Favorite = () => {
    const { user } = useContext(UserContext);
    const navigate = useNavigate();
    const [favorites, setFavorites] = useState([]); // 찜한 상품 목록
    const [loading, setLoading] = useState(true);

    //  찜 목록 조회
    useEffect(() => {
        if (!user) {
            alert("로그인이 필요합니다.");
            navigate("/login");
            return;
        }

        axios.get(`http://localhost:8080/favorite/find/${user.userId}`)
            .then(response => {
                console.log("찜 목록 데이터:", response.data);
                setFavorites(response.data);
                setLoading(false);
            })
            .catch(error => {
                console.error("찜 목록 조회 실패:", error);
                setLoading(false);
            });
    }, [user, navigate]);

    //  찜 목록에서 상품 삭제
    const handleRemoveFavorite = async (favoriteId) => {
        try {
            await axios.delete(`http://localhost:8080/favorite/remove/${user.userId}/${favoriteId}`);
            setFavorites(favorites.filter(item => item.favoriteId !== favoriteId)); // UI 업데이트
            alert("찜 목록에서 삭제되었습니다.");
        } catch (error) {
            console.error("찜 삭제 실패:", error);
            alert("삭제 실패!");
        }
    };

    // 찜 목록에서 장바구니로 추가
    const handleAddToCart = async (favoriteId) => {
        try {
            const response = await axios.post(`http://localhost:8080/cart/${user.cartId}/add-product/${favoriteId}`, {
                qty: 1
            });
            console.log("장바구니 추가 성공:", response.data);
            alert("장바구니에 추가되었습니다.");
        } catch (error) {
            console.error("장바구니 추가 실패:", error);
            alert("장바구니 추가 실패!");
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
                            <p><strong>찜한 날짜:</strong> {new Date(favorite.favoriteAt).toLocaleDateString()}</p>
                        </div>
                        <div className="favorite-actions">
                            <button className="cart-button" onClick={() => handleAddToCart(favorite.favoriteId)}>장바구니 추가</button>
                            <button className="remove-button" onClick={() => handleRemoveFavorite(favorite.favoriteId)}>삭제</button>
                        </div>
                    </div>
                ))}
            </div>
            <FooterNav/>
        </div>
    );
};


export default Favorite;
