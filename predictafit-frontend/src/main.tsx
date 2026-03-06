import { StrictMode } from "react"
import { createRoot } from "react-dom/client"

import { Provider } from "react-redux"
import { AuthProvider } from "react-oauth2-code-pkce"

import App from "./App"
import { store } from "./store/store"
import { authConfig } from "./auth/authConfig"

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <AuthProvider authConfig={authConfig}>
      <Provider store={store}>
        <App />
      </Provider>
    </AuthProvider>
  </StrictMode>
)
