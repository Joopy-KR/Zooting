<template>
  <div class="ready__container">
    <div class="flex justify-between">
      <Subgame class="subgame"/>
      <ReadyRecordList class="record"/>
    </div>
    <div>
      <ReadyMatch/>
    </div>
  </div>
</template>

<script setup lang="ts">
import ReadyMatch from '@/components/home/ReadyMatch.vue'
import Subgame from '@/components/subgame/Subgame.vue'
import ReadyRecordList from '@/components/home/ReadyRecordList.vue'
import ReadyMeeting from '@/components/home/ReadyMeeting.vue'
</script>

<style scoped>
.ready__container {
  @apply flex flex-col grow h-full p-5 gap-5;
  min-width: 955px;
  overflow: hidden;
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
</style>