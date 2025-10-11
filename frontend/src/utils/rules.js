export const emailRules = [
   value => {
      if (value) {
         return true
      }

      return 'Email obrigatório*'
   },
   value => {
      if (value.includes('@')) {
         return true
      }

      return 'Email inválido! Deve conter "@" para validar'
   }
]

export const nameRules = [
   value => {
      if (value) {
         return true
      }

      return 'Nome de usuário obrigatório*'
   },
   value => {
      if (value.length >= 3) {
         return true
      }

      return 'Nome de usuário inválido! deve ser maior ou igual há 3 caracteres'
   },
]

export const phoneRules = [
   value => {
      if (value) {
         return true
      }

      return 'Número de telefone obrigatório*'
   },
   value => {
      let cleanValue = value.replace(/\D/g, ''); // Remove todos os caracteres não numéricos

      if (cleanValue.length >= 11) {
         return true
      }

      return 'Número de telefone inválido! deve conter exatamente 11 dígitos'
   }
]

function hasSpecialCharacters(str) {
   return /[^\w\s]/.test(str)
}

export const passwordRules = [
   value => {
      if (value) {
         return true
      }

      return 'Senha obrigatória*'
   },
   value => {
      if (value.length >= 8) {
         return true
      }

      return 'Senha inválida! Minimo de 8 caracteres ou digitos'
   },
   value => {
      if (hasSpecialCharacters(value)) {
         return true
      }

      return 'Senha inválida! Deve possuir pelo menos 1 caracter especial'
   }
]