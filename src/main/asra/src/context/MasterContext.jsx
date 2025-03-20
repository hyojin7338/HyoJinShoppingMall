import { createContext, useState, useEffect } from "react";

export const MasterContext = createContext();

export const MasterProvider = ({ children }) => {
    const [master, setMaster] = useState(() => {
        // localStorage에서 유저 정보를 불러오기
        const savedMaster = localStorage.getItem("master");
        return savedMaster ? JSON.parse(savedMaster) : null;
    });

    useEffect(() => {
        // master 정보가 바뀔 때마다 localStorage에 저장
        if (master) {
            localStorage.setItem("master", JSON.stringify(master));
        } else {
            localStorage.removeItem("master");
        }
    }, [master]);


    const logout = () => {
        setMaster(null); // user 상태 초기화
        localStorage.removeItem("master"); // localStorage에서 삭제
    };

    return (
        <MasterContext.Provider value={{ master,setMaster, logout }}>
            {children}
        </MasterContext.Provider>
    );

}