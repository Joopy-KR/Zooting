<template>
  <div class="flex">
    <TheSideBar/>
    <!-- <RouterView class="ms-14"/> -->
    <test class="ms-14"/>
    <!-- <VideoMaskFilter class="ms-14"/>/ -->
  </div>
</template>

<script setup lang="ts">
import { RouterView } from 'vue-router'
import TheSideBar from '@/components/TheSideBar.vue'
import test from '@/components/video-chat/test.vue'
import VideoMaskFilter from './components/video-chat/VideoMaskFilter.vue'
</script>

<style scoped>
</style>
