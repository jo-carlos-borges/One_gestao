<script setup>
import { Menu, Search, Bell, ChevronDown } from 'lucide-vue-next'
import { inject, ref, onMounted, onUnmounted } from 'vue'
import { useStore } from 'vuex'


const store = useStore()
const logout = () => {
  store.dispatch('auth/LogOut')
}

const isDark = inject('isDark')
const sidebarMinimized = inject('sidebarMinimized')

// Estado para menus dropdown
const showNotifications = ref(false)
const showUserMenu = ref(false)
const unreadNotifications = ref(3)
const showSearchModal = ref(false)

// Funções para alternar menus
const toggleNotifications = () => {
  showNotifications.value = !showNotifications.value
  if (showUserMenu.value) showUserMenu.value = false
}

const toggleUserMenu = () => {
  showUserMenu.value = !showUserMenu.value
  if (showNotifications.value) showNotifications.value = false
}

const toggleSidebar = () => {
  sidebarMinimized.value = !sidebarMinimized.value
}

const toggleSearchModal = () => {
  showSearchModal.value = !showSearchModal.value
}

// Removido o método toggleDarkMode comentado

const setTheme = (dark) => {
  isDark.value = dark
  localStorage.setItem('theme', dark ? 'dark' : 'light')
  document.documentElement.classList.toggle('dark', dark)
}

// Fechar dropdowns quando clicar fora
const handleClickOutside = (event) => {
  const notificationEl = document.querySelector('.dropdown-notifications')
  const userEl = document.querySelector('.dropdown-user')
  const searchModalEl = document.querySelector('.modal-search')

  if (notificationEl && !notificationEl.contains(event.target) && showNotifications.value) {
    showNotifications.value = false
  }

  if (userEl && !userEl.contains(event.target) && showUserMenu.value) {
    showUserMenu.value = false
  }

  if (searchModalEl && !searchModalEl.contains(event.target) &&
    !event.target.closest('.btn-search-mobile') && showSearchModal.value) {
    showSearchModal.value = false
  }
}

onMounted(() => {
  document.addEventListener('click', handleClickOutside)
})

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
})

const closeAllModals = () => {
  showSearchModal.value = false
  showNotifications.value = false
  showUserMenu.value = false
}
</script>

<template>
  <header class="navbar navbar-expand fixed-top shadow-sm" :class="isDark ? 'navbar-dark color__theme__dark' : 'navbar-light color__theme__light'">
    <!-- Left side - Logo and toggle button -->
    <div class="container-fluid">
      <div class="d-flex align-items-center">
        <button class="btn btn-link p-0 me-2" @click="toggleSidebar">
          <Menu :class="isDark ? 'text-light' : 'text-dark'" />
        </button>
        <a class="navbar-brand d-flex align-items-center m-0" href="#">
          <!-- <span class="fw-bold" :class="isDark ? 'text-light' : 'text-dark'">ThunderObras</span> -->
          <img height="20" src="../../assets/images/thundersales-no-logo.png" />
        </a>
      </div>

      <!-- Right side - Search, notifications and user profile -->
      <div class="d-flex align-items-center">
        <!-- Search - Desktop -->
        <div class="d-none d-md-block me-3">
          <div class="input-group">
            <span class="input-group-text border-end-0">
              <Search size="16" />
            </span>
            <input type="text" class="form-control border-start-0" placeholder="Pesquisar...">
          </div>
        </div>

        <!-- Search - Mobile -->
        <div class="d-block d-md-none me-3">
          <button class="btn btn-link p-0 btn-search-mobile" @click="toggleSearchModal">
            <Search :class="isDark ? 'text-light' : 'text-dark'" />
          </button>
        </div>

        <!-- Notifications -->
        <div class="dropdown dropdown-notifications me-3">
          <button class="btn btn-link position-relative p-0" @click="toggleNotifications">
            <Bell :class="isDark ? 'text-light' : 'text-dark'" />
            <span v-if="unreadNotifications"
              class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-primary">
              {{ unreadNotifications }}
            </span>
          </button>
          <div class="dropdown-menu dropdown-menu-end"
            :class="[{ 'show': showNotifications }, isDark ? 'dropdown-menu-dark' : '']">
            <div class="dropdown-header">Notificações</div>
            <a class="dropdown-item" href="#">Novo usuário registrado</a>
            <a class="dropdown-item" href="#">Sistema atualizado</a>
            <a class="dropdown-item" href="#">Você tem 3 novas mensagens</a>
            <div class="dropdown-divider"></div>
            <a class="dropdown-item text-primary" href="#">Ver todas</a>
          </div>
        </div>

        <!-- User Menu -->
        <div class="dropdown dropdown-user">
          <button class="btn btn-link d-flex align-items-center p-0" @click="toggleUserMenu">
            <img src="https://randomuser.me/api/portraits/women/44.jpg" alt="User" class="rounded-circle me-2"
              width="32" height="32">
            <span class="d-none d-md-inline" :class="isDark ? 'text-light' : 'text-dark'">Usuário</span>
            <ChevronDown :class="isDark ? 'text-light' : 'text-dark'" class="ms-1" size="14" />
          </button>
          <div class="dropdown-menu dropdown-menu-end"
            :class="[{ 'show': showUserMenu }, isDark ? 'dropdown-menu-dark' : '']">
            <div class="dropdown-header">Seu Perfil</div>
            <a class="dropdown-item" href="#">Configurações</a>

            <!-- Seletor de tema -->
            <div class="dropdown-item">
              <div class="mb-2 fw-medium">Tema</div>
              <div class="d-flex gap-3">
                <label class="form-check">
                  <input type="radio" name="theme" class="form-check-input" :checked="!isDark"
                    @change="setTheme(false)">
                  <span class="form-check-label">Claro</span>
                </label>
                <label class="form-check">
                  <input type="radio" name="theme" class="form-check-input" :checked="isDark" @change="setTheme(true)">
                  <span class="form-check-label">Escuro</span>
                </label>
              </div>
            </div>

            <div class="dropdown-divider"></div>
            <a class="dropdown-item" href="#" @click="logout">Sair</a>
          </div>
        </div>
      </div>
    </div>
  </header>

  <!-- Modal de pesquisa para dispositivos móveis -->
  <div class="modal fade" :class="{ 'show d-block': showSearchModal }" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered modal-search">
      <div class="modal-content" :class="isDark ? 'bg-dark text-light' : ''">
        <div class="modal-header">
          <h5 class="modal-title">Pesquisar</h5>
          <button type="button" class="btn-close" :class="isDark ? 'btn-close-white' : ''"
            @click="toggleSearchModal"></button>
        </div>
        <div class="modal-body">
          <div class="input-group">
            <span class="input-group-text border-end-0">
              <Search size="16" />
            </span>
            <input type="text" class="form-control border-start-0" placeholder="Pesquisar...">
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- Backdrop para modais em dispositivos móveis -->
  <div v-if="showSearchModal" class="modal-backdrop fade show d-md-none" @click="closeAllModals"></div>
</template>

<style scoped>
/* Ajustes específicos que o Bootstrap não cobre */
.navbar {
  height: 60px;
  z-index: 1000;
}

/* Ajuste para o dropdown não ficar cortado */
.dropdown-menu {
  margin-top: 0.5rem;
  /* margin-left: -6rem; */
  z-index: 1060;
  /* Aumentado o z-index para ficar acima do backdrop */
  min-width: 180px; /* Mesma largura mínima do botão */
	margin-top: 0.5rem;
	right: 0; /* Alinha à direita */
	left: auto; /* Cancela o alinhamento à esquerda */
}

/* Ajuste para o modal de pesquisa */
.modal {
  background-color: rgba(0, 0, 0, 0.5);
}

/* Ajuste para o avatar do usuário */
.rounded-circle {
  object-fit: cover;
}

/* Ajuste para o backdrop */
.modal-backdrop {
  z-index: 1040;
  /* Valor menor que o z-index dos dropdowns */
}
</style>
