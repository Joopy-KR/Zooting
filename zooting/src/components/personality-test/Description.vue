<template>
  <div>
    <button @click="testStart">
      간단한 테스트를 통해
      {{ nickname }} 님의 성격 유형을 알아볼게요
    </button>
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

</style>