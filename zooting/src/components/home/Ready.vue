<template>
  <div class="ready__container">
    <div class="ready__header">
      <img src="/images/logo_lg.png" alt="">
    </div>
    <div class="ready__main">
      <ReadyParticipation />
      <ReadyRecntList />
    </div>
  </div>
</template>

<script setup lang="ts">
import ReadyParticipation from './ReadyParticipation.vue'
import ReadyRecntList from './ReadyRecntList.vue'

</script>

<style scoped>
.ready__container {
  @apply flex flex-col grow px-10 pb-5;
  min-width: 430px;
}
.ready__header {
  @apply flex flex-row justify-center items-center h-1/5 my-5 border border-black; 
}
.ready__header img {
  @apply h-5/6;
}
.ready__main {
  @apply grid grid-cols-5 h-full gap-10;
}
</style>