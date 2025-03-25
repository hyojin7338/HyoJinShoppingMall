import {useState, useEffect, useContext} from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { MasterContext } from "../../context/MasterContext.jsx";

const CreateProduct = () => {
    const navigate = useNavigate();

    // 판매자 정보
    const { master, logout } = useContext(MasterContext);

    // 카테고리 상태 변수
    const [categories, setCategories] = useState([]); // 대분류
    const [selectedParent, setSelectedParent] = useState(null); // 선택한 대분류
    const [children, setChildren] = useState([]); // 중분류
    const [selectedChild, setSelectedChild] = useState(null); // 선택한 중분류
    const [subCategories, setSubCategories] = useState([]); // 소분류
    const [selectedSub, setSelectedSub] = useState(null); // 선택한 소분류

    // 상품 정보 상태 변수
    const [productCode, setProductCode] = useState("");  // 상품 코드
    const [isCodeValid, setIsCodeValid] = useState(null); // 코드 중복 여부
    const [productName, setProductName] = useState("");
    const [productAmount, setProductAmount] = useState("");
    const [productContents, setProductContents] = useState("");
    const [mainImage, setMainImage] = useState(null);
    const [subImages, setSubImages] = useState([]);

    const [sizes, setSizes] = useState([
        { size: "S", cnt: 0 },
        { size: "M", cnt: 0 },
        { size: "L", cnt: 0 },
        { size: "XL", cnt: 0 },
        { size: "XXL", cnt: 0 },
    ]);

    const handleSizeChange = (index, value) => {
        const newSizes = [...sizes]; // 기존 배열 복사
        newSizes[index] = { ...newSizes[index], cnt: value || 0 }; // 새로운 값 적용
        setSizes(newSizes); // 상태 업데이트
    };


    // 1. 대분류 가져오기
    useEffect(() => {
        axios.get("http://localhost:8080/master/category/parents/find")
            .then(response => setCategories(response.data))
            .catch(error => console.error("대분류 불러오기 실패:", error));
    }, []);

    useEffect(() => {
        console.log("로그인 상태:", master);
    }, [master]);

    // 2. 선택한 대분류에 맞는 중분류 가져오기
    useEffect(() => {
        if (!selectedParent) return;
        axios.get(`http://localhost:8080/master/categoryChild/find/${selectedParent}`)
            .then(response => {
                setChildren(response.data);
                setSelectedChild(null);
                setSubCategories([]); // 중분류 변경 시 소분류 초기화
            })
            .catch(error => console.error("중분류 불러오기 실패:", error));
    }, [selectedParent]);

    // 3. 선택한 중분류에 맞는 소분류 가져오기
    useEffect(() => {
        if (!selectedChild) return;
        axios.get(`http://localhost:8080/master/categorySub/find/${selectedChild}`)
            .then(response => {
                setSubCategories(response.data);
                setSelectedSub(null);
            })
            .catch(error => console.error("소분류 불러오기 실패:", error));
    }, [selectedChild]);

    // 4. 상품 코드 중복 확인
    const handleCodeCheck = async () => {
        if (!productCode.trim()) {
            alert("상품 코드를 입력해주세요.");
            return;
        }
        try {
            const response = await axios.get(`http://localhost:8080/Product/codeCheck?code=${productCode}`);
            if (response.status === 200) {
                alert("사용 가능한 상품 코드입니다.");
                setIsCodeValid(true);
            }
        } catch (error) {
            alert("이미 존재하는 상품 코드입니다.");
            setIsCodeValid(false);
        }
    };

    // 5. 상품 등록 API 호출
    const handleCreateProduct = async () => {
        console.log("선택한 대분류:", selectedParent);
        console.log("선택한 중분류:", selectedChild);
        console.log("선택한 소분류:", selectedSub);
        console.log("마스터Id" , master)

        if (!selectedParent || !selectedChild || !selectedSub) {
            alert("카테고리를 모두 선택해주세요.");
            return;
        }

        const formData = new FormData();
        formData.append("requestDto", new Blob([JSON.stringify({
            code: productCode,
            name: productName,
            amount: productAmount,
            contents: productContents,
            parentsCategoryId: selectedParent,
            childCategoryId: selectedChild,
            subCategoryId: selectedSub,
            masterId: master?.masterId,
            sizes: sizes.filter(size => size.cnt > 0)
        })], { type: "application/json" }));



        if (mainImage) formData.append("mainImage", mainImage);
        subImages.forEach((image) => formData.append("subImages", image));

        try {
            const response = await axios.post("http://localhost:8080/master/createProduct", formData, {
                headers: { "Content-Type": "multipart/form-data" }
            });
            alert("상품이 등록되었습니다!");
            navigate("/MasterMain");
        } catch (error) {
            console.error("상품 등록 실패:", error);
            alert("상품 등록에 실패했습니다.");
        }
    };

    return (
        <div className="min-h-screen p-4">
            <nav className="flex justify-between items-center py-4 border-b border-gray-600">
                <button onClick={() => navigate("/MasterMain")} className="btn">뒤로 가기</button>
                <div className="text-2xl font-bold text-white">상품 등록</div>
            </nav>

            <h1 className="text-2xl font-bold my-6 text-center">🛍 상품 등록</h1>

            {/* 상품 코드 입력 */}
            <div className="mb-4">
                <label>상품 코드</label>
                <div className="flex">
                    <input
                        type="text"
                        value={productCode}
                        onChange={(e) => setProductCode(e.target.value)}
                        className="flex-grow"
                    />
                    <button onClick={handleCodeCheck} className="ml-2 btn">중복 확인</button>
                </div>
                {isCodeValid === false && <p className="text-red-500">이미 존재하는 코드입니다.</p>}
                {isCodeValid === true && <p className="text-green-500">사용 가능한 코드입니다.</p>}
            </div>

            {/* 카테고리 선택 */}
            <div className="mb-4">
                <label>대분류</label>
                <select onChange={(e) => setSelectedParent(e.target.value)}>
                    <option value="">대분류 선택</option>
                    {categories.map((cat) => (
                        <option key={cat.parentsId} value={cat.parentsId}>{cat.name}</option>
                    ))}
                </select>
            </div>


            <div className="mb-4">
                <label>중분류</label>
                <select onChange={(e) => setSelectedChild(e.target.value)} disabled={!children.length}>
                    <option value="">중분류 선택</option>
                    {children.map((child) => (
                        <option key={child.childCategoryId} value={child.childCategoryId}>{child.name}</option>
                    ))}
                </select>
            </div>

            <div className="mb-4">
                <label>소분류</label>
                <select onChange={(e) => setSelectedSub(e.target.value)} disabled={!subCategories.length}>
                    <option value="">소분류 선택</option>
                    {subCategories.map((sub) => (
                        <option key={sub.subCategoryId} value={sub.subCategoryId}>{sub.name}</option>
                    ))}
                </select>
            </div>


            <div>
                <h3>사이즈 및 재고 입력</h3>
                {sizes.map((size, index) => (
                    <div key={size.size}>
                        <label>{size.size}:</label>
                        <input
                            type="text"
                            min="0"
                            value={size.cnt}
                            onChange={(e) => handleSizeChange(index, parseInt(e.target.value, 10) || 0)}
                        />
                    </div>
                ))}
            </div>

            {/* 상품 정보 입력 */}
            <div className="mb-4">
                <label>상품명</label>
                <input type="text" value={productName} onChange={(e) => setProductName(e.target.value)} />
            </div>

            <div className="mb-4">
                <label>가격</label>
                <input type="number" value={productAmount} onChange={(e) => setProductAmount(e.target.value)} />
            </div>

            <div className="mb-4">
                <label>상품 설명</label>
                <textarea value={productContents} onChange={(e) => setProductContents(e.target.value)} />
            </div>

            {/* 이미지 업로드 */}
            <div className="mb-4">
                <label>대표 이미지</label>
                <input type="file" accept="image/*" onChange={(e) => setMainImage(e.target.files[0])} />
            </div>

            <div className="mb-4">
                <label>추가 이미지</label>
                <input type="file" accept="image/*" multiple onChange={(e) => setSubImages([...e.target.files])} />
            </div>

            {/* 상품 등록 버튼 */}
            <button className="btn" onClick={handleCreateProduct}>상품 등록</button>
        </div>
    );
};

export default CreateProduct;
