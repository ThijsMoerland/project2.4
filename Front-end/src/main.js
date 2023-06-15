import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import footer from './components/main/Footer.vue';
import header from './components/main/Header.vue';
import router from './router'

import './assets/css/main.css'

const app = createApp(App)

app.component('headerNavigation', header)
app.component('footerNavigation', footer)

app.use(createPinia())
app.use(router)

app.mount('#app')
