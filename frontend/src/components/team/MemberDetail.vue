<script setup lang="ts">
import { ref, inject } from 'vue';

import PenaltyList from './PenaltyList.vue';

const signStore = inject('signStore');
const client = inject('client');
const eventBus = inject('eventBus');

defineProps({
    member: Object
});

const editMode = ref(false);

const amountAdjusted = ref("");

function edit(member) {
    amountAdjusted.value = member.amountAdjusted || "";
    editMode.value = true;
}

async function save(member) {
    let adjustedAmount = parseInt(amountAdjusted.value);
    if (adjustedAmount === member.amountCalculated) {
        adjustedAmount = 0;
        amountAdjusted.value = '';
    }
    await client.adjustPenaltyTotal(member.id, adjustedAmount || 0);
    editMode.value = false;
    eventBus.emit('team:penaltyTotalAdjusted');
}

function cancel(member) {
    amountAdjusted.value = member.amountAdjusted || "";
    editMode.value = false;
}
</script>

<template>
<div class="member-info">
    <main>
        <span>{{member.name}}</span>
        <span>{{member.amountCalculated}}</span>
        <input v-if="editMode" type="text" v-model="amountAdjusted"/>
        <span v-else>{{member.amountAdjusted || ""}}</span>
        <button v-if="signStore.signed && !editMode" @click="edit(member)">Edit</button>
        <button v-if="signStore.signed && editMode" @click="save(member)">Save</button>
        <button v-if="signStore.signed && editMode" @click="cancel(member)">Cancel</button>
    </main>
    <PenaltyList :penalties="member.penalties"/>
</div>
</template>

<style scoped>
.member-info > main {
    display: flex;
    flex-basis: auto;
    flex-shrink: 0;
}

.member-info > main > * {
    width: 100px;
}

.member-info > main > button {
    width: auto;
    margin-right: 1em;
}
</style>
