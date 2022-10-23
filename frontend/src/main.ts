import { createApp } from 'vue'
import { createPinia } from 'pinia'

import mitt from 'mitt';

import App from './App.vue'
import router from './router'

import { SwearJarApiClient } from "@/services/SwearJarApiClient";
import useSignStore from '@/stores/sign';

import './assets/main.css'

const app = createApp(App)

app.use(createPinia())
app.use(router)

app.provide('signStore', useSignStore());
app.provide('client', new SwearJarApiClient());
app.provide('eventBus', mitt());

app.mount('#app')
