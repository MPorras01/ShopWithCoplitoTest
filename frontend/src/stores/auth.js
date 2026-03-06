import { reactive } from 'vue'

const saved = localStorage.getItem('shop-auth')

export const authState = reactive(saved ? JSON.parse(saved) : {
  token: '',
  username: '',
  roles: []
})

export function isAuthenticated() {
  return Boolean(authState.token)
}

export function isAdminLike() {
  return authState.roles.includes('ROLE_ADMIN') || authState.roles.includes('ROLE_MANAGER')
}

export function setAuth(payload) {
  authState.token = payload.token
  authState.username = payload.username
  authState.roles = payload.roles || []
  localStorage.setItem('shop-auth', JSON.stringify(authState))
}

export function clearAuth() {
  authState.token = ''
  authState.username = ''
  authState.roles = []
  localStorage.removeItem('shop-auth')
}
