<template>
  <div class="flex">
    <TheSideBar/>
    <RouterView class="ms-14"/>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { RouterView } from 'vue-router'
import TheSideBar from '@/components/TheSideBar.vue'
const currentReceiver = ref<string>('')
</script>

<style scoped>
</style>
