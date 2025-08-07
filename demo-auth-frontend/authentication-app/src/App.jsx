// import { animate, scroll } from "motion"
import { Navigate, Route, BrowserRouter as Router, Routes } from "react-router-dom";
import AuthProvider, { useAuth } from "./auth/Authentication";
import AuthForm from "./pages/AuthForm";
import Dashboard from "./pages/Dashboard";

function PrivateRoute({ children }) {
  const { token } = useAuth();
  return token ? children : <Navigate to="/auth" />;
}

function App() {
  return (
    <AuthProvider>
      <Router>
      <div className="background" />
        <Routes>
          <Route path="/auth" element={<AuthForm />} />
          <Route path="/dashboard" element={
              <PrivateRoute>
                <Dashboard />
              </PrivateRoute>
            }
          />
          <Route path="*" element={<Navigate to="/dashboard" />} />
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App
