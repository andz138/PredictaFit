import { createSlice } from "@reduxjs/toolkit"
import type { PayloadAction } from "@reduxjs/toolkit"

interface AuthState {
  user: any | null
  token: string | null
  userId: string | null
}

const initialState: AuthState = {
  user: JSON.parse(localStorage.getItem("user") || "null"),
  token: localStorage.getItem("token"),
  userId: localStorage.getItem("userId")
}

const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    setCredentials: (state, action: PayloadAction<AuthState>) => {
      state.user = action.payload.user
      state.token = action.payload.token
      state.userId = action.payload.userId

      localStorage.setItem("user", JSON.stringify(state.user))
      localStorage.setItem("token", state.token ?? "")
      localStorage.setItem("userId", state.userId ?? "")
    },

    logout: (state) => {
      state.user = null
      state.token = null
      state.userId = null

      localStorage.removeItem("user")
      localStorage.removeItem("token")
      localStorage.removeItem("userId")
    }
  }
})

export const { setCredentials, logout } = authSlice.actions
export default authSlice.reducer