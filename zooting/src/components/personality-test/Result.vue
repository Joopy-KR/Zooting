<template>
  <div>
    테스트 결과:
    {{ personality.title }}
    <div v-for="content in personality.content">
      {{ content }}
    </div>
    "{{ personality.match }}" 유형과 궁합이 맞아요
  </div>
</template>

<script setup lang="ts">
import { useStore } from '@/stores/store.ts'

const props = defineProps<{
  testResult: string
}>()

const store = useStore()
const personality = store.personality[props.testResult]
</script>

<style scoped>

</style>