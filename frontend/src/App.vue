<script setup lang="ts">
import { provide } from 'vue';
import { RouterLink, RouterView } from 'vue-router';

import AppBar from '@/components/appbar/AppBar.vue';
</script>

<template>
  <AppBar/>

  <main>
    <RouterView />
  </main>
</template>
