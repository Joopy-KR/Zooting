<script setup lang="ts"></script>

<template>
  <div class="flex flex-col">
    <div class="content-center p-1">
      <a href="http://localhost:8080/login/oauth2/authorization/kakao">
        <img src="@/assets/images/login/kakao_login.png" alt="Kakao Login" class="w-full h-auto" />
      </a>
    </div>
    <div class="p-1">
      <a href="http://localhost:8080/login/oauth2/authorization/google">
        <img
          src="@/assets/images/login/google_login.png"
          alt="Google Login"
          class="w-full h-auto"
        />
      </a>
    </div>
    <div class="p-1">
      <a href="http://localhost:8080/login/oauth2/authorization/naver">
        <img src="@/assets/images/login/naver_login.png" alt="Naver Login" class="w-full h-auto" />
      </a>
    </div>
  </div>
</template>

<style scoped></style>
