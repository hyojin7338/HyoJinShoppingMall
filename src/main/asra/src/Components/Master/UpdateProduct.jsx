import React, { useContext, useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import axios from "axios";
import { MasterContext } from "../../context/MasterContext.jsx";
import "../../styles/UpdateProduct.css"; // 스타일 추가

const UpdateProduct = () => {
    const { master } = useContext(MasterContext);
    const { productId } = useParams(); // URL에서 productId 가져오기
    const navigate = useNavigate();

    // 상품 정보 상태
    const [product, setProduct] = useState({
        name: "",
        contents: "",
        amount: 0,
        sizes: [],
        parentsCategoryId: null,
        childCategoryId: null,
        subCategoryId: null,
        mainImg: "",
        imageUrls: []
    });

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // 상품 데이터 가져오기
    useEffect(() => {
        axios.get(`http://15.164.216.15/api/product/${productId}`,{
            withCredentials: true,
        })
            .then((res) => {
                console.log("📌 상품 데이터:", res.data);
                setProduct(res.data);
            })
            .catch((err) => {
                console.error("❌ 상품 불러오기 실패:", err);
                setError("상품 정보를 불러올 수 없습니다.");
            })
            .finally(() => setLoading(false));
    }, [productId]);

    // 입력값 변경 핸들러
    const handleChange = (e) => {
        const { name, value } = e.target;
        setProduct({ ...product, [name]: value });
    };

    // 상품 수정 요청
    const handleUpdate = async (e) => {
        e.preventDefault();

        // FormData 객체 생성
        const formData = new FormData();

        // updateRequestDto를 JSON 문자열로 변환 후 Blob으로 추가
        formData.append("updateRequestDto", new Blob([JSON.stringify(product)], { type: "application/json" }));

        // 이미지 파일 추가 (이미지가 변경된 경우에만 추가)
        if (product.mainImg instanceof File) {
            formData.append("image", product.mainImg);
        }

        try {
            await axios.put(`http://15.164.216.15/api/master/productUpdate/${productId}`, formData, {
                withCredentials: true,
                headers: {
                    "Content-Type": "multipart/form-data",
                },
            });

            alert("✅ 상품이 수정되었습니다.");
            navigate(`/MasterMain`);
        } catch (err) {
            console.error("❌ 상품 수정 실패:", err);
            alert("상품 수정 중 오류가 발생했습니다.");
        }
    };



    if (loading) return <div className="loading">⏳ 상품 정보를 불러오는 중...</div>;
    if (error) return <div className="error">{error}</div>;

    return (
        <div className="update-product-container">
            <h2>🛠 상품 수정</h2>
            <form onSubmit={handleUpdate}>
                <label>상품명</label>
                <input type="text" name="name" value={product.name} onChange={handleChange} required />

                <label>상품 설명</label>
                <textarea name="contents" value={product.contents} onChange={handleChange} required />

                <label>가격</label>
                <input type="number" name="amount" value={product.amount} onChange={handleChange} required />

                <label>대표 이미지</label>
                <input type="text" name="mainImg" value={product.mainImg} onChange={handleChange} />

                {/* 사이즈 & 재고 입력 필드 추가 */}
                <div className="product-sizes">
                    <strong>사이즈 & 재고 :</strong>
                    {product.sizes.map((size, index) => (
                        <div key={size.productSizeId} className="size-item">
                            <label>사이즈</label>
                            <input
                                type="text"
                                value={size.size}
                                readOnly
                            />

                            <label>재고 (개수)</label>
                            <input
                                type="number"
                                value={size.cnt}
                                onChange={(e) => {
                                    const newSizes = [...product.sizes];
                                    newSizes[index].cnt = parseInt(e.target.value, 10);
                                    setProduct({ ...product, sizes: newSizes });
                                }}
                            />
                        </div>
                    ))}
                </div>


                <button type="submit" className="update-btn">상품 수정</button>


            </form>
        </div>
    );
};

export default UpdateProduct;
