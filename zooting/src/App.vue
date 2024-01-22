<template>
  <div class="flex">
    <TheSideBar></TheSideBar>
    <RouterView class="ms-14"/>
  </div>
</template>

<script setup lang="ts">
import { RouterView } from 'vue-router'
import TheSideBar from '@/components/TheSideBar.vue'

</script>

<style scoped>
</style>
