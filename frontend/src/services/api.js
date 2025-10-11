// src/services/api.js
import axios from 'axios'
import store from '@/store/store'

const api = axios.create({
   baseURL: 'http://localhost:8080/api/v1/',
   withCredentials: true 
})

api.interceptors.request.use(
   config => {
      const token = store.state.auth.userData?.sessionToken
      console.log('Token encontrado:', token) // Debug
      console.log('Store state:', store.state.auth) // Debug
      if (token) {
         config.headers['Authorization'] = `Bearer ${token}`
         console.log('Authorization header set:', config.headers['Authorization']) // Debug
      }
      return config
   },
   error => Promise.reject(error)
)

api.interceptors.response.use(
   response => response,
   error => {
      if (error.response && error.response.status === 401) {
         // Corrigindo o dispatch para o logout
         store.dispatch('auth/LogOut')
      }
      return Promise.reject(error)
   }
)

export default api
