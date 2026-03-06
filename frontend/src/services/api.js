import axios from 'axios'
import { authState } from '../stores/auth'

export const api = axios.create({
  baseURL: '/'
})

api.interceptors.request.use((config) => {
  if (authState.token) {
    config.headers.Authorization = `Bearer ${authState.token}`
  }
  return config
})

export async function login(username, password) {
  const { data } = await api.post('/auth/login', { username, password })
  return data
}

export async function getProducts() {
  const { data } = await api.get('/products')
  return data
}

export async function createProduct(payload) {
  const { data } = await api.post('/products', payload)
  return data
}

export async function updateProduct(id, payload) {
  const { data } = await api.put(`/products/${id}`, payload)
  return data
}

export async function deleteProduct(id) {
  await api.delete(`/products/${id}`)
}
