<template>
  <div class="match__container">
    <div class="matching-button" @click="meetingRegister">
      <img :src="matchingButton()" alt="matching-button">
    </div>
    <div class="matching" v-show="store.isMatching">
      <div class="matching__text">
				Matching...
      </div>
      <div class="matching__timer">
				{{ store.formattedTimer }}      
			</div>
			<button class="btn-hover color-11" @click="matchingEnd">Cancel</button>
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

const matchingEnd = () => {
	store.matchingEnd()
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
  @apply flex justify-center items-center relative cursor-pointer;
}
.matching-button img {
  @apply w-5/6;
}
.matching {
  @apply absolute flex flex-col justify-center items-center gap-2;
}
.matching__text {
  @apply text-5xl;
}
.matching__timer {
  @apply text-3xl;
}
.btn-hover {
	@apply absolute top-60;
    width: 150px;
    font-size: 20px;
    /* font-weight: 600; */
    color: #fff;
    cursor: pointer;
    margin: 20px;
    height: 50px;
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
.btn-hover.color-11 {
  background-image: linear-gradient(to right, #eb3941, #f15e64, #e14e53, #e2373f);  box-shadow: 0 5px 15px rgba(242, 97, 103, .4);
}
</style>
