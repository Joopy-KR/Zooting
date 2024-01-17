<template>
  <div>
    <h2>{{ test.question }}</h2>
    <div>
      <button @click="selectAnswer(1)">{{ test.answer1 }}</button>
      <button @click="selectAnswer(2)">{{ test.answer2 }}</button>
    </div>
  </div>
</template>

<script setup lang="ts">
const props = defineProps<{
  test: {
    question: string
    answer1: string
    answer2: string
  }
}>()

const emit = defineEmits(['selectAnswer'])

const selectAnswer = (selectedOption: number) => {
  emit('selectAnswer', selectedOption)
}
</script>

<style scoped>
button {
  background-color: aqua;
  margin: 1rem;
}
</style>