<template>
  <div class="ready__container">
    <Subgame class="subgame"/>
    <ReadyRecordList class="record"/>
    <ReadyMatch/>
  </div>
</template>

<script setup lang="ts">
import ReadyMatch from '@/components/home/ReadyMatch.vue'
import Subgame from '@/components/subgame/Subgame.vue'
import ReadyRecordList from '@/components/home/ReadyRecordList.vue'
</script>

<style scoped>
.ready__container {
  @apply flex flex-col grow h-full relative;
  min-width: 955px;
  min-height: 800px;
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
.record {
  @apply absolute;
}
</style>