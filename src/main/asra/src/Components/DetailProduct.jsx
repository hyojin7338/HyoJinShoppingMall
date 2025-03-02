import React, {useContext, useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import { UserContext } from "../context/UserContext";
import axios from "axios";
import "../styles/DetailProduct.css"
import FooterNav from "../Components/FooterNav.jsx";

const DetailProduct = () => {
    const { user } = useContext(UserContext);

    const {productId} = useParams(); //  URL에서 productId 가져오기
    const [product, setProduct] = useState(null);
    const [loading, setLoading] = useState(true);
    const [selectedSize, setSelectedSize] = useState(""); //  선택한 사이즈
    const [quantity, setQuantity] = useState(1); //  선택한 수량 (기본값 1)

    // 사용자 정보에서 cartId 가져오기
    const userData = JSON.parse(localStorage.getItem("user"));
    const cartId = userData?.cartId;
    const userId = userData?.userId;

    useEffect(() => {
        axios.get(`http://localhost:8080/product/${productId}`)
            .then(response => {
                console.log("상품 상세 데이터:", response.data);
                setProduct(response.data);
                setLoading(false);
            })
            .catch(error => {
                console.error("상품 상세 정보 불러오기 실패:", error);
                setLoading(false);
            });
    }, [productId]);

    // 사이즈 변경 핸들러
    const handleSizeChange = (event) => {
        setSelectedSize(event.target.value);
    };

    // 수량 변경 핸들러
    const handleQuantityChange = (event) => {
        const value = parseInt(event.target.value, 10);
        if (value > 0) {
            setQuantity(value);
        }
    };

    // 구매하기 핸들러
    const handlePurchase = () => {
        if (!selectedSize) {
            alert("사이즈를 선택해주세요.");
            return;
        }
        console.log(`구매하기 - 상품ID: ${productId}, 사이즈: ${selectedSize}, 수량: ${quantity}`);
        alert("구매가 완료되었습니다!");
    };


    // 장바구니 추가 핸들러
    const handleAddToCart = async () => {
        if (!selectedSize) {
            alert("사이즈를 선택해주세요.");
            return;
        }
        if (!cartId) {
            alert("장바구니가 없습니다.");
            return;
        }

        try {
            const response = await axios.post(
                `http://localhost:8080/${cartId}/add-product/${productId}?qty=${quantity}`
            );

            console.log("장바구니 추가 성공:", response.data);
            alert("장바구니에 추가되었습니다!");
        } catch (error) {
            console.error("장바구니 추가 실패:", error);
            alert("장바구니 추가에 실패했습니다.");
        }
    };


    // 찜하기 핸들러
    const handleAddToWishlist = async () => {
        if (!userId) {
            alert("로그인이 필요합니다.");
            return;
        }

        try {
            const response = await axios.post("http://localhost:8080/favorite/add", {
                userId: userId,
                productId: productId
            });


            console.log("찜하기 성공!! ", response.data);
            alert("찜목록 추가 되었습니다.");

        } catch (error) {
        console.error("찜하기 실패!", error);
        alert(error.response?.data.message || "찜하기 실패!! ");
        }
    };

    useEffect(() => {
        console.log("로그인 상태 변경됨:", user);
    }, [user]);

        if (loading) return <p>로딩 중...</p>;
    if (!product) return <p>상품을 찾을 수 없습니다.</p>;

    return (
        <div className="detail-container">
            <h2>{product.name}</h2>
            <img src={product.mainImgUrl} alt={product.name} className="detail-image"/>
            <p>{product.contents}</p>
            <p><strong>가격:</strong> {product.amount}원</p>
            <p><strong>판매자:</strong> {product.businessName}</p>

            {/* 사이즈 선택 드롭박스 드롭박스 -> (Select Box) */}
            <h3>사이즈 선택</h3>
            <select value={selectedSize} onChange={handleSizeChange} className="size-dropdown">
                <option value="">사이즈를 선택하세요</option>
                {product.sizes.map(size => (
                    <option key={size.size} value={size.size}>
                        {size.size} ({size.cnt}개 남음)
                    </option>
                ))}
            </select>

            {/*수량 선택하기 */}
            <h3>수량 선택</h3>
            <input
              type="number"
              value={quantity}
              onChange={handleQuantityChange}
              min="1"
              className="quantity-input"
            />

        {/*구매, 장바구니,  찜버튼*/}
            <div className="button-group">
                <button className="buy-button">구매하기</button>
                <button className="cart-button" onClick={handleAddToCart}>장바구니</button>
                <button className="wishlist-button">찜</button>
            </div>
            <FooterNav/>
        </div>
    );
};

export default DetailProduct;
