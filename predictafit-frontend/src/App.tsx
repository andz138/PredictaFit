import { Box, Button, Typography } from "@mui/material"
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom"

import { useContext, useEffect } from "react"
import { useDispatch } from "react-redux"

import { AuthContext } from "react-oauth2-code-pkce"
import { setCredentials } from "./store/authSlice"

import ActivityDetail from "./components/ActivityDetail"
import ActivitiesPage from "./pages/ActivitiesPage"

function App() {

  const { token, tokenData, logIn, logOut } = useContext(AuthContext)

  const dispatch = useDispatch()

  const isAuthenticated = !!token

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

        <Box
          sx={{
            height: "100vh",
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            justifyContent: "center",
            textAlign: "center"
          }}
        >

          <Typography variant="h4" gutterBottom>
            Welcome to PredictaFit
          </Typography>

          <Typography variant="subtitle1" sx={{ mb: 3 }}>
            Please login to track your activities
          </Typography>

          <Button
            variant="contained"
            size="large"
            onClick={() => logIn()}
          >
            LOGIN
          </Button>

        </Box>

      ) : (

        <Box sx={{ p: 2 }}>

          <Button
            variant="contained"
            color="secondary"
            onClick={() => logOut()}
          >
            Logout
          </Button>

          <Routes>

            <Route
              path="/activities"
              element={<ActivitiesPage />}
            />

            <Route
              path="/activities/:id"
              element={<ActivityDetail />}
            />

            <Route
              path="/"
              element={<Navigate to="/activities" replace />}
            />

          </Routes>

        </Box>

      )}

    </Router>
  )
}

export default App