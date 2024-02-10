<template>
  <TransitionRoot as="template" :show="store.isMatchingComplete">
    <Dialog as="div" class="relative z-10" @close="$emit('closeModal')">
      <TransitionChild as="template" enter="ease-out duration-300" enter-from="opacity-0" enter-to="opacity-100" leave="ease-in duration-200" leave-from="opacity-100" leave-to="opacity-0">
        <div class="fixed inset-0 transition-opacity bg-gray-500 bg-opacity-75" />
      </TransitionChild>

      <div class="fixed inset-0 z-10 w-screen overflow-y-auto">
        <div class="flex items-end justify-center min-h-full p-4 text-center sm:items-center sm:p-0">
          <TransitionChild as="template" enter="ease-out duration-300" enter-from="opacity-0 translate-y-4 sm:translate-y-0 sm:scale-95" enter-to="opacity-100 translate-y-0 sm:scale-100" leave="ease-in duration-200" leave-from="opacity-100 translate-y-0 sm:scale-100" leave-to="opacity-0 translate-y-4 sm:translate-y-0 sm:scale-95">
            <DialogPanel class="relative px-4 pt-5 pb-4 overflow-hidden text-left transition-all transform bg-white rounded-lg shadow-xl sm:my-8 sm:w-full sm:max-w-lg sm:p-6">
              <div>
                <div class="flex items-center justify-center w-12 h-12 mx-auto bg-green-100 rounded-full">
                  <CheckIcon class="w-6 h-6 text-green-600" aria-hidden="true" />
                </div>
                <div class="mt-3 text-center sm:mt-5">
                  <DialogTitle as="h3" class="text-2xl font-semibold leading-6 text-gray-900 ">매칭 완료</DialogTitle>
                  <div class="mt-5">
                    <p class="text-lg text-gray-500">방에 입장하시겠습니까? </p>
                  </div>
                </div>
              </div>
              <div class="relative h-5 mx-10 mt-10 overflow-hidden bg-gray-300 rounded-full">
                <div class="absolute top-0 bottom-0 left-0 rounded-full bg-gradient-to-r from-pink-500 to-purple-500"
                     :style="{ width : enterRoomTimeLimit +'%'}">
                </div>
              </div>
              <div class="mt-5 sm:mt-6 sm:grid sm:grid-flow-row-dense sm:grid-cols-2 sm:gap-3">
                <div class="inline-flex justify-center w-full px-3 py-2 text-sm font-semibold text-white bg-indigo-600 rounded-md shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600 sm:col-start-2" @click="meetingAccept">입장</div>
                <div class="inline-flex justify-center w-full px-3 py-2 mt-3 text-sm font-semibold text-gray-900 bg-white rounded-md shadow-sm hover:bg-gray-50 sm:col-start-1 sm:mt-0" ref="cancelButtonRef" @click="meetingExit">취소</div>
              </div>
            </DialogPanel>
          </TransitionChild>

        </div>
      </div>
    </Dialog>
  </TransitionRoot>
</template>

<script setup lang="ts">
import { onMounted, ref, watch } from 'vue'
import { Dialog, DialogPanel, DialogTitle, TransitionChild, TransitionRoot } from '@headlessui/vue'
import { CheckIcon } from '@heroicons/vue/24/outline'
import { useAccessTokenStore } from '../stores/store'

const store = useAccessTokenStore()
const emit = defineEmits(['closeModal'])
const enterRoomTimeLimit = ref<any>(0)

onMounted(()=>{
  setInterval(()=>{
    enterRoomTimeLimit.value += 0.5
  }, 20)
})

const meetingAccept = () => {
  store.meetingAccept()
}

const meetingExit = () => {
  store.meetingExit()
}

watch(()=> enterRoomTimeLimit.value, ()=>{
  if (enterRoomTimeLimit.value === 100) {
    store.meetingExit()
  }
})
</script>
