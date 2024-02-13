<template>
  <TransitionRoot as="template" :show="isRecieveMeeting">
    <Dialog as="div" class="relative z-10">
      <TransitionChild as="template" enter="ease-out duration-300" enter-from="opacity-0" enter-to="opacity-100" leave="ease-in duration-200" leave-from="opacity-100" leave-to="opacity-0">
        <div class="fixed inset-0 transition-opacity bg-gray-500 bg-opacity-75" />
      </TransitionChild>

      <div class="fixed inset-0 z-10 w-screen overflow-y-auto">
        <div class="flex items-end justify-center min-h-full p-4 text-center sm:items-center sm:p-0">
          <TransitionChild as="template" enter="ease-out duration-300" enter-from="opacity-0 translate-y-4 sm:translate-y-0 sm:scale-95" enter-to="opacity-100 translate-y-0 sm:scale-100" leave="ease-in duration-200" leave-from="opacity-100 translate-y-0 sm:scale-100" leave-to="opacity-0 translate-y-4 sm:translate-y-0 sm:scale-95">
            <DialogPanel class="relative px-4 pt-5 pb-4 overflow-hidden text-left transition-all transform bg-white rounded-lg shadow-xl sm:my-8 sm:w-full sm:max-w-lg sm:p-6">
              <div>
                <div class="mt-3 text-center sm:mt-5">
                  <DialogTitle as="h3" class="text-2xl font-semibold leading-6 text-gray-900 ">저랑 데이트할래요?</DialogTitle>
                  <div class="mt-5">
                    <p class="text-lg text-gray-500"><span class="font-semibold text-pink-500">{{ meetingSender }}</span> 님에게 미팅 신청이 왔어요</p>
                  </div>
                </div>
              </div>
              <div class="relative h-5 mx-10 mt-10 overflow-hidden bg-gray-300 rounded-full">
                <div class="absolute top-0 bottom-0 left-0 rounded-full bg-gradient-to-r from-pink-500 to-purple-500"
                     :style="{ width : enterRoomTimeLimit +'%'}">
                </div>
              </div>
              <div class="mt-6 sm:mt-6 sm:grid sm:grid-flow-row-dense sm:grid-cols-2 sm:gap-3">
                <div class="inline-flex justify-center w-full px-3 py-2 mt-3 text-sm font-semibold text-white bg-indigo-600 rounded-md shadow-sm cursor-pointer hover:bg-indigo-500 sm:col-start-1 sm:mt-0" @click="meetingAccept">수락</div>
                <div class="inline-flex justify-center w-full px-3 py-2 text-sm font-semibold text-gray-900 bg-white rounded-md shadow-sm cursor-pointer hover:bg-gray-50 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600 sm:col-start-2" @click="meetingReject">거절</div>
              </div>
            </DialogPanel>
          </TransitionChild>

        </div>
      </div>
    </Dialog>
  </TransitionRoot>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { Dialog, DialogPanel, DialogTitle, TransitionChild, TransitionRoot } from '@headlessui/vue'
import { useAccessTokenStore } from '../stores/store'

const store = useAccessTokenStore()
const enterRoomTimeLimit = ref<any>(0)
const isRecieveMeeting = ref<boolean>(false)
const meetingSender = ref<string>(store.meetingSender)

watch(()=> store.isRecieveMeeting, () => {
  isRecieveMeeting.value = store.isRecieveMeeting
  meetingSender.value = store.meetingSender

  if (store.isRecieveMeeting) {
    const intervalId = setInterval(() => {
      enterRoomTimeLimit.value += 0.1
  
      if (enterRoomTimeLimit.value >= 100) {
        clearInterval(intervalId)
        // 미팅 거절
        if (store.isRecieveMeeting) {
          store.meetingRejectFriend()
        }
      }
    }, 20)
  }
})

// 미팅 거절
const meetingReject = () => {
  store.meetingRejectFriend()
}

// 미팅 수락
const meetingAccept = () => {
  store.meetingAcceptFriend()
}
</script>
