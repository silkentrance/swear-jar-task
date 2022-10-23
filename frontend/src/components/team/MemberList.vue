<script setup lang="ts">
import { ref, inject } from 'vue';

import { TeamMemberDetailResponse } from '@/services/SwearJarApiClient';

import MemberDetail from '@/components/team/MemberDetail.vue';

const signStore = inject('signStore');

defineProps({
    members: Array<TeamMemberDetailResponse>
});
</script>

<template>
    <div class="member-list">
        <header>
            <span>Name</span>
            <span>Penalties</span>
            <span>Adjusted</span>
            <span v-if="signStore.signed">Actions</span>
        </header>
        <MemberDetail v-for="member in members" :member="member"/>
    </div>
</template>

<style scoped>
.member-list > header {
    display: flex;
    flex-basis: auto;
    flex-shrink: 0;
}

.member-list > header > span {
    font-weight: 700;
    width: 100px;
}
</style>
