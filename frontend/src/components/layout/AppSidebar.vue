<script setup>
import { inject, computed } from 'vue'
import { useRoute } from 'vue-router'
import {
  LayoutDashboard,
  UserCircle,
  ShoppingCart,
  Package,
  Code,
  Cloud,
  DollarSign,
  Briefcase,
  Users,
  Settings
} from 'lucide-vue-next'

const isDark = inject('isDark')
const sidebarMinimized = inject('sidebarMinimized')
const route = useRoute()

const activeRoute = computed(() => {
  return route.name
})

const menuItems = [
  { name: 'Dashboard', icon: LayoutDashboard, route: '/DashboardView', active: 'DashboardView' },
  { name: 'Clientes', icon: UserCircle, route: '/clients', active: 'Clients' },
  { name: 'Produtos FiveM', icon: Package, route: '/gta-products', active: 'GtaProducts' },
  { name: 'Vendas FiveM', icon: ShoppingCart, route: '/gta-sales', active: 'GtaSales' },
  { name: 'Fábrica de Software', icon: Code, route: '/projects', active: 'Projects' },
  { name: 'Assinaturas (SaaS)', icon: Cloud, route: '/subscriptions', active: 'Subscriptions' },
  { name: 'Financeiro', icon: DollarSign, route: '/financial', active: 'Financial' },
  { name: 'Pontuação (Devs)', icon: Briefcase, route: '/commissions/developers', active: 'DeveloperCommissions' },
  { name: 'Comissão (Vendas)', icon: DollarSign, route: '/commissions/salespeople', active: 'SalespersonCommissions' },
  { name: 'Funcionários', icon: Users, route: '/employees', active: 'Employees' },
  { name: 'Configurações', icon: Settings, route: '/settings', active: 'Settings' },
]
</script>

<template>
  <aside class="sidebar sidebar__container" :class="[ isDark ? 'color__theme__dark' : 'color__theme__light', { 'sidebar__minimized': sidebarMinimized }]">
    <div class="sidebar__content">
      <div class="sidebar__menu">
        <ul class="sidebar__list">
          <li v-for="(item, index) in menuItems" :key="index" class="sidebar__item">
            <router-link :to="item.route" class="sidebar__link" :class="{ 'sidebar__link__active': activeRoute === item.active }">
              <span class="sidebar__icon">
                <component :is="item.icon" class="sidebar__icon__svg" />
              </span>
              <span class="sidebar__text">{{ item.name }}</span>
            </router-link>
          </li>
        </ul>
      </div>
    </div>
  </aside>
</template>

<style scoped>
.sidebar__container {
  width: 250px;
  height: 100vh;
  padding-top: 60px;
  position: fixed;
  top: 0;
  left: 0;
  overflow-y: auto;
  transition: width 0.3s ease;
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
  z-index: 40;
}

.sidebar__minimized {
  width: 70px;
}

.sidebar__content {
  height: 100%;
  overflow-y: auto;
}

.sidebar__menu {
  padding: 1rem;
}

.sidebar__list {
  list-style-type: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.sidebar__item {
  width: 100%;
}

.sidebar__link {
  display: flex;
  align-items: center;
  padding: 0.75rem 1rem;
  border-radius: 0.5rem;
  text-decoration: none;
  color: inherit;
  transition: all 0.2s ease;
}

.sidebar__link:hover {
  background: var(--linear-gradient-logo-color);
  color: rgb(26, 92, 255);
}

.sidebar__link__active {
  background: var(--linear-gradient-logo-color);
  color: rgb(26, 92, 255);
  font-weight: 500;
}

.sidebar__icon {
  display: flex;
  align-items: center;
  justify-content: center;
  min-width: 24px;
  margin-right: 1rem;
  transition: transform 0.2s ease;
}

.sidebar__link:hover .sidebar__icon {
  transform: scale(1.1);
}

.sidebar__icon__svg {
  width: 20px;
  height: 20px;
}

.sidebar__text {
  white-space: nowrap;
  overflow: hidden;
}

.sidebar__minimized .sidebar__text {
  display: none;
}

.sidebar__minimized .sidebar__link {
  justify-content: center;
  padding: 0.75rem;
}

.sidebar__minimized .sidebar__icon {
  margin-right: 0;
}
</style>
