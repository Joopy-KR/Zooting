<script setup lang="ts">
const { VITE_SERVER_API_URL } = import.meta.env;

const KAKAO_LOGIN_URL = `${VITE_SERVER_API_URL}/oauth2/authorization/kakao`;

console.log("====================================");
console.log("MODE", import.meta.env.MODE);
console.log("BASE_URL", import.meta.env.BASE_URL);
console.log("PROD", import.meta.env.PROD);
console.log("DEV", import.meta.env.DEV);
console.log("SSR", import.meta.env.SSR);
console.log("VITE_SERVER_API_URL", import.meta.env.VITE_SERVER_API_URL);
console.log("ENV: ", VITE_SERVER_API_URL, KAKAO_LOGIN_URL);
console.log("====================================");

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
