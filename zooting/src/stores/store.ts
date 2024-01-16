import { ref, computed } from "vue";
import { defineStore } from "pinia";

export const useCounterStore = defineStore("counter", () => {
  return {};
});

interface AccessTokenState {
  accessToken: string | null;
}

export const useAccessTokenStore = defineStore(
  "access-token",
  () => {
    const state = ref<AccessTokenState>({
      accessToken: null,
    });

    function setAccessToken(token: string | null) {
      state.value.accessToken = token;
    }
    function getAccessToken() {
      return state.value.accessToken;
    }
    return {
      setAccessToken,
      getAccessToken,
    };
  },
  { persist: true }
);
