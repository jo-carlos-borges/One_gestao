import { createStore } from 'vuex'
import auth from './modules/auth'
import loading from './modules/loading'

const store = createStore({
   modules: {
      auth,
      loading,
   },
})

export default store
