<script setup lang="ts">
const { VITE_SERVER_API_URL } = import.meta.env
const KAKAO_LOGIN_URL = `${VITE_SERVER_API_URL}/oauth2/authorization/kakao`
</script>

<template>
  <div class="login__container">
    <div class="zooting-logo animate__bounceIn">
      <img src="/assets/images/logo/logo_lg.png" alt="" class="">
    </div>
    <a :href="KAKAO_LOGIN_URL" class="login__button">
      <img src="/assets/images/login/kakao_login_lg.png" alt="">
    </a>
  </div>
</template>

<style scoped>
.login__container {
  @apply w-screen h-screen flex flex-col justify-center items-center;
  overflow: hidden;
  margin:0;
  background-image: url("/assets/images/login/background.png");
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
}
.zooting-logo {
  @apply w-1/2;
}
.login__button {
  @apply flex justify-center w-1/2 mt-10 cursor-pointer;
}
.login__button img {
  @apply w-5/6 2xl:w-1/2 shadow-md;
}
</style>
