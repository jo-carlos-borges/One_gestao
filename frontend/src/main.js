import { createApp } from 'vue'
import App from '@/App.vue'

import router from '@/routes/routes'
import store from '@/store/store'
import api from '@/services/api'

import jQuery from 'jquery'
import 'bootstrap'
import 'bootstrap/dist/css/bootstrap.min.css'

import '@/assets/css/global.css'
import '@/assets/css/theme.css'
import '@/assets/css/responsive.css'

import sweetalert2 from '@/plugins/sweetalert2';

import ApexCharts from 'vue3-apexcharts'

const app = createApp(App)

app.use(router)
app.use(store)
app.use(sweetalert2)
app.component('ApexCharts', ApexCharts)

app.config.globalProperties.$jQuery = jQuery
app.config.globalProperties.$axios = api
store.$axios = api

window.store = store // Tornando o store acessível globalmente pelo objeto window

// Inicializar autenticação se houver token salvo
store.dispatch('auth/initAuth')

app.mount('#app')
