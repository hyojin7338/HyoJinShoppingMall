import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import './index.css';
import App from './App.jsx';
import { UserProvider } from "./context/UserContext";  // ✅ UserProvider 가져오기

createRoot(document.getElementById('root')).render(
    <StrictMode>
        <UserProvider> 
            <App />
        </UserProvider>
    </StrictMode>
);
