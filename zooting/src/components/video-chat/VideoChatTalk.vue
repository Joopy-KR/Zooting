<template>
  <div class="grid items-center justify-center">
    <div id="video-container" class="grid grid-cols-2 gap-8" v-if="session">
      <user-video :stream-manager="publisher"/>
      <user-video v-for="sub in subscribers" :key="sub.stream.connection.connectionId" :stream-manager="sub"/>
    </div>
  </div>

</template>

<script setup lang="ts">
import UserVideo from '@/components/video-chat/UserVideo.vue' 

defineProps({
  session: Object,
  publisher: Object,
  subscribers: Object,
})

</script>


<style scoped>

</style>  