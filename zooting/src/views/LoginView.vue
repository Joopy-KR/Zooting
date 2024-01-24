<script setup lang="ts">
import { onMounted } from "vue";
import { useRouter } from "vue-router";
import { useAccessTokenStore } from "@/stores/store";

const router = useRouter();
const store = useAccessTokenStore();

const handleSocialLoginRedirect = async () => {
  try {
    const accessToken = await getAccessTokenFromRedirectURL();
    if (accessToken) {
      await store.setAccessToken(accessToken);
      store.getUserInfo()
      router.push({ name: "home" });
    }
  } catch (error) {
    console.error("소셜 로그인 실패: ", error);
    router.push({ name: "signin" });
  }
};

const getAccessTokenFromRedirectURL = () => {
  const urlParams = new URLSearchParams(window.location.search);
  return urlParams.get("access-token");
};

onMounted(() => {
  handleSocialLoginRedirect();
});
</script>

<template>
  <div></div>
</template>

<style scoped></style>
