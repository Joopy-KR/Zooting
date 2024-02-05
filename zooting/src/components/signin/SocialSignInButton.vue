<script setup lang="ts">
const { VITE_SERVER_API_URL } = import.meta.env;

const KAKAO_LOGIN_URL = `${VITE_SERVER_API_URL}/oauth2/authorization/kakao`;
console.log("ENV: ", VITE_SERVER_API_URL, KAKAO_LOGIN_URL);
</script>

<template>
  <div class="flex flex-col">
    <div class="py-1">
      <a :href="KAKAO_LOGIN_URL">
        <img src="@/assets/images/login/kakao_login.png" alt="Kakao Login" class="w-full h-auto" />
      </a>
    </div>
    <div class="py-1">
      <a href="http://localhost:8080/login/oauth2/authorization/google">
        <img
          src="@/assets/images/login/google_login.png"
          alt="Google Login"
          class="w-full h-auto"
        />
      </a>
    </div>
    <div class="py-1">
      <a href="http://localhost:8080/login/oauth2/authorization/naver">
        <img src="@/assets/images/login/naver_login.png" alt="Naver Login" class="w-full h-auto" />
      </a>
    </div>
  </div>
</template>

<style scoped></style>
