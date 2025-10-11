/***************************************************************************** 
  COLOR THEME
*****************************************************************************/
import { ref, watch } from 'vue'

export function useColorTheme() {
  const isDark = ref(false)

  const initializeTheme = () => {
    const savedTheme = localStorage.getItem('theme')

    if (savedTheme) {
      isDark.value = savedTheme === 'dark'
    } else {
      const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches
      isDark.value = prefersDark
      localStorage.setItem('theme', prefersDark ? 'dark' : 'light')
    }

    applyTheme()
  }

  const applyTheme = () => {
    document.documentElement.classList.toggle('dark', isDark.value)
  }

  watch(isDark, applyTheme)

  initializeTheme()

  return { isDark }
}
