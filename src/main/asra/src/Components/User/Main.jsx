import React, { useEffect, useState, useContext } from "react";
import axios from "axios";
import "../../styles/Main.css";
import { useNavigate } from "react-router-dom";
import { UserContext } from "../../context/UserContext.jsx";
import FooterNav from "./FooterNav.jsx";

const Main = () => {
    const navigate = useNavigate();
    const { user } = useContext(UserContext);
    const { logout } = useContext(UserContext);


    const [categories, setCategories] = useState([]); // 대분류 카테고리
    const [selectedParent, setSelectedParent] = useState(null); // 선택한 대분류
    const [children, setChildren] = useState([]); // 중분류 카테고리
    const [selectedChild, setSelectedChild] = useState(null); // 선택한 중분류
    const [subCategories, setSubCategories] = useState([]); // 소분류 카테고리
    const [selectedSub, setSelectedSub] = useState(null); // 선택한 소분류
    const [products, setProducts] = useState([]); // 상품 목록


    //  1. 대분류(ParentsCategory) 가져오기 (화면 진입 시 자동 조회)
    useEffect(() => {
        axios.get("http://15.164.216.15/api/master/category/parents/find",{
            withCredentials: true,
        })
            .then(response => {
                console.log("대분류 데이터:", response.data);
                setCategories(response.data);
            })
            .catch(error => console.error("대분류 불러오기 실패:", error));
    }, []);

    //  2. 선택한 대분류에 맞는 중분류(ChildCategory) 가져오기
    useEffect(() => {
        if (!selectedParent?.parentsId) return;

        console.log("선택된 대분류 ID:", selectedParent.parentsId);

        axios.get(`http://15.164.216.15/api/master/categoryChild/find/${selectedParent.parentsId}`,{
            withCredentials: true,
        })
            .then(response => {
                console.log("중분류 데이터:", response.data);
                setChildren(response.data);
                setSelectedChild(null);
                setSubCategories([]); // 중분류가 변경되면 소분류 초기화
            })
            .catch(error => console.error("중분류 불러오기 실패:", error));
    }, [selectedParent]);

    //  3. 선택한 중분류에 맞는 소분류(SubCategory) 가져오기
    useEffect(() => {
        if (!selectedChild?.childCategoryId) return;

        console.log("선택된 중분류 ID:", selectedChild.childCategoryId);

        axios.get(`http://15.164.216.15/api/master/categorySub/find/${selectedChild.childCategoryId}`,{
            withCredentials: true,
        })
            .then(response => {
                console.log("소분류 데이터:", response.data);
                setSubCategories(response.data);
                setSelectedSub(null);
            })
            .catch(error => console.error("소분류 불러오기 실패:", error));
    }, [selectedChild]);

    //  4. 선택한 소분류에 맞는 상품 목록 가져오기
    useEffect(() => {
        if (!selectedSub) return;
        axios.get(`http://15.164.216.15/api/products/findByProductAndSubCategory?subCategoryId=${selectedSub.subCategoryId}`,{
            withCredentials: true,
        })
            .then(response => {
                setProducts(response.data);
            })
            .catch(error => console.error("상품 불러오기 실패:", error));
    }, [selectedSub]);


    useEffect(() => {
        console.log("로그인 상태 변경됨:", user);
    }, [user]);

    // 숫자에 콤마를 추가하는 함수
    const formatPrice = (price) => {
        return price.toLocaleString();  // 1,000 단위로 콤마 추가
    };


    return (
        <div className="main-container">

            <nav className="navbar">
                <div className="logo">ASRA</div> {/* 왼쪽 상단 로고 */}
                <div className="search-bar">
                    <input type="text" placeholder="검색어를 입력하세요" />
                    <button>검색</button>
                </div>

                <button
                    onClick={() => {
                        logout();         // 로그아웃 실행
                        navigate("/login"); // 로그인 페이지로 이동
                    }}
                >
                    로그아웃
                </button>
            </nav>

            {/*  1. 대분류 선택 */}
            <div className="parents-category-bar">
                {categories.map(parent => (
                    <button
                        key={parent.parentsId} //  index 제거하고 고유한 parentsId 사용
                        className={`category-item ${selectedParent?.parentsId === parent.parentsId ? "active" : ""}`}
                        onClick={() => {
                            console.log("대분류 버튼 클릭됨:", parent);
                            setSelectedParent(parent);
                        }}
                    >
                        {parent.name}
                    </button>
                ))}
            </div>

            {/*  2. 중분류 선택 */}
            {selectedParent && children.length > 0 && (
                <div className="child-category-bar">
                    {children.map(child => (
                        <button
                            key={child.childCategoryId} // index 제거하고 고유한 childCategoryId 사용
                            className={`sub-category-item ${selectedChild?.childCategoryId === child.childCategoryId ? "active" : ""}`}
                            onClick={() => {
                                console.log("중분류 버튼 클릭됨:", child);
                                setSelectedChild(child);
                            }}
                        >
                            {child.name}
                        </button>
                    ))}
                </div>
            )}

            {/*  3. 소분류 선택 */}
            {selectedChild && subCategories.length > 0 && (
                <div className="sub-category-bar">
                    {subCategories.map(sub => (
                        <button
                            key={sub.subCategoryId} //  index 제거하고 고유한 subCategoryId 사용
                            className={`sub-category-item ${selectedSub?.subCategoryId === sub.subCategoryId ? "active" : ""}`}
                            onClick={() => {
                                console.log("소분류 버튼 클릭됨:", sub);
                                setSelectedSub(sub);
                            }}
                        >
                            {sub.name}
                        </button>
                    ))}
                </div>
            )}

            {/* 4. 소분류까지 선택한 상품 가지고 오기 */}
            <div className="product-grid">
                {products.length > 0 ? (
                    products.map((product, index) => {
                        return (
                            <div
                                key={product.productId ? `product-${product.productId}` : `product-index-${index}`} //  예외 처리
                                className="product-card"
                                onClick={() => navigate(`/product/${product.productId}`)}
                            >
                                <img src={product.mainImgUrl} alt={product.name} className="product-image" />
                                <div className="product-info">
                                    <h3>{product.name}</h3>
                                    <p>{product.contents}</p>
                                    <p><strong>가격:</strong> {formatPrice(product.amount)}원</p>
                                    <p><strong>판매자:</strong> {product.businessName}</p>
                                </div>
                            </div>
                        );
                    })
                ) : (
                    <p className="product-NotFound">선택한 카테고리에 맞는 상품이 없습니다.</p>
                )}

            </div>
            <FooterNav/>
        </div>
    );
};

export default Main;
