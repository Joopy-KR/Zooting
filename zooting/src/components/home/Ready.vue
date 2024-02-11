<template>
  <div class="ready__container">
    <Subgame class="subgame"/>
    <ReadyMatch/>
  </div>
</template>

<script setup lang="ts">
import ReadyMatch from './ReadyMatch.vue'
import Subgame from '../subgame/Subgame.vue'
</script>

<style scoped>
.ready__container {
  @apply flex flex-col grow h-full;
  min-width: 700px;
  overflow: hidden;
  margin:0;
  background-image: url("/assets/images/login/background.png");
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
}
.ready__header img {
  @apply h-5/6;
}
.ready__main {
  @apply h-full;
}
.subgame {
  @apply absolute;
}
</style>