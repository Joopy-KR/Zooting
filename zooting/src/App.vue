<template>
  <div class="flex">
    <TheSideBar/>
    <!-- <RouterView class="ms-14"/> -->
    <VideoChatView class="ms-14"/>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { RouterView } from 'vue-router'
import TheSideBar from '@/components/TheSideBar.vue'
import VideoChatView from './views/VideoChatView.vue'

const currentReceiver = ref<string>('')
</script>

<style scoped>
</style>
