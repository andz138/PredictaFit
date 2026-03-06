export const authConfig = {
    clientId: "predictafit-frontend",
    authorizationEndpoint: "http://localhost:8181/realms/predictafit/protocol/openid-connect/auth",
    tokenEndpoint: "http://localhost:8181/realms/predictafit/protocol/openid-connect/token",
    redirectUri: "http://localhost:5173",
    scope: "openid profile email offline_access",
    onRefreshTokenExpire: (event: any) => event.logIn()
}