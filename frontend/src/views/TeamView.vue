<script setup lang="ts">
import { ref, onMounted, inject } from 'vue';

import { TeamMemberDetailResponse } from '@/services/SwearJarApiClient';

import MemberList from '@/components/team/MemberList.vue';

const client = inject('client');
const eventBus = inject('eventBus');

const teamMemberDetails = ref<TeamMemberDetailResponse[]>([]);

onMounted(async () => {
    refresh();
});

async function refresh() {
    // TODO this should be a bulk request e.g. client.teamMemberDetails()
    const dashboardResponse = await client.dashboard();
    const details = [];
    for (const member of dashboardResponse.members) {
        const teamMemberDetailResponse = await client.teamMemberDetail(member.id);
        details.push(teamMemberDetailResponse);
    }
    teamMemberDetails.value = details;
}

eventBus.on('team:penaltyTotalAdjusted', refresh);
eventBus.on('team:penaltyChanged', refresh);
eventBus.on('team:penaltyRemoved', refresh);
</script>

<template>
    <h2>Team Members</h2>
    <MemberList :members="teamMemberDetails"/>
</template>
