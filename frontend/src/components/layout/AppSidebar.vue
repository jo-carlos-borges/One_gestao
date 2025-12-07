<script setup>
import { inject, computed, ref, watch } from 'vue'
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
  Settings,
  Wrench,
  ChevronRight,
  ChevronDown,
  FolderOpen,
  Folder
} from 'lucide-vue-next'

const isDark = inject('isDark')
const sidebarMinimized = inject('sidebarMinimized')
const route = useRoute()

const activeRoute = computed(() => {
  return route.name
})

const expandedMenus = ref({
  fivem: false,
  fabrica: false,
  saas: false,
  comissoes: false
})

const toggleMenu = (menuKey) => {
  expandedMenus.value[menuKey] = !expandedMenus.value[menuKey]
}

const isActiveParent = (children) => {
  return children?.some(child => activeRoute.value === child.active)
}

// Expande automaticamente o menu se uma rota filha estiver ativa
const menuItemsWithChildren = [
  {
    key: 'fivem',
    children: [
      { active: 'GtaProducts' },
      { active: 'GtaSales' },
      { active: 'GtaSaleNew' },
      { active: 'GtaSaleDetail' }
    ]
  },
  {
    key: 'fabrica',
    children: [
      { active: 'Projects' },
      { active: 'ProjectNew' }
    ]
  },
  {
    key: 'saas',
    children: [
      { active: 'Subscriptions' },
      { active: 'SubscriptionNew' },
      { active: 'SaasFinancial' }
    ]
  },
  {
    key: 'comissoes',
    children: [
      { active: 'DeveloperCommissions' },
      { active: 'SalespersonCommissions' },
      { active: 'InstallationBonuses' }
    ]
  }
]

watch(activeRoute, (newRoute) => {
  menuItemsWithChildren.forEach(menu => {
    if (menu.children.some(child => child.active === newRoute)) {
      expandedMenus.value[menu.key] = true
    }
  })
}, { immediate: true })

const menuItems = [
  { 
    name: 'Dashboard', 
    icon: LayoutDashboard, 
    route: '/DashboardView', 
    active: 'DashboardView' 
  },
  { 
    name: 'Clientes', 
    icon: UserCircle, 
    route: '/clients', 
    active: 'Clients' 
  },
  {
    name: 'FiveM',
    icon: Folder,
    iconExpanded: FolderOpen,
    key: 'fivem',
    children: [
      { name: 'Produtos FiveM', icon: Package, route: '/gta-products', active: 'GtaProducts' },
      { name: 'Vendas FiveM', icon: ShoppingCart, route: '/gta-sales', active: 'GtaSales' },
    ]
  },
  {
    name: 'Fábrica de Software',
    icon: Folder,
    iconExpanded: FolderOpen,
    key: 'fabrica',
    children: [
      { name: 'Projetos', icon: Code, route: '/projects', active: 'Projects' },
    ]
  },
  {
    name: 'Comissões',
    icon: Folder,
    iconExpanded: FolderOpen,
    key: 'comissoes',
    children: [
      { name: 'Pontuação (Devs)', icon: Briefcase, route: '/commissions/developers', active: 'DeveloperCommissions' },
      { name: 'Comissão (Vendas)', icon: DollarSign, route: '/commissions/salespeople', active: 'SalespersonCommissions' },
      { name: 'Bônus Instalação', icon: Wrench, route: '/commissions/bonuses', active: 'InstallationBonuses' },
    ]
  },
  {
    name: 'SaaS',
    icon: Folder,
    iconExpanded: FolderOpen,
    key: 'saas',
    children: [
      { name: 'Assinaturas', icon: Cloud, route: '/subscriptions', active: 'Subscriptions' },
      { name: 'Financeiro SaaS', icon: DollarSign, route: '/subscriptions/financial', active: 'SaasFinancial' },
    ]
  },
  { 
    name: 'Financeiro', 
    icon: DollarSign, 
    route: '/financial', 
    active: 'Financial' 
  },
  { 
    name: 'Funcionários', 
    icon: Users, 
    route: '/employees', 
    active: 'Employees' 
  },
  { 
    name: 'Configurações', 
    icon: Settings, 
    route: '/settings', 
    active: 'Settings' 
  },
]
</script>

<template>
  <aside class="sidebar sidebar__container" :class="[ isDark ? 'color__theme__dark' : 'color__theme__light', { 'sidebar__minimized': sidebarMinimized }]">
    <div class="sidebar__content">
      <div class="sidebar__menu">
        <ul class="sidebar__list">
          <li v-for="(item, index) in menuItems" :key="index" class="sidebar__item">
            <!-- Item simples (sem subníveis) -->
            <router-link 
              v-if="!item.children" 
              :to="item.route" 
              class="sidebar__link" 
              :class="{ 'sidebar__link__active': activeRoute === item.active }"
            >
              <span class="sidebar__icon">
                <component :is="item.icon" class="sidebar__icon__svg" />
              </span>
              <span class="sidebar__text">{{ item.name }}</span>
            </router-link>

            <!-- Item com subníveis -->
            <div v-else class="sidebar__group">
              <div 
                class="sidebar__link sidebar__link__parent" 
                :class="{ 
                  'sidebar__link__active': isActiveParent(item.children),
                  'sidebar__link__expanded': expandedMenus[item.key]
                }"
                @click="toggleMenu(item.key)"
              >
                <span class="sidebar__icon">
                  <component 
                    :is="expandedMenus[item.key] ? item.iconExpanded : item.icon" 
                    class="sidebar__icon__svg" 
                  />
                </span>
                <span class="sidebar__text">{{ item.name }}</span>
                <span class="sidebar__chevron">
                  <component 
                    :is="expandedMenus[item.key] ? ChevronDown : ChevronRight" 
                    class="sidebar__chevron__svg" 
                  />
                </span>
              </div>
              <transition name="submenu">
                <ul v-if="expandedMenus[item.key]" class="sidebar__sublist">
                  <li v-for="(child, childIndex) in item.children" :key="childIndex" class="sidebar__subitem">
                    <router-link 
                      :to="child.route" 
                      class="sidebar__link sidebar__link__child" 
                      :class="{ 'sidebar__link__active': activeRoute === child.active }"
                    >
                      <span class="sidebar__icon">
                        <component :is="child.icon" class="sidebar__icon__svg" />
                      </span>
                      <span class="sidebar__text">{{ child.name }}</span>
                    </router-link>
                  </li>
                </ul>
              </transition>
            </div>
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

.sidebar__group {
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
  cursor: pointer;
}

.sidebar__link__parent {
  justify-content: space-between;
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

.sidebar__link__child {
  padding-left: 2.5rem;
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
  flex: 1;
}

.sidebar__chevron {
  display: flex;
  align-items: center;
  justify-content: center;
  min-width: 16px;
  transition: transform 0.2s ease;
}

.sidebar__chevron__svg {
  width: 16px;
  height: 16px;
}

.sidebar__sublist {
  list-style-type: none;
  padding: 0;
  margin: 0.25rem 0 0 0;
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.sidebar__subitem {
  width: 100%;
}

.submenu-enter-active,
.submenu-leave-active {
  transition: all 0.3s ease;
  overflow: hidden;
}

.submenu-enter-from,
.submenu-leave-to {
  opacity: 0;
  max-height: 0;
}

.submenu-enter-to,
.submenu-leave-from {
  opacity: 1;
  max-height: 500px;
}

.sidebar__minimized .sidebar__text {
  display: none;
}

.sidebar__minimized .sidebar__chevron {
  display: none;
}

.sidebar__minimized .sidebar__link {
  justify-content: center;
  padding: 0.75rem;
}

.sidebar__minimized .sidebar__link__parent {
  justify-content: center;
}

.sidebar__minimized .sidebar__icon {
  margin-right: 0;
}

.sidebar__minimized .sidebar__link__child {
  padding-left: 0.75rem;
}

.sidebar__minimized .sidebar__sublist {
  display: none;
}
</style>
