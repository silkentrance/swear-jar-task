<script setup lang="ts">
import { ref, inject } from 'vue';

import { PenaltyInfo } from '@/services/SwearJarApiClient';

import PenaltyDetail from '@/components/team/PenaltyDetail.vue';

const signStore = inject('signStore');

defineProps({
    penalties: Array<PenaltyInfo>
});
</script>

<template>
    <div class="penalty-list">
        <header>
            <span>Datetime</span>
            <span>Amount</span>
            <span v-if="signStore.signed">Actions</span>
        </header>
        <PenaltyDetail v-for="penalty in penalties" :penalty="penalty"/>
    </div>
</template>

<style scoped>
.penalty-list {
    margin-left: 2em;
}

.penalty-list > header {
    display: flex;
    flex-basis: 300px;
}

.penalty-list > header > * {
    font-weight: 700;
}
</style>
