<script setup lang="ts">
import SocialSignInButton from "@/components/signin/SocialSignInButton.vue"
import VideoComp from "@/components/signin/Video.vue"
</script>

<template>
  <div>
    <div class="grid h-screen md:grid-cols-2">
      <div class="hidden h-screen bg-slate-500 md:block">
        <VideoComp />
      </div>
      <div class="flex flex-col items-center justify-center">
          <img src="/images/logo_lg.png" alt="" class="w-3/4">
          <div class="signin-button">
            <SocialSignInButton />
          </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.signin-button {
  width: 300px;
}
</style>
