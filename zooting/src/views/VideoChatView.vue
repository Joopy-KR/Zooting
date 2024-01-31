<template>
  <div class="video__container">
    <VideoChatTalk class="col-span-8 col-start-1"/>
    <VideoChatSideBarVue />
  </div>
</template>

<script setup lang="ts">
import VideoChatTalk from '@/components/VideoChatTalk.vue'
import VideoChatSideBarVue from '@/components/VideoChatSideBar.vue'

</script>

<style scoped>
.video__container {
  @apply grid grid-cols-2 gap-12 w-screen;
}

</style>