<template>
  <div class="flex">
    <TheSideBar></TheSideBar>
    <RouterView class="ms-14"/>
    <FaceLandmarkDetection />
  </div>
</template>

<script setup lang="ts">
import { RouterView } from 'vue-router'
import TheSideBar from '@/components/TheSideBar.vue'
import FaceLandmarkDetection from '@/components/FaceLandmarkDetection.vue'

</script>

<style scoped>
</style>
