<template>
  <div class="ready__container">
    <div class="flex justify-between">
      <Subgame/>
      <ReadyRecordList/>
    </div>
    <ReadyMatch
      @matching-start="$emit('matchingStart')"
      :matching-count="$props.matchingCount"
    />
  </div>
</template>

<script setup lang="ts">
import {onMounted} from 'vue';
import ReadyMatch from '@/components/home/ReadyMatch.vue'
import Subgame from '@/components/subgame/Subgame.vue'
import ReadyRecordList from '@/components/home/ReadyRecordList.vue'
import { useAccessTokenStore } from '@/stores/store'

const store = useAccessTokenStore()
const emit = defineEmits(['matchingStart'])
const props = defineProps<{
  matchingCount:number
}>()

onMounted(async () => {
  store.getMeetingLog()
})
</script>

<style scoped>
.ready__container {
  @apply flex flex-col grow h-screen p-5 gap-5 relative;
  min-width: 955px;
  overflow: hidden;
  background-image: url("/assets/images/login/background.png");
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
}
.ready-camera {
  @apply absolute bg-blue-300;
  width: 22rem;
  height: 25rem;
  top: 22rem;
  left: 4rem;
}
</style>