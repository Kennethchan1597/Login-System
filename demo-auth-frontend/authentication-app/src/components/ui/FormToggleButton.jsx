"use client"

import * as motion from "motion/react-client"

const CONTAINER_WIDTH = 135
const HANDLE_SIZE = 33
const PADDING = 6

export default function FormToggleButton({ isLogin, onClick }) {
  return (
    <button
    type="button"
      onClick={onClick}
      style={{
        width: CONTAINER_WIDTH,
        height: HANDLE_SIZE + PADDING * 2,
        backgroundColor: "#333",
        borderRadius: 50,
        cursor: "pointer",
        border: "1px solid #ccc",
        padding: PADDING,
        display: "flex",
        alignItems: "center",
        textAlign: "center",
        justifyContent: "space-between",
        userSelect: "none",
        position: "relative",
        overflow: "hidden",
        boxShadow: "2px 2px 12px rgba(0, 0, 0, 0.15)",
        transition: "box-shadow 0.5s ease",
      }}
    >
      <span
        style={{
          fontWeight: "600",
          color: "#fff",
          pointerEvents: "none",
          order: isLogin ? 2 : 1,
          marginLeft: isLogin ? 0 : 10,
          marginRight: isLogin ? 10 : 0,
          whiteSpace: "nowrap",
          textShadow: "2px 4px 13px #a4a4a4",
          userSelect: "none",
        }}
      >
        {isLogin ? "Register" : "Login"}
      </span>

      <motion.div
        layout
        transition={{ type: "spring", bounce: 0.3, duration: 1 }}
        style={{
          width: HANDLE_SIZE,
          height: HANDLE_SIZE,
          backgroundColor: "#f3f3f3",
          borderRadius: "50%",
          order: isLogin ? 1 : 2,
          flexShrink: 0,
          pointerEvents: "none",
          boxShadow: `
            0 0 2px  #11dfff,
            0 0 15px#33d7f0,
            0 0 30px#46bccf
          `,
          filter: "drop-shadow(0 0 1px #11ff78)",
          transition: "box-shadow 0.3s ease",
        }}
        whileTap={{ scale: 0.95 }}
      />
    </button>
  )
}
