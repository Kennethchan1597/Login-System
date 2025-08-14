import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { useState } from "react";
import { FaUser } from "react-icons/fa";
import { RiLockPasswordFill } from "react-icons/ri";
import { useNavigate } from "react-router-dom";
import publicInstance from "../api/publicAxios.js";
import { useAuth } from "../auth/Authentication";
import '../styles/AuthFormStyle.css';

function LoginForm() {

  const { login } = useAuth();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    console.log("Login Form is working")
    e.preventDefault();
    setError("");
    setSuccess("");
    const data = { username, password };
    try {
      const response = await publicInstance.post("/login", data);
      login(response.data.token)
      setSuccess("Login succeeded");
      navigate("/pages/dashboard");
    } catch (error) {
      console.log(error);
      setError(error.response.data.message);
    }
  }

  return (
    <>
      <form className="form" onSubmit={handleLogin}><br />
        <div className="grid w-full max-w-sm items-center gap-2">
          <Input type="text" id="user"
            value={username}
            onChange={c => (setUsername(c.target.value))} required 
            className = "text" autoComplete='off' />
            <Label htmlFor="user" className="label">Username</Label>
            <FaUser className="fromIcon" style={{top: "36%"}} />
        </div>
        <br />
        <div className="grid w-full max-w-sm items-center gap-2">
          <Input type="password" id="pw"
            value={password}
            onChange={c => (setPassword(c.target.value))} required 
            className = "text" autoComplete='off' />
            <Label htmlFor="pw" className="label">Password</Label>
            <RiLockPasswordFill className="fromIcon" style={{top: "53%"}} />
        </div><br />
        <Button className="formButton" variant="outline" type="submit">Login</Button>
      </form>
      {error && <p style={{ color: "red" }}>{error}</p>}
      {success && <p style={{ color: "green" }}>{success}</p>}
    </>
  );
}

export default LoginForm

