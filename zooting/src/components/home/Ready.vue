<template>
  <div class="ready__container">
    <div class="ready__header">
      <img src="/images/pupple_dog.png" alt="" class="hidden 2xl:block animate__bounceIn">
      <img src="/images/logo_lg.png" alt="" class="animate__bounceIn">
      <img src="/images/pupple_cat.png" alt="" class="hidden 2xl:block animate__bounceIn">
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
  @apply flex flex-row justify-center items-center h-1/5 bg-white mb-5 rounded-2xl shadow-lg;
  background-color: #bcb9ee;
}
.ready__header img {
  @apply h-5/6;
}
.ready__main {
  @apply grid grid-cols-7 h-full gap-5;
}
</style>