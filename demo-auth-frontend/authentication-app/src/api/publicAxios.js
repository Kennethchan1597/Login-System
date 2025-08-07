import axios from "axios";

const publicInstance = axios.create({
  baseURL: "http://localhost:8091/auth"
});

export default publicInstance;
