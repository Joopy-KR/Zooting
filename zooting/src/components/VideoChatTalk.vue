<template>
  <div class="flex flex-col items-center justify-center h-screen">
    <div class="grid grid-cols-2 gap-8">
      <div v-for="(item, index) in 4" :key="index" class="camera-box">
        <h1 class="flex flex-col items-start justify-end h-full">{{item}}: nickname</h1>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const help = ref(0)
</script>

<style scoped>
.camera-box {
  @apply bg-slate-400;
  width: 462px; 
  height: 260px;
}


</style>  