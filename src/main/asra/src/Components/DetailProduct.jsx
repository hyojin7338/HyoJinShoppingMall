import React, {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import axios from "axios";
import "../styles/DetailProduct.css"

const DetailProduct = () => {
    const {productId} = useParams(); //  URL에서 productId 가져오기
    const [product, setProduct] = useState(null);
    const [loading, setLoading] = useState(true);
    const [selectedSize, setSelectedSize] = useState(""); //  선택한 사이즈
    const [quantity, setQuantity] = useState(1); //  선택한 수량 (기본값 1)

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
    const handleAddToCart = () => {
        if (!selectedSize) {
            alert("사이즈를 선택해주세요.");
            return;
        }
        console.log(`장바구니 추가 - 상품ID: ${productId}, 사이즈: ${selectedSize}, 수량: ${quantity}`);
        alert("장바구니에 추가되었습니다!");
    };

    // 찜하기 핸들러
    const handleAddToWishlist = () => {
        console.log(`찜하기 - 상품ID: ${productId}`);
        alert("찜 목록에 추가되었습니다!");
    };


    if (loading) return <p>로딩 중...</p>;
    if (!product) return <p>상품을 찾을 수 없습니다.</p>;

    return (
        <div className="detail-container">
            <h2>{product.name}</h2>
            <img src={product.mainImgUrl} alt={product.name} className="detail-image"/>
            <p>{product.contents}</p>
            <p><strong>가격:</strong> {product.amount}원</p>
            <p><strong>판매자:</strong> {product.businessName}</p>

            {/* 사이즈 선택 드롭박스 */}
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
            <div className="button-grop">
                <button className="buy-button" onClick={handlePurchase}>구매하기</button>
                <button className="cart-button" onClick={handleAddToCart}>장바구니</button>
                <button className="wishlist-button" onClick={handleAddToWishlist}>찜</button>
            </div>
        </div>
    );
};

export default DetailProduct;
