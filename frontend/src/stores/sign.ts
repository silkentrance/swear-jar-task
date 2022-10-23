import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

/* simulate web service sign-in / sign-out */
const useSignStore = defineStore('sign', () => {
  const signed = ref(false);
  function signIn() {
    signed.value = true;
  }
  function signOut() {
    signed.value = false;
  }

  return { signed, signIn, signOut };
});

export default useSignStore;
