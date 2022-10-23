<script setup lang="ts">
import { provide } from 'vue';
import { RouterLink, RouterView } from 'vue-router';

import AppLogo from './AppLogo.vue';
import AppLinks from './AppLinks.vue';
import Sign from './Sign.vue';
</script>

<template>
    <div class="app-bar">
        <AppLogo/>
        <AppLinks/>
        <Sign/>
    </div>
</template>


<style scoped>
.app-bar {
    display: flex;
}
</style>
