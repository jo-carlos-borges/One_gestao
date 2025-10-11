// global.d.ts
import { Store } from 'vuex'

declare global {
  interface Window {
    store: Store<any>
  }
}
