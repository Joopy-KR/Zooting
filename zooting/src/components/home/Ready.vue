<template>
  <div class="ready__container">
    <div class="ready__header">
      <img src="/images/logo_lg.png" alt="">
    </div>
    <div class="ready__main">
      <ReadyMatch />
      <ReadyRecord />
    </div>
  </div>
</template>

<script setup lang="ts">
import ReadyMatch from './ReadyMatch.vue'
import ReadyRecord from './ReadyRecord.vue'

</script>

<style scoped>
.ready__container {
  @apply flex flex-col grow p-5;
  min-width: 700px;
}
.ready__header {
  @apply flex flex-row justify-center items-center h-1/5 bg-white mb-5 rounded-xl; 
}
.ready__header img {
  @apply h-5/6;
}
.ready__main {
  @apply grid grid-cols-6 h-full gap-5;
}
</style>