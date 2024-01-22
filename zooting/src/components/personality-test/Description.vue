<template>
  <div class="description__container" @click="testStart">
      <p>간단한 테스트를 통해 {{ nickname }} 님의 성격 유형을 알아볼게요</p>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const nickname = ref<string>('nickname')
const emit = defineEmits(['testStart'])

const testStart = () => {
  emit('testStart')
}
</script>

<style scoped>
.description__container {
 @apply flex flex-col items-center justify-center w-full h-full text-center bg-white border border-gray-200; 
}
.description__container p {
  @apply text-2xl font-black
}
</style>