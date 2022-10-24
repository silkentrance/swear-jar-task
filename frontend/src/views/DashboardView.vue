<script setup lang="ts">
import { ref, onMounted, inject } from 'vue';

import { DashboardResponse } from "@/services/SwearJarApiClient";

import AddPenalty from "@/components/dashboard/AddPenalty.vue";
import MemberList from "@/components/dashboard/MemberList.vue";

const client = inject('client');
const eventBus = inject('eventBus');

const dashboardResponse = ref<DashboardResponse>({});

onMounted(async () => {
    refresh();
});

async function refresh() {
    const res = await client.dashboard();
    dashboardResponse.value = res;
}

eventBus.on('dashboard:penaltyAdded', refresh);
eventBus.on('dashboard:penaltyTotalAdjusted', refresh);
</script>

<template>
    <h2>Dashboard</h2>
    <AddPenalty/>
    <MemberList :members="dashboardResponse.members"/>
</template>
