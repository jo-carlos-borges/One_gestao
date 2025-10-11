import router from '@/routes/routes'

export default {
   namespaced: true,
   state: () => ({
      isLogged: false,
      userData: null,
   }),
   mutations: {
      setUserData(state, data) {
         state.userData = data
      },
      setSessionToken(state, token) {
         if (state.userData) {
            state.userData.sessionToken = token
         } else {
            state.userData = { sessionToken: token }
         }
         localStorage.setItem('sessionToken', token)
      },
      clearSessionToken(state) {
         if (state.userData) {
            state.userData.sessionToken = null
         }
         localStorage.removeItem('sessionToken')
      },
      setIsLogged(state, isLogged) {
         state.isLogged = isLogged
      },
   },
   actions: {
      initAuth({ commit }) {
         const token = localStorage.getItem('sessionToken')
         const email = localStorage.getItem('email')
         console.log('Initializing auth with token:', token, 'email:', email) // Debug
         if (token && email) {
            commit('setUserData', { email })
            commit('setSessionToken', token)
            commit('setIsLogged', true)
         }
      },
      async LogOut({ commit }) {
         // commit('loading/toggleScreenLoading', true, { root: true })

         localStorage.removeItem('email')
         localStorage.removeItem('password')
         localStorage.removeItem('connected')

         commit('setIsLogged', false)
         commit('clearSessionToken')
         commit('setUserData', {})

         if (router.currentRoute.value.path !== '/') await router.push('/')

         // limpa header da instância axios
         if (window?.store?.$axios) {
            window.store.$axios.defaults.headers.common['Authorization'] = ''
         }

         // setTimeout(() => {
         //    commit('loading/toggleScreenLoading', false, { root: true })
         // }, 200)
      },

      LogIn({ commit }, userCredentials) {
         if (!userCredentials.email || !userCredentials.password) return

         if (userCredentials.connected) localStorage.setItem('connected', userCredentials.connected)

         commit('loading/toggleScreenLoading', true, { root: true })

         window.store.$axios.post('auth/login', userCredentials, {
            headers: { 'Content-Type': 'application/json' },
         }).then((response) => {
            console.log('Login response:', response.data) // Debug
            commit('setUserData', response.data)
            commit('setSessionToken', response.data.token)
            commit('setIsLogged', true)
            
            // Salvar email no localStorage para inicialização
            localStorage.setItem('email', response.data.email)

            if (router.currentRoute.value.path !== '/DashboardView') {
               router.push('/DashboardView')
            }            
         }).catch((error) => {
            console.error('Login error:', error) // Debug
            throw error
         }).finally(() => {
            commit('loading/toggleScreenLoading', false, { root: true })
         })
      }
   },
}
