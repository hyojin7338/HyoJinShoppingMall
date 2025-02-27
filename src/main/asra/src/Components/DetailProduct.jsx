import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import "../styles/DetailProduct.css"

const DetailProduct = () => {
    const { productId } = useParams(); // ✅ URL에서 productId 가져오기
    const [product, setProduct] = useState(null);
    const [loading, setLoading] = useState(true);

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

    if (loading) return <p>로딩 중...</p>;
    if (!product) return <p>상품을 찾을 수 없습니다.</p>;

    return (
        <div className="detail-container">
            <h2>{product.name}</h2>
            <img src={product.mainImgUrl} alt={product.name} className="detail-image" />
            <p>{product.contents}</p>
            <p><strong>가격:</strong> {product.amount}원</p>
            <p><strong>판매자:</strong> {product.businessName}</p>

            <h3>사이즈 선택</h3>
            <div className="size-options">
                {product.sizes.map(size => (
                    <button key={size.size} className="size-button">
                        {size.size} ({size.cnt}개 남음)
                    </button>
                ))}
            </div>
        </div>
    );
};

export default DetailProduct;
