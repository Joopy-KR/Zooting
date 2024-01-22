<template>
  <div class="result__container">
    <div class="result__title">
      <h1>{{ nickname }} 님의 성격 유형은</h1>
      <h1>
        <span :class="getColorClass(personality.title)">
          {{ personality.title }}
        </span>이에요
      </h1>
    </div>
    
    <div class="result__content">
      <li v-for="(content, index) in personality.content" :key="index">
        {{ content }}
      </li>
      <li><span :class="getColorClass(personality.match)">
        {{ personality.match }}
      </span> 유형과 궁합이 맞아요.</li>
    </div>
    <!-- click => axios & Home으로 라우팅 -->
    <button class="test-completed">미팅 하러 가기</button>

  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useStore } from '@/stores/store.ts'

const props = defineProps<{
  testResult: string
}>()

const store = useStore()
const personality = store.personality[props.testResult]

const nickname = ref<string>('nickname')

const getColorClass = (value: string) => {
  if (value.includes('여름')) {
    return 'summer-text'
  } else if (value.includes('겨울')) {
    return 'winter-text'
  } else if (value.includes('봄')) {
    return 'spring-text'
  } else if (value.includes('가을')) {
    return 'autumn-text'
  }
}
</script>

<style scoped>
.result__container {
  @apply h-full w-full flex flex-col justify-center bg-white border border-gray-200 shadow p-3 items-center;

}
.result__title {
  @apply text-2xl font-black my-5;
}

.summer-text {
  color: #4CA975;
}

.winter-text {
  color: #6A5ACD;
}

.spring-text {
  color: #FF7493;
}

.autumn-text {
  color: #D27D32;
}

.result__content {
  @apply bg-white border border-gray-200 rounded-lg shadow py-5 px-5 text-left;
}
.result__content span {
  @apply font-black;
}
.test-completed {
  @apply text-gray-900 bg-gradient-to-r from-red-200 via-red-300 to-yellow-200 hover:bg-gradient-to-bl focus:outline-none focus:ring-red-100 font-medium rounded-lg text-lg px-5 py-2.5 text-center mx-3 my-7 w-80;
}
</style>
