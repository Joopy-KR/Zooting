<template>
  <div class="result__container">
      <transition name="fade">
      <div v-if="isShow">
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
        <button class="test-completed" @click.prevent="completedTest">미팅 하러 가기</button>
      </div>
    </transition>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useStore, useAccessTokenStore } from '@/stores/store'
import { useRouter } from 'vue-router'

const props = defineProps<{
  testResult: string
}>()

const store = useStore()
const accessTokenStore = useAccessTokenStore()
const router = useRouter()

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

const completedTest = () => {
  accessTokenStore.setPersonality(props.testResult)
}

const isShow = ref(false)

onMounted(Run)

async function Run() {
    await wait(0.5)
    isShow.value = true
}

const wait = (sec:number) => {
      return new Promise(resolve => setTimeout(resolve, sec * 1000));
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
.fade-enter-active,
.fade-leave-active {
  transition: opacity 1s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
