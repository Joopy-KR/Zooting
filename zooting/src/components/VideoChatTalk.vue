<template>
  <div class="flex flex-col items-center justify-center">
    <div class="grid grid-cols-2 gap-8">
      <div v-for="(item, index) in four" :key="index" class="bg-slate-400" style="width: 355px; height: 200px;">
        <h1 class="flex flex-col items-start justify-end h-full">{{item}}: nickname</h1>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const four = ref([1, 2, 3, 4])
const help = ref(0)
</script>

<style scoped>

</style>  