<template>
  <div class="notice__container">
    <div>
      <div class="notice-title">공지사항</div>
    </div>
    <NoticeList :noticeList="noticeList" @notice-contents="getNoticeDetail" @open-detail-modal="openDetailModal"/>
    <div class="page-number-list-container">
      <ul class="page-number-list-ul">
        <li v-for="idx in totalPage" class="page-number-list-li">
          <p :class="idx === currentPage ? 'page-number--active' : 'page-number--inactive'"
             v-on:click="movePage(idx)" style="cursor:pointer;">{{idx}}</p>
        </li>
      </ul>
    </div>

    <!-- 공지사항 디테일 -->
    <TransitionRoot as="template" class="z-40" :show="detailModalOpen">
      <Dialog as="div" class="relative z-10" @close="detailModalOpen = false">
        <TransitionChild as="template" enter="ease-out duration-300" enter-from="opacity-0" enter-to="opacity-100" leave="ease-in duration-200" leave-from="opacity-100" leave-to="opacity-0">
          <div class="fixed inset-0 transition-opacity bg-gray-500 bg-opacity-75" />
        </TransitionChild>

        <div class="fixed inset-0 z-10 w-screen overflow-y-auto">
          <div class="flex items-end justify-center min-h-full p-4 text-center sm:items-center sm:p-0">
            <TransitionChild as="template" enter="ease-out duration-300" enter-from="opacity-0 translate-y-4 sm:translate-y-0 sm:scale-95" enter-to="opacity-100 translate-y-0 sm:scale-100" leave="ease-in duration-200" leave-from="opacity-100 translate-y-0 sm:scale-100" leave-to="opacity-0 translate-y-4 sm:translate-y-0 sm:scale-95">
              <DialogPanel class="relative px-4 pt-5 pb-4 overflow-hidden text-left transition-all transform bg-white rounded-lg shadow-xl sm:my-8 sm:w-full sm:max-w-lg sm:p-6">
                <div class="absolute top-0 right-0 hidden pt-4 pr-4 sm:block">
                  <button type="button" class="text-gray-400 bg-white rounded-md hover:text-gray-500 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2" @click="detailModalOpen = false;">
                    <span class="sr-only">Close</span>
                    <XMarkIcon class="w-6 h-6" aria-hidden="true" />
                  </button>
                </div>
                <div class="sm:flex sm:items-start">
                  <div class="flex items-center justify-center flex-shrink-0 w-12 h-12 mx-auto bg-red-100 rounded-full sm:mx-0 sm:h-10 sm:w-10">
                    <ExclamationTriangleIcon class="w-6 h-6 text-red-600" aria-hidden="true" />
                  </div>
                  <div class="mt-3 text-center sm:ml-4 sm:mt-0 sm:text-left">
                    <DialogTitle as="h3" class="text-base font-semibold leading-6 text-gray-900">{{ detailContent.title }}</DialogTitle>
                    <div class="mt-2">
                      <p class="text-sm text-gray-500">{{ detailContent.content }}</p>
                    </div>
                  </div>
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
import {onMounted, ref, watch} from 'vue'
import type { Notice } from "@/types/global";
import { useAccessTokenStore } from '@/stores/store'
import NoticeList from './NoticeList.vue'
import {ExclamationTriangleIcon, XMarkIcon} from "@heroicons/vue/24/outline";
import {Dialog, DialogPanel, DialogTitle, TransitionChild, TransitionRoot} from "@headlessui/vue";
const store = useAccessTokenStore()


const noticePage = ref(store.noticePage)
const noticeList = ref(store.noticeList)
const totalPage = ref(store.noticePage?.totalPage);
const currentPage = ref(store.noticePage?.totalPage)
const DEFAULT_PAGE_SIZE = 6;
const detailContent = ref({ noticeId : null, title : null, content : null, createdAt : null});
const detailModalOpen = ref(false);
const movePage = (item: number) => {
  store.getNoticeList({page: item-1, size : DEFAULT_PAGE_SIZE})
}

watch(()=> store.noticePage, (UpdatePage)=>{
  noticePage.value = UpdatePage
  totalPage.value = noticePage.value?.totalPage
  currentPage.value = noticePage.value?.currentPage + 1
})
watch(()=> store.noticeList, (UpdateList)=>{
  noticeList.value = UpdateList
})
function getNoticeDetail(data :Notice) {
  detailContent.value = data;
  console.log(detailContent.value)
}
function openDetailModal(data : Boolean) {
  detailModalOpen.value = data;
  console.log(detailModalOpen.value)
}



</script>

<style scoped>
.notice-title{
  @apply  text-indigo-600 py-6 px-6;
  font-weight : bold;
  font-size: medium;
}
.notifications__container {
  width: 100%;
  height: 100%;
}
.page-number--active {
  @apply  text-indigo-600;
}
.page-number--inactive {
  @apply border-transparent text-gray-400 hover:border-gray-200 hover:text-gray-500;
}
.page-number-list-container{
  @apply flex justify-center
}
.page-number-list-ul {
  @apply list-none
}
.page-number-list-li {
  @apply float-left pr-3.5
}
</style>