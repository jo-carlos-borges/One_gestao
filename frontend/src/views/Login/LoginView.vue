<script setup>
import { Eye, EyeOff } from 'lucide-vue-next'
import { inject, ref, onMounted } from 'vue'
import { useStore } from 'vuex'
import { initRainAnimation } from '@/utils/rain'

const store = useStore()
const isDark = inject('isDark')
const showPassword = ref(false)
const userCredentials = ref({
   email: null,
   password: null,
   connected: false
})

const togglePasswordVisibility = () => {
   showPassword.value = !showPassword.value
}

const login = () => {
   store.dispatch('auth/LogIn', userCredentials.value)
      .then(() => {
         return;
      })
      .catch((error) => {
         const msg = error?.response?.data?.message || 'Erro ao realizar Login'
         window.$toast().fire({
            icon: 'success',
            title: msg
         })
      })
}

onMounted(() => {
   initRainAnimation();

   setTimeout(() => {
      const loginWrapper = document.querySelector(".login__wrapper")
      if (loginWrapper) {
         loginWrapper.classList.toggle("open")
      }
   }, 1000)
})

</script>

<template>
   <div class="login__wrapper" :class="isDark ? 'bg-dark text-white' : 'bg-white text-dark'">
      <div class="animate__lines"></div>
      <div class="login__left">
         <img height="80" src="../../assets/images/thundersales.png" />
      </div>
      <div class="login__card" :class="isDark ? 'color__theme__dark' : 'color__theme__light'">
         <form class="login__right" @submit.prevent>
            <div class="login__title" :class="isDark ? 'text-white' : 'text-dark'">Faça login para acessar o sistema
            </div>
            <div class="form__group">
               <input type="text" id="username" placeholder="Usuário" v-model="userCredentials.email" />
               <label for="username">Usuário</label>
            </div>
            <div class="form__group password__group">
               <input :type="showPassword ? 'text' : 'password'" id="Password" placeholder="Senha"
                  v-model="userCredentials.password" @keydown.enter="login" />
               <label for="Password">Senha</label>
               <button class="password__toggle" type="button" @click="togglePasswordVisibility">
                  <Eye v-if="!showPassword" size="18" />
                  <EyeOff v-else size="18" />
               </button>
            </div>
            <div class="login__options">
               <div class="checkbox__container">
                  <input type="checkbox" id="keepConnected" v-model="userCredentials.connected" />
                  <div class="text__checkbox">Manter-me conectado</div>
               </div>
               <a href="#" class="forgot__password">Esqueceu sua senha?</a>
            </div>
            <div class="button__area">
               <button type="button" class="btn w-100"
                  :class="isDark ? 'color__theme__dark__btn' : 'color__theme__light__btn'"
                  @click="login">Entrar</button>
            </div>
         </form>
      </div>

      <!-- Footer -->
      <footer class="login__footer" :class="isDark ? 'text-white' : 'text-dark'">
         &copy; {{ new Date().getFullYear() }}
         <a href="https://thunderdevs.com.br/" target="_blank" rel="noopener noreferrer"><b
               class="thunderdevs">ThunderDevs</b>.</a>
         Todos os direitos reservados.
      </footer>
   </div>
</template>

<style scoped>
input {
   -webkit-appearance: none;
   -moz-appearance: none;
   font-size: 16px;
   color: inherit;
   border-radius: 0;
}

input[type="text"],
input[type="password"] {
   width: 100%;
   height: 40px;
   border-bottom: 1px solid #aaaaaa;
   background-color: transparent;
   border-top: none;
   border-left: none;
   border-right: none;
}

button,
input:focus {
   outline: 0;
}

::-webkit-input-placeholder {
   font-size: 16px;
   font-weight: 300;
   letter-spacing: -0.00933333em;
}

.form__group {
   position: relative;
   padding-top: 15px;
   margin-top: 10px;
}

.password__group {
   position: relative;
}

.password__toggle {
   position: absolute;
   right: 0;
   top: 15px;
   background: transparent;
   border: none;
   height: 40px;
   color: inherit;
   z-index: 5;
   cursor: pointer;
   padding: 0;
   display: flex;
   align-items: center;
}

label {
   position: absolute;
   left: 0;
   top: 15px;
   opacity: 0;
   color: #aaa;
   font-weight: 300;
   font-size: 13px;
   letter-spacing: -0.00933333em;
   transition: all 0.2s ease-out;
}

input:not(:placeholder-shown)+label {
   opacity: 1;
   transform: translateY(-15px);
}

.h1 {
   color: #fff;
   opacity: 0.8;
   font-size: 20px;
   font-weight: 400;
   text-transform: uppercase;
   letter-spacing: 0.2405em;
   transition: all 770ms cubic-bezier(0.51, 0.04, 0.12, 0.99);
   text-align: center;
   cursor: pointer;
   position: absolute;
   transform: translateY(-10px);
}

.open .h1 {
   -webkit-transform: translateX(200px) translateZ(0) translateY(-10px);
   transform: translateX(200px) translateZ(0) translateY(-10px);
}

.login__title {
   font-size: 18px;
   text-transform: uppercase;
   letter-spacing: -0.00933333em;
   font-weight: 600;
   text-align: center;
   margin-bottom: 20px;
}

.login__wrapper {
   /* width: 100dvw; */
   height: 100dvh;
   box-shadow: 0px 2px 50px rgba(0, 0, 0, 0.2);
   border-radius: 4px;
   overflow: hidden;
   position: relative;
}

.login__left {
   width: 100%;
   height: 100%;
   display: flex;
   justify-content: center;
   align-items: center;
   transition: all 770ms cubic-bezier(0.51, 0.04, 0.12, 0.99);
   overflow: hidden;
   animation: fadeIn 0.5s ease-in;
   opacity: 1;
}

.login__left img {
   display: block;
   transition: all 770ms cubic-bezier(0.51, 0.04, 0.12, 0.99);
   object-position: left;
}

.open .login__left {
   -webkit-transform: translateX(-400px) translateZ(0);
   transform: translateX(-400px) translateZ(0);
}

.login__card {
   position: absolute;
   top: 30vh;
   right: 15%;
   width: 30%;
   transform: translateX(100dvw) translateZ(0);
   transition: all 770ms cubic-bezier(0.51, 0.04, 0.12, 0.99);
   border-radius: 8px;
   box-shadow: 2px 4px 6px rgba(0, 0, 0, 0.1);
   overflow: hidden;
}

.open .login__card {
   -webkit-transform: translateX(0px) translateZ(0);
   transform: translateX(0px) translateZ(0);
}

.login__right {
   padding: 20px;
   width: 100%;
}

.login__options {
   display: flex;
   justify-content: space-between;
   align-items: center;
   margin-top: 35px;
}

.forgot__password {
   color: #5b7fec !important;
   font-size: 14px;
   text-decoration: none;
   transition: color 0.3s ease;
}

.forgot__password:hover {
   text-decoration: underline;
}

.checkbox__container {
   display: flex;
   align-items: center;
}

.text__checkbox {
   color: #aaa;
   font-size: 16px;
   letter-spacing: -0.00933333em;
   font-weight: 300;
   margin-left: 8px;
}

input[type="checkbox"] {
   cursor: pointer;
   margin: 0;
   height: 18px;
   width: 18px;
   -webkit-appearance: checkbox;
   -moz-appearance: checkbox;
   appearance: checkbox;
}

.button__area {
   display: flex;
   justify-content: space-between;
   margin-top: 30px;
}

.btn {
   width: 100%;
   -webkit-appearance: none;
   -moz-appearance: none;
   border: none;
   border-radius: 4px;
   height: 40px;
   padding: 0 35px;
   cursor: pointer;
   font-size: 16px;
   text-transform: uppercase;
   letter-spacing: -0.00933333em;
}

.login__footer {
   position: absolute;
   bottom: 20px;
   width: 100%;
   text-align: center;
   font-size: 14px;
}

.login__footer a {
   color: inherit;
   text-decoration: none;
   transition: color 0.3s ease;
}

.thunderdevs {
   /* color: #5b7fec; */
   background: var(--linear-gradient-logo-color);
   -webkit-background-clip: text;
   background-clip: text;
   color: transparent;
   -webkit-text-fill-color: transparent;
   font-weight: 600;
}

.animate__lines {
   position: fixed;
   margin: auto;
   top: 0;
   left: 0;
   right: 0;
   height: 100vh;
   width: 100vw;
   z-index: 0;
   pointer-events: none;
}

/***************************************************************************** 
   MEDIA QUERIES
*****************************************************************************/
@media (max-width: 991px) {
   .open .login__left img {
      -webkit-transform: translateX(150px) translateZ(0);
      transform: translateX(150px) translateZ(0);
   }

   .login__card {
      width: 50%;
      right: 10%;
   }
}

@media (max-width: 700px) {
   .open .login__left {
      display: none;
   }

   .login__card {
      width: 90%;
      right: 5%;
      top: 20vh;
   }

   .login__options {
      flex-direction: column;
      align-items: flex-start;
      gap: 15px;
   }
}

@media (max-width: 480px) {
   .login__card {
      width: 95%;
      right: 2.5%;
   }

   .login__title {
      font-size: 16px;
   }

   .text__checkbox {
      font-size: 14px;
   }

   .forgot__password {
      font-size: 13px;
   }
}

/***************************************************************************** 
   KEYFRAMES
*****************************************************************************/
@keyframes fadeIn {
   0% {
      opacity: 0;
   }

   100% {
      opacity: 1;
   }
}
</style>
