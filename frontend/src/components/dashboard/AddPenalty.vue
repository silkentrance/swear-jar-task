<script setup lang="ts">
import { inject, onConstruct, reactive } from 'vue';

import { AddPenaltyRequest, AddPenaltyResponse } from '@/services/SwearJarApiClient';

const request = reactive({
    memberName: '',
    amount: ''
});

const client = inject('client');
const eventBus = inject('eventBus');

async function addPenalty() {
    if (!request.memberName || parseInt(request.amount) <= 0) {
        return;
    }
    await client.addPenalty(request.memberName, request.amount);
    request.memberName = '';
    request.amount = '';
    eventBus.emit('dashboard:penaltyAdded');
}
</script>

<template>
    <div class="add-penalty">
        <h2>Add Penalty</h2>
        <div>
            <div>
                <div>
                    <label for="member">Member</label>
                    <input name="member" type="text" v-model="request.memberName"/>
                </div>
                <div>
                    <label for="amount">Amount</label>
                    <input name="amount" type="text" v-model="request.amount"/>
                </div>
            </div>
            <button @click="addPenalty()" :disabled="!request.memberName || !request.amount">
                <img alt="Add Penalty" src="@/assets/pig-grabbing-money.svg" width="64"/>
            </button>
        </div>
    </div>
</template>


<style scoped>
.add-penalty {
}

.add-penalty > div {
    display: flex;
    flex-basis: auto;
    flex-shrink: 0;
}

.add-penalty > div > div {
    margin-left: 1em;
}

.add-penalty label {
    display: inline-block;
    width: 50px;
}

.add-penalty input {
    margin-left: 2em;
}

.add-penalty div button {
    cursor: pointer;
    background: none;
    border: none;
    margin-left: 2em;
    height: 48px;
}

.add-penalty div button:disabled {
    cursor: default;
}

</style>
