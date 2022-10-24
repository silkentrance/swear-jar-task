<script setup lang="ts">
import { inject } from 'vue';
const signStore = inject('signStore');
</script>

<template>
    <div class="sign">
        <div v-if="signStore.signed">
            <a href="#" @click="signStore.signOut()">Sign out</a>
        </div>
        <div v-else>
            <p>
                <a href="#" @click="signStore.signIn()">Sign in</a>
            </p>
        </div>
    </div>
</template>

<style scoped>
.sign {
    display: flex;
    align-items: flex-end;
    margin-left: 2em;
}
</style>
