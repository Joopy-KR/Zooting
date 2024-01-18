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
      if (token) {
        localStorage.setItem("accessToken", token);
      } else {
        localStorage.removeItem("accessToken");
      }
      state.value.accessToken = token;
    }
    function getAccessToken() {
      if (state.value.accessToken) {
        return state.value.accessToken;
      } else {
        const accessToken = localStorage.getItem("accessToken");
        if (accessToken) {
          state.value.accessToken = accessToken;
          return accessToken;
        } else {
          // TODO: go to home page
          alert("Access token not found");
        }
      }
    }
    return {
      setAccessToken,
      getAccessToken,
    };
  },
  { persist: true }
);
