// src/api/authAxios.js
import axios from "axios";

const authAxios = axios.create({
  baseURL: "http://localhost:8090/auth"
});

// Add token to every request
instance.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Handle token expiration
instance.interceptors.response.use(
  (response) => response,
  (error) => {
    if (
      error.response &&
      (error.response.status === 401 || error.response.status === 403)
    ) {
      // Token is invalid or expired
      localStorage.removeItem("token");
      window.location.href = "/login"; // Or use navigate("/login") if in React component
    }
    return Promise.reject(error);
  }
);

export default authAxios;
