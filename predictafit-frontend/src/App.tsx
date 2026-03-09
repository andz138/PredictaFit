import { Box, Button, Typography, AppBar, Toolbar, Container, ThemeProvider, createTheme } from "@mui/material"
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom"
import FitnessCenterIcon from "@mui/icons-material/FitnessCenter"
import { useContext, useEffect } from "react"
import { useDispatch } from "react-redux"

import { AuthContext } from "react-oauth2-code-pkce"
import { setCredentials } from "./store/authSlice"

import ActivityDetail from "./components/ActivityDetail"
import ActivitiesPage from "./pages/ActivitiesPage"

const theme = createTheme({
  palette: {
    primary: {
      main: "#1976d2",
    },
    secondary: {
      main: "#dc004e",
    },
  },
  typography: {
    fontFamily: '"Inter", "Roboto", "Helvetica", "Arial", sans-serif',
    h4: {
      fontWeight: 700,
    },
  },
})

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
    <ThemeProvider theme={theme}>
      <Router>

        {!isAuthenticated ? (

          <Box
            sx={{
              height: "100vh",
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
              justifyContent: "center",
              textAlign: "center",
              background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
            }}
          >

            <Box sx={{ mb: 4, display: "flex", alignItems: "center", justifyContent: "center", gap: 1 }}>
              <FitnessCenterIcon sx={{ fontSize: 48, color: "white" }} />
              <Typography variant="h3" sx={{ color: "white", fontWeight: 800 }}>
                PredictaFit
              </Typography>
            </Box>

            <Typography variant="h5" sx={{ mb: 2, color: "rgba(255,255,255,0.95)" }}>
              Track Your Fitness Journey
            </Typography>

            <Typography variant="body1" sx={{ mb: 4, color: "rgba(255,255,255,0.8)", maxWidth: 400 }}>
              Log your activities, monitor your progress, and get personalized fitness insights
            </Typography>

            <Button
              variant="contained"
              size="large"
              onClick={() => logIn()}
              sx={{ 
                px: 6, 
                py: 2, 
                fontSize: "1.1rem",
                backgroundColor: "white",
                color: "#667eea",
                fontWeight: 600,
                "&:hover": {
                  backgroundColor: "#f0f0f0"
                }
              }}
            >
              Get Started
            </Button>

          </Box>

        ) : (

          <Box sx={{ display: "flex", flexDirection: "column", minHeight: "100vh" }}>
            
            <AppBar position="static">
              <Toolbar>
                <FitnessCenterIcon sx={{ mr: 2 }} />
                <Typography variant="h6" sx={{ flexGrow: 1, fontWeight: 700 }}>
                  PredictaFit
                </Typography>
                <Button 
                  color="inherit" 
                  onClick={() => logOut()}
                  sx={{ fontWeight: 600 }}
                >
                  Logout
                </Button>
              </Toolbar>
            </AppBar>

            <Container maxWidth="lg" sx={{ py: 4, flex: 1 }}>
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
            </Container>

          </Box>

        )}

      </Router>
    </ThemeProvider>
  )
}

export default App