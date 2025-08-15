import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { useEffect, useState } from "react";
import { FaUser } from "react-icons/fa";
import { RiLockPasswordFill } from "react-icons/ri";
import publicInstance from "../api/publicAxios";
import '../styles/AuthFormStyle.css';

function RegisterForm() {

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [passwordConfirm, setPasswordConfirm] = useState("");
  const [message, setMessage] = useState("");
  const [email, setEmail] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const handleRegister = async (e) => {
    e.preventDefault();
    const data = { username, password, email };
    if (passwordConfirm !== password) {
      setError("Password is not matched")
      return;
    }
    try {
      const response = await publicInstance.post("/register", data)
      setError("");
      setSuccess(response.data);
    } catch (err) {
      if (err.response && err.response.data) {
        if (typeof (err.response.data.message) === "string") {
          setError(err.response.data.message);
          return;
        }
        if (err.response.data.password) {
          setError(err.response.data.password);
          return;
        }
        if (typeof (err.response.data) === "object" && !err.response.data.password) {
          setError(err.response.data.password);
          return;
        }
        else {
          console.log(err.response)
          setError("Registration Failed")
        }
      } else {
        setError("Network or unexpected error occurred")
      }
    }
  }

  useEffect(() => {
    if (!passwordConfirm) {
      setMessage("Please confirm password");
    } else if (passwordConfirm === password) {
      setMessage("Password is matched");
    } else {
      setMessage("Password is not matched");
    }
  }, [password, passwordConfirm]);

  return (
    <>
      <form className="form" id="register" onSubmit={handleRegister}>
        <div className="grid w-full max-w-sm items-center gap-2">
          <Input type="text" id="text"
            value={username}
            onChange={c => (setUsername(c.target.value))} required
            className="text" autoComplete='off' />
          <Label htmlFor="text" className="label">Username</Label>
          <FaUser className="fromIcon" style={{ top: "16%" }} />
        </div>
        <br />
        <div className="grid w-full max-w-sm items-center gap-2">
          <Input type="password" id="password"
            value={password}
            onChange={c => (setPassword(c.target.value))} required
            className="text" autoComplete='off' />
          <Label htmlFor="password" className="label">Password</Label>
          <RiLockPasswordFill className="fromIcon" style={{ top: "33%" }} />
        </div>
        <br />
        <div className="grid w-full max-w-sm items-center gap-2">
          <Input type="password" id="passwordConfirm"
            value={passwordConfirm}
            onChange={c => setPasswordConfirm(c.target.value)} required
            className="text" autoComplete='off' />
          <Label htmlFor="password" className="label">Password Confirmation</Label>
          <RiLockPasswordFill className="fromIcon" style={{ top: "50%" }} />
        </div>
        <p className="message" style={{ color: (passwordConfirm !== password ? "red" : "green") }}>{message}</p>

        <div className="grid w-full max-w-sm items-center gap-2">
          <Input type="email" id="email"
            value={email}
            onChange={c => (setEmail(c.target.value))} required
            className="text" />
          <Label htmlFor="email" className="label" autoComplete="off">Email</Label>
        </div><br />
        <Button className="formButton" variant="outline" type="submit">Register</Button>
      </form>
      <div className="h-6 text-red-500">
      {error && <p style={{ color: "red", padding: 5 }}>{error}</p>}
      {success && <p style={{ color: "green", padding: 5 }}>{success}</p>}
      </div>
    </>
  );

}

export default RegisterForm