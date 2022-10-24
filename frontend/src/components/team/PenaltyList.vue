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
        <h5>Penalties</h5>
        <header>
            <h6>Datetime</h6>
            <h6>Amount</h6>
            <h6 v-if="signStore.signed">Actions</h6>
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
}

.penalty-list > header > h6 {
    width: 200px;
}
</style>
