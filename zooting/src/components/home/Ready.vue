<template>
  <div class="ready__container">
    <div class="ready__header">
      <img src="/assets/images/logo/logo_lg.png" alt="" class="animate__bounceIn">
    </div>
    <div class="ready__main">
      <Subgame class="mb-10 subgame"/>
      <ReadyMatch />
      <ReadyRecord />
    </div>
  </div>
</template>

<script setup lang="ts">
import ReadyMatch from './ReadyMatch.vue'
import ReadyRecord from './ReadyRecord.vue'
import Subgame from '../subgame/Subgame.vue';

</script>

<style scoped>
.ready__container {
  @apply flex flex-col grow p-5;
  min-width: 700px;
  background-color: #d2d0fe;
}
.ready__header {
  @apply flex flex-row justify-center items-center h-1/5 mb-5 rounded-2xl shadow-lg;
  background-color: #ffff;
}
.ready__header img {
  @apply h-5/6;
}
.ready__main {
  @apply grid grid-cols-7 h-full gap-5;
}
@media (max-width : 640px) {
  .subgame {
    display : none;
  }
}

</style>