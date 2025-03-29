import React, { useContext, useEffect, useState } from "react";
import axios from "axios";
import { MasterContext } from "../../context/MasterContext.jsx";
import { useNavigate } from "react-router-dom";
import "../../styles/MasterMain.css"

const MasterMain = () => {
    const { master, logout } = useContext(MasterContext);
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        if (master?.masterId) {
            setLoading(true);
            axios.get(`http://localhost:8080/product/Master/${master.masterId}`)
                .then((res) => {
                    console.log("📌 API 응답:", res.data);
                    setProducts(Array.isArray(res.data) ? res.data : []);
                })
                .catch((err) => console.error("❌ 상품 불러오기 실패:", err))
                .finally(() => setLoading(false));
        }
    }, [master]);

    useEffect(() => {
        console.log("로그인 상태:", master);
    }, [master]);

    const handleLogout = async () => {
        try {
            await logout();
            navigate("/MasterLogin");
        } catch (error) {
            console.error("❌ 로그아웃 실패:", error);
        }
    };

    return (
        <div className="min-h-screen p-4">
            {/* 네비게이션 바 */}
            <nav className="flex justify-between items-center py-4 border-b border-gray-600">
                <div className="text-2xl font-bold text-white">Asra</div>
                <button className="btn" onClick={handleLogout}>로그아웃</button>

                {/* 상품 등록 버튼 */}
                <div className="Create-Product">
                    <button
                        className="btn btn-primary"
                        onClick={() => navigate("/createProduct")}
                    >
                        ➕ 상품 등록

                    </button>
                </div>

                <div className="QnA">
                    <button
                        className="QnA-btn"
                        onClick={() => navigate("/MasterQuestion")}
                        >

                        ❓문의사항 확인하기

                    </button>
                </div>

            </nav>

            {/* 페이지 제목 */}
            <h1 className="text-2xl font-bold my-6 text-center">📦 판매자 상품 목록</h1>


            {/* 로딩 상태 메시지 */}
            {loading && <div className="text-message">⏳ 상품을 불러오는 중입니다...</div>}

            {/* 상품 목록 */}
            <div className="grid-container">
                {products.map((product) => (
                    <div key={product.productId}
                         className="product-card"
                         onClick={() => navigate(`/productSetting/${product.productId}`)}
                    >
                        <div className="product-image">
                            <img src={product.mainImg || "/no-image.png"} alt={product.name} />
                        </div>
                        <div className="product-info">
                            <div className="product-name">{product.name}</div>
                            <div className="product-price">₩{product.amount.toLocaleString()}</div>
                            <div className="product-desc">{product.contents}</div>
                            <div className="product-qty">{product.size}</div>

                            <div className="product-sizes">
                                <strong>사이즈 & 재고 :</strong>
                                <ul>
                                    {product.sizes.map((size) => (
                                        <li key={size.productSizeId}>
                                            {size.size}----------{size.cnt}개 남음
                                        </li>
                                    ))}
                                </ul>
                            </div>
                        </div>
                    </div>
                ))}
            </div>

            {/* 상품 없음 메시지 */}
            {!loading && products.length === 0 && <div className="text-message">📭 등록된 상품이 없습니다.</div>}
        </div>
    );
};

export default MasterMain;
