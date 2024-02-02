<template>
  <div class="flex">
    <TheSideBar/>
    <!-- <RouterView class="ms-14"/> -->
    <VideoChatView class="ms-14"/>
    <!-- <test class="ms-14"/> -->
  </div>
</template>

<script setup lang="ts">
import { RouterView } from 'vue-router'
import TheSideBar from '@/components/TheSideBar.vue'
import VideoChatView from './views/VideoChatView.vue'
import test from '@/components/video-chat/test.vue'
</script>

<style scoped>
</style>
