<template>
  <div class="match__container">
    <div class="matching-button">
      <img :src="matchingButton()" alt="matching-button">
      <button v-if="!store.isMatching" class="btn-hover color-1" @click="meetingRegister">Match</button>
      <button v-if="store.isMatching" class="btn-hover color-2" @click="meetingExit">Cancel</button>
    </div>
    <div class="matching" v-show="store.isMatching">
      <div class="matching__timer">
        {{ store.formattedTimer }}
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useAccessTokenStore } from '@/stores/store'

const store = useAccessTokenStore()

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
.match__page {
  @apply flex flex-col justify-center items-center relative;
}
.matching-button {
  @apply flex justify-center items-center relative;
}
.matching-button img {
  @apply w-5/6;
}
.matching {
  @apply absolute flex flex-col justify-center items-center gap-2;
}
.matching__timer {
  @apply text-5xl;
}
.btn-hover {
	@apply absolute -bottom-24;
  width: 170px;
  font-size: 25px;
  /* font-weight: 600; */
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
</style>
