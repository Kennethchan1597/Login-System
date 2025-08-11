import axios from "axios";

const publicInstance = axios.create({
  baseURL: "http://localhost:8090/auth"
});

export default publicInstance;
