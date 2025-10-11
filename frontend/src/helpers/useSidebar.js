/***************************************************************************** 
 MINIMIZED SIDEBAR
*****************************************************************************/
import { ref, watch } from 'vue'
import { resizeWindow } from './resizeWindow'

export function useSidebar() {
  const sidebarMinimized = ref(false)
  const sidebarOpen = ref(true)
  const { isMobileView } = resizeWindow()

  watch(sidebarMinimized, (minimized) => {
    document.body.classList.toggle('minimized', minimized)
  })

  watch(isMobileView, (isMobile) => {
    if (isMobile) {
      sidebarMinimized.value = true
    }
  })

  return {
    sidebarMinimized,
    sidebarOpen,
    isMobileView
  }
}


