<template>
  <div class="grid items-center justify-center">
    <div id="video-container" class="grid grid-cols-2 gap-8" v-if="session">
      <user-video :stream-manager="publisher" :cameraHeight="cameraHeight" :cameraWidth="cameraWidth"/>
      <user-video v-for="sub in subscribers" :key="sub.stream.connection.connectionId" :stream-manager="sub" :cameraHeight="cameraHeight" :cameraWidth="cameraWidth"/>
    </div>
  </div>

</template>

<script setup lang="ts">
import UserVideo from '@/components/video-chat/UserVideo.vue' 

defineProps(['session', 'publisher', 'subscribers', 'cameraHeight', 'cameraWidth'])

</script>


<style scoped>
.camera-box {
  @apply bg-slate-400;
  width: 462px; 
  height: 260px;
}

</style>  