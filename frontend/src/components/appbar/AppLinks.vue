<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router';
</script>

<template>
    <div class="app-links">
        <RouterLink to="/">Dashboard</RouterLink>
        <RouterLink to="/team">Team</RouterLink>
    </div>
</template>

<style scoped>
.app-links {
    display: flex;
    align-items: flex-end;
}

.app-links a {
    margin-left: 2em;
}
</style>
