import { Button } from "@mui/material"
import { BrowserRouter as Router } from "react-router-dom"

import { useContext, useEffect } from "react"
import { useDispatch } from "react-redux"

import { AuthContext } from "react-oauth2-code-pkce"
import { setCredentials } from "./store/authSlice"

function App() {
  const { token, tokenData, logIn, logOut } = useContext(AuthContext)
  const isAuthenticated = !!token
  const dispatch = useDispatch()

  useEffect(() => {
    if (token) {
      dispatch(
        setCredentials({
          token,
          user: tokenData,
          userId: tokenData?.sub ?? null
        })
      )
    }
  }, [token, tokenData, dispatch])

  return (
    <Router>
      {!isAuthenticated ? (
        <Button variant="contained" onClick={() => logIn()}>
          LOGIN
        </Button>
      ) : (
        <div>
          <h3>Token Data</h3>
          <pre>{JSON.stringify(tokenData, null, 2)}</pre>

          <h3>Access Token</h3>
          <pre>{JSON.stringify(token, null, 2)}</pre>

          <Button variant="outlined" onClick={() => logOut()}>
            LOGOUT
          </Button>
        </div>
      )}
    </Router>
  )
}

export default App