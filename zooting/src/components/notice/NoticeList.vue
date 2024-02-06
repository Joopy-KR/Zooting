<template>
  <div class="notice__container">
    <ul role="list" class="notice-list">
      <li v-for="(notice, index) in noticeList" :key="index" class="notice-list__item">
        <div class="flex items-center gap-4 w-full">
          <div class="font-medium w-full">
            <div class="flex items-center">
            </div>
            <div class="flex justify-between w-full cursor-pointer" @click="getNoticeContents(notice)">
              <div class="notice-list__title">{{ notice.title }}</div>
              <div class="notice-list__content">{{ notice.createdAt }}</div>
            </div>
            <div class="notice-list__content">{{ notice.content }}</div>
          </div>
        </div>
      </li>
    </ul>
    <div>
    </div>
  </div>

</template>

<script setup lang="ts">
import {defineProps, defineEmits, ref} from 'vue'
import type { Notice } from "@/types/global";
const props = defineProps(["noticeList"])
const emits = defineEmits(["noticeContents", "openDetailModal"])
const getNoticeContents = (item : Notice) => {
    emits("noticeContents", item);
    emits("openDetailModal", true) // 함수 따로 뺴기
}



</script>

<style scoped>

.notice__container {
  width: 100%;
  height: 100%;
}
.notice__container::-webkit-scrollbar {
  width: 6px;
  background-color: white;
}
.notice__container::-webkit-scrollbar-thumb {
  background-color: #d6d6d6;
  border-radius: 4px;
}
.notice__container::-webkit-scrollbar-track {
  background-color: transparent;
}
.notice-list {
  @apply m-1 divide-y divide-gray-200;
}
.notice-list__item {
  @apply flex justify-between px-6 py-4;
}
.notice-list__item__link {
  @apply flex items-center justify-between gap-4;
}
.notice-list__img {
  @apply w-10 h-10 rounded-full;
}
.notice-list__title {
  @apply text-black;
  font-size: medium;
}
.notice-list__content {
  @apply text-sm text-gray-500;
}


button {
  @apply rounded bg-white px-4 text-xs font-semibold text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 hover:bg-gray-50 h-8;
}
.gender-icon {
  @apply text-xl;
}
</style>