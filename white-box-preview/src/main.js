import {createApp} from 'vue'
import App from './App.vue'
import store from './store'
import router from './router'

import installElementPlus from './plugins/element'

const app = createApp(App)
installElementPlus(app)
app.use(store).use(router).mount('#app');
