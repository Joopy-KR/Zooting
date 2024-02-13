<template>
  <div class="match__container">
    <div class="matching__state">
      <img :src="matchingButton()" alt="matching">
      <div class="matching__timer" v-show="store.isMatching">
        {{ store.formattedTimer }}
      </div>
    </div>
    <button v-if="!store.isMatching && !isRequesting" class="btn-hover color-1" @click="meetingRegister">Match</button>
    <button v-else-if="store.isMatching" class="btn-hover color-2" @click="meetingExit">Cancel</button>
    <button v-else class="btn-hover color-3">Match</button>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { useAccessTokenStore } from '@/stores/store'

const store = useAccessTokenStore()
const isRequesting = ref<boolean>(false) // 일대일 미팅 요청 중 여부

watch(()=> store.isRequesting, (update) => {
  isRequesting.value = update
})

const matchingButton = () => {
  let imgUrl: URL
  if (store.isMatching && !store.isMatchingComplete) {
    imgUrl = new URL('/assets/images/home/heart-active.gif', import.meta.url)
  } else {
    imgUrl = new URL('/assets/images/home/heart-static.png', import.meta.url)
  }
  return imgUrl.href
}

const meetingRegister = () => {
  store.meetingRegister()
}

const meetingExit = () => {
	store.meetingExit()
}
</script>

<style scoped>
.match__container {
  @apply flex flex-col items-center justify-center h-full;
}
.matching__state {
  @apply w-1/3 flex justify-center items-center;
}
.matching__timer {
  @apply absolute text-6xl;
}
.btn-hover {
  width: 170px;
  font-size: 25px;
  color: #fff;
  cursor: pointer;
  margin: 20px;
  height: 55px;
  text-align:center;
  border: none;
  background-size: 300% 100%;

  border-radius: 50px;
  -o-transition: all .4s ease-in-out;
  -webkit-transition: all .4s ease-in-out;
  transition: all .4s ease-in-out;
}
.btn-hover:hover {
  background-position: 100% 0;
  -o-transition: all .4s ease-in-out;
  -webkit-transition: all .4s ease-in-out;
  transition: all .4s ease-in-out;
}
.btn-hover:focus {
  outline: none;
}
.btn-hover.color-1 {
  background-image: linear-gradient(to right, #25aae1, #4481eb, #04befe, #3f86ed);
  box-shadow: 0 4px 15px 0 rgba(65, 132, 234, 0.75);
}
.btn-hover.color-2 {
  background-image: linear-gradient(to right, #eb3941, #f15e64, #e14e53, #e2373f);
  box-shadow: 0 5px 15px rgba(242, 97, 103, .4);
}
.btn-hover.color-3 {
  @apply bg-gray-400;
}
</style>
