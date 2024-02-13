<template>
  <TransitionRoot as="template" :show="!isTimeOver">
    <Dialog as="div" class="relative">
      <TransitionChild as="template" enter="ease-out durastion-300" enter-from="opacity-0" enter-to="opacity-100" leave="ease-in duration-200" leave-from="opacity-100" leave-to="opacity-0">
        <div class="fixed inset-0 transition-opacity bg-pink-100 bg-opacity-75" />
      </TransitionChild>

      <div class="fixed inset-0 w-screen overflow-y-auto">
        <div class="flex items-end justify-center min-h-full p-4 text-center sm:items-center sm:p-0">
          <TransitionChild as="template" enter="ease-out duration-300" enter-from="opacity-0 translate-y-0 scale-95" enter-to="opacity-100 translate-y-0 scale-100" leave="ease-in duration-200" leave-from="opacity-100 translate-y-0 scale-100" leave-to="opacity-0 translate-y-0 scale-95">
            <DialogPanel class="relative p-6 px-4 pt-5 pb-4 my-8 overflow-hidden text-center transition-all transform bg-white rounded-lg shadow-xl dialog-panel min-w-6xl">
                <div class="flex flex-col items-center justify-center w-full h-full mt-0 ml-4 bmt-3">
                    <div class="mt-16">
                      <h1 class="text-4xl font-bold text-black">이성 선택</h1>
                      <p class="mt-5 text-gray-500">미팅 중 가장 마음에 든 이성을 골라주세요! (20초)</p>
                    </div>
                    <div class="flex items-center justify-center gap-40 mt-12">
                      <div class="flex flex-col items-center justify-center" v-for="(item, index) in oppositeGenderList" :key="index">
                        <img :class="[selectNickname === item.nickname ? 'bg-violet-200 hover:bg-violet-300' : 'hover:bg-gray-200', 'rounded-full w-60 h-60 selectImg']" :src="getProfileImage(item.animal)" alt="프로필 사진" @click="selectAnimal(item.nickname)">
                        <p class="mt-5 text-3xl font-extrabold">{{ item.nickname }}</p>
                      </div>
                    </div>
                    <div class="relative w-full h-5 mt-20 mr-6 overflow-hidden bg-gray-300 rounded-full">
                      <div class="absolute top-0 bottom-0 left-0 h-5 rounded-full bg-gradient-to-r from-pink-500 to-purple-500"
                          :style="{ width : enterRoomTimeLimit +'%'}">
                      </div>
                    </div>
                </div>
            </DialogPanel>
          </TransitionChild>
        </div>
      </div>
    </Dialog>
  </TransitionRoot>

  <TransitionRoot as="template" :show="isTimeOver">
    <Dialog as="div" class="relative">
      <TransitionChild as="template" enter="ease-out durastion-300" enter-from="opacity-0" enter-to="opacity-100" leave="ease-in duration-200" leave-from="opacity-100" leave-to="opacity-0">
        <div class="fixed inset-0 transition-opacity bg-pink-100 bg-opacity-75" />
      </TransitionChild>

      <div class="fixed inset-0 w-screen overflow-y-auto">
        <div class="flex items-end justify-center min-h-full p-4 text-center sm:items-center sm:p-0">
          <TransitionChild as="template" enter="ease-out duration-300" enter-from="opacity-0 translate-y-0 scale-95" enter-to="opacity-100 translate-y-0 scale-100" leave="ease-in duration-200" leave-from="opacity-100 translate-y-0 scale-100" leave-to="opacity-0 translate-y-0 scale-95">
            <DialogPanel class="relative p-6 px-4 pt-5 pb-4 my-8 overflow-hidden text-center transition-all transform bg-white rounded-lg shadow-xl dialog-panel min-w-6xl">
                <div class="flex flex-col items-center justify-center w-full h-full mt-0 ml-4 bmt-3">
                    <div class="mt-16">
                      <h1 class="text-4xl font-bold text-black">미팅이 종료되었어요</h1>
                      <p class="mt-5 text-gray-500">잠시 후 메인 화면으로 이동할께요</p>
                    </div>
                    <div class="flex items-center justify-center gap-40 mt-12">
                      <img class="rounded-full w-80 h-80 " src="/assets/images/animal/pupple_cat.png" alt="프로필 사진">
                    </div>
                </div>
            </DialogPanel>
          </TransitionChild>
        </div>
      </div>
    </Dialog>
  </TransitionRoot>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { Dialog, DialogPanel, DialogTitle, TransitionChild, TransitionRoot } from '@headlessui/vue'
import { ExclamationTriangleIcon } from '@heroicons/vue/24/outline'
import { useAccessTokenStore } from '@/stores/store.ts'
import axios from 'axios'

const store = useAccessTokenStore()

const oppositeGenderList = store.oppositeGenderList

const selectNickname = ref('')

// 동물 선택하기 (selectNickname에 값 들어감)
const selectAnimal = function(nickname: any) {
  if (selectNickname.value === nickname) {
    selectNickname.value = ''
  } else {
    selectNickname.value = nickname
  }
}

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

// 대기 함수
function wait(sec) {
    return new Promise(resolve => setTimeout(resolve, sec * 1000));
}

// 타이머
const enterRoomTimeLimit = ref<any>(0)
// 선택시간 끝났는지 판단할 플래그
const isTimeOver = ref(false)

// 0.1에 20이 20초. 시연을 위해 빨리 해두기
onMounted(() => {
  const intervalId = setInterval(() => {
    enterRoomTimeLimit.value += 1
 
    if (enterRoomTimeLimit.value >= 100) {
      clearInterval(intervalId)
      axios



      isTimeOver.value = true
    }
  }, 20)
})

const afterFinish = async function() {
  
}
</script>

<style scoped>
.dialog-panel {
  width: 180vh;
  height: 90vh;
  min-width: 180vh;
  min-height: 90vh;
}

.selectImg {
  /* @apply hover:bg-violet-300; */
  cursor: pointer;
}
</style>