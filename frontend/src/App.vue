<script setup>
import { provide, computed, onMounted } from 'vue'
import { useStore } from 'vuex'
import { useColorTheme } from './helpers/colorTheme'
import { useSidebar } from './helpers/useSidebar'
import AppHeader from './components/layout/AppHeader.vue'
import AppSidebar from './components/layout/AppSidebar.vue'
import LoginView from './views/Login/LoginView.vue'


/***************************************************************************** 
 AUTH
*****************************************************************************/
const store = useStore()
const isLogged = computed(() => store.state.auth.isLogged)

/***************************************************************************** 
 COLOR THEME
*****************************************************************************/
const { isDark } = useColorTheme()
provide('isDark', isDark)

/***************************************************************************** 
 MINIMIZED SIDEBAR
*****************************************************************************/
const { sidebarMinimized, sidebarOpen, isMobileView } = useSidebar()
provide('sidebarMinimized', sidebarMinimized)
provide('sidebarOpen', sidebarOpen)
onMounted(() => {
  if (isMobileView.value) {
    sidebarMinimized.value = true
  }
})

</script>

<template>
  <div :class="['app__container', { 'app__container__minimized': sidebarMinimized }]">
    <div class="screen-loading" :class="{ active: store.state.loading.isLoading }">
      <div class="loading-spinner"></div>
    </div>
    
    <div v-if="isLogged">
      <AppHeader />
      <AppSidebar :class="{ 'open': sidebarOpen }" />

      <main class="main__content" :class="isDark ? 'color__theme__dark' : 'color__theme__light2'">
        <div class="container-fluid">
          <div class="row">
            <router-view />
          </div>
        </div>
      </main>
    </div>
    <div v-else>
      <LoginView />
    </div>
  </div>
</template>

<style>
/* Estilos globais para transições */
.sidebar-transition {
  transition: all 0.3s ease;
}

.card-hover:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

.card-hover {
  transition: all 0.3s ease;
}

.main__content {
  margin-left: 250px;
  margin-top: 60px;
  padding: 1rem;
  width: calc(100% - 250px);
  transition: margin-left 0.3s, width 0.3s;
  height: calc(100vh - 60px);
  overflow-y: auto;
}

.app__container__minimized .main__content {
  margin-left: 70px;
  width: calc(100% - 70px);
}

/* Estilo para a scrollbar */
::-webkit-scrollbar {
  width: 8px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
}

::-webkit-scrollbar-thumb {
  background: #888;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: #555;
}
</style>
