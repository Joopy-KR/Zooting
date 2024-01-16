<script setup lang="ts">
import SocialSignInButton from "@/components/signin/SocialSignInButton.vue";
</script>

<template>
  <div class="h-screen flex flex-col justify-center">
    <div class="flex flex-row divide-x-2 divide-gray-100">
      <div class="flex flex-col justify-center w-7/12 m-4 text-center">
        <h1>Description</h1>
      </div>
      <div class="flex justify-center w-5/12">
        <div class="flex flex-col justify-center w-1/2">
          <div class="flex justify-center p-8">
            <p class="text-6xl text-center text-purple-900">ZOOTING</p>
          </div>
          <SocialSignInButton />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
