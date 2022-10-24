<script setup lang="ts">
import { provide } from 'vue';
</script>

<template>
    <div class="app-logo">
        <img alt="Swearjar Logo" src="@/assets/logo.svg"/>
        <span>The Swear Jar</span>
    </div>
</template>


<style scoped>
.app-logo {
    display: flex;
}

.app-logo img {
    width: 1.5em;
}

.app-logo span {
    display: inline-block;
    font-size: 1em;
    margin-left: .5em;
}
</style>
