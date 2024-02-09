<template>
  <TransitionRoot as="template" :show="$props.showFromParent">
    <Dialog as="div" class="relative z-10" @close="closeDialog">
      <TransitionChild as="template" enter="ease-out duration-300" enter-from="opacity-0" enter-to="opacity-100"
                       leave="ease-in duration-200" leave-from="opacity-100" leave-to="opacity-0">
        <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity"/>
      </TransitionChild>

      <div class="fixed inset-0 z-10 w-screen overflow-y-auto">
        <div class="flex min-h-full items-end justify-center p-4 text-center sm:items-center sm:p-0">
          <TransitionChild as="template" enter="ease-out duration-300"
                           enter-from="opacity-0 translate-y-4 sm:translate-y-0 sm:scale-95"
                           enter-to="opacity-100 translate-y-0 sm:scale-100" leave="ease-in duration-200"
                           leave-from="opacity-100 translate-y-0 sm:scale-100"
                           leave-to="opacity-0 translate-y-4 sm:translate-y-0 sm:scale-95">
            <DialogPanel
                class="relative transform overflow-hidden rounded-lg bg-white px-10 pb-8 pt-5 text-left shadow-xl transition-all max-h-fit">
              <!-- Content -->
              <ul role="list"
                  class="grid grid-cols-3 gap-x-10 gap-y-8 sm:gap-x-6 sm:grid-cols-2 lg:grid-cols-3 md:grid-cols-3">
                <li v-for="file in files" :key="file.source" class="relative lg:max-w-96 lg:max-h-72">
                  <div
                      class="group aspect-h-7 aspect-w-10 block w-full overflow-hidden rounded-lg bg-gray-100 focus-within:ring-2 focus-within:ring-indigo-500 focus-within:ring-offset-2 focus-within:ring-offset-gray-100">
                    <img :src="file.source" alt="" class="pointer-events-none object-cover group-hover:opacity-75"/>
                    <button type="button" class="absolute inset-0 focus:outline-none">
                      <span class="sr-only">View details for {{ file.title }}</span>
                    </button>
                  </div>
                  <p class="pointer-events-none mt-2 block truncate text-sm font-medium text-gray-900">{{
                      file.title
                    }}</p>
                </li>
              </ul>
              <!-- 확인 버튼 -->
              <div class="flex justify-center mt-5 sm:mt-6">
                <button type="button"
                        class="w-1/2 rounded-md bg-violet-500 px-3 py-2 text-sm font-semibold text-white shadow-sm hover:bg-violet-400 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-violet-500"
                        @click="closeDialog">돌아가기
                </button>
              </div>
            </DialogPanel>
          </TransitionChild>
        </div>
      </div>
    </Dialog>
  </TransitionRoot>
</template>

<script setup>
import {ref} from 'vue'
import {Dialog, DialogPanel, DialogTitle, TransitionChild, TransitionRoot} from '@headlessui/vue'
import {CheckIcon} from '@heroicons/vue/24/outline'

const props = defineProps({
  showFromParent: {
    type: Boolean,
    default: false,
  },
})
const emits = defineEmits(["setParentShow"])

const closeDialog = () => {
  emits("setParentShow", false);
}

const files = [
  {
    title: 'IMG_4985.HEIC',
    size: '3.9 MB',
    source:
        'https://images.unsplash.com/photo-1582053433976-25c00369fc93?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=512&q=80',
  },
  {
    title: 'IMG_4985.HEIC',
    size: '3.9 MB',
    source:
        'https://images.unsplash.com/photo-1582053433976-25c00369fc93?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=512&q=80',
  },{
    title: 'IMG_4985.HEIC',
    size: '3.9 MB',
    source:
        'https://images.unsplash.com/photo-1582053433976-25c00369fc93?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=512&q=80',
  },{
    title: 'IMG_4985.HEIC',
    size: '3.9 MB',
    source:
        'https://images.unsplash.com/photo-1582053433976-25c00369fc93?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=512&q=80',
  },
  {
    title: 'IMG_4985.HEIC',
    size: '3.9 MB',
    source:
        'https://images.unsplash.com/photo-1582053433976-25c00369fc93?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=512&q=80',
  },
  {
    title: 'IMG_4985.HEIC',
    size: '3.9 MB',
    source:
        'https://images.unsplash.com/photo-1582053433976-25c00369fc93?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=512&q=80',
  },


  // More files...
]
</script>

<style scoped>
/* 이미지 컨테이너를 제한된 크기로 설정하여 이미지가 해당 크기 내에서 리사이징될 수 있도록 함 */
.group {
  max-width: 100%;
  max-height: 100%;
}

/* 이미지를 컨테이너에 가득 차게 함 */
.group img {
  width: 100%;
  height: 100%;
}
</style>