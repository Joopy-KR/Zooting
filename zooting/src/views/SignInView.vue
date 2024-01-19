<script setup lang="ts">
import SocialSignInButton from "@/components/signin/SocialSignInButton.vue";
import VideoComp from "@/components/signin/Video.vue";
import DescriptionComp from "@/components/signin/Description.vue";
</script>

<template>
  <div class="flex flex-col justify-center h-screen">
    <div class="flex flex-row divide-x-2 divide-gray-100">
      <div class="flex flex-row justify-end w-7/12 max-h-screen">
        <VideoComp />
      </div>
      <div class="flex flex-col items-center justify-center w-5/12">
        <DescriptionComp />
        <div class="w-1/2">
          <SocialSignInButton />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
