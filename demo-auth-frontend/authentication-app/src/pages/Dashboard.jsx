import { jwtDecode } from "jwt-decode";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../auth/Authentication";

const Dashboard = () => {
  const { logout, token } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (token) {
      try {
        const decoded = jwtDecode(token);
        const now = Date.now() / 1000;

        if (decoded.exp < now) {
          logout(); // This will remove token and redirect to /auth
        }
      } catch (error) {
        console.error("Invalid token:", error);
        logout();
      }
    } else {
      logout(); // no token
    }
  }, [token]);

  const handleLogout = (e) => {
    e.preventDefault();
    logout();
  };

  return (
    <>
      <h1>This is a dashboard</h1>
      <button type="submit" onClick={handleLogout}>Logout</button>
    </>
  );
};

export default Dashboard;
