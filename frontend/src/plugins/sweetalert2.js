import Swal from 'sweetalert2'

function getThemeClass() {
  return document.documentElement.classList.contains('dark') ? 'color__theme__dark' : 'color__theme__light'
}

export const toast = () => {
  return Swal.mixin({
    toast: true,
    position: 'top-end',
    showConfirmButton: false,
    timer: 3500,
    timerProgressBar: true,
    customClass: {
      popup: getThemeClass()
    },
    didOpen: (toast) => {
      toast.addEventListener('mouseenter', Swal.stopTimer)
      toast.addEventListener('mouseleave', Swal.resumeTimer)
    }
  })
}

// const swalWithCustomButtons = Swal.mixin({
//    customClass: {
//       textInput: 'custom-text-input',
//       confirmButton: 'custom-confirm-button',
//       cancelButton: 'custom-cancel-button'
//    },
//    buttonsStyling: true
// })

export default {
   install(app) {
      app.config.globalProperties.$swal = Swal
      app.config.globalProperties.$toast = toast

      window.$swal = Swal
      window.$toast = toast
   }
}
