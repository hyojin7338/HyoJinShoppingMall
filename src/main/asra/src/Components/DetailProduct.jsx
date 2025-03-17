import React, {useContext, useEffect, useState} from "react";
import {useParams, useNavigate} from "react-router-dom";
import {UserContext} from "../context/UserContext";
import axios from "axios";
import "../styles/DetailProduct.css"
import FooterNav from "../Components/FooterNav.jsx";

const DetailProduct = () => {
    const {user} = useContext(UserContext);
    const {productId} = useParams(); //  URL에서 productId 가져오기
    const navigate = useNavigate();

    const [product, setProduct] = useState(null);
    const [productSize, setProductSize] = useState(null);
    const [loading, setLoading] = useState(true);

    const [quantity, setQuantity] = useState(1); //  선택한 수량 (기본값 1)
    const [isFavorite, setIsFavorite] = useState(false); // 찜여부 상태 추가

    // 사용자 정보에서 cartId 가져오기
    const userData = JSON.parse(localStorage.getItem("user"));
    const cartId = userData?.cartId;
    const userId = userData?.userId;

    const [selectedSize, setSelectedSize] = useState(""); //  선택한 사이즈
    const [selectedProductSizeId, setSelectedProductSizeId] = useState(null); // 선택한 사이즈의 ID 추가


    useEffect(() => {
        axios.get(`http://localhost:8080/product/${productId}`)
            .then(response => {
                console.log("상품 상세 데이터:", response.data);
                setProduct(response.data);
                setProductSize(response.data.sizes);
                setLoading(false);
            })
            .catch(error => {
                console.error("상품 상세 정보 불러오기 실패:", error);
                setLoading(false);
            });
        if (userId) {
            checkIfFavorite(); // 로그인된 경우 찜 여부 확인
        }
    }, [productId, userId]);

    // 사용자가 이미 찜을 하였는지 확인하기
    const checkIfFavorite = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/favorite/check?userId=${userId}&productId=${productId}`);
            setIsFavorite(response.data); // `true`면 이미 찜한 상태
        } catch (error) {
            console.error("찜 여부 확인 실패:", error);
        }
    };

    // 사이즈 변경 핸들러
    const handleSizeChange = (event) => {
        const selectedSizeValue = event.target.value;
        setSelectedSize(event.target.value);


        console.log(" selectedSizeValue:", selectedSizeValue);
        console.log(" productSize:", productSize);


        const selectedProductSize = productSize?.find(size => size.size === selectedSizeValue);

        if (selectedProductSize) {
            console.log(" 선택된 productSize 객체:", selectedProductSize);
            setSelectedProductSizeId(selectedProductSize.id);
        } else {
            console.log(" 일치하는 사이즈 없음 - productSizeId 못 찾음");
        }
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

        const productSizeId = productSize?.find(size => size.size === selectedSize)?.productSizeId;

        console.log(" 선택된 productSizeId:", selectedProductSizeId); // 콘솔 로그 추가
        console.log(" 선택된 사이즈:", selectedSize);
        console.log(" 선택한 수량:", quantity);

        navigate(`/checkout/${productId}`, {
            state: {
                product,
                selectedSize,
                quantity,
                productSizeId: productSizeId
            }
        });
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

            setIsFavorite(true); //  (찜 상태 변경)
            alert("찜 목록에 추가되었습니다.");

        } catch (error) {
            console.error("찜하기 실패!", error);
            alert(error.response?.data.message || "찜하기 실패!! ");
        }
    };

    // 찜하기 취소
    const handleRemoveFromWishlist = async () => {
        if (!userId) {
            alert("로그인이 필요합니다.");
            return;
        }

        try {
            await axios.delete(`http://localhost:8080/favorite/remove/${userId}/${productId}`);
            setIsFavorite(false);
            alert("찜 목록에서 삭제되었습니다.");
        } catch (error) {
            console.error("찜 삭제 실패!", error);
            alert(error.response?.data.message || "찜 삭제 실패!! ");
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
                <button className="buy-button" onClick={handlePurchase}>구매하기</button>
                <button className="cart-button" onClick={handleAddToCart}>장바구니</button>
                {isFavorite ? (
                    <button className="wishlist-button remove" onClick={handleRemoveFromWishlist}>찜 취소</button>
                ) : (
                    <button className="wishlist-button" onClick={handleAddToWishlist}>찜</button>
                )}
            </div>
            <FooterNav/>
        </div>
    );
};

export default DetailProduct;
