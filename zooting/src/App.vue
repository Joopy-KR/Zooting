<template>
  <div>
    <TheSideBar></TheSideBar>
    <RouterView/>
  </div>
</template>

<script setup lang="ts">
import { RouterView } from 'vue-router'
import TheSideBar from '@/components/TheSideBar.vue'
</script>

<style scoped>

</style>
