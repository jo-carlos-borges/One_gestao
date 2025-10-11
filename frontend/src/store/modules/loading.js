export default {
   namespaced: true,
   state: () => ({
      isLoading: false,
      errorImg: 'https://i.ibb.co/rc1PMc9/sem-foto.png',
   }),
   mutations: {
      toggleScreenLoading(state, isLoading) {
         state.isLoading = isLoading
      },
      imageError(state, e) {
         e.target.src = state.errorImg
      },
   },
   actions: {
      toggleScreenLoading({ commit }, isLoading) {
         commit('toggleScreenLoading', isLoading)
      },
   },
}
