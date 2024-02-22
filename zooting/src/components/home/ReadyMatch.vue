<template>
  <div class="match__container">
    <div class="matching__state">
      <img :src="matchingButton()" alt="matching">
      <div class="absolute flex flex-col items-center justify-center" v-show="store.isMatching">
        <p class="mt-5 text-6xl">{{ store.formattedTimer }}</p>
        <p class="mt-5 text-xl">현재 대기 중인 인원</p>
        <p class="text-2xl">{{ $props.matchingCount }}</p>
      </div>
    </div>
    <!-- 매칭 중이거나 일대일 매칭 중이면 매칭 시도 금지 -->
    <button v-if="!isMatching && !isRequesting && !isbuttonClick" class="btn-hover color-1" @click="meetingRegister">Match</button>
    <button v-else-if="isMatching" class="btn-hover color-2" @click="meetingExit">Cancel</button>
    <button v-else class="btn-hover color-3">Match</button>
  
    <!-- 카메라 권한 허용 요청 모달 -->
    <TransitionRoot as="template" :show="!isAllowCamera">
      <Dialog as="div" class="relative z-50" @close="close">
        <TransitionChild as="template" enter="ease-out duration-300" enter-from="opacity-0" enter-to="opacity-100" leave="ease-in duration-200" leave-from="opacity-100" leave-to="opacity-0">
          <div class="fixed inset-0 transition-opacity bg-gray-500 bg-opacity-75" />
        </TransitionChild>
        <div class="fixed inset-0 z-10 w-screen overflow-y-auto">
          <div class="flex items-end justify-center min-h-full p-4 text-center sm:items-center sm:p-0">
            <TransitionChild as="template" enter="ease-out duration-300" enter-from="opacity-0 translate-y-4 sm:translate-y-0 sm:scale-95" enter-to="opacity-100 translate-y-0 sm:scale-100" leave="ease-in duration-200" leave-from="opacity-100 translate-y-0 sm:scale-100" leave-to="opacity-0 translate-y-4 sm:translate-y-0 sm:scale-95">
              <DialogPanel class="relative px-4 pt-5 pb-4 overflow-hidden text-left transition-all transform bg-white rounded-lg shadow-xl sm:my-8 sm:w-full sm:max-w-lg sm:p-6">
                <div>
                  <div class="flex items-center justify-center w-12 h-12 mx-auto">
                    <svg xmlns="http://www.w3.org/2000/svg" class="w-10 h-10 text-red-500 stroke-current shrink-0" fill="none" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
                    </svg>
                  </div>
                  <div class="mt-3 text-center sm:mt-5">
                    <DialogTitle as="h3" class="text-2xl font-semibold leading-6">카메라/마이크 접근 불가</DialogTitle>
                    <div class="mt-5">
                      <p class="text-lg text-gray-500">카메라 및 마이크의 접근을 허용하지 않으면</p>
                      <p class="text-lg text-gray-500">미팅에 입장하실 수 없어요</p>
                      <p class="text-lg text-gray-500"><a class="text-blue-600 underline hover:text-blue-700" href="https://support.google.com/chrome/answer/2693767?hl=ko&co=GENIE.Platform%3DDesktop&oco=0">브라우저 설정</a>을 확인해 주세요</p>
                    </div>
                  </div>
                </div>
                <div class="mt-5 sm:mt-6">
                  <div class="inline-flex justify-center w-full px-3 py-2 mt-3 text-sm font-semibold text-gray-900 bg-white rounded-md shadow-sm cursor-pointer hover:bg-gray-50 sm:mt-0" ref="cancelButtonRef" @click="close">닫기</div>
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
import { useAccessTokenStore } from '@/stores/store'
import { Dialog, DialogPanel, DialogTitle, TransitionChild, TransitionRoot } from '@headlessui/vue'

const store = useAccessTokenStore()
const isMatching = ref<boolean>(false)  // 매칭 중 여부
const isRequesting = ref<boolean>(false) // 일대일 미팅 요청 중 여부
const isMatchingComplete = ref<boolean>(false)  // 매칭 완료 여부
const isAllowCamera = ref<boolean>(true) // 카메라 권한 허용 여부
const isbuttonClick = ref<boolean>(false)

const emit = defineEmits(['matchingStart'])
const props = defineProps<{
  matchingCount:number
}>()

watch(()=> store.isRequesting, (update) => {
  isRequesting.value = update
})

watch(()=> store.isMatching, (update) => {
  isMatching.value = update
})

watch(()=> store.isMatchingComplete, (update) => {
  isMatchingComplete.value = update
})

const matchingButton = () => {
  let imgUrl: URL
  // 매칭 중이면서 매칭 완료가 되기 전이라면 하트가 돌아감
  if (isMatching.value && !isMatchingComplete.value) {
    imgUrl = new URL('/assets/images/home/heart-active.gif', import.meta.url)
  } else {
    imgUrl = new URL('/assets/images/home/heart-static.png', import.meta.url)
  }
  return imgUrl.href
}

const meetingExit = () => {
  isbuttonClick.value = false
	store.meetingExit()
}

const meetingRegister = async () => {
  isbuttonClick.value = true
  try {
    const stream = await navigator.mediaDevices.getUserMedia({ video: true })
    store.meetingRegister()
    isAllowCamera.value = true
    emit('matchingStart')
    // 카메라 권한을 허용하지 않았다면 미팅을 시작하지 않음
  } catch (error: any) {
    if (error.name === 'NotAllowedError') {
      store.meetingExit()
      isAllowCamera.value = false
      isbuttonClick.value = false
    }
  }
}

const close = () => {
  isAllowCamera.value = true
  isbuttonClick.value = false
}
</script>

<style scoped>
.match__container {
  @apply flex flex-col items-center justify-center h-full;
}
.matching__state {
  @apply w-1/2 flex justify-center items-center relative;
}
.matching__state img {
  @apply w-2/3;
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
