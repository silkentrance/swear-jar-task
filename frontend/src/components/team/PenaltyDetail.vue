<script setup lang="ts">
import { ref, inject } from 'vue';

defineProps({
    penalty: Object
});

const client = inject('client');
const signStore = inject('signStore');
const eventBus = inject('eventBus');
const editMode = ref(false);
const amount = ref(0);

function edit(penalty) {
    amount.value = penalty.amount;
    editMode.value = true;
}

async function save(penalty) {
    let actualAmount = parseInt(amount.value);
    if (actualAmount === penalty.amount || actualAmount <= 0) {
        amount.value = '';
    } else {
        await client.changePenalty(penalty.id, actualAmount);
    }
    editMode.value = false;
    eventBus.emit('team:penaltyChanged');
}

async function remove(penalty) {
    await client.removePenalty(penalty.id);
    eventBus.emit('team:penaltyRemoved');
}

function cancel(penalty) {
    amount.value = penalty.value;
    editMode.value = false;
}
</script>

<template>
<div class="penalty-info">
    <span>{{penalty.dateTime}}</span>
    <input v-if="signStore.signed && editMode" type="text" v-model="amount">
    <span v-else>{{penalty.amount}}</span>
    <button v-if="signStore.signed && !editMode" @click="edit(penalty)">Edit</button>
    <button v-if="signStore.signed && !editMode" @click="remove(penalty)">Remove</button>
    <button v-if="signStore.signed && editMode" @click="save(penalty)">Save</button>
    <button v-if="signStore.signed && editMode" @click="cancel(penalty)">Cancel</button>
</div>
</template>

<style scoped>
.penalty-info {
    display: flex;
    flex-basis: auto;
    flex-shrink: 0;
}

.penalty-info > * {
    width: 200px;
}

.penalty-info > button {
    width: auto;
    margin-right: 1em;
}
</style>
