import './App.css';
import { Routes, Route } from 'react-router-dom';
import { AuthPage, PrivateRoute, ToDoPage } from './pages/index';
function App() {
  return (
    <div className="App">
      <Routes>
        <Route path="/login" element={<AuthPage />} />
        <Route 
          path="/todo" 
          element={
            <PrivateRoute>
              <ToDoPage />
            </PrivateRoute>} 
        />

      </Routes>
    </div>
  );
}

export default App;
