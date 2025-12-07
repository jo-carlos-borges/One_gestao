import { createMemoryHistory, createRouter } from 'vue-router'
import store from '@/store/store'

import LoginView from '../views/Login/LoginView.vue'
import DashboardView from '../views/Dashboard/DashboardView.vue'
import EmpresasView from '../views/Cadastros/EmpresasView.vue'
import ClientesView from '../views/Cadastros/ClientesView.vue'
import ProdutosView from '../views/Cadastros/ProdutosView.vue'
import ClientsView from '../views/Clients/ClientsView.vue'
import ProductsView from '../views/Products/ProductsView.vue'
import SaleFormView from '../views/Sales/SaleFormView.vue'
import SalesListView from '../views/Sales/SalesListView.vue'
import SaleDetailView from '../views/Sales/SaleDetailView.vue'
import ProjectsListView from '../views/Projects/ProjectsListView.vue'
import ProjectFormView from '../views/Projects/ProjectFormView.vue'
import DeveloperCommissionsView from '../views/Commissions/DeveloperCommissionsView.vue'
import SalespersonCommissionsView from '../views/Commissions/SalespersonCommissionsView.vue'
import InstallationBonusView from '../views/Commissions/InstallationBonusView.vue'
import SubscriptionsListView from '../views/Subscriptions/SubscriptionsListView.vue'
import SubscriptionFormView from '../views/Subscriptions/SubscriptionFormView.vue'

const routes = [
   { path: '/', name: 'LoginView', component: LoginView, meta: { requiresAuth: true } },
   { path: '/DashboardView', name: 'DashboardView', component: DashboardView, meta: { requiresAuth: true } },
   { path: '/clients', name: 'Clients', component: ClientsView, meta: { requiresAuth: true } },
   { path: '/gta-products', name: 'GtaProducts', component: ProductsView, meta: { requiresAuth: true } },
   { path: '/gta-sales', name: 'GtaSales', component: SalesListView, meta: { requiresAuth: true } },
   { path: '/gta-sales/new', name: 'GtaSaleNew', component: SaleFormView, meta: { requiresAuth: true } },
   { path: '/gta-sales/:id', name: 'GtaSaleDetail', component: SaleDetailView, meta: { requiresAuth: true } },
   { path: '/empresas', name: 'EmpresasView', component: EmpresasView, meta: { requiresAuth: true } },
   { path: '/clientes', name: 'ClientesView', component: ClientesView, meta: { requiresAuth: true } },
   { path: '/produtos', name: 'ProdutosView', component: ProdutosView, meta: { requiresAuth: true } },
   { 
     path: '/projects', 
     name: 'Projects', // Corresponde ao 'active' que definimos no menu
     component: ProjectsListView, 
     meta: { requiresAuth: true } 
   },
   { 
     path: '/projects/new', 
     name: 'ProjectNew', 
     component: ProjectFormView,
     meta: { requiresAuth: true } 
   },
   { 
     path: '/commissions/developers', 
     name: 'DeveloperCommissions',
     component: DeveloperCommissionsView, 
     meta: { requiresAuth: true } 
   },
   { 
     path: '/commissions/salespeople', 
     name: 'SalespersonCommissions',
     component: SalespersonCommissionsView, 
     meta: { requiresAuth: true } 
   },
   { 
     path: '/commissions/bonuses', 
     name: 'InstallationBonuses', 
     component: InstallationBonusView,
     meta: { requiresAuth: true } 
   },
   { 
     path: '/subscriptions', 
     name: 'Subscriptions', 
     component: SubscriptionsListView, 
     meta: { requiresAuth: true } 
   },
   { 
     path: '/subscriptions/new', 
     name: 'SubscriptionNew', 
     component: SubscriptionFormView, 
     meta: { requiresAuth: true } 
   }
]

const router = createRouter({
   history: createMemoryHistory(),
   routes,
})

router.beforeEach((to, from, next) => {
   const isLogged = store.state.auth.isLogged

   if (to.path === '/' && isLogged) {
      next({ name: 'DashboardView' })
   } else if (to.matched.some(record => record.meta.requiresAuth) && !isLogged) {
      if (to.path !== '/') {
         next({ name: 'LoginView' })
      } else {
         next()
      }
   } else {
      next()
   }
})

export default router
