<script setup lang="ts">
import SocialSignInButton from "@/components/signin/SocialSignInButton.vue";
</script>

<template>
  <div class="h-screen">
    <div class="flex flex-row items-center justify-center m-20">
      <div class="w-1/2 pr-8 text-center">
        <h1>Description</h1>
      </div>
      <div class="flex items-center justify-center w-1/2">
        <div class="flex flex-col items-center w-1/2">
          <div class="flex justify-center p-8">
            <p class="text-6xl text-center text-purple-900">ZOOTING</p>
          </div>
          <div class="justify-center p-4">
            <SocialSignInButton />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
