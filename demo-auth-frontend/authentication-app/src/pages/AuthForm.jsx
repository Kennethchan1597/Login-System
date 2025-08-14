import FormToggleButton from "@/components/ui/FormToggleButton"
import React, { useState } from "react"
import LoginForm from "../components/LoginForm"
import RegisterForm from "../components/RegisterForm"

const AuthForm = () => {
  const [isLogin, setIsLogin] = useState(true)

  const toggleForm = () => {
    setIsLogin((prev) => !prev)
  }

  return (
    <div className="flex items-center justify-center min-h-screen" style={{ position: "relative", bottom: "70px"}}>
      <div className="w-full max-w-md mx-auto p-6 border rounded-xl shadow-md bg-black/30 backdrop-blur-md">
        <h2 className="text-3xl font-bold mb-4 text-center text-white drop-shadow-[2px_4px_10px_#5fe9a4] transition-transform duration-300">
          {isLogin ? "Login" : "Register"}
        </h2>

        {isLogin ? <LoginForm /> : <RegisterForm />}

        <div className="flex justify-center mt-6 p-3">
          <FormToggleButton
            isLogin={isLogin}
            onClick={toggleForm}
          />
        </div>
      </div>
    </div>
  )
}


export default AuthForm
