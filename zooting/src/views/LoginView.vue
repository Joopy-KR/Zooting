<script setup lang="ts">
import { onMounted } from "vue";
import { useRouter } from "vue-router";
import { useAccessTokenStore } from "@/stores/store";

const router = useRouter();
const store = useAccessTokenStore();

const handleSocialLoginRedirect = async () => {
  try {
    const token = getTokenFromURL();
    if (token["accessToken"]) {
      store.setAccessToken(token["accessToken"]);
      if (token["refreshToken"]) {
        store.setRefreshToken(token["refreshToken"]);
      }
      await store.getUserInfo();
      router.push({ name: "home" });
    }
  } catch (error) {
    console.error("소셜 로그인 실패: ", error);
    router.push({ name: "signin" });
  }
};

const getTokenFromURL = () => {
  const urlParams = new URLSearchParams(window.location.search);
  const accessToken = urlParams.get("access-token");
  const refreshToken = urlParams.get("refresh-token");
  return {
    accessToken: accessToken,
    refreshToken: refreshToken,
  };
};

onMounted(() => {
  handleSocialLoginRedirect();
});
</script>

<template>
  <div></div>
</template>

<style scoped></style>