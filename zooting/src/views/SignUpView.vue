<template>
    <div class="signup-container flex base-bg">
        <InfoInput class="rounded opacity-1"/>
    </div>
</template>

<script setup lang="ts">
import InfoInput from '@/components/signup/InfoInput.vue'
</script>

<style scoped>
.signup-container {
    @apply flex flex-col lg:h-screen w-full p-6;
    overflow-y: auto;
}
.base-bg {
  background-image: url("/assets/images/login/background.png");
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
}
</style>