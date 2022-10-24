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
        <h3>Team</h3>
        <header>
            <h4>Name</h4>
            <h4>Penalties</h4>
            <h4>Adjusted</h4>
            <h4 v-if="signStore.signed">Actions</h4>
        </header>
        <MemberDetail v-for="member in members" :member="member"/>
    </div>
</template>

<style scoped>
.member-list > header {
    display: flex;
}

.member-list > header > h4 {
    width: 100px;
}
</style>
