import { ref, onMounted, onUnmounted, watchEffect } from 'vue';

export function resizeWindow() {
   const width = ref(window.innerWidth);
   const isMobileView = ref(width.value < 750);

   const updateWidth = () => {
      width.value = window.innerWidth;
   };

   watchEffect(() => {
      isMobileView.value = width.value < 750;
   });

   onMounted(() => {
      window.addEventListener('resize', updateWidth);
   });

   onUnmounted(() => {
      window.removeEventListener('resize', updateWidth);
   });

   return {
      isMobileView
   };
}
