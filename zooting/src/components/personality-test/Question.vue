<template>
  <div class="test__container">
    <div class="test__count">
      {{ test.id }} / 4
    </div>
    <div class="test__image">
      <img :src="getImageUrl()" alt="season-image">
    </div>
    <div class="test__question">
      {{ test.question }}
    </div>
    <div class="test__select-button">
      <button @click="selectAnswer(test.answerType1)">{{ test.answer1 }}</button>
      <button @click="selectAnswer(test.answerType2)">{{ test.answer2 }}</button>
    </div>
  </div>
</template>

<script setup lang="ts">
const props = defineProps<{
  test: {
    id: number
    question: string
    answer1: string
    answer2: string
    answerType1: string
    answerType2: string
  }
}>()

const emit = defineEmits(['selectAnswer'])

const selectAnswer = (selectedOption: string) => {
  emit('selectAnswer', selectedOption)
}

const getImageUrl = () => `images/season${props.test.id}.png`
</script>

<style scoped>
.test__container {
  @apply h-full w-full flex flex-col justify-center items-center bg-white border border-gray-200 shadow p-3;
}
.test__select-button {
  @apply flex flex-col lg:w-1/2;
}
.test__select-button button {
  @apply rounded-md bg-violet-50 px-3.5 py-2.5 text-sm font-semibold text-violet-600 shadow-sm hover:bg-violet-100 m-2;
}
.test__image {
  @apply flex mt-3;
  width: 200px;
}
.test__question {
  @apply my-5
}
</style>