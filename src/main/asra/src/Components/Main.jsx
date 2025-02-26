import React, { useEffect, useState } from "react";
import axios from "axios";
import "../styles/Main.css"; // 스타일 적용을 위한 CSS 파일

const Main = () => {
    const [categories, setCategories] = useState([]); // 대분류 카테고리
    const [selectedParent, setSelectedParent] = useState(null); // 선택한 대분류
    const [children, setChildren] = useState([]); // 중분류 카테고리
    const [selectedChild, setSelectedChild] = useState(null); // 선택한 중분류
    const [subCategories, setSubCategories] = useState([]); // 소분류 카테고리
    const [selectedSub, setSelectedSub] = useState(null); // 선택한 소분류

    //  1. 대분류(ParentsCategory) 가져오기 (화면 진입 시 자동 조회)
    useEffect(() => {
        axios.get("http://localhost:8080/master/category/parents/find")
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

        axios.get(`http://localhost:8080/master/categoryChild/find/${selectedParent.parentsId}`)
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

        axios.get(`http://localhost:8080/master/categorySub/find/${selectedChild.childCategoryId}`)
            .then(response => {
                console.log("소분류 데이터:", response.data);
                setSubCategories(response.data);
                setSelectedSub(null);
            })
            .catch(error => console.error("소분류 불러오기 실패:", error));
    }, [selectedChild]);

    return (
        <div className="main-container">
            <h2>카테고리 선택</h2>

            {/*  1. 대분류 선택 */}
            <div className="parents-category-bar">
                {categories.map((parent, index) => (
                    <button
                        key={parent.parentsId || index}
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
                    {children.map((child, index) => (
                        <button
                            key={child.childCategoryId || index} // `childCategoryId` 사용
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
                    {subCategories.map((sub, index) => (
                        <button
                            key={sub.subCategoryId || index} // `subCategoryId` 사용
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
        </div>
    );
};

export default Main;
