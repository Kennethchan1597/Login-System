import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { useState } from "react";
import { FaUser } from "react-icons/fa";
import { RiLockPasswordFill } from "react-icons/ri";
import publicInstance from "../api/publicAxios";
import '../styles/AuthFormStyle.css';

function RegisterForm() {

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [email, setEmail] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const handleRegister = async (e) => {
    e.preventDefault();
    const data = { username, password, email };
    try {
      const response = await publicInstance.post("/register", data)
      setError("");
      setSuccess(response.data);
    } catch (err) {
      if (err.response && err.response.data) {
        if (typeof (err.response.data.message) === "string")
          setError(err.response.data.message);
        if (typeof (err.response.data) === "object")
          setError(Object.values(err.response.data).join(","));
        else {
          setError("Registration Failed")
        }
      }
      setError("Network or unexpected error occurred")
    }
  }

  return (
    <>
      <form className="form" id="register" onSubmit={handleRegister}>
      <div className="grid w-full max-w-sm items-center gap-2">
          <Input type="text" id="text"  
                 value={username}  
                 onChange={c => (setUsername(c.target.value))} required 
                 className = "text" autoComplete='off' />
                 <Label htmlFor="text" className="label">Username</Label>
                 <FaUser className="fromIcon" style={{top: "14%"}} />
        </div>
        <br />
        <div className="grid w-full max-w-sm items-center gap-2">
          <Input type="password" id="password"  
                 value={password}  
                 onChange={c => (setPassword(c.target.value))} required 
                 className = "text" autoComplete='off' />
                 <Label htmlFor="password" className="label">Password</Label>
                 <RiLockPasswordFill className="fromIcon" style={{top: "38%"}} />
        </div>
        <br />
        <div className="grid w-full max-w-sm items-center gap-2">
          <Input type="email" id="email"  
                 value={email}  
                 onChange={c => (setEmail(c.target.value))} required 
                 className = "text" />
                 <Label htmlFor="email" className="label">Email</Label>
        </div><br />
        <Button className="formButton" variant = "outline" type="submit">Register</Button>
      </form>
      {error && <p style={{color: "red"}}>{error}</p>}
      {success && <p style={{ color: "green" }}>{success}</p>}
    </>
  );

}

export default RegisterForm