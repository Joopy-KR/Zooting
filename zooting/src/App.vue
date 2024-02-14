<template>
  <div class="flex">
    <RouterView/>
    <MatchingCompleteModal
      v-if="store.isLogin"
      class="z-40"
      />
    <MeetingAcceptModal
      v-if="store.isLogin"
      class="z-40"
    />
    <TransitionRoot as="div" :show="store.showResult">
    <Dialog as="div" class="relative z-10" @close="store.showResult = false">
      <TransitionChild as="div" enter="ease-out duration-300" enter-from="opacity-0" enter-to="opacity-100" leave="ease-in duration-200" leave-from="opacity-100" leave-to="opacity-0">
        <div class="fixed inset-0 transition-opacity bg-gray-500 bg-opacity-75" />
      </TransitionChild>

      <div class="fixed inset-0 z-10 w-screen overflow-y-auto">
        <div class="flex items-center justify-center min-h-full p-0 text-center">
          <TransitionChild as="div" enter="ease-out duration-300" enter-from="opacity-0 translate-y-0 scale-95" enter-to="opacity-100 translate-y-0 scale-100" leave="ease-in duration-200" leave-from="opacity-100 translate-y-0 scale-100" leave-to="opacity-0 translate-y-0 scale-95">
            <DialogPanel class="relative w-screen max-w-sm p-6 px-4 pt-5 pb-4 my-8 overflow-hidden text-left transition-all transform bg-white rounded-lg shadow-xl">
              <div>
                <div class="flex items-center justify-center mx-auto rounded-full">
                  <font-awesome-icon :icon="['fas', 'heart']" size="2xl" style="color: #ffbdbd;" />
                </div>
                <div class="mt-3 text-center sm:mt-5">
                  <DialogTitle as="h3" class="mb-3 text-xl font-semibold leading-6 text-gray-900">나를 선택한 사람들</DialogTitle>
                  <div class="flex items-center justify-center gap-20">
                    <!-- store.meetingResult -->
                    <div class="flex flex-col items-center justify-center mt-5" v-for="(item, index) in store.meetingResult" :key="index">
                      <img class="w-24 h-24" :src="getProfileImage(item.pickerAnimal)" alt="프로필 사진">
                      <p class="mt-5 text-lg font-bold">{{ item.pickerNickname }}</p>
                    </div>
                    <p v-if="store.meetingResult.length === 0">더 좋은 인연이 기다리고 있을거에요!</p>
                  </div>
                </div>
              </div>
              <div class="mt-5 sm:mt-6">
                <div class="inline-flex justify-center w-full px-3 py-2 mt-3 text-sm font-semibold text-gray-900 bg-white rounded-md shadow-sm cursor-pointer hover:bg-gray-50 sm:col-start-1 sm:mt-0" ref="cancelButtonRef" @click="store.showResult = false">닫기</div>
              </div>
            </DialogPanel>
          </TransitionChild>
        </div>
      </div>
    </Dialog>
  </TransitionRoot>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { Dialog, DialogPanel, DialogTitle, TransitionChild, TransitionRoot } from '@headlessui/vue'
import { RouterView } from 'vue-router'
import MatchingCompleteModal from "@/components/MatchingCompleteModal.vue"
import MeetingAcceptModal from "@/components/MeetingAcceptModal.vue"
import { useAccessTokenStore } from './stores/store'

const store = useAccessTokenStore()

// 프로필 이미지 경로구하는 함수
const getProfileImage = (animal: String) => {
  let imgUrl: URL;
  if (animal === '강아지') {
    imgUrl = new URL('/assets/images/animal/dog.png', import.meta.url);
  } else if (animal === '고양이') {
    imgUrl = new URL('/assets/images/animal/cat.png', import.meta.url);
  } else if (animal === '곰') {
    imgUrl = new URL('/assets/images/animal/bear.png', import.meta.url);
  } else if (animal === '공룡') {
    imgUrl = new URL('/assets/images/animal/dino.png', import.meta.url);
  } else if (animal === '펭귄') {
    imgUrl = new URL('/assets/images/animal/penguin.png', import.meta.url);
  } else if (animal === '토끼') {
    imgUrl = new URL('/assets/images/animal/rabbit.png', import.meta.url);
  } else if (animal === '사슴') {
    imgUrl = new URL('/assets/images/animal/deer.png', import.meta.url);
  } else {
    imgUrl = new URL('/assets/images/animal/animal_group.png', import.meta.url);
  }
  return imgUrl.href;
}
</script>
<style scoped>

@import url("https://hangeul.pstatic.net/hangeul_static/css/nanum-square.css");
* {
  font-family: 'NanumSquareAcb';
  -ms-user-select: none;
  -moz-user-select: -moz-none;
  -webkit-user-select: none;
  -khtml-user-select: none;
  user-select: none;
}
</style>
